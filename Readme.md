# Simple HTTP Server - Step 4

## 학습 목표

1. Thread Pool을 이용한 HTTP Server 구현
2. Producer-Consumer 패턴 이해 및 구현
3. Thread 동기화 메커니즘 학습
4. HTTP 서버의 기본 동작 원리 이해

## 주요 구현 과제

### 1. Worker Thread Pool 구현
- [WorkerThreadPool.java](/src/main/java/com/nhnacademy/http/WorkerThreadPool.java)
- Thread Pool 초기화 및 관리
- Thread 생명주기 제어 (start, stop)
- Thread 동기화 처리

### 2. Request Channel 구현 
- [RequestChannel.java](/src/main/java/com/nhnacademy/http/channel/RequestChannel.java)
- Producer-Consumer 패턴을 이용한 작업 큐 관리
- Thread-safe한 작업 큐 구현
- wait/notify를 통한 Thread 동기화

### 3. HTTP Request Handler 구현
- [HttpRequestHandler.java](/src/main/java/com/nhnacademy/http/HttpRequestHandler.java) 
- HTTP 요청 처리 로직 구현
- Thread Pool과 연동하여 요청 처리

### 4. Simple HTTP Server 구현
- [SimpleHttpServer.java](/src/main/java/com/nhnacademy/http/SimpleHttpServer.java)
- 서버 소켓 생성 및 클라이언트 연결 수락
- Request Channel을 통한 작업 분배

## 테스트 케이스

- Thread Pool 생성/제어 테스트
- Request Channel 동작 테스트  
- HTTP 요청/응답 처리 테스트
- 서버 기능 통합 테스트

## 기술 스택

- Java 21
- JUnit 5
- Lombok
- Logback