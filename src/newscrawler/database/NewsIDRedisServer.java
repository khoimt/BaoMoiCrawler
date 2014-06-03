package newscrawler.database;

import java.util.Properties;
import newscrawler.Main;
import redis.clients.jedis.Jedis;

public class NewsIDRedisServer {
    protected static NewsIDRedisServer _instance;
    protected static Jedis _connection;
    protected String idKeyName = "article_id";
    
    public static NewsIDRedisServer getInstance() {
        if (_instance == null) {
            _instance = new NewsIDRedisServer();
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
    
    public long getNewArticleId () {
        return getConnection().incr(this.getArticleIdName());
    }
    
    public long nextArticleId () {
        return getConnection().incr(this.getArticleIdName());
    }
    
    public long getArticleId() {
        return Long.valueOf(getConnection().get(this.getArticleIdName()));
    }
    
    public void test() {
        System.out.println(getConnection().ping());
        System.out.println(getConnection().set("foo", "0"));
        System.out.println(getConnection().get("foo"));
        System.out.println(getConnection().incr("foo"));
        System.out.println(getConnection().get("foo"));
    } 
}
