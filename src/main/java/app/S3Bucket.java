package app;

import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;

import java.util.List;
import java.util.UUID;
import java.io.File;
import java.io.IOException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

public class S3Bucket {
	
	public static AmazonS3 s3;
	
	public S3Bucket(AmazonS3 s3) {
		S3Bucket.s3 = s3;
	}
	
	public Bucket getBucket(String bucket_name) {
        Bucket named_bucket = null;
        List<Bucket> buckets = s3.listBuckets();
        for (Bucket b : buckets) {
            if (b.getName().equals(bucket_name)) {
                named_bucket = b;
            }
        }
        return named_bucket;
    }
	
	public Bucket createBucket(String bucket_name) {
        Bucket b = null;
        try {
            System.out.println("Creating bucket " + bucket_name + "\n");
            b = s3.createBucket(bucket_name);
            System.out.println("Bucket created!");
        } catch (AmazonS3Exception e) {
            System.err.println(e.getErrorMessage());
        }
        return b;
    }
    
    public void deleteBucket(String bucket_name) {
    	try {
    		s3.deleteBucket(bucket_name);
    	} catch (AmazonS3Exception e) {
    	    System.err.println(e.getErrorMessage());
    	}
    }
    
    public String generateKey() {
    	return UUID.randomUUID().toString();
    }
    
    public String uploadObject(String bucket_name, String pathname) {
        String key = "";

        try {
        	System.out.println("Uploading file located at: " + pathname + " to bucket: " + bucket_name);
            // Upload a file as a new object with ContentType and title specified.
        	key = generateKey();
        	System.out.println("Key is " + key);
            PutObjectRequest request = new PutObjectRequest(bucket_name, key, new File(pathname));
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image");
            metadata.addUserMetadata("x-amz-meta-title", "someTitle");
            request.setMetadata(metadata);
            s3.putObject(request);
        }
        catch(AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process 
            // it, so it returned an error response.
            e.printStackTrace();
        }
        catch(SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }
        return key;
    }
    
    public S3Object getObject(String bucket_name, String key) {
    	S3Object object = null;
        try {
        	object = s3.getObject(new GetObjectRequest(bucket_name, key));
        }
        catch(AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process 
            // it, so it returned an error response.
            e.printStackTrace();
        }
        catch(SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }
        return object;
    }

}
