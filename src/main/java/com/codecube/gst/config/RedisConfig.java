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
	
	
	private final static String host = "localhost";
	static int port = 6379;
	static Jedis jedis = new Jedis(host,port);
	
	public static String redisGetauthtoken(String gstn)
	{
		String auth_token = "null";
		String response = "{\"auth_token\":\"null\"}";
		try {
			response = jedis.get(gstn+"authresponse");
			JSONParser parser1 = new JSONParser();
			JSONObject json = (JSONObject) parser1.parse(response);
			auth_token = (String) json.get("auth_token");
			return auth_token;
		}
		catch(Exception e)
		{
			System.out.println("Error in Connecting the Redis Servesr");
			System.out.println(e);
		}
		return auth_token;
	}
	public static String redisGetappkey(String gstn)
	{
		String response = "Generate App Key";
		try {
			response = jedis.get(gstn+"encodedappkey");
			return response;
		}
		catch(Exception e)
		{
			System.out.println("Error in Connecting the Redis Server");
			System.out.println(e);
		}
		return response;
	}
	public static String redisGetsek(String gstn)
	{
		String sek = "null";
		String response = "{\"sek\":\"null\"}";
		try {
			response = jedis.get(gstn+"authresponse");
			JSONParser parser1 = new JSONParser();
			JSONObject json = (JSONObject) parser1.parse(response);
			sek = (String) json.get("sek");
			return sek;
		}
		catch(Exception e)
		{
			System.out.println("Error in Connecting the Redis Servesr");
			System.out.println(e);
		}
		return sek;	
	}
	public static String redisGetEncryptedAppkey(String gstn)
	{
		String response = "Generate App Key";
		try {
			response = jedis.get(gstn+"encryptedappkey");
			return response;
		}
		catch(Exception e)
		{
			System.out.println("Error in Connecting the Redis Server");
			System.out.println(e);
		}
		return response;
	}
	public static String redisSetEncryptedAppkey(String gstn, String value)
	{
		String response = "null";
		try {
			jedis.set(gstn+"encryptedappkey", value);
		}
		catch(Exception e)
		{
			System.out.println("Error in Connecting the Redis Server");
			System.out.println(e);
		}
		return response;	
	}	
	public static String redisSetEncodedAppkey(String gstn, String value)
	{
		String response = "null";
		try {
			jedis.set(gstn+"encodedappkey", value);
		}
		catch(Exception e)
		{
			System.out.println("Error in Connecting the Redis Server");
			System.out.println(e);
		}
		return response;
	}
	
}
