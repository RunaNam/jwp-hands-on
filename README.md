# JWP Hands-On

## 만들면서 배우는 스프링 실습 코드

### 학습 순서
- cache
- thread
- servlet
- reflection
- di

## Cache
### 마주했던 이슈

- 스프링부트 2.6 으로 업그레이드 시 요청 경로를 ControllerHandler에 매칭시키기 위한 전략의 기본 값 즉, `spring.mvc.pathmatch.matching-strategy` 기본 값이 `ant_path_matcher` 에서 `path_pattern_parser` 로 변경된다.

  출처: [https://haenny.tistory.com/297](https://haenny.tistory.com/297)


- 참고 자료
    - [MDN HTTP caching](https://developer.mozilla.org/en-US/docs/Web/HTTP/Caching#heuristic_caching)
    - [MDN Cache-Control](https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/Cache-Control)

### ****0단계 - 휴리스틱 캐싱 제거하기****

- 휴리스틱 캐시란?
    - Cache-Control이 지정되지 않더라도, 기본적으로 cache는 최대한 사용해 성능을 높이는 것이 추천되기 때문에 기본적으로 적용되는 캐시 정책.
    - 기본적으로 1년동안 업데이트 되지 않은 컨텐츠는 잘 업데이트 되지 않는 것으로 알려져있다. 따라서, 클라이언트는 max-age가 설정되지 않았더라도 어느정도 사용되게 된다.
    - 재사용 기간은 구현에 따라 다르지만, 기본적으로 0.1년 정도를 권장한다.

### ****1단계 - HTTP Compression 설정하기****

[Enable HTTP Response Compression](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto.webserver.enable-response-compression) 를 참고

- HTTP 응답을 압축하여 웹 사이트 성능 향상

### ****2단계 - ETag/If-None-Match 적용하기****

RFC(Request for Comments) 표준 방식에 의해 캐시 validation 방식이 변경됨

기존 : etag, last-modified 둘다 비교

변경 : etag 만 비교

- etag = entity tag
    - 해시 값, id값 등이 될 수 있음. 응답 body가 변하면 etag도 바뀐다.
- ShallowEtagFilter를 통한 구현 방식
    - 모든 응답에 etag

        ```java
        @Bean
        public ShallowEtagHeaderFilter shallowEtagHeaderFilter() {
            return new ShallowEtagHeaderFilter();
        }
        ```

    - 특정 url에만 etag

        ```java
        @Bean
        public FilterRegistrationBean<ShallowEtagHeaderFilter> shallowEtagHeaderFilter() {
            FilterRegistrationBean<ShallowEtagHeaderFilter> filterRegistrationBean
              = new FilterRegistrationBean<>( new ShallowEtagHeaderFilter());
            filterRegistrationBean.addUrlPatterns("/foos/*");
            filterRegistrationBean.setName("etagFilter");
            return filterRegistrationBean;
        }
        ```