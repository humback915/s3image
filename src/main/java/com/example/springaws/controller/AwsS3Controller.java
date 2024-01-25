package com.example.springaws.controller;

import com.example.springaws.service.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/s3")
public class AwsS3Controller {

    private final AwsS3Service awsS3Service;

    /**
     * input
     * @param multipartFileList
     * @return
     */
    @PostMapping("/inputImage")
    public List<String> uploadFile(@RequestPart(name ="files", required = false) List<MultipartFile> multipartFileList){
        return awsS3Service.uploadFile(multipartFileList);
    }

    /**
     * delete
     * @param fileName
     * @return
     */
    @DeleteMapping("/deleteImage")
    public ResponseEntity<Void> deleteFile(@RequestParam(name = "fileName",required = false) String fileName){
        System.out.println("file : "+fileName);
        awsS3Service.deleteFile(fileName);
        return null;
    }
}
