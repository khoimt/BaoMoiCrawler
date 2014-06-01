package newscrawler;

import newscrawler.crawler.CrawlConfig;
import newscrawler.crawler.CrawlController;
import newscrawler.fetcher.PageFetcher;
import newscrawler.robotstxt.RobotstxtConfig;
import newscrawler.robotstxt.RobotstxtServer;

public class Main {

    public static void main(String[] args) throws Exception {
        String crawlStorageFolder = args[0];
        int numberOfCrawlers = Integer.parseInt(args[1]);

        CrawlConfig config = new CrawlConfig();

        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setPolitenessDelay(1000);
        config.setMaxDepthOfCrawling(2);

        config.setMaxPagesToFetch(1000000);
        config.setResumableCrawling(false);

        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

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

        controller.start(BaoMoiCrawler.class, numberOfCrawlers);
    }
}
