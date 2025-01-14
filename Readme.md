# Step 7의 주요 목표 및 구현 사항

## 1. Context 인터페이스 및 ContextHolder 구현
- **Context 인터페이스**: 객체를 등록, 삭제, 조회할 수 있는 메소드를 정의합니다.
  ```java
  public interface Context {
      void setAttribute(String name, Object object);
      void removeAttribute(String name);
      Object getAttribute(String name);
  }
  ```

- **ContextHolder**: `Singleton` 패턴을 사용하여 `Context` 객체를 전역적으로 관리합니다. 이를 통해 서로 다른 스레드에서 `Context`에 접근할 수 있습니다.
  ```java
  public class ContextHolder {
      private static final Context context = new ApplicationContext();
      private ContextHolder(){}
      public static synchronized ApplicationContext getApplicationContext() {
          return (ApplicationContext) context;
      }
  }
  ```

## 2. HttpService 객체의 효율적 관리
- **객체 공유**: `IndexHttpService`와 `InfoHttpService` 객체를 `Context`에 등록하여, 여러 요청에서 동일한 객체를 재사용할 수 있도록 합니다. 이를 통해 객체 생성 비용을 줄이고, 서버의 성능을 향상시킵니다.
  ```java
  Context context = ContextHolder.getApplicationContext();
  context.setAttribute("/index.html", new IndexHttpService());
  context.setAttribute("/info.html", new InfoHttpService());
  context.setAttribute(CounterUtils.CONTEXT_COUNTER_NAME, 0L);
  ```

## 3. HttpRequestHandler 개선
- **서비스 객체의 재사용**: `HttpRequestHandler`에서 요청 URI에 따라 `Context`에 등록된 `HttpService` 객체를 가져와 요청을 처리합니다. 이를 통해 객체를 매번 생성하지 않고, 효율적으로 관리할 수 있습니다.
  ```java
  HttpService httpService = (HttpService) context.getAttribute(httpRequest.getRequestURI());
  if (httpService != null) {
      httpService.service(httpRequest, httpResponse);
  } else {
      // 404 Not Found 처리
  }
  ```

## 4. CounterUtils 개선
- **전역 카운터 관리**: `CounterUtils`를 통해 전역 카운터를 관리하고, `Context`에 등록하여 여러 스레드에서 공유할 수 있도록 합니다. 이를 통해 요청 수를 효율적으로 추적할 수 있습니다.


## 결론
`Step7`은 `Context`를 활용하여 `HttpService` 객체를 효율적으로 관리하고, 여러 스레드에서 공유할 수 있도록 하는 데 중점을 두고 있습니다. 이를 통해 서버의 성능을 향상시키고, 자원 사용을 최적화하는 것이 목표입니다.