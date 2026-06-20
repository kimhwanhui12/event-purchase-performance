# 🚀 PerfScale

> 대규모 트래픽 환경에서의 이커머스 시스템 아키텍처 개선 및 성능 최적화

## 개요
한림대학교 멋쟁이사자처럼 14기 백엔드 심화 세션 프로젝트입니다.  
단일 서버에서 분산 시스템으로 진화하며 발생하는 병목 현상을 해결하고 아키텍처의 확장성(Scalability)을 실험합니다.

## 기술 스택
- **Java 21** / **Spring Boot**
- **AWS EC2 / RDS / ECR / S3 / CodeDeploy**
- **Docker**
- **Nginx**
- **Gradle**

## CI/CD 파이프라인
`main` 브랜치에 push 시 GitHub Actions가 자동으로 실행됩니다.

```
Push → Gradle Build → Docker Image → ECR Push → S3 Upload → CodeDeploy → EC2
```

## 실행 방법

```bash
# 로컬 빌드
./gradlew bootJar -x test

# Docker 빌드
docker build -t perfscale .
```
