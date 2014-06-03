package newscrawler;

import java.util.regex.Pattern;
import newscrawler.crawler.NewsPage;
import newscrawler.crawler.Page;
import newscrawler.crawler.WebCrawler;
import newscrawler.database.NewsMongo;
import newscrawler.url.WebURL;
import org.apache.log4j.Logger;

public class NewsCrawler extends WebCrawler {
    protected static Logger logger;
    protected static String baseURL; 
    protected final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g"
            + "|png|tiff?|mid|mp2|mp3|mp4"
            + "|wav|avi|mov|mpeg|ram|m4v|pdf"
            + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
    
    static {
        baseURL = Main.config.getProperty("baseURL");
        logger = Logger.getLogger(NewsCrawler.class);
    }

    @Override
    public boolean shouldVisit(WebURL url) {
        String href = url.getURL().toLowerCase();
        return !FILTERS.matcher(href).matches()
                && href.startsWith(baseURL);
    }

    @Override
    public void visit(Page page) {
        if (page instanceof NewsPage) {
            this.visit((NewsPage) page);
        }
    }
    
    public void visit(NewsPage page) {
        logger.info("=== Visit: " + page.getURL());
        if (page.isArticle()) {
            page.assignId();
            logger.info("id: " + page.getId());
            logger.info("url: " + page.getURL());
            logger.info("title: " + page.getTitle());
            logger.info("time: " + page.getTime());
            logger.info("description: " + page.getDescription());
            logger.info("content: " + page.getContent());
            String arr[] = page.getKeywords();
            if (arr != null) {
                logger.info("keywords: ");
                StringBuilder str = new StringBuilder();
                for (int i = 0; i < arr.length; i++) {
                    str.append(arr[i] + ", ");
                }
                logger.info(str.toString());
            }
            NewsMongo.getInstance().insert(page);
        }
    }
}
