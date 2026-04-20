package com.sprint.mission.discodeit.storage.s3;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Properties;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Slf4j
public class AWSS3Test {

  private final S3Client s3Client;
  private final S3Presigner s3Presigner;
  private final String bucket;

  public AWSS3Test(Properties props) {
    String accessKey = props.getProperty("AWS_S3_ACCESS_KEY");
    String secretKey = props.getProperty("AWS_S3_SECRET_KEY");
    Region region = Region.of(props.getProperty("AWS_S3_REGION"));
    this.bucket = props.getProperty("AWS_S3_BUCKET");

    StaticCredentialsProvider credentials = StaticCredentialsProvider.create(
        AwsBasicCredentials.create(accessKey, secretKey)
    );

    this.s3Client = S3Client.builder()
        .region(region)
        .credentialsProvider(credentials)
        .build();

    this.s3Presigner = S3Presigner.builder()
        .region(region)
        .credentialsProvider(credentials)
        .build();
  }

  public String testUpload() {
    String key = "test/" + UUID.randomUUID() + ".txt";
    byte[] content = "Hello, S3! - discodeit test".getBytes();

    s3Client.putObject(
        r -> r.bucket(bucket).key(key).contentType("text/plain"),
        RequestBody.fromBytes(content)
    );

    log.info("[Upload] 성공 - key: {}", key);
    return key;
  }

  public void testDownload(String key) {
    try (ResponseInputStream<GetObjectResponse> response = s3Client.getObject(
        r -> r.bucket(bucket).key(key)
    )) {
      byte[] bytes = response.readAllBytes();
      log.info("[Download] 성공 - key: {}, size: {}bytes", key, bytes.length);
      log.info("[Download] 내용: {}", new String(bytes));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void testPresignedUrl(String key) {
    PresignedGetObjectRequest presigned = s3Presigner.presignGetObject(
        GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(10))
            .getObjectRequest(r -> r.bucket(bucket).key(key))
            .build()
    );

    URL url = presigned.url();
    log.info("[PresignedUrl] 성공 - 유효시간: 10분");
    log.info("[PresignedUrl] URL: {}", url);
  }

  private static Properties loadEnv() throws IOException {
    String[] candidates = {".env", "discodeit/.env"};
    for (String candidate : candidates) {
      Path path = Paths.get(candidate);
      if (Files.exists(path)) {
        Properties props = new Properties();
        try (InputStream input = new FileInputStream(path.toFile())) {
          props.load(input);
        }
        log.info(".env 로드 완료 - 경로: {}", path.toAbsolutePath());
        return props;
      }
    }
    throw new IOException(".env 파일을 찾을 수 없습니다. 현재 디렉토리: "
        + Paths.get("").toAbsolutePath());
  }

  public static void main(String[] args) throws IOException {
    Properties props = loadEnv();
    AWSS3Test test = new AWSS3Test(props);

    // 1. 업로드
    String key = test.testUpload();

    // 2. 다운로드
    test.testDownload(key);

    // 3. PresignedUrl 생성
    test.testPresignedUrl(key);
  }
}