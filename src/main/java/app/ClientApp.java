package app;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.simpledb.AmazonSimpleDBClientBuilder;

public class ClientApp {
	
	static AmazonEC2      ec2;
    static AmazonS3       s3;
    
    // Taken from AwsConsoleApp.java sample
    private static void init() throws Exception {

        /*
         * The ProfileCredentialsProvider will return your [default]
         * credential profile by reading from the credentials file located at
         * (~/.aws/credentials).
         */
        ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
        try {
            credentialsProvider.getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (~/.aws/credentials), and is in valid format.",
                    e);
        }
        ec2 = AmazonEC2ClientBuilder.standard()
            .withCredentials(credentialsProvider)
            .withRegion("us-west-2")
            .build();
        s3  = AmazonS3ClientBuilder.standard()
            .withCredentials(credentialsProvider)
            .withRegion("us-west-2")
            .build();
    }
    
    public static void main(String[] args){
    	
    	//Create Bucket
    	S3Bucket bucket = new S3Bucket(s3);
    	String bucket_name = "karine_mellata" + S3Bucket.generateKey();
    	S3Bucket.createBucket(bucket_name);
    	
    	//Upload file
    	String pathname = "./img.png";
    	S3Bucket.uploadObject(bucket_name, pathname);
    	
    	//
    	
    }

}
