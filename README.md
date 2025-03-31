# NURIM 
Spring Boot를 활용한 공공서비스 신청 어플리케이션을 구현하였습니다.

# ⏰개발 기간
- 2025-03-24(월) ~ 2025-03-31(월)

# 팀원 소개
|      [팀장] 김성찬      |         [팀원] 이보연         |     [팀원] 홍수경         |        [팀원] 박성호        |        [팀원] 원채빈        |                                                                                                             
| :------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------: | :------------------------------------------------------------------------------: | 
|                      <img width="160px" src="https://github.com/user-attachments/assets/a2e14b87-8082-4b5a-95bf-03b10820163b" />    |                      <img width="160px" src="https://avatars.githubusercontent.com/u/151502121?v=4" />    |                   <img width="160px" src="https://avatars.githubusercontent.com/u/67555277?v=4"/>   |                   <img width="160px" src="https://avatars.githubusercontent.com/u/192585473?v=4"/>   |                   <img width="160px" src="https://avatars.githubusercontent.com/u/192691209?v=4"/>   |
|   [@friedSaewoo](https://github.com/friedSaewoo)   |    [@iboyeon0816](https://github.com/iboyeon0816)  | [@seiky17](https://github.com/seiky17)  | [@pshshsh](https://github.com/pshshsh)  | [@wonchaebin](https://github.com/wonchaebin)  |
# 🧀기능 담당
- 김성찬
  - 공지사항 관리 기능
  - 공지사항 조회수(캐싱 도전과제)
  - 서버 배포
- 이보연
  - 회원 관리 기능
  - 동시성 제어 (도전 과제)
  - 시연 영상
- 홍수경
  -  리뷰 관리 기능
  -  인기 검색어 기능
  -  검색 캐싱(in-memory Cache)
- 박성호
  - 프로그램 관리 기능
  - 프로그램 조회수(캐싱 도전과제)
- 원채빈
  - 프로그램 신청 관리
  - 쿼리 최적화 (도전과제) 
# 🔲 개발환경
- Version : Java 17
- IDE : IntelliJ
- Framework : SpringBoot 3.4.4
- ORM : JPA

# 🚀 실행 방법
### 아래 설정들을 추가한 후 실행해주세요
- 로컬 환경에 Redis 서버를 실행하신 후 프로젝트를 실행해주세요!
- application-db.properties
```
spring.datasource.url=jdbc:${본인_DB_URL}
spring.datasource.username=${USER_NAME}
spring.datasource.password=${PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update

spring.jpa.properties.hibernate.show_sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.use_sql_comments=true

spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

- application-local.properties
```
jwt.secret.key = ${SECRET_KEY}
jwt.access.expiration = ${ACCESS_EXP}
jwt.refresh.expiration = ${REFRESH_EXP}

spring.data.redis.host=localhost
spring.data.redis.port=6379
```

# 📋 주요기능
###  [1. 쿼리 최적화(Indexing)](https://github.com/sparta-team17/sparta-nurim/wiki/1.-%EC%B5%9C%EC%A0%81%ED%99%94(Indexing))
###  [2.동시성 제어](https://github.com/sparta-team17/sparta-nurim/wiki/2.-%EB%8F%99%EC%8B%9C%EC%84%B1-%EC%A0%9C%EC%96%B4) 
###  [3. 캐싱](https://github.com/sparta-team17/sparta-nurim/wiki/3.-%EC%BA%90%EC%8B%B1)

# ERD
![Image](https://github.com/user-attachments/assets/57ae314e-c729-4900-9121-68f89741605c)
# API
<details>
<summary>회원</summary>
<div markdown="1">

![Image](https://github.com/user-attachments/assets/f6a9309b-8786-4d5f-991f-1f749ac6849d)

![Image](https://github.com/user-attachments/assets/7741e69d-1fae-4ba4-8e8f-02869c70ec68)

</div>
</details>

<details>
<summary>프로그램</summary>
<div markdown="1">

![Image](https://github.com/user-attachments/assets/6124e03a-0c95-493b-a1a4-b57ccd06a5d7)

![Image](https://github.com/user-attachments/assets/8dca0d18-bea2-44db-9fe6-c08d1ff54012)

</div>
</details>

<details>
<summary>공지사항</summary>
<div markdown="1">

![Image](https://github.com/user-attachments/assets/b5ba2d37-52de-41fa-b3c7-b18c705540ac)

![Image](https://github.com/user-attachments/assets/d952cf23-4a45-4586-804c-4af02fcd327f)

</div>
</details>

<details>
<summary>리뷰</summary>
<div markdown="1">

![Image](https://github.com/user-attachments/assets/911974f9-fb4e-43ae-b206-d409a92c18e7)

![Image](https://github.com/user-attachments/assets/2d5e32a1-b5e5-45bb-b3f1-f69292fb75cc)

</div>
</details>

<details>
<summary>신청, 인기검색어</summary>
<div markdown="1">

![Image](https://github.com/user-attachments/assets/cc65e66b-1362-4a7a-891e-61eefedbe86e)

</div>
</details>

# 트러블슈팅
- [[트러블 슈팅] EC2 배포 시 RDS 연결 오류 해결: Connection refused](https://velog.io/@zriag/%ED%8A%B8%EB%9F%AC%EB%B8%94-%EC%8A%88%ED%8C%85-EC2-%EB%B0%B0%ED%8F%AC-%EC%8B%9C-RDS-%EC%97%B0%EA%B2%B0-%EC%98%A4%EB%A5%98-%ED%95%B4%EA%B2%B0-Connection-refused-%ED%8A%B8%EB%9F%AC%EB%B8%94%EC%8A%88%ED%8C%85)
