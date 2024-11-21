<br/>
<br/>

# 1. Project Overview (프로젝트 개요)
- 프로젝트 이름: SPRING PLUS
- 프로젝트 설명: 프로젝트 성능 리팩토링 및 버그 수정에 중점을 둔 프로젝트

<br/>
<br/>


# 2. Key Features (주요 수정 사항)
- **AOP**:
  - 에러 수정 및 메소드 실행 순서 수정

- **JPA**:
  - 날짜 검색, 담당자 등록 시 cascade 기능 추가

- **QueryDSL**:
  - N+1 문제 해결
  - JPQL로 작성된 메서드를 QueryDSL로 변경
  - 검색 조건(제목, 생성일 범위, 닉네임)을 설정하여 일정 검색 가능

- **Spring Security**:
  - 기존 필터와 resolver 대신 spring security를 이용해 접근 권한 및 유저 권한 설정
  - JWT를 이용한 토큰 기반 인증 방식 유지

- **Transactional**:
  - 매니저 등록 시 로그 테이블 자동으로 저장


<br/>
<br/>


# 3. Language 
| SpringBoot | 


<br/>


