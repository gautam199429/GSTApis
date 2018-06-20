/**
 * @author gautam kumar sah
 */


package com.codecube.gst.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;

import redis.clients.jedis.Jedis;

@Repository
public class RedisConfig {
	
	public void redisConfig()
	{
		try {
			String host = "localhost";
			int port = 6379;
			Jedis jedis = new Jedis(host,port);
		}
		catch(Exception e)
		{
			System.out.println("Error in Connecting the Redis Server");
			System.out.println(e);
		}
		
	}
}
