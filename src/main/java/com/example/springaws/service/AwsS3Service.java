package com.example.springaws.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AwsS3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public List<String> uploadFile(List<MultipartFile> multipartFileList){
        List<String> fileNameList = new ArrayList<>();

        // MultipartFile 타입으로 들어온 list를 순환하면서 하나씩 처리
        multipartFileList.forEach(file -> {
            // 파일 이름
            String fileName = createFileName(file.getOriginalFilename());

            // S3에서 보내고 받는 HTTP헤더가 포함된 객체 메타데이터 지정
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try(InputStream inputStream = file.getInputStream()){
                // 업로드 요청 및 외부 접근 허용
                amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            }catch(IOException e){
                // 에러 예외처리
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"파일 업로드 실패");
            }
            fileNameList.add(fileName);
        });

        System.out.println("fileNameList : "+fileNameList);
        return fileNameList;
    }
    private String createFileName(String fileName){
        //return UUID.randomUUID().toString().concat(fileName);
        return fileName;
    }
    public void deleteFile(String fileName){
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
    }
}
