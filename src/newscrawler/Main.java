package newscrawler;

import java.util.Properties;
import newscrawler.crawler.Controller;

public class Main {

    public static Properties config;
    public static Controller controller;

    public static void main(String[] args) {
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
