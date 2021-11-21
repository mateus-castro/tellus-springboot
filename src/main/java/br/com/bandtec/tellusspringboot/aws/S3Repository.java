package br.com.bandtec.tellusspringboot.aws;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.HeadBucketRequest;
import com.amazonaws.waiters.WaiterParameters;

import java.io.File;

public class S3Repository {
    private final AmazonS3Client s3Client;
    public S3Repository(AmazonS3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String criarBucket(){
        s3Client
                .waiters()
                .bucketExists()
                .run(
                        new WaiterParameters<>(
                                new HeadBucketRequest("my-awesome-bucket")
                        )
                );
        return "OK";
    }

    public void putFile(File file){
        s3Client.putObject("my-bucket", "my-key", file).isRequesterCharged();
    }

}