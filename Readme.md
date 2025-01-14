# Step 6: 주요 목표 및 구현 사항

## 1. HttpService 인터페이스 구현
- **요청 처리**
    - `HttpService` 인터페이스는 **GET**과 **POST** 메소드를 처리할 수 있도록 설계되었습니다.
    - 기본적으로 **GET 요청**에 대한 처리를 구현하며, **POST 요청**의 경우 예외를 발생시킵니다.
- **예외 처리**
    - **POST 요청**이 들어올 경우 `405 - Method Not Allowed` 예외를 발생시켜, 지원하지 않는 메소드에 대한 명확한 응답을 제공합니다.

---

## 2. 다양한 HttpService 구현체 개발
- **IndexHttpService**
    - `/index.html` 요청을 처리하는 서비스입니다.
    - `doGet()` 메소드를 구현하여 해당 페이지를 반환합니다.
- **InfoHttpService**
    - `/info.html` 요청을 처리하며, **URL 파라미터**를 읽어 HTML 템플릿에 값을 삽입하여 응답합니다.
- **NotFoundHttpService**
    - 요청된 페이지가 존재하지 않을 경우 `404 - Not Found` 응답을 생성합니다.
- **MethodNotAllowedService**
    - 지원하지 않는 **HTTP 메소드 요청**에 대해 `405 - Method Not Allowed` 응답을 생성합니다.

---

## 3. HttpRequestHandler 개선
- **요청에 따른 서비스 할당**
    - `HttpRequestHandler`는 요청 **URI**에 따라 적절한 `HttpService` 구현체를 할당하고,
      `service()` 메소드를 호출하여 요청을 처리합니다.
- **예외 상황 처리**
    - 요청 처리 중 발생할 수 있는 예외를 적절히 처리하여 서버의 안정성을 높입니다.
