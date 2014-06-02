package newscrawler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import newscrawler.crawler.NewsPage;
import newscrawler.crawler.Page;
import newscrawler.crawler.WebCrawler;
import newscrawler.parser.HtmlParseData;
import newscrawler.url.WebURL;
import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.jsoup.select.Elements;

public class NewsCrawler extends WebCrawler {
    protected String baseURL = "http://www.baomoi.com";
    protected final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g"
            + "|png|tiff?|mid|mp2|mp3|mp4"
            + "|wav|avi|mov|mpeg|ram|m4v|pdf"
            + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

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
            System.out.println("url: " + page.getURL());
            System.out.println("title: " + page.getTitle());
            System.out.println("time: " + page.getTime());
            System.out.println("description: " + page.getDescription());
            System.out.println("content: " + page.getContent());
            String arr[] = page.getKeywords();
            if (arr != null) {
                System.out.print("keywords: ");
                for (int i = 0; i < arr.length; i++) {
                    System.out.print(arr[i] + " ");
                }
                System.out.println();
            }
        }
    }
}
