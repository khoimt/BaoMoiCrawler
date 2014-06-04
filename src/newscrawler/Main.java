package newscrawler;

import java.util.Properties;
import newscrawler.crawler.CrawlController;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class Main {

    public static Properties config;
    public static CrawlController controller;

    public static void main(String[] args) {
        CrawlController controller;
        try {
            if (args.length > 0) {
                controller = Bootstrap.bootstrap(args[0]);
            } else {
                controller = Bootstrap.bootstrap("./conf/application.properties");
            }

            controller.start(NewsCrawler.class,
                    Integer.parseInt(Main.config.getProperty("numberOfCrawlers")));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
