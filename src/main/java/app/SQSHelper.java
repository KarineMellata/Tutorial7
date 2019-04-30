package app;

import java.util.List;
import java.util.Map.Entry;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SetQueueAttributesRequest;

public class SQSHelper {
	
	public static AmazonSQS sqs;
	
	public SQSHelper(AmazonSQS sqs) {
		SQSHelper.sqs = sqs;
	}
	
	public String createQueue(String queue_name) {
		CreateQueueResult result = null;
		try {
			System.out.println("Creating a new SQS queue called " + queue_name);
			CreateQueueRequest createQueueRequest = new CreateQueueRequest(queue_name).addAttributesEntry("ReceiveMessageWaitTimeSeconds", "20");
			System.out.println("Hello");
			result = sqs.createQueue(createQueueRequest);
		} catch (AmazonSQSException e) {
		    if (!e.getErrorCode().equals("QueueAlreadyExists")) {
		        throw e;
		    }
		}
		
		String queue_url = result.getQueueUrl();
		
		// Enable long polling on an existing queue
        SetQueueAttributesRequest set_attrs_request = new SetQueueAttributesRequest()
                .withQueueUrl(queue_url)
                .addAttributesEntry("ReceiveMessageWaitTimeSeconds", "20");
        sqs.setQueueAttributes(set_attrs_request);
        
		return queue_url;
	}
	
	public void sendMessage(String queue_url, String message) {
		sqs.sendMessage(new SendMessageRequest(queue_url, message));
	}
	
	public void receiveMessage(String queue_url) {
		// Receive messages
        System.out.println("Receiving messages\n");
        ReceiveMessageRequest receive_request = new ReceiveMessageRequest()
                .withQueueUrl(queue_url)
                .withWaitTimeSeconds(20);
        List<Message> messages = sqs.receiveMessage(receive_request).getMessages();
        for (Message message : messages) {
            System.out.println("  Message");
            System.out.println("    Body:          " + message.getBody());
        }
        System.out.println();
	}
	
	public void deleteQueue(String queue_url) {
		// Delete a message
        System.out.println("Deleting a message.\n");
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queue_url);
        List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
        //System.out.println(messages.size());
        String messageReceiptHandle = messages.get(0).getReceiptHandle();
        sqs.deleteMessage(new DeleteMessageRequest(queue_url, messageReceiptHandle));
        sqs.deleteQueue(new DeleteQueueRequest(queue_url));
        System.out.println("Deleting the queue:" + queue_url);
	}
	
	public String getQueue() {
        return sqs.listQueues().getQueueUrls().get(0);
	}
	
}
