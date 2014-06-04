package newscrawler.database;

import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.OperationStatus;
import java.util.Properties;
import newscrawler.Main;
import newscrawler.crawler.CrawlConfig;
import newscrawler.frontier.DocIDServer;
import newscrawler.util.Util;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

public class NewsIDRedisServer extends DocIDServer {

    protected static NewsIDRedisServer _instance;
    protected static Jedis _connection;
    protected String docIdKeyName = "crawler_doc_id";
    protected String urlKeyName = "crawler_urls";
    protected String idKeyName = "crawler_article_id";

    public NewsIDRedisServer(CrawlConfig config) {
        super(config);
        if (Main.config.getProperty("notResetURLQueue") == null 
                || !Boolean.valueOf(Main.config.getProperty("notResetURLQueue"))) {
            getConnection().del(docIdKeyName);
            getConnection().del(urlKeyName);
            getConnection().del(idKeyName);
        }
        _instance = this;
    }

    public static NewsIDRedisServer getInstance() {
        if (_instance == null) {
            _instance = new NewsIDRedisServer(null);
        }
        return _instance;
    }

    protected static Jedis getConnection() {
        Properties cfg = Main.config;
        if (_connection == null) {
            _connection = RedisConnector.connect(cfg.getProperty("redis.idserver.host"),
                    Integer.valueOf(cfg.getProperty("redis.idserver.port")),
                    Integer.valueOf(cfg.getProperty("redis.idserver.db")));
        }
        return _connection;
    }

    public boolean resetId() {
        return getConnection().set(this.getArticleIdName(), "0").equals("OK");
    }

    public String getArticleIdName() {
        return this.idKeyName;
    }

    public void setArticleIdName(String value) {
        this.idKeyName = value;
    }

    public long getNewArticleId() {
        return getConnection().incr(this.getArticleIdName());
    }

    public long nextArticleId() {
        return getConnection().incr(this.getArticleIdName());
    }

    public long getArticleId() {
        return Long.valueOf(getConnection().get(this.getArticleIdName()));
    }

    public long getNewDocId() {
        return getConnection().incr(docIdKeyName);
    }

    public long nextDocId() {
        return getConnection().incr(docIdKeyName);
    }
    
    @Override
    public int getDocId(String url) {
        synchronized (mutex) {
            String hash = Util.md5(url);
            String rs = getConnection().hget(urlKeyName, hash);
            if (rs != null) {
                return Integer.valueOf(rs);
            }

            return -1;
        }
    }

    @Override
    public int getNewDocID(String url) {
        synchronized (mutex) {
            // Make sure that we have not already assigned a docid for this URL
            String hashed = Util.md5(url);
            int docId = getDocId(url);
            if (docId > 0) {
                return docId;
            }

            docId = Integer.valueOf(
                    String.valueOf(nextDocId()));
            getConnection().hset(urlKeyName, hashed, String.valueOf(docId));

            return docId;
        }
    }

    @Override
    public void addUrlAndDocId(String url, int docId) throws Exception {
        synchronized (mutex) {
            int lastDocId = this.getDocCount();
            if (docId <= lastDocId) {
                logger.error("Requested doc id: " + docId + " is not larger than: " + lastDocId);
                return;
            }

            int prevDocid = getDocId(url);
            if (prevDocid > 0) {
                if (prevDocid == docId) {
                    return;
                }
                logger.error("Doc id: " + prevDocid + " is already assigned to URL: " + url);
            }

            getConnection().hset(urlKeyName, Util.md5(url), String.valueOf(docId));
        }
    }

    @Override
    public boolean isSeenBefore(String url) {
        return getDocId(url) != -1;
    }

    @Override
    public int getDocCount() {
        String rs = getConnection().get(docIdKeyName);
        if (rs != null) return Integer.valueOf(rs);
        return 0;
    }

    @Override
    public void sync() {
        return;
    }

    @Override
    public void close() {
        return;
    }
    
    public void test() {
        System.out.println(getConnection().ping());
        System.out.println(this.getDocId("http://test"));
        System.out.println(this.getNewDocID("http://test"));
        System.out.println(this.getDocId("http://test"));

    }
}
