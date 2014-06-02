package newscrawler.database;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import newscrawler.crawler.NewsPage;

public class NewsMongo {

    private static DBCollection collection;
    protected static String name = "baomoi";
    
    public static DBCollection getCollection() {
        if (collection == null)
            collection = MongoConnector.getDB().getCollection(name);
        return collection;
    }

    public static void insert(NewsPage page) {
        BasicDBObject document = new BasicDBObject();
        document.put("title", page.getTitle());
        document.put("time", page.getTime());
        document.put("description", page.getDescription());
        document.put("content", page.getContent());
        document.put("keywords", page.getKeywords());
        getCollection().insert(document);
    }
}
