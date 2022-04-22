package com.accenture.chicagocrime;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kinesis.KinesisClient;
import software.amazon.awssdk.services.kinesis.model.DescribeStreamResponse;
import software.amazon.awssdk.services.kinesis.model.DescribeStreamRequest;
import software.amazon.awssdk.services.kinesis.model.Shard;
import software.amazon.awssdk.services.kinesis.model.GetShardIteratorRequest;
import software.amazon.awssdk.services.kinesis.model.GetShardIteratorResponse;
import software.amazon.awssdk.services.kinesis.model.Record;
import software.amazon.awssdk.services.kinesis.model.GetRecordsRequest;
import software.amazon.awssdk.services.kinesis.model.GetRecordsResponse;
import java.util.ArrayList;
import java.util.List;

import java.util.Scanner;
import java.io.FileWriter;   // Import the FileWriter class
import java.io.PrintWriter;
import java.io.IOException;

import com.amazonaws.services.kinesis.AmazonKinesis;
//snippet-end:[kinesis.java2.getrecord.import]


//Imports for Writing data into S3

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;

/**
 * Demonstrates how to read data from a Kinesis data stream. Before running this
 * Java code example, populate a data stream by running the StockTradesWriter
 * example. Then you can use that populated data stream for this example.
 */

public class CrimeDataS3Put {

	/*public static final String AWS_ACCESS_KEY = "aws.accessKeyId";
	public static final String AWS_SECRET_KEY = "aws.secretKey";

	static {
		// add your AWS account access key and secret key

		// acloudguru access key & secret access key

		System.setProperty(AWS_ACCESS_KEY, "AKIA2TBLTTPSBC4IMFF6");
        System.setProperty(AWS_SECRET_KEY, "oKqIUBewqfXNhboghNLx4ZveKtwEopQCkWlWgAl9");
	}*/
	
	
	
	public static void main(String[] args) {
		
	//	AmazonKinesis kinesisClient = AwsKinesisClient.getKinesisClient();

		final String USAGE = "\n" + "Usage:\n" + "    GetRecords <streamName>\n\n" + "Where:\n"
				+ "    streamName - The Kinesis data stream to read from (i.e., StockTradeStream)\n\n" + "Example:\n"
				+ "    GetRecords streamName\n";

		/*
		 * if (args.length < 1) { System.out.println(USAGE); System.exit(1); }
		 */

		String streamName = "chicagoCrimeStream";

		Region region = Region.US_EAST_1;
		KinesisClient kinesisClient = KinesisClient.builder().region(region).build();

		getCrimeDataRecords(kinesisClient, streamName);
	}

	// snippet-start:[kinesis.java2.getrecord.main]
	public static void getCrimeDataRecords(KinesisClient kinesisClient, String streamName) {

		String shardIterator;
		String lastShardId = null;

		// Retrieve the shards from a data stream
		DescribeStreamRequest describeStreamRequest = DescribeStreamRequest.builder().streamName(streamName).build();
		List<Shard> shards = new ArrayList<>();

		DescribeStreamResponse streamRes;
		do {
			streamRes = kinesisClient.describeStream(describeStreamRequest);
			shards.addAll(streamRes.streamDescription().shards());

			if (shards.size() > 0) {
				lastShardId = shards.get(shards.size() - 1).shardId();
			}
		} while (streamRes.streamDescription().hasMoreShards());

		GetShardIteratorRequest itReq = GetShardIteratorRequest.builder().streamName(streamName)
				.shardIteratorType("TRIM_HORIZON").shardId(shards.get(0).shardId()).build();
		
	//Possible Values:- 	AT_SEQUENCE_NUMBER ; AFTER_SEQUENCE_NUMBER ; AT_TIMESTAMP ;	TRIM_HORIZON ;	LATEST

		GetShardIteratorResponse shardIteratorResult = kinesisClient.getShardIterator(itReq);
		shardIterator = shardIteratorResult.shardIterator();

		// Continuously read data records from a shard
		List<Record> records;

		// Create a GetRecordsRequest with the existing shardIterator,
		// and set maximum records to return to 1000
		GetRecordsRequest recordsRequest = GetRecordsRequest.builder().shardIterator(shardIterator).limit(1000).build();

		GetRecordsResponse result = kinesisClient.getRecords(recordsRequest);

		// Put result into a record list, result might be empty
		records = result.records();

				
		// To write the Records into a File on the local machine
		
		Scanner myObj = new Scanner(System.in);
		System.out.println("Enter the File Path to Write the Data");
		String OutputPath = myObj.nextLine();
		try
		{
			FileWriter myWriter = new FileWriter(OutputPath);
			PrintWriter print_line = new PrintWriter(myWriter);
			
			for (Record record : records) {
			SdkBytes byteBuffer = record.data();
			
			print_line.printf("%s\n",new String(byteBuffer.asByteArray()));
			
			//System.out.println(
				//	String.format("Seq No: %s - %s", record.sequenceNumber(), new String(byteBuffer.asByteArray())));
		    }
			print_line.close();
		}
		catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    } 

	// Pushing Data into S3
		
	 Regions clientRegion = Regions.US_EAST_1;
	 String bucketName = "demoemrbucket1047925506";
	 String fileObjKeyName = "Inputfiles/CrimeDataset.csv";
	 //String fileName = "*** Path to file to upload ***";
	 
	 try {
         //This code expects that you have AWS credentials set up per:
         // https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/setup-credentials.html
         AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(clientRegion).build();
         
         // Upload a file as a new object with ContentType and title specified.
         PutObjectRequest request = new PutObjectRequest(bucketName, fileObjKeyName, new File(OutputPath));
         ObjectMetadata metadata = new ObjectMetadata();
         metadata.setContentType("plain/text");
         metadata.addUserMetadata("title", "someTitle");
         request.setMetadata(metadata);
         s3Client.putObject(request);
	 } catch (AmazonServiceException e) {
         // The call was transmitted successfully, but Amazon S3 couldn't process 
         // it, so it returned an error response.
         e.printStackTrace();
     } catch (SdkClientException e) {
         // Amazon S3 couldn't be contacted for a response, or the client
         // couldn't parse the response from Amazon S3.
         e.printStackTrace();
     }
         
         // snippet-end:[kinesis.java2.getrecord.main]

	 }       
}
