MySQL
- Connector/J: automatically create database if not exists
    - See MySQL Connector/J Developer Guide, 6.3.2 Connection
    - Add ?createDatabaseIfNotExist=true in db path
- Hibernate: automatically create table if not exists
    - See https://docs.spring.io/spring-boot/how-to/data-initialization.html
    - Add spring.jpa.hibernate.ddl-auto: update
- schema.sql doesn't work
  - JPA Conflict: If you use JPA or Hibernate, you cannot use both schema.sql and Hibernate's automatic schema generation.  You must set spring.jpa.hibernate.ddl-auto=none (or empty) to disable Hibernate's schema creation, allowing Spring Boot to execute the script.
  - Initialization Mode: For non-embedded databases (like MySQL or PostgreSQL), the default initialization mode is embedded. You must explicitly set spring.sql.init.mode=always
- Hibernate: LazyInitializationException Cannot lazily initialize collection of role
  - The LazyInitializationException occurs when an application attempts to access a lazy-loaded collection (e.g., @OneToMany or @ManyToMany) after the Hibernate session has closed or when working with detached entities.
  - Solution: Eager Fetching: Change the fetch type to FetchType.EAGER in annotations, though this may impact performance.
  - Solution2: Explicitly Initialize: Use Hibernate.initialize(entity.getCollection()) or access the collection (e.g., .size()) within a @Transactional method before the session closes.
  - **Used** Solution3: Transactional + DTOs: Map entities to Data Transfer Objects within the service layer to avoid serializing lazy proxies.
    - See https://www.baeldung.com/java-dto-pattern
    > DTOs or Data Transfer Objects are objects that carry data between processes in order to reduce the number of methods calls.
    - STILL NOT WORKING
      - @EntityGraph: 在 Repository 方法上添加 @EntityGraph("graphName")，JPA 会根据该图生成包含必要 JOIN 的 SQL 查询。
        - StackOverflowError
          - Solution: 改成單向關聯

Spring Security
- print log
    - yaml: logging.level.org.springframework.security=DEBUG
- use yaml data as admin data
    - @Value(VALUE) > @ConfigurationProperties(prefix = PREFIX)
    - See 3. Simple Properties of https://www.baeldung.com/configuration-properties-in-spring-boot
        - then ConfigProperties class add to SecurityConfig
            - See https://spring.io/guides/gs/securing-web
- use default login
    - See https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/form.html
- access non-request mapping URL responding 403
    - because it will respond /error, which is not accessible
    - add: .requestMatchers("/error").permitAll()
- three login pages(for admin/seller/customer)
    - See https://www.baeldung.com/spring-security-two-login-pages
- json login failed
  - 失败通常是因为默认的 UsernamePasswordAuthenticationFilter 期望 application/x-www-form-urlencoded 格式，而 API 发送的是 JSON 字符串，导致用户名和密码无法被正确解析。 
  - 預設的登錄流程設計是針對 HTML 表單提交
- get current user information
  - @AuthenticationPrincipal CustomUserDetails user
- seller cannot login error: No static resource seller/login for request '/seller/login'
  - WARN: Found 2 UserDetailsService beans, with names [sellerDetailsService, userDetailsService]. Global Authentication Manager will not use a UserDetailsService for username/password login. Consider publishing a single UserDetailsService bean.
  > uses **a** UserDetailsService and PasswordEncoder to authenticate a username and password
  - See https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/dao-authentication-provider.html
  - Solution: use one UserDetailsService to include every roles, instead three UserDetailsService
  - DOES NOT FIX THE PROBLEM (need to handle extra login field)
  - Solution1: Single login/logout page and custom authentication(session)
    - Session tutorial https://www.youtube.com/watch?v=4_NXWzqR5ZQ
  - Solution2: Single login/logout page, use default login processing and extra login fields
    - See https://www.baeldung.com/spring-security-extra-login-fields
  - **Used** Solution3: Single login/logout page, use default login processing and extend UsernamePasswordAuthenticationFilter
    - 通过继承默认过滤器并重写 attemptAuthentication 方法，可以提取额外参数并存入 Session 或合并到认证令牌中。
    - See https://docs.spring.io/spring-security/reference/servlet/appendix/faq.html#appendix-faq-extra-login-fields
    - See https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/form.html
      > When the username and password are submitted, the UsernamePasswordAuthenticationFilter creates a UsernamePasswordAuthenticationToken which is a type of Authentication, by extracting the username and password from the HttpServletRequest instance.
    
Spring Data JPA
- InvalidDataAccessApiUsageException/TransactionRequiredException: cannot reliably process 'remove' call
  - See https://www.baeldung.com/spring-data-jpa-delete
- IllegalArgumentException: Can not get josh.ecommerce.Entity.User field josh.ecommerce.Entity.Cart.customer on java.lang.Integer
  - User id datatype is int change to Integer
  - STILL NOT WORKING
  - change CartItem connect to Cart(id)
- Composite PK
  - See https://www.baeldung.com/jpa-composite-primary-keys