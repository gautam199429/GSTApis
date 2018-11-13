package com.codecube.gst.config;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.springframework.beans.factory.annotation.Autowired;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.codecube.gst.entity.GSTR1Model;


public class AmazonS3Pull {
	public final static String accessKeyID = "AKIAIJEN6SVLTY36AAAA";
	public final static String secretKey = "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";
	public final static String bucketName = "codecubegst";
	
	static AWSCredentials credentials = new BasicAWSCredentials(
			  accessKeyID, 
			  secretKey
			);
	static AmazonS3 s3client = AmazonS3ClientBuilder
			  .standard()
			  .withCredentials(new AWSStaticCredentialsProvider(credentials))
			  .withRegion(Regions.AP_SOUTH_1)
			  .build();
	
	@Autowired
	GSTR1Model mod;
	
	@Autowired
	public static void getDataFromS3Bucket(String filehash)
	{
		try {
			ClientConfiguration clientConfiguration = new ClientConfiguration();
			clientConfiguration.setConnectionTimeout(1000000000);
			S3Object s3object = s3client.getObject(bucketName, filehash);
			S3ObjectInputStream inputStream = s3object.getObjectContent();
			BufferedReader buff = new BufferedReader(new InputStreamReader(inputStream));
			String line;
	        while ((line = buff.readLine()) != null) {      	
	        }
	        
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	public static void main(String[] args) {
		getDataFromS3Bucket("Game.mp3");
	}
	
}
