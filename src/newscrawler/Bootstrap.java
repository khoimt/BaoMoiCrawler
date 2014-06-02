package newscrawler;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import newscrawler.crawler.Controller;
import newscrawler.crawler.CrawlConfig;
import newscrawler.crawler.CrawlController;
import newscrawler.fetcher.PageFetcher;
import newscrawler.robotstxt.RobotstxtConfig;
import newscrawler.robotstxt.RobotstxtServer;

public class Bootstrap {

    protected static Logger logger;

    static {
        logger = Logger.getLogger(Bootstrap.class.getName());
    }

    public static Properties loadConfig(String config) {
        Properties prop = new Properties();

        try {
            prop.load(new BufferedInputStream(new FileInputStream(config)));
            return prop;
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Could not load config", ex);
        }
        return null;
    }

    public static CrawlController bootstrap(String configPath) throws Exception {
        Properties cfg = Main.config = loadConfig(configPath);

        CrawlConfig crawlConfig = new CrawlConfig();
        crawlConfig.setCrawlStorageFolder(cfg.getProperty("crawlStorageFolder"));
        crawlConfig.setPolitenessDelay(Integer.parseInt(cfg.getProperty("politenessDelay")));
        crawlConfig.setMaxDepthOfCrawling(Integer.parseInt(cfg.getProperty("maxDepthOfCrawling")));
        crawlConfig.setMaxPagesToFetch(Integer.parseInt(cfg.getProperty("maxPagesToFetch")));
        crawlConfig.setResumableCrawling(Boolean.valueOf(cfg.getProperty("resumableCrawling")));
        crawlConfig.setUserAgentString(cfg.getProperty("userAgentString"));

        PageFetcher pageFetcher = new PageFetcher(crawlConfig);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);

        // page seeds
        Controller controller = new Controller(crawlConfig, pageFetcher, robotstxtServer);
        controller.addSeed("http://www.baomoi.com/");
        controller.addSeed("http://www.baomoi.com/Home/XaHoi.epi");
        controller.addSeed("http://www.baomoi.com/Home/VanHoa.epi");
        controller.addSeed("http://www.baomoi.com/Home/KinhTe.epi");
        controller.addSeed("http://www.baomoi.com/Home/KHCN.epi");
        controller.addSeed("http://www.baomoi.com/Home/TheThao.epi");
        controller.addSeed("http://www.baomoi.com/Home/PhapLuat.epi");
        controller.addSeed("http://www.baomoi.com/Home/GiaoDuc.epi");
        controller.addSeed("http://www.baomoi.com/Home/SucKhoe.epi");
        controller.addSeed("http://www.baomoi.com/Home/OtoXemay.epi");
        controller.addSeed("http://www.baomoi.com/Home/NhaDat.epi");

        return controller;
    }
}
