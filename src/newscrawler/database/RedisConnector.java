package newscrawler.database;

import newscrawler.Main;
import redis.clients.jedis.Jedis;

public class RedisConnector {
    public static Jedis connect(String host, int port, int db) { 
        Jedis redisClient = new Jedis(host, port);
        redisClient.select(db);
        return redisClient; 
    }
    
    public static Jedis connect(String args[]) {
        return connect(args[0], Integer.valueOf(args[1]), Integer.valueOf(args[2]));
    }
}