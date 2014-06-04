package newscrawler.crawler;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import java.util.List;
import newscrawler.frontier.Counters;
import newscrawler.frontier.DocIDServer;
import newscrawler.frontier.Frontier;
import newscrawler.frontier.InProcessPagesDB;
import newscrawler.frontier.WorkQueues;
import newscrawler.url.WebURL;
import org.apache.log4j.Logger;

public class NewsFrontier extends Frontier {
    protected static final Logger logger = Logger.getLogger(Frontier.class.getName());

    public NewsFrontier(CrawlConfig config, DocIDServer docIdServer) {
        super(config);
        this.docIdServer = docIdServer;
    }
    
    public NewsFrontier(Environment env, CrawlConfig config, DocIDServer docIdServer) {
        super(env, config, docIdServer);
        this.counters = new Counters(env, config);
        this.docIdServer = docIdServer;
        try {
            workQueues = new WorkQueues(env, "PendingURLsDB", config.isResumableCrawling());
            if (config.isResumableCrawling()) {
                scheduledPages = counters.getValue(Counters.ReservedCounterNames.SCHEDULED_PAGES);
                inProcessPages = new InProcessPagesDB(env);
                long numPreviouslyInProcessPages = inProcessPages.getLength();
                if (numPreviouslyInProcessPages > 0) {
                    logger.info("Rescheduling " + numPreviouslyInProcessPages + " URLs from previous crawl.");
                    scheduledPages -= numPreviouslyInProcessPages;
                    while (true) {
                        List<WebURL> urls = inProcessPages.get(100);
                        if (urls.size() == 0) {
                            break;
                        }
                        scheduleAll(urls);
                        inProcessPages.delete(urls.size());
                    }
                }
            } else {
                inProcessPages = null;
                scheduledPages = 0;
            }
        } catch (DatabaseException e) {
            logger.error("Error while initializing the Frontier: " + e.getMessage());
            workQueues = null;
        }
    }

    public void scheduleAll(List<WebURL> urls) {
        int maxPagesToFetch = config.getMaxPagesToFetch();
        synchronized (mutex) {
            int newScheduledPage = 0;
            for (WebURL url : urls) {
                if (maxPagesToFetch > 0 && (scheduledPages + newScheduledPage) >= maxPagesToFetch) {
                    break;
                }
                try {
                    workQueues.put(url);
                    newScheduledPage++;
                } catch (DatabaseException e) {
                    logger.error("Error while puting the url in the work queue.");
                }
            }
            if (newScheduledPage > 0) {
                scheduledPages += newScheduledPage;
                counters.increment(Counters.ReservedCounterNames.SCHEDULED_PAGES, newScheduledPage);
            }
            synchronized (waitingList) {
                waitingList.notifyAll();
            }
        }
    }

    public void schedule(WebURL url) {
        int maxPagesToFetch = config.getMaxPagesToFetch();
        synchronized (mutex) {
            try {
                if (maxPagesToFetch < 0 || scheduledPages < maxPagesToFetch) {
                    workQueues.put(url);
                    scheduledPages++;
                    counters.increment(Counters.ReservedCounterNames.SCHEDULED_PAGES);
                }
            } catch (DatabaseException e) {
                logger.error("Error while puting the url in the work queue.");
            }
        }
    }

    public void getNextURLs(int max, List<WebURL> result) {
        while (true) {
            synchronized (mutex) {
                if (isFinished) {
                    return;
                }
                try {
                    List<WebURL> curResults = workQueues.get(max);
                    workQueues.delete(curResults.size());
                    if (inProcessPages != null) {
                        for (WebURL curPage : curResults) {
                            inProcessPages.put(curPage);
                        }
                    }
                    result.addAll(curResults);
                } catch (DatabaseException e) {
                    logger.error("Error while getting next urls: " + e.getMessage());
                    e.printStackTrace();
                }
                if (result.size() > 0) {
                    return;
                }
            }
            try {
                synchronized (waitingList) {
                    waitingList.wait();
                }
            } catch (InterruptedException ignored) {
                // Do nothing
            }
            if (isFinished) {
                return;
            }
        }
    }

    public void setProcessed(WebURL webURL) {
        counters.increment(Counters.ReservedCounterNames.PROCESSED_PAGES);
        if (inProcessPages != null) {
            if (!inProcessPages.removeURL(webURL)) {
                logger.warn("Could not remove: " + webURL.getURL() + " from list of processed pages.");
            }
        }
    }

    public long getQueueLength() {
        return workQueues.getLength();
    }

    public long getNumberOfAssignedPages() {
        return inProcessPages.getLength();
    }

    public long getNumberOfProcessedPages() {
        return counters.getValue(Counters.ReservedCounterNames.PROCESSED_PAGES);
    }

    public void sync() {
        workQueues.sync();
        docIdServer.sync();
        counters.sync();
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void close() {
        sync();
        workQueues.close();
        counters.close();
    }

    public void finish() {
        isFinished = true;
        synchronized (waitingList) {
            waitingList.notifyAll();
        }
    }
}