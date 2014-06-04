package newscrawler;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import newscrawler.database.NewsIDRedisServer;
import newscrawler.database.RedisConnector;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;

public class Test {
    public static void main(String args[]) throws Exception {
//        Jedis jedis = RedisConnector.connect("192.168.56.102", 6379, 1);
//        System.out.println(jedis.hget("hfoo", "foo"));
//        System.out.println(jedis.hget("hfoo", "bar"));
//        Bootstrap.dev("./conf/application.properties");
//        new NewsIDRedisServer(null).test();
        
        short x = 123;
        byte y = (byte) (23);
        
        Map<String, Object> map = new HashMap<>();
        map.put("foo", 123);
        map.put("bar", "Hello World");
        map.put("baz",2.33);
        map.put("bax", (int) x);
        map.put("bay", (int) y);
        
        JSONObject obj = new JSONObject(map);
//        JSONObject obj = new JSONObject();
//        obj.append("foo", 123);
//        obj.append("bar", "Hello World");
//        obj.append("baz", 2.33);
        
        String str = obj.toString();
        System.out.println(JSONObject.stringToValue(str));
        System.out.println(obj.get("foo"));
        System.out.println(obj.get("bar"));
        System.out.println(obj.get("baz"));
        
        JSONObject o2 = new JSONObject(str);
        System.out.println(o2);
        System.out.println((int)o2.get("foo") );
        System.out.println((String)o2.get("bar"));
        System.out.println((double)o2.get("baz"));
        System.out.println( Short.valueOf(
                                String.valueOf(
                                   (int) o2.get("bax")
                                )
                            )
                            );
        System.out.println((byte)o2.get("bay"));
//        System.out.println(JSONObject.stringToValue(str));
    }
}