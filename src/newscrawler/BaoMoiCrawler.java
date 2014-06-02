package newscrawler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
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

public class BaoMoiCrawler extends WebCrawler {

    protected final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g"
            + "|png|tiff?|mid|mp2|mp3|mp4"
            + "|wav|avi|mov|mpeg|ram|m4v|pdf"
            + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

    @Override
    public boolean shouldVisit(WebURL url) {
        String href = url.getURL().toLowerCase();
        return !FILTERS.matcher(href).matches()
                && href.startsWith("http://www.baomoi.com");
    }

    @Override
    public void visit(Page page) {
        if (page.getParseData() instanceof HtmlParseData) {
            String html = ((HtmlParseData) page.getParseData()).getHtml();
            Document d;
            try {
                d = Jsoup.parse(new ByteArrayInputStream(html.getBytes("UTF-8")), "UTF-8", "http://www.baomoi.com");
                d.outputSettings().escapeMode(Entities.EscapeMode.xhtml);
                Elements e = d.select("div.article-body");
                if (!e.isEmpty()) {
                    insertPage(page, e);
                }
            } catch (IOException ex) {
                Logger.getLogger(BaoMoiCrawler.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        System.out.println("=============");
    }

    public String getHTML(Elements e, String query, String defaultValue) {
        Elements tmp = e.select(query);
        if (!tmp.isEmpty()) {
            String result = tmp.first().html().toString();
//            result = result.replace("&lt;", "<")
//                        .replace("&gt;", ">")
//                        .replace("&amp;", "&")
//                        .replace("&apos;", "'")
//                        .replace("&quot;", "\"");
            return result;
        }
        return defaultValue;
    }

    public String getText(Elements e, String query, String defaultValue) {
        Elements tmp = e.select(query);
        if (!tmp.isEmpty()) {
            String result = tmp.first().text().toString();
            return result;
        }
        return defaultValue;
    }

    public void insertPage(Page page, Elements article) {
        String url = page.getWebURL().getURL();
        String des = getText(article, "h2.summary", "");
        String time = getText(article, "span.time", "");
        String title = getText(article, "h1.title", "");
        String content = getHTML(article, "div[itemprop=articleBody] table td", "");

        System.out.println("URL: " + url);
        System.out.println("des: " + des);
        System.out.println("time: " + time);
        System.out.println("title: " + title);
        System.out.println("content: " + content);
    }
}
