package newscrawler.database;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import newscrawler.Main;

public class MongoConnector {
    public static MongoClient mongo;
    public static DB db;
    
    public static DB init(Properties config) {
        if (config == null) config = Main.config;
        try {
            mongo = new MongoClient(config.getProperty("mongo.host"), Integer.valueOf(config.getProperty("mongo.port")));
            db = mongo.getDB(config.getProperty("mongo.db"));
            return db;
        } catch (UnknownHostException ex) {
            Logger.getLogger(MongoConnector.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        return null;
    }
    
    public static DB getDB() {
        if (db == null) return init(null);
        return db;
    }
}