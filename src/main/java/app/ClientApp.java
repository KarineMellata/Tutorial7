package app;

import java.util.concurrent.TimeUnit;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.simpledb.AmazonSimpleDBClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

public class ClientApp {
	
	static AmazonEC2      ec2;
    static AmazonS3       s3;
    static AmazonSQS	  sqs;
    
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
        
        s3  = AmazonS3ClientBuilder.standard()
            .withCredentials(credentialsProvider)
            .withRegion("us-west-2")
            .build();
        
        sqs = AmazonSQSClientBuilder.standard()
                .withCredentials(credentialsProvider)
                .withRegion(Regions.US_WEST_2)
                .build();
    }
    
    
    
    public static void main(String[] args) throws Exception{
    	
    	init();
    	
    	//Create Bucket
//    	S3Helper s3bucket = new S3Helper(s3);
//    	String bucket_name = "karine-mellata";
//    	s3bucket.createBucket(bucket_name);
//    	
//    	//Upload file
//    	String pathname = "/Users/karinemellata/Downloads/img.png";
//    	String key = s3bucket.uploadObject(bucket_name, pathname);
//    	
//    	//Get object
//    	S3Object object = s3bucket.getObject(bucket_name, key);
//    	System.out.println("Content-Type: "  + object.getObjectMetadata().getContentType());
    	
    	//s3bucket.deleteBucket(bucket_name);
    	
    	//Create queue
    	SQSHelper sqsHelper = new SQSHelper(sqs);
    	String queue_name = "karine-mellata";
    	String queue_url = sqsHelper.createQueue(queue_name);
    	sqsHelper.sendMessage(queue_url, "hello");
    	
    	//sqsHelper.deleteQueue("https://sqs.us-west-2.amazonaws.com/061164283712/MyQueue");
    }

}
