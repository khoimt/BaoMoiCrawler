package newscrawler.database;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import newscrawler.crawler.NewsPage;

public class NewsMongo {
    protected static NewsMongo _instance;
    protected static DBCollection collection;
    protected static String name = "baomoi";
    
    public static NewsMongo getInstance() {
        if (_instance == null) {
            _instance = new NewsMongo();
        }
        return _instance;
    }
    
    public static DBCollection getCollection() {
        if (collection == null)
            collection = MongoConnector.getDB().getCollection(name);
        return collection;
    }

    public void insert(NewsPage page) {
        BasicDBObject document = new BasicDBObject();
        document.put("id", page.getId());
        document.put("url", page.getURL());
        document.put("title", page.getTitle());
        document.put("time", page.getTime());
        document.put("description", page.getDescription());
        document.put("content", page.getContent());
        document.put("keywords", page.getKeywords());
        getCollection().insert(document);
    }
}
