package com.hmsapp.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class BucketService {
    // Implement methods for interacting with AWS S3 bucket
    @Autowired
    private AmazonS3 amazonS3;
    public String uploadFile(MultipartFile file, String bucketName) {
        //check if the file is empty
        if(file.isEmpty()){
            throw new IllegalStateException("Cannot upload empty file");
        }
        try{
            // Convert the MultipartFile to a File object

            //In Java, a MultipartFile represents a file that has been uploaded via a web form, but it exists in memory or as a stream. To upload it to AWS S3 or perform operations that require a physical file (e.g., file-based APIs), it is necessary to convert the MultipartFile to a File object.

            //The AWS S3 SDK's putObject method expects a File object, InputStream, or a string as input for the content. Converting the MultipartFile to a File ensures compatibility with the SDK.

            File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());

            // Save the file to a temporary directory
            file.transferTo(convFile);
            try {
                // Upload the file to the specified S3 bucket
                amazonS3.putObject(bucketName,convFile.getName(), convFile);
                // Return the public URL of the uploaded file
                return amazonS3.getUrl(bucketName, file.getOriginalFilename()).toString();
            } catch (AmazonS3Exception s3Exception) {
                // Handle S3-specific errors
                return "Unable to upload file :" + s3Exception.getMessage();
            }catch(Exception e) {
                // Handle other exceptions during file upload
                throw new IllegalStateException("Failed to upload file");
            }

        } catch (IOException e) {
            // Handle exceptions while converting the MultipartFile
            throw new RuntimeException(e);
        }
    }
}
