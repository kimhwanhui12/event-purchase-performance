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

## 도메인 모델
- `User`, `Cart`/`CartItem`, `Order`/`OrderItem`, `Product`/`ProductOption`, `Brand`, `Category`
- `Main`(Banner / Section / SectionProduct), `QuickMenu`
- `Payment`, `Wish`(좋아요·찜), `RecentView`(최근 본 상품)
- 전체 테이블 스키마는 [`멋사세션_심화.sql`](./멋사세션_심화.sql) 참고 (dbdiagram 스타일 DDL 문서, 실제 DB는 Hibernate `ddl-auto`로 생성됨)

## 구현된 API
### 상품 (`/api/v1/products`)
- `GET /products` — 키워드/카테고리/성별 필터, 무한 스크롤 페이징(count 쿼리 없이 size+1 커서 방식)
- `GET /products/{id}` — 상세 + 옵션 목록

### 장바구니 (`/api/v1/carts`)
- `GET /carts`, `POST /carts`(동일 상품+옵션 수량 병합), `PATCH /carts/{cartItemId}`
- `DELETE /carts/{cartItemId}`, `DELETE /carts?cartItemIds=1,2,3`, `DELETE /carts/clear`
- `POST /carts/calculate`

### 주문 (`/api/v1/orders`)
- `POST /orders` — 재고/판매상태 검증, 재고 차감, 상품 스냅샷 저장, 주문 코드(`ORD-yyyyMMdd-000001`) 발급
- `GET /orders`, `GET /orders/{orderId}`, `POST /orders/{orderId}/cancel` — 취소 시 재고 복원

## 개발 현황
- 인증(JWT/Redis)은 아직 없습니다. `@CurrentUserId`로 고정 테스트 유저를 주입하는 자리표시자로 우회 중이며, 서버 기동 시 `DataInitializer`가 테스트 유저/상품 시드 데이터를 생성합니다.
- 결제, 마이페이지, 좋아요 토글, 카테고리/메인배너 API, 관리자 주문상태 변경은 다음 라운드에서 구현 예정입니다.
