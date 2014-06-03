package newscrawler;

import java.util.Properties;
import newscrawler.crawler.CrawlController;
import newscrawler.database.NewsIDRedisServer;

public class Main {

    public static Properties config;
    public static CrawlController controller;
    
    static {
        Main.config = new Properties();
    }

    public static void main(String[] args) throws Exception {
        CrawlController controller = Bootstrap.bootstrap("./conf/application.properties");
        NewsIDRedisServer tmp = NewsIDRedisServer.getInstance();

        controller.start(NewsCrawler.class, 
                Integer.parseInt(Main.config.getProperty("numberOfCrawlers")));
    }
}
