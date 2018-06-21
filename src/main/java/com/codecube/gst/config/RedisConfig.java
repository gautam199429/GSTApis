/**
 * @author gautam kumar sah
 */


package com.codecube.gst.config;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Repository;

import redis.clients.jedis.Jedis;

@Repository
public class RedisConfig {
	
	@SuppressWarnings("resource")
	public static String redisGetauthtoken(String gstn)
	{
		String auth_token = "null";
		String response = "{\"auth_token\":\"null\"}";
		try {
			String host = "localhost";
			int port = 6379;
			Jedis jedis = new Jedis(host,port);
			response = jedis.get(gstn+"authresponse");
			JSONParser parser1 = new JSONParser();
			JSONObject json = (JSONObject) parser1.parse(response);
			auth_token = (String) json.get("auth_token");
		}
		catch(Exception e)
		{
			System.out.println("Error in Connecting the Redis Servesr");
			System.out.println(e);
		}
		
		return auth_token;
		
	}
	@SuppressWarnings("resource")
	public static String redisGetappkey(String gstn)
	{
		String response = "null";
		try {
			String host = "localhost";
			int port = 6379;
			Jedis jedis = new Jedis(host,port);
			response = jedis.get(gstn+"encodedappkey");
		}
		catch(Exception e)
		{
			System.out.println("Error in Connecting the Redis Server");
			System.out.println(e);
		}
		
		return response;
		
	}
	@SuppressWarnings("resource")
	public static String redisGetsek(String gstn)
	{
		String sek = "null";
		String response = "{\"auth_token\":\"null\"}";
		try {
			String host = "localhost";
			int port = 6379;
			Jedis jedis = new Jedis(host,port);
			response = jedis.get(gstn+"authresponse");
			JSONParser parser1 = new JSONParser();
			JSONObject json = (JSONObject) parser1.parse(response);
			sek = (String) json.get("sek");
		}
		catch(Exception e)
		{
			System.out.println("Error in Connecting the Redis Servesr");
			System.out.println(e);
		}
		
		return sek;
		
	}
	
	@SuppressWarnings("resource")
	public static String redisGetEncryptedAppkey(String gstn)
	{
		String response = "null";
		try {
			String host = "localhost";
			int port = 6379;
			Jedis jedis = new Jedis(host,port);
			response = jedis.get(gstn+"encryptedappkey");
		}
		catch(Exception e)
		{
			System.out.println("Error in Connecting the Redis Server");
			System.out.println(e);
		}
		
		return response;
		
	}
	
}
