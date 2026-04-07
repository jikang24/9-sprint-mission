package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.InputStream;
import java.time.Duration;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Component
// discodeit.storage.type=s3 일때만 Bean으로 등록
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
public class S3BinaryContentStorage implements BinaryContentStorage {

  private final String accessKey;
  private final String secretKey;
  private final String region;
  private final String bucket;

  // 생성자에 @Value로 application.yml에서 값 주입
  public S3BinaryContentStorage(
      @Value("${discodeit.storage.s3.access-key}") String accessKey,
      @Value("${discodeit.storage.s3.secret-key}") String secretKey,
      @Value("${discodeit.storage.s3.region}") String region,
      @Value("${discodeit.storage.s3.bucket}") String bucket) {
    this.accessKey = accessKey;
    this.secretKey = secretKey;
    this.region = region;
    this.bucket = bucket;
  }

  private S3Client getS3Client() {
    StaticCredentialsProvider credentials = StaticCredentialsProvider.create(
        AwsBasicCredentials.create(accessKey, secretKey)
    );
    return S3Client.builder()
        .region(Region.of(region))
        .credentialsProvider(credentials)
        .build();
  }

  private String generatePresignedUrl(String key, String contentType) {
    StaticCredentialsProvider credentials = StaticCredentialsProvider.create(
        AwsBasicCredentials.create(accessKey, secretKey)
    );
    try (S3Presigner presigner = S3Presigner.builder()
        .region(Region.of(region))
        .credentialsProvider(credentials)
        .build()) {

      PresignedGetObjectRequest presigned = presigner.presignGetObject(
          GetObjectPresignRequest.builder()
              .signatureDuration(Duration.ofMinutes(10))
              .getObjectRequest(GetObjectRequest.builder()
                  .bucket(bucket)
                  .key(key)
                  .build())
              .build()
      );
      return presigned.url().toString();
    }
  }

  @Override
  public UUID put(UUID binaryContentId, byte[] bytes) {
    getS3Client().putObject(
        PutObjectRequest.builder()
            .bucket(bucket)
            .key(binaryContentId.toString())
            .build(),
        RequestBody.fromBytes(bytes)
    );
    return binaryContentId;
  }

  @Override
  public InputStream get(UUID binaryContentId) {
    return getS3Client().getObject(
        GetObjectRequest.builder()
            .bucket(bucket)
            .key(binaryContentId.toString())
            .build()
    );
  }

  @Override
  public ResponseEntity<?> download(BinaryContentDto metaData) {
    String url = generatePresignedUrl(
        metaData.id().toString(),
        metaData.contentType()
    );
    return ResponseEntity
        .status(HttpStatus.FOUND)          // 302 리다이렉트
        .header(HttpHeaders.LOCATION, url) // URL로 이동
        .build();
  }
}
