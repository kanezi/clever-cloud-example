package com.kanezi.clevercloudexample;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import io.netty.util.internal.StringUtil;
import jakarta.annotation.PostConstruct;
import lombok.Value;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Service
@Value
@Log4j2
public class S3Service {

    S3Properties s3Properties;
    AmazonS3 s3Client;

    /**
     * Adds random characters to bucket name
     * used in case originally wanted bucket name already exists
     * @param bucketName original bucket name
     * @return original name with random characters and cleaned to satisfy bucket name creation rules
     */
    private String randomizeBucketName(String bucketName) {
        if (StringUtil.isNullOrEmpty(bucketName)) {
            return bucketName;
        }

        String cleanedBucketName = String
                .format("%s_%s", s3Properties.bucket, UUID.randomUUID())
                .replace("_", "")
                .replace("-", "");

        return cleanedBucketName
                // bucket name should be between 3 and 63 characters long
                .substring(0, Math.min(63, cleanedBucketName.length()));
    }

    private void createBucket() {
        try {
            s3Client.createBucket(new CreateBucketRequest(s3Properties.bucket, s3Properties.region));
        } catch (Exception e) {
            log.error("Error creating bucket: {}", e.getMessage());
        }
//        if (s3Client.doesBucketExistV2(s3Properties.bucket)) {
//            log.info("Bucket name is not available."
//                             + " Trying with a random Bucket name.");
//
//            s3Properties.setBucket(randomizeBucketName(s3Properties.bucket));
//
//            log.info("trying with bucket name: {}", s3Properties.bucket);
//        }

    }

    void listObjectsInBucket() {
        ObjectListing objectListing = s3Client.listObjects(s3Properties.bucket);
        for (S3ObjectSummary os : objectListing.getObjectSummaries()) {
            log.info("key: {} / size: {} / etag: {} / storage class: {}", os.getKey(), os.getSize(), os.getETag(),
                     os.getStorageClass());
        }
    }

    URL saveFile(File file) {
        s3Client.putObject(
                s3Properties.bucket,
                file.getName(),
                file
        );

        return s3Client.getUrl(s3Properties.bucket, file.getName());
    }

    /**
     * Saves user avatar to s3 bucket and grants read privilege to everyone
     * <p>
     * We can use more fine-grained approach with explicit access control list:
     * <br>
     * <code>
     *     PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, keyName, image);
     *     AccessControlList acl = new AccessControlList();
     *     acl.grantPermission(GroupGrantee.AllUsers, Permission.Read); //all users or authenticated
     *     putObjectRequest.setAccessControlList(acl);
     *     s3client.putObject(putObjectRequest);
     *</code>
     *
     * @param file to upload to s3 bucket
     * @return URL to download the file
     * @throws IOException handling getInputStream
     */
    URL saveFile(MultipartFile file) throws IOException {

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        PutObjectRequest putObjectRequest = new PutObjectRequest(s3Properties.bucket,
                                                                 file.getOriginalFilename(),
                                                                 file.getInputStream(),
                                                                 objectMetadata)
                // grant everyone the read access
                .withCannedAcl(CannedAccessControlList.PublicRead);

        s3Client.putObject(putObjectRequest);

        return s3Client.getUrl(s3Properties.bucket, file.getOriginalFilename());
    }

    void deleteFile(String key) {
        s3Client.deleteObject(s3Properties.bucket, key);
    }



    URL presignedUrl(String key) {
        LocalDate oneYearFromNow = LocalDate.now()
                                            .plusDays(4);
        return s3Client.generatePresignedUrl(s3Properties.bucket, key, Date.from(
                oneYearFromNow.atStartOfDay(ZoneId.systemDefault())
                              .toInstant()), HttpMethod.GET);
    }


    @PostConstruct
    void setUpS3() {
        if (!s3Client.doesBucketExistV2(s3Properties.bucket)) {
            log.info("Creating bucket");
            createBucket();
        } else {
            log.info("Bucket: {} already exists! Skip creation!", s3Properties.bucket);
        }
    }

}
