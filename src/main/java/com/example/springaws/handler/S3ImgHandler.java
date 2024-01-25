package com.example.springaws.handler;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification.S3EventNotificationRecord;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class S3ImgHandler implements RequestHandler<S3Event, String> {

    // max 사이즈
    private static final float MAX_WIDTH = 500;
    private static final float MAX_HEIGHT = 500;

    // 이미지 타입
    private final String JPG_TYPE = (String) "jpg";
    private final String JPG_MIME = (String) "image/jpeg";

    private final String JPEG_TYPE = (String) "jpeg";
    private final String JPEG_MIME = (String) "image/jpeg";

    private final String PNG_TYPE = (String) "png";
    private final String PNG_MIME = (String) "image/png";

    private final String GIF_TYPE = (String) "gif";
    private final String GIF_MIME = (String) "image/gif";

    @Override
    public String handleRequest(S3Event input, Context context) {

        try{

            // S3 recode 정보
            S3EventNotificationRecord s3record = input.getRecords().get(0);
            String originalBuckect = s3record.getS3().getBucket().getName();
            System.out.println("originalBuckect : "+originalBuckect);

            String key = s3record.getS3().getObject().getUrlDecodedKey();
            System.out.println("key : "+key);
            String resizedBucket = originalBuckect.replace("temp","resized");

            Matcher matcher = Pattern.compile(".*\\.([^\\.]*)").matcher(key);
            if(!matcher.matches()){
                System.out.println("키의 이미지 타입을 사용할 수 없습니다. : "+key);
                return "";
            }
            // 이미지 타입 체크
            String imageType = matcher.group(1);
            System.out.println("imageType : "+imageType);
            if(!(JPG_TYPE.equals(imageType)) && !(JPEG_TYPE.equals(imageType))
                && !(PNG_TYPE.equals(imageType)) && !(GIF_TYPE.equals(imageType))){
                System.out.println("이미지가 없습니다. : "+key);
                return "";
            }
            // s3 오리지널 버킷에서 이미지 다운
            AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();
            S3Object s3Object = s3Client.getObject(new GetObjectRequest(originalBuckect, key));

            InputStream objectData = s3Object.getObjectContent();
            BufferedImage originalImage = ImageIO.read(objectData);

            int originalHeight = originalImage.getHeight();
            int originalWidth = originalImage.getWidth();
            System.out.println("originalHeight : "+originalHeight);
            System.out.println("originalWidth : "+originalWidth);

            float scalingFactor = Math.min(MAX_WIDTH / originalWidth, MAX_HEIGHT / originalHeight);
            System.out.println("scalingFactor : "+scalingFactor);
            int width = (int) (scalingFactor * originalWidth);
            int height = (int) (scalingFactor * originalHeight);
            System.out.println("width : "+width);
            System.out.println("height : "+height);
            // 이미지 리사이징
            BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = resizedImage.createGraphics();
            g.setPaint(Color.white);
            g.fillRect(0,0,width,height);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(originalImage, 0, 0, width, height, null);
            g.dispose();
            // 리사이징된 이미지 포맷 설정
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(resizedImage, imageType, os);
            InputStream is = new ByteArrayInputStream(os.toByteArray());
            // ContentLength랑 ContentType 설정
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(os.size());

            if(JPG_TYPE.equals(imageType)){
                metadata.setContentType(JPG_MIME);
            }
            if(JPEG_TYPE.equals(imageType)){
                metadata.setContentType(JPEG_MIME);
            }
            if(PNG_TYPE.equals(imageType)){
                metadata.setContentType(PNG_MIME);
            }
            if(GIF_TYPE.equals(imageType)){
                metadata.setContentType(GIF_MIME);
            }
            // 리사이즈 버킷으로 업로딩
            String nkey = "resized-"+key;
            try{
                resizedBucket += "-resized";
                System.out.println("업로딩 중 : "+resizedBucket+"/"+nkey);
                s3Client.putObject(new PutObjectRequest(resizedBucket, nkey, is, metadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            }catch(AmazonServiceException e){
                System.out.println(e.getMessage());
                System.exit(1);
            }
            System.out.println("성공 : "+originalBuckect+"/"+key);
            System.out.println("리사이징 업로드 성공 : "+resizedBucket+"/"+nkey);

            return "Success";

        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }
}
