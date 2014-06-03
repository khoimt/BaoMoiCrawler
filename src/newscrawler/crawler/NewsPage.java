package newscrawler.crawler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import newscrawler.NewsCrawler;
import newscrawler.database.NewsIDRedisServer;
import newscrawler.parser.HtmlParseData;
import newscrawler.url.WebURL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.jsoup.select.Elements;

public class NewsPage extends Page {

    protected String baseURL = "http://www.baomoi.com";
    protected Elements mainContent = null;
    protected long _id = -1;

    public NewsPage(WebURL url) {
        super(url);
    }
    
    public long assignId() {
        this.setId(NewsIDRedisServer.getInstance().nextArticleId());
        return this.getId();
    }
    
    public long getId () {
        return this._id;
    }
    
    public void setId(long id) {
        this._id = id;
    }

    public boolean isArticle() {
        return (this.getMainContent() != null && !this.getContent().isEmpty());
    }

    public Elements getMainContent() {
        if (this.mainContent != null) {
            return this.mainContent;
        }

        if (this.getParseData() instanceof HtmlParseData) {
            String html = ((HtmlParseData) this.getParseData()).getHtml();
            Document d;
            try {
                d = Jsoup.parse(new ByteArrayInputStream(html.getBytes("UTF-8")), "UTF-8", this.baseURL);
                d.outputSettings().escapeMode(Entities.EscapeMode.xhtml);
                return d.select("div.article-body");
            } catch (IOException ex) {
                Logger.getLogger(NewsCrawler.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }

        return null;
    }

    public String getHTML(String query, String defaultValue) {
        Elements e = this.getMainContent();
        if (e == null) {
            return defaultValue;
        }

        Elements rsElements = e.select(query);
        if (!rsElements.isEmpty()) {
            String result = rsElements.first().html().toString();
//            result = result.replace("&lt;", "<")
//                        .replace("&gt;", ">")
//                        .replace("&amp;", "&")
//                        .replace("&apos;", "'")
//                        .replace("&quot;", "\"");
            return result;
        }
        return defaultValue;
    }

    public String getText(String query, String defaultValue) {
        Elements e = this.getMainContent();
        if (e == null) {
            return defaultValue;
        }

        Elements rsElements = e.select(query);
        if (!rsElements.isEmpty()) {
            return rsElements.first().text();
        }
        return defaultValue;
    }

    public String getURL() {
        return this.getWebURL().getURL();
    }

    public String getDescription() {
        return this.getDescription(null);
    }

    public String getDescription(String query) {
        if (query != null) {
            return this.getText(query, "");
        }
        return this.getText("h2.summary", "");
    }

    public String getTime() {
        return this.getTime(null);
    }

    public String getTime(String query) {
        if (query != null) {
            return this.getText(query, "");
        }
        return this.getText("span.time", "");
    }

    public String getTitle() {
        return this.getTitle(null);
    }

    public String getTitle(String query) {
        if (query != null) {
            return this.getText(query, "");
        }
        return this.getText("h1.title", "");
    }

    public String getContent() {
        return this.getContent(null);
    }

    public String getContent(String query) {
        if (query != null) {
            return this.getText(query, "");
        }
        return this.getText("div[itemprop=articleBody] table td", "");
    }

    public String[] getKeywords() {
        return this.getKeywords(null);
    }

    public String[] getKeywords(String query) {
        Elements keywords;
        String[] result;

        keywords = (query == null) ? this.getMainContent().select("ul.itemlisting a[itemprop=keywords]")
                : this.getMainContent().select(query);
        
        if (keywords.size() > 0) {
            result = new String[keywords.size()];
            for (int i = 0; i < result.length; i++) {
                result[i] = keywords.get(i).text();
            }
            return result;
        }
        return null;
    }
}
