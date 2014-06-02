package newscrawler.crawler;
import newscrawler.fetcher.PageFetcher;
import newscrawler.robotstxt.RobotstxtServer;

public class Controller extends CrawlController {

    public Controller(CrawlConfig config, PageFetcher pageFetcher, RobotstxtServer robotstxtServer) throws Exception {
        super(config, pageFetcher, robotstxtServer);
    }
    
}