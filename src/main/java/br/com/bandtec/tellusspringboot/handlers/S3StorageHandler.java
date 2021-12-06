package br.com.bandtec.tellusspringboot.handlers;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

@Service
public class S3StorageHandler {

    private AmazonS3 s3Client;

    @Value("${amazonProperties.bucket.name}")
    private String bucketName;

    @PostConstruct
    private void initializeAmazon(){
        this.s3Client = new AmazonS3Client();
    }

    public String uploadFile(MultipartFile file){
        System.out.println(bucketName);
        File convertedFile = convertMultiPartFileToFile(file);
        String fileName = System.currentTimeMillis()+"_"+file.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, convertedFile));
        convertedFile.delete();
        return "File uploaded: " + fileName;
    }

    public byte[] downloadFile(String fileName){
        S3Object s3Object = s3Client.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try{
            byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public String deleteFile(String fileName){
        s3Client.deleteObject(bucketName, fileName);
        return fileName + " has been removed ...";
    }

    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(convertedFile)){
            fos.write(file.getBytes());
        } catch (IOException e){
            System.out.println("Erro ao converter MultipartFile em File " + e);
        }
        return convertedFile;
    }


}
