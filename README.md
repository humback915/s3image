# 프로젝트 이름

<p align="justify">
  AWS S3 이미지 파일 리사이징 업로드
</p>

목차

## 프로젝트 소개

<p align="justify">
REST API 호출시 이미지 파일 S3로 업로드 및 S3 업로드 시 트리거를 통해 Lambda 동작하여 리사이징된 이미지 파일 다른 S3 업로드 해보기
</p>

## 기술 스택
<p align="justify">
구성 Skill :  Spring Boot + Lambda + S3
</p>

<br>

## 설명 참조
<p align="justify">
블로그
</p>
<p align="justify">
https://def-xyj.tistory.com/entry/AWS-S3-%EC%9D%B4%EB%AF%B8%EC%A7%80-%ED%8C%8C%EC%9D%BC-%EB%A6%AC%EC%82%AC%EC%9D%B4%EC%A7%95-%EC%97%85%EB%A1%9C%EB%93%9C
</p>
<br>
## 구현 순서
<p align="justify">1. S3 원본 이미지 버킷과 리사이징 이미지 버킷 생성</p>
<p align="justify">3. IAM 사용자 추가 및 S3 사용 권한 부여</p>
<p align="justify">4. Java Controller, Lambda 소스 및 빌드</p>
<p align="justify">5. Lambda 트리거 구성</p>
<p align="justify">6. AWS Lambda 원본 저장, 리사이징 저장 처리 결과</p>

![](https://blog.kakaocdn.net/dn/HzA9S/btsAQeg25nC/WluRykzb814VGeK69a3iTK/img.png)

<p align="justify">오리지널 버킷</p>

![](https://blog.kakaocdn.net/dn/b31HsT/btsAQeahoBf/LuNcyYesL6MYbpDk5yfUm0/img.png)

<p align="justify">리사이징 버킷</p>

![](https://blog.kakaocdn.net/dn/0tbzj/btsAOJCk1O6/9EmEb4Ipg8jpxVwEz69Hp0/img.png)
<br>
