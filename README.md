AWS S3 이미지 파일 리사이징 업로드

REST API 호출시 이미지 파일 S3로 업로드 및 S3 업로드 시 트리거를 통해 Lambda 동작하여 리사이징된 이미지 파일 다른 S3 업로드 해보기

구성 Skill :  Spring Boot + Lambda + S3

1. S3 원본 이미지 버킷과 리사이징 이미지 버킷 생성
2. IAM 사용자 추가 및 S3 사용 권한 부여
3. Java Controller, Lambda 소스 및 빌드
4. Lambda 트리거 구성
5. AWS Lambda 원본 저장, 리사이징 저장 처리 결과
