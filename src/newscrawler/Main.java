package newscrawler;

import newscrawler.crawler.CrawlController;
import java.util.Properties;

public class Main {

    public static Properties config;
    public static CrawlController controller;
    
    static {
        Main.config = new Properties();
    }

    public static void main(String[] args) throws Exception {
        CrawlController controller = Bootstrap.bootstrap("./conf/application.properties");

        controller.start(NewsCrawler.class, 
                Integer.parseInt(Main.config.getProperty("numberOfCrawlers")));
    }
}
