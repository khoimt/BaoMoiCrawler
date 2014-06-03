package newscrawler;

import java.util.regex.Pattern;
import newscrawler.crawler.NewsPage;
import newscrawler.crawler.Page;
import newscrawler.crawler.WebCrawler;
import newscrawler.database.NewsMongo;
import newscrawler.url.WebURL;

public class NewsCrawler extends WebCrawler {
    protected static String baseURL; 
    protected final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g"
            + "|png|tiff?|mid|mp2|mp3|mp4"
            + "|wav|avi|mov|mpeg|ram|m4v|pdf"
            + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
    
    static {
        baseURL = Main.config.getProperty("baseURL");
    }

    @Override
    public boolean shouldVisit(WebURL url) {
        String href = url.getURL().toLowerCase();
        return !FILTERS.matcher(href).matches()
                && href.startsWith(baseURL);
    }

    @Override
    public void visit(Page page) {
        System.out.println("=============");
        if (page instanceof NewsPage) {
            this.visit((NewsPage) page);
        }
    }
    
    public void visit(NewsPage page) {
        if (page.isArticle()) {
            page.assignId();
            System.out.println("id: " + page.getId());
            System.out.println("url: " + page.getURL());
            System.out.println("title: " + page.getTitle());
            System.out.println("time: " + page.getTime());
            System.out.println("description: " + page.getDescription());
            System.out.println("content: " + page.getContent());
            String arr[] = page.getKeywords();
            if (arr != null) {
                System.out.print("keywords: ");
                for (int i = 0; i < arr.length; i++) {
                    System.out.print(arr[i] + ", ");
                }
                System.out.println();
            }
            NewsMongo.getInstance().insert(page);
        }
    }
}
