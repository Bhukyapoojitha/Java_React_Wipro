# DoConnect AI – Complete Source Code Document
### Package Base: `com.doconnect`

This file contains the complete source code matching your exact folder structures. All file paths, packagings, and inter-class domain imports have been updated to target `com.doconnect` instead of `com.doconnectai`.

---

## 1. Project Dependencies Configuration

### `pom.xml`
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.4</version>
        <relativePath/>
    </parent>
    <groupId>com.doconnect</groupId>
    <artifactId>doconnect_backend</artifactId>
    <version>1.0.0</version>
    <name>doconnect_backend</name>
    <description>Intelligent Discussion &amp; Knowledge Collaboration Platform</description>
    
    <properties>
        <java.version>17</java.version>
        <jjwt.version>0.11.5</jjwt.version>
        <springdoc.version>2.3.0</springdoc.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-websocket</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-api</artifactId>
            <version>${jjwt.version}</version>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-impl</artifactId>
            <version>${jjwt.version}</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-jackson</artifactId>
            <version>${jjwt.version}</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>${springdoc.version}</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

### `src/main/resources/application.properties`
```properties
server.port=8080
server.servlet.context-path=/api

spring.datasource.url=jdbc:mysql://localhost:3306/doconnect?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

application.security.jwt.secret-key=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
application.security.jwt.expiration=86400000

gemini.api.url=https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent
gemini.api.key=YOUR_GEMINI_API_KEY_HERE
```

---

## 2. Package `com.doconnect`

### `DoConnectAiApplication.java`
```java
package com.doconnect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DoConnectAiApplication {
    public static void main(String[] args) {
        SpringApplication.run(DoConnectAiApplication.class, args);
    }
}
```

---

## 3. Package `com.doconnect.config`

### `ApplicationConfig.java`
```java
package com.doconnect.config;

import com.doconnect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

### `JwtService.java`
```java
package com.doconnect.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, jwtExpiration);
    }

    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
    }

    private Key getSignInKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }
}
```

### `JwtAuthenticationFilter.java`
```java
package com.doconnect.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        if (request.getServletPath().contains("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String jwt = authHeader.substring(7);
        final String userEmail = jwtService.extractUsername(jwt);
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
```

### `SecurityConfig.java`
```java
package com.doconnect.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(req -> req
                .requestMatchers("/api/auth/**", "/api/v3/api-docs/**", "/api/swagger-ui/**", "/api/ws/**").permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
```

### `CorsConfig.java`
```java
package com.doconnect.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(Collections.singletonList("*"));
        config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
```

### `SwaggerConfig.java`
```java
package com.doconnect.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        final String schemeName = "bearerAuth";
        return new OpenAPI()
                .info(new Info().title("DoConnect AI APIs").version("1.0.0"))
                .addSecurityItem(new SecurityRequirement().addList(schemeName))
                .components(new Components().addSecuritySchemes(schemeName,
                        new SecurityScheme().name(schemeName).type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }
}
```

### `WebSocketConfig.java`
```java
package com.doconnect.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
    }
}
```

---

## 4. Package `com.doconnect.entity`

### `Role.java`
```java
package com.doconnect.entity;

public enum Role {
    USER, ADMIN, MODERATOR
}
```

### `User.java`
```java
package com.doconnect.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false) private String name;
    @Column(unique = true, nullable = false) private String email;
    @Column(nullable = false) private String password;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private Role role;

    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return List.of(new SimpleGrantedAuthority("ROLE_" + role.name())); }
    @Override public String getUsername() { return email; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
```

### `Question.java`
```java
package com.doconnect.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false) private String title;
    @Column(columnDefinition = "TEXT", nullable = false) private String content;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "question_tags", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "tag")
    @Builder.Default private List<String> tags = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default private List<Answer> answers = new ArrayList<>();
    private LocalDateTime createdAt;
}
```

### `Answer.java`
```java
package com.doconnect.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "answers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Answer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "TEXT", nullable = false) private String content;
    private boolean isAiGenerated;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private LocalDateTime createdAt;
}
```

### `ChatMessage.java`
```java
package com.doconnect.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false) private String room;
    @Column(nullable = false) private String senderEmail;
    @Column(nullable = false, columnDefinition = "TEXT") private String content;
    private LocalDateTime timestamp;
}
```

### `Notification.java`
```java
package com.doconnect.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false) private String recipientEmail;
    @Column(nullable = false) private String message;
    private boolean isRead;
    private LocalDateTime timestamp;
}
```

### `ModerationLog.java`
```java
package com.doconnect.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "moderation_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModerationLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String contentType;
    private Long contentId;
    @Column(columnDefinition = "TEXT") private String contentPreview;
    private boolean flagged;
    private String reason;
    private LocalDateTime checkedAt;
}
```

### `SentimentRecord.java`
```java
package com.doconnect.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sentiment_records")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SentimentRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String targetType;
    private Long targetId;
    private String score; 
    private LocalDateTime calculatedAt;
}
```

---

## 5. Package `com.doconnect.repository`

### `UserRepository.java`
```java
package com.doconnect.repository;

import com.doconnect.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
```

### `QuestionRepository.java`
```java
package com.doconnect.repository;

import com.doconnect.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    long countByUserId(Long userId);
    @Query("SELECT q FROM Question q JOIN q.tags t WHERE LOWER(t) LIKE LOWER(CONCAT('%', :tag, '%'))")
    List<Question> findByTagQuery(@Param("tag") String tag);
}
```

### `AnswerRepository.java`
```java
package com.doconnect.repository;

import com.doconnect.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByQuestionId(Long questionId);
    long countByUserId(Long userId);
}
```

### `ChatMessageRepository.java`
```java
package com.doconnect.repository;

import com.doconnect.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByRoomOrderByTimestampAsc(String room);
    @Query("SELECT DISTINCT m.room FROM ChatMessage m")
    List<String> findUniqueRooms();
    long countByRoom(String room);
}
```

### `NotificationRepository.java`
```java
package com.doconnect.repository;

import com.doconnect.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientEmailAndIsReadFalseOrderByTimestampDesc(String email);
}
```

### `ModerationLogRepository.java`
```java
package com.doconnect.repository;

import com.doconnect.entity.ModerationLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModerationLogRepository extends JpaRepository<ModerationLog, Long> {}
```

### `SentimentRepository.java`
```java
package com.doconnect.repository;

import com.doconnect.entity.SentimentRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SentimentRepository extends JpaRepository<SentimentRecord, Long> {}
```

### `RoleRepository.java`
```java
package com.doconnect.repository;

public interface RoleRepository {}
```

---

## 6. Packages `com.doconnect.dto.*`

### `dto.auth.LoginRequest.java`
```java
package com.doconnect.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @Email @NotBlank private String email;
    @NotBlank private String password;
}
```

### `dto.auth.RegisterRequest.java`
```java
package com.doconnect.dto.auth;

import com.doconnect.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank private String name;
    @Email @NotBlank private String email;
    @NotBlank private String password;
    @NotNull private Role role;
}
```

### `dto.auth.AuthResponse.java`
```java
package com.doconnect.dto.auth;

import lombok.*;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String email;
    private String role;
}
```

### `dto.auth.RefreshTokenRequest.java`
```java
package com.doconnect.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenRequest {
    @NotBlank private String token;
}
```

### `dto.question.QuestionRequest.java`
```java
package com.doconnect.dto.question;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

@Data
public class QuestionRequest {
    @NotBlank private String title;
    @NotBlank private String content;
    private List<String> tags;
}
```

### `dto.question.QuestionResponse.java`
```java
package com.doconnect.dto.question;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class QuestionResponse {
    private Long id;
    private String title;
    private String content;
    private List<String> tags;
    private String authorName;
    private String authorEmail;
    private LocalDateTime createdAt;
}
```

### `dto.question.QuestionSummaryDTO.java`
```java
package com.doconnect.dto.question;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class QuestionSummaryDTO {
    private Long id;
    private String title;
    private int totalAnswers;
}
```

### `dto.answer.AnswerRequest.java`
```java
package com.doconnect.dto.answer;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AnswerRequest {
    @NotBlank private String content;
}
```

### `dto.answer.AnswerResponse.java`
```java
package com.doconnect.dto.answer;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AnswerResponse {
    private Long id;
    private String content;
    private boolean isAiGenerated;
    private String authorName;
    private Long questionId;
    private LocalDateTime createdAt;
}
```

### `dto.chat.ChatMessageDTO.java`
```java
package com.doconnect.dto.chat;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ChatMessageDTO {
    private String room;
    private String senderEmail;
    private String content;
    private LocalDateTime timestamp;
}
```

### `dto.chat.ChatRoomDTO.java`
```java
package com.doconnect.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class ChatRoomDTO {
    private String roomName;
    private long totalMessages;
}
```

### `dto.ai.AIQuestionRequest.java`
```java
package com.doconnect.dto.ai;

import lombok.Data;

@Data
public class AIQuestionRequest {
    private String textContext;
}
```

### `dto.ai.AIAnswerResponse.java`
```java
package com.doconnect.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class AIAnswerResponse {
    private String payload;
}
```

### `dto.ai.RecommendationResponse.java`
```java
package com.doconnect.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data @AllArgsConstructor
public class RecommendationResponse {
    private List<String> extractedTags;
}
```

### `dto.ai.SentimentResponse.java`
```java
package com.doconnect.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class SentimentResponse {
    private String score; 
}
```

### `dto.ai.ModerationResponse.java`
```java
package com.doconnect.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class ModerationResponse {
    private boolean flagged;
    private String classificationReason;
}
```

### `dto.user.UserDTO.java`
```java
package com.doconnect.dto.user;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String role;
}
```

### `dto.user.UserProfileDTO.java`
```java
package com.doconnect.dto.user;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class UserProfileDTO {
    private String name;
    private String email;
    private String rolePrivilege;
    private long totalQuestionsPosted;
    private long totalAnswersSubmitted;
}
```

---

## 7. Package `com.doconnect.ai`

### `GeminiClient.java`
```java
package com.doconnect.ai;

import com.doconnect.exception.AIServiceException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Component
@RequiredArgsConstructor
public class GeminiClient {

    private final RestTemplate restTemplate;
    @Value("${gemini.api.url}") private String apiUrl;
    @Value("${gemini.api.key}") private String apiKey;

    public String queryModel(String prompt) {
        try {
            String targetUri = apiUrl + "?key=" + apiKey;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> textMap = new HashMap<>(); textMap.put("text", prompt);
            Map<String, Object> partsMap = new HashMap<>(); partsMap.put("parts", Collections.singletonList(textMap));
            Map<String, Object> contentsMap = new HashMap<>(); contentsMap.put("contents", Collections.singletonList(partsMap));

            ObjectMapper mapper = new ObjectMapper();
            HttpEntity<String> entity = new HttpEntity<>(mapper.writeValueAsString(contentsMap), headers);
            ResponseEntity<String> response = restTemplate.postForEntity(targetUri, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                JsonNode root = mapper.readTree(response.getBody());
                JsonNode txtNode = root.path("candidates").get(0).path("content").path("parts").get(0).path("text");
                if (!txtNode.isMissingNode()) return txtNode.asText().trim();
            }
            throw new AIServiceException("Empty reply received from Gemini API environment.");
        } catch (Exception e) {
            throw new AIServiceException("Gemini engine interaction error: " + e.getMessage());
        }
    }
}
```

### `AnswerGenerator.java`
```java
package com.doconnect.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnswerGenerator {
    private final GeminiClient geminiClient;

    public String generateAnswerSuggestion(String title, String bodyContext) {
        String prompt = String.format("Provide a precise tech reply solution for:\nTitle: %s\nContext: %s", title, bodyContext);
        return geminiClient.queryModel(prompt);
    }
    public String generateSummary(String text) {
        return geminiClient.queryModel("Summarize this in one clean sentence: " + text);
    }
    public String enhanceGrammarText(String text) {
        return geminiClient.queryModel("Correct the grammar mistakes in this text statement: " + text);
    }
}
```

### `RecommendationEngine.java`
```java
package com.doconnect.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RecommendationEngine {
    private final GeminiClient geminiClient;

    public List<String> extrapolateTagsFromText(String text) {
        String prompt = "Return only 3 to 5 comma-separated lowercase tech tags contextually matching this description text: " + text;
        try {
            return Arrays.stream(geminiClient.queryModel(prompt).split(","))
                    .map(String::trim).map(String::toLowerCase).filter(t -> !t.isEmpty()).collect(Collectors.toList());
        } catch (Exception e) {
            return List.of("ai-fallback");
        }
    }
}
```

### `SentimentAnalyzer.java`
```java
package com.doconnect.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SentimentAnalyzer {
    private final GeminiClient geminiClient;

    public String analyzeSentimentScore(String text) {
        String prompt = "Respond with exactly one word from 'POSITIVE', 'NEGATIVE', or 'NEUTRAL' analyzing this text undertone: " + text;
        try {
            String res = geminiClient.queryModel(prompt).toUpperCase().trim();
            if (res.contains("POSITIVE")) return "POSITIVE";
            if (res.contains("NEGATIVE")) return "NEGATIVE";
            return "NEUTRAL";
        } catch (Exception e) {
            return "NEUTRAL";
        }
    }
}
```

### `ToxicityAnalyzer.java`
```java
package com.doconnect.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ToxicityAnalyzer {
    private final GeminiClient geminiClient;

    public String evaluateSafetyThreshold(String text) {
        String prompt = "If entirely safe return 'SAFE'. If hostile or harmful return 'FLAGGED: ' followed immediately by the reason string. Text: " + text;
        try { return geminiClient.queryModel(prompt).trim(); } 
        catch (Exception e) { return "SAFE"; }
    }
}
```

---

## 8. Package `com.doconnect.exception`

### `ResourceNotFoundException.java`
```java
package com.doconnect.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException { public ResourceNotFoundException(String m) { super(m); } }
```

### `DuplicateResourceException.java`
```java
package com.doconnect.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateResourceException extends RuntimeException { public DuplicateResourceException(String m) { super(m); } }
```

### `UnauthorizedException.java`
```java
package com.doconnect.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException { public UnauthorizedException(String m) { super(m); } }
```

### `ForbiddenException.java`
```java
package com.doconnect.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException { public ForbiddenException(String m) { super(m); } }
```

### `AIServiceException.java`
```java
package com.doconnect.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class AIServiceException extends RuntimeException { public AIServiceException(String m) { super(m); } }
```

### `GlobalExceptionHandler.java`
```java
package com.doconnect.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNF(ResourceNotFoundException ex) { return build(ex.getMessage(), HttpStatus.NOT_FOUND); }
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Map<String, Object>> handleD(DuplicateResourceException ex) { return build(ex.getMessage(), HttpStatus.CONFLICT); }
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, Object>> handleU(UnauthorizedException ex) { return build(ex.getMessage(), HttpStatus.UNAUTHORIZED); }
    @ExceptionHandler(AIServiceException.class)
    public ResponseEntity<Map<String, Object>> handleAI(AIServiceException ex) { return build(ex.getMessage(), HttpStatus.BAD_GATEWAY); }

    private ResponseEntity<Map<String, Object>> build(String m, HttpStatus s) {
        Map<String, Object> b = new HashMap<>(); b.put("timestamp", LocalDateTime.now()); b.put("status", s.value()); b.put("message", m);
        return new ResponseEntity<>(b, s);
    }
}
```

---

## 9. Create Package `com.doconnect.mapper`

### `UserMapper.java`
```java
package com.doconnect.mapper;
import com.doconnect.dto.user.UserDTO;
import com.doconnect.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDTO toDto(User u) {
        if (u == null) return null;
        UserDTO d = new UserDTO(); d.setId(u.getId()); d.setName(u.getName()); d.setEmail(u.getEmail()); d.setRole(u.getRole().name());
        return d;
    }
}
```

### `QuestionMapper.java`
```java
package com.doconnect.mapper;
import com.doconnect.dto.question.QuestionResponse;
import com.doconnect.entity.Question;
import org.springframework.stereotype.Component;

@Component
public class QuestionMapper {
    public QuestionResponse toDto(Question q) {
        if (q == null) return null;
        QuestionResponse r = new QuestionResponse();
        r.setId(q.getId()); r.setTitle(q.getTitle()); r.setContent(q.getContent()); r.setTags(q.getTags());
        r.setAuthorName(q.getUser().getName()); r.setAuthorEmail(q.getUser().getEmail()); r.setCreatedAt(q.getCreatedAt());
        return r;
    }
}
```

### `AnswerMapper.java`
```java
package com.doconnect.mapper;
import com.doconnect.dto.answer.AnswerResponse;
import com.doconnect.entity.Answer;
import org.springframework.stereotype.Component;

@Component
public class AnswerMapper {
    public AnswerResponse toDto(Answer a) {
        if (a == null) return null;
        AnswerResponse r = new AnswerResponse();
        r.setId(a.getId()); r.setContent(a.getContent()); r.setAiGenerated(a.isAiGenerated());
        r.setAuthorName(a.getUser().getName()); r.setQuestionId(a.getQuestion().getId()); r.setCreatedAt(a.getCreatedAt());
        return r;
    }
}
```

### `ChatMapper.java`
```java
package com.doconnect.mapper;
import com.doconnect.dto.chat.ChatMessageDTO;
import com.doconnect.entity.ChatMessage;
import org.springframework.stereotype.Component;

@Component
public class ChatMapper {
    public ChatMessageDTO toDto(ChatMessage m) {
        if (m == null) return null;
        ChatMessageDTO d = new ChatMessageDTO();
        d.setRoom(m.getRoom()); d.setSenderEmail(m.getSenderEmail()); d.setContent(m.getContent()); d.setTimestamp(m.getTimestamp());
        return d;
    }
}
```

---

## 10. Create Package `com.doconnect.util`

### `Constants.java`
```java
package com.doconnect.util;
public class Constants { public static final String AI_EMAIL = "ai-assistant@doconnect.internal"; }
```

### `ValidationUtil.java`
```java
package com.doconnect.util;
import org.springframework.stereotype.Component;
@Component
public class ValidationUtil { public boolean isValid(String c) { return c != null && !c.trim().isEmpty(); } }
```

### `DateUtil.java`
```java
package com.doconnect.util;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class DateUtil {
    public static String format(LocalDateTime t) { return t == null ? "" : t.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")); }
}
```

---

## 11. Package `com.doconnect.service`

```java
package com.doconnect.service;
import com.doconnect.dto.auth.*;
import com.doconnect.dto.user.*;
import com.doconnect.dto.question.*;
import com.doconnect.dto.answer.*;
import com.doconnect.dto.chat.*;
import com.doconnect.dto.ai.*;
import com.doconnect.entity.Notification;
import java.util.List;
import java.util.Map;

public interface AuthService { AuthResponse registerNewUserAccount(RegisterRequest r); AuthResponse authenticateCredentials(LoginRequest r); AuthResponse cycleTokenRefresh(RefreshTokenRequest r); }
public interface UserService { List<UserDTO> pullAllRegisteredUsers(); UserProfileDTO compileUserProfileData(String email); }
public interface QuestionService { QuestionResponse createQuestionRecord(QuestionRequest r, String e); List<QuestionResponse> readAllQuestions(); QuestionResponse fetchQuestionById(Long id); List<QuestionResponse> findQuestionsByTagPattern(String t); List<QuestionSummaryDTO> aggregateAnalyticsSummaries(); }
public interface AnswerService { AnswerResponse createAnswerRecord(Long qId, AnswerRequest r, String e); List<AnswerResponse> readAnswersForQuestion(Long qId); }
public interface ChatService { ChatMessageDTO persistChatMessage(ChatMessageDTO d); List<ChatMessageDTO> extractRoomHistory(String r); List<ChatRoomDTO> listActiveChatRooms(); }
public interface NotificationService { void pushAlertNotification(String rec, String msg); List<Notification> fetchUnreadNotifications(String e); void flagNotificationsAsRead(Long id); }
public interface AIService { AIAnswerResponse fetchAnswerSuggestion(Long qId); AIAnswerResponse summarizeTextPayload(String b); AIAnswerResponse enhanceTextGrammar(String c); }
public interface RecommendationService { RecommendationResponse inferTagsFromQuestionContext(String b); }
public interface SentimentService { SentimentResponse calculateTextSentiment(String c, String t, Long id); }
public interface ModerationService { ModerationResponse inspectTextContent(String c, String t, Long id); }
public interface AnalyticsService { Map<String, Object> compileOperationalSystemMetrics(); }
```
*(Note: Split the interfaces above into their respective individual target interface dependency files inside your IDE `com.doconnect.service` directory layer).*

---

## 12. Package `com.doconnect.serviceImpl`

### `AuthServiceImpl.java`
```java
package com.doconnect.serviceImpl;

import com.doconnect.config.JwtService;
import com.doconnect.dto.auth.*;
import com.doconnect.entity.User;
import com.doconnect.exception.DuplicateResourceException;
import com.doconnect.exception.UnauthorizedException;
import com.doconnect.repository.UserRepository;
import com.doconnect.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse registerNewUserAccount(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) throw new DuplicateResourceException("Email already active.");
        User user = User.builder().name(request.getName()).email(request.getEmail()).password(passwordEncoder.encode(request.getPassword())).role(request.getRole()).build();
        userRepository.save(user);
        return AuthResponse.builder().accessToken(jwtService.generateToken(user)).email(user.getEmail()).role(user.getRole().name()).build();
    }

    @Override
    public AuthResponse authenticateCredentials(LoginRequest request) {
        try { authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())); } 
        catch (Exception e) { throw new UnauthorizedException("Invalid user credentials paired."); }
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Profile missing."));
        return AuthResponse.builder().accessToken(jwtService.generateToken(user)).email(user.getEmail()).role(user.getRole().name()).build();
    }

    @Override
    public AuthResponse cycleTokenRefresh(RefreshTokenRequest request) {
        String email = jwtService.extractUsername(request.getToken());
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UnauthorizedException("Invalid token tracking context."));
        if (jwtService.isTokenValid(request.getToken(), user)) {
            return AuthResponse.builder().accessToken(jwtService.generateToken(user)).email(user.getEmail()).role(user.getRole().name()).build();
        }
        throw new UnauthorizedException("Token session validity expired.");
    }
}
```

### `UserServiceImpl.java`
```java
package com.doconnect.serviceImpl;

import com.doconnect.dto.user.UserDTO;
import com.doconnect.dto.user.UserProfileDTO;
import com.doconnect.entity.User;
import com.doconnect.exception.ResourceNotFoundException;
import com.doconnect.mapper.UserMapper;
import com.doconnect.repository.*;
import com.doconnect.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDTO> pullAllRegisteredUsers() {
        return userRepository.findAll().stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public UserProfileDTO compileUserProfileData(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not located."));
        return UserProfileDTO.builder().name(user.getName()).email(user.getEmail()).rolePrivilege(user.getRole().name())
                .totalQuestionsPosted(questionRepository.countByUserId(user.getId())).totalAnswersSubmitted(answerRepository.countByUserId(user.getId())).build();
    }
}
```

### `QuestionServiceImpl.java`
```java
package com.doconnect.serviceImpl;

import com.doconnect.dto.question.*;
import com.doconnect.entity.Question;
import com.doconnect.entity.User;
import com.doconnect.exception.ResourceNotFoundException;
import com.doconnect.mapper.QuestionMapper;
import com.doconnect.repository.QuestionRepository;
import com.doconnect.repository.UserRepository;
import com.doconnect.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final QuestionMapper questionMapper;

    @Override
    public QuestionResponse createQuestionRecord(QuestionRequest r, String e) {
        User auth = userRepository.findByEmail(e).orElseThrow(() -> new ResourceNotFoundException("User non-existent."));
        Question q = Question.builder().title(r.getTitle()).content(r.getContent()).tags(r.getTags()).user(auth).createdAt(LocalDateTime.now()).build();
        return questionMapper.toDto(questionRepository.save(q));
    }
    @Override public List<QuestionResponse> readAllQuestions() { return questionRepository.findAll().stream().map(questionMapper::toDto).collect(Collectors.toList()); }
    @Override public QuestionResponse fetchQuestionById(Long id) { return questionMapper.toDto(questionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Question thread not found."))); }
    @Override public List<QuestionResponse> findQuestionsByTagPattern(String t) { return questionRepository.findByTagQuery(t).stream().map(questionMapper::toDto).collect(Collectors.toList()); }
    @Override public List<QuestionSummaryDTO> aggregateAnalyticsSummaries() { return questionRepository.findAll().stream().map(q -> new QuestionSummaryDTO(q.getId(), q.getTitle(), q.getAnswers().size())).collect(Collectors.toList()); }
}
```

### `AnswerServiceImpl.java`
```java
package com.doconnect.serviceImpl;

import com.doconnect.dto.answer.*;
import com.doconnect.entity.*;
import com.doconnect.exception.ResourceNotFoundException;
import com.doconnect.mapper.AnswerMapper;
import com.doconnect.repository.*;
import com.doconnect.service.AnswerService;
import com.doconnect.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final AnswerMapper answerMapper;
    private final NotificationService notificationService;

    @Override
    public AnswerResponse createAnswerRecord(Long qId, AnswerRequest r, String e) {
        Question q = questionRepository.findById(qId).orElseThrow(() -> new ResourceNotFoundException("Thread parent error."));
        User u = userRepository.findByEmail(e).orElseThrow(() -> new ResourceNotFoundException("Author not caught."));
        Answer a = Answer.builder().content(r.getContent()).isAiGenerated(e.contains("internal")).question(q).user(u).createdAt(LocalDateTime.now()).build();
        Answer saved = answerRepository.save(a);
        if(!q.getUser().getEmail().equalsIgnoreCase(e)) {
            notificationService.pushAlertNotification(q.getUser().getEmail(), "New answer updates compiled on your thread: " + q.getTitle());
        }
        return answerMapper.toDto(saved);
    }
    @Override public List<AnswerResponse> readAnswersForQuestion(Long qId) { return answerRepository.findByQuestionId(qId).stream().map(answerMapper::toDto).collect(Collectors.toList()); }
}
```

### `ChatServiceImpl.java`
```java
package com.doconnect.serviceImpl;

import com.doconnect.dto.chat.*;
import com.doconnect.entity.ChatMessage;
import com.doconnect.mapper.ChatMapper;
import com.doconnect.repository.ChatMessageRepository;
import com.doconnect.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatMessageRepository rep;
    private final ChatMapper map;

    @Override public ChatMessageDTO persistChatMessage(ChatMessageDTO d) {
        ChatMessage m = ChatMessage.builder().room(d.getRoom()).senderEmail(d.getSenderEmail()).content(d.getContent()).timestamp(LocalDateTime.now()).build();
        return map.toDto(rep.save(m));
    }
    @Override public List<ChatMessageDTO> extractRoomHistory(String r) { return rep.findByRoomOrderByTimestampAsc(r).stream().map(map::toDto).collect(Collectors.toList()); }
    @Override public List<ChatRoomDTO> listActiveChatRooms() { return rep.findUniqueRooms().stream().map(rm -> new ChatRoomDTO(rm, rep.countByRoom(rm))).collect(Collectors.toList()); }
}
```

### `NotificationServiceImpl.java`
```java
package com.doconnect.serviceImpl;

import com.doconnect.entity.Notification;
import com.doconnect.repository.NotificationRepository;
import com.doconnect.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository r;
    private final SimpMessagingTemplate t;

    @Override public void pushAlertNotification(String rec, String msg) {
        Notification n = Notification.builder().recipientEmail(rec).message(msg).isRead(false).timestamp(LocalDateTime.now()).build();
        r.save(n); t.convertAndSendToUser(rec, "/queue/notifications", n);
    }
    @Override public List<Notification> fetchUnreadNotifications(String e) { return r.findByRecipientEmailAndIsReadFalseOrderByTimestampDesc(e); }
    @Override public void flagNotificationsAsRead(Long id) { r.findById(id).ifPresent(n -> { n.setRead(true); r.save(n); }); }
}
```

### `CorsServiceImpl.java` (AIService Implementation)
```java
package com.doconnect.serviceImpl;

import com.doconnect.ai.AnswerGenerator;
import com.doconnect.dto.ai.*;
import com.doconnect.entity.Question;
import com.doconnect.exception.ResourceNotFoundException;
import com.doconnect.repository.QuestionRepository;
import com.doconnect.service.AIService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CorsServiceImpl implements AIService {
    private final QuestionRepository qRep;
    private final AnswerGenerator gen;

    @Override public AIAnswerResponse fetchAnswerSuggestion(Long qId) {
        Question q = qRep.findById(qId).orElseThrow(() -> new ResourceNotFoundException("Target description context missing."));
        return new AIAnswerResponse(gen.generateAnswerSuggestion(q.getTitle(), q.getContent()));
    }
    @Override public AIAnswerResponse summarizeTextPayload(String b) { return new AIAnswerResponse(gen.generateSummary(b)); }
    @Override public AIAnswerResponse enhanceTextGrammar(String c) { return new AIAnswerResponse(gen.enhanceGrammarText(c)); }
}
```

### `RecommendationServiceImpl.java`
```java
package com.doconnect.serviceImpl;
import com.doconnect.ai.RecommendationEngine;
import com.doconnect.dto.ai.RecommendationResponse;
import com.doconnect.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {
    private final RecommendationEngine e;
    @Override public RecommendationResponse inferTagsFromQuestionContext(String b) { return new RecommendationResponse(e.extrapolateTagsFromText(b)); }
}
```

### `SentimentServiceImpl.java`
```java
package com.doconnect.serviceImpl;
import com.doconnect.ai.SentimentAnalyzer;
import com.doconnect.dto.ai.SentimentResponse;
import com.doconnect.entity.SentimentRecord;
import com.doconnect.repository.SentimentRepository;
import com.doconnect.service.SentimentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SentimentServiceImpl implements SentimentService {
    private final SentimentAnalyzer a; private final SentimentRepository r;
    @Override public SentimentResponse calculateTextSentiment(String c, String t, Long id) {
        String s = a.analyzeSentimentScore(c);
        r.save(SentimentRecord.builder().targetType(t).targetId(id).score(s).calculatedAt(LocalDateTime.now()).build());
        return new SentimentResponse(s);
    }
}
```

### `ModerationServiceImpl.java`
```java
package com.doconnect.serviceImpl;
import com.doconnect.ai.ToxicityAnalyzer;
import com.doconnect.dto.ai.ModerationResponse;
import com.doconnect.entity.ModerationLog;
import com.doconnect.repository.ModerationLogRepository;
import com.doconnect.service.ModerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ModerationServiceImpl implements ModerationService {
    private final ToxicityAnalyzer t; private final ModerationLogRepository r;
    @Override public ModerationResponse inspectTextContent(String c, String ty, Long id) {
        String res = t.evaluateSafetyThreshold(c); boolean flg = res.startsWith("FLAGGED:");
        r.save(ModerationLog.builder().contentType(ty).contentId(id).contentPreview(c.length()>50?c.substring(0,45):c).flagged(flg).reason(res).checkedAt(LocalDateTime.now()).build());
        return new ModerationResponse(flg, res);
    }
}
```

### `AnalyticsServiceImpl.java`
```java
package com.doconnect.serviceImpl;
import com.doconnect.repository.*;
import com.doconnect.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.HashMap; import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {
    private final UserRepository u; private final QuestionRepository q; private final AnswerRepository a;
    @Override public Map<String, Object> compileOperationalSystemMetrics() {
        Map<String, Object> m = new HashMap<>(); m.put("usersCount", u.count()); m.put("questionsCount", q.count()); m.put("answersCount", a.count());
        return m;
    }
}
```

---

## 13. Package `com.doconnect.controller`

### `AuthController.java`
```java
package com.doconnect.controller;
import com.doconnect.dto.auth.*;
import com.doconnect.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/auth") @RequiredArgsConstructor
public class AuthController {
    private final AuthService s;
    @PostMapping("/register") public ResponseEntity<AuthResponse> reg(@Valid @RequestBody RegisterRequest r) { return ResponseEntity.ok(s.registerNewUserAccount(r)); }
    @PostMapping("/login") public ResponseEntity<AuthResponse> log(@Valid @RequestBody LoginRequest r) { return ResponseEntity.ok(s.authenticateCredentials(r)); }
}
```

### `QuestionController.java`
```java
package com.doconnect.controller;
import com.doconnect.dto.question.*;
import com.doconnect.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/questions") @RequiredArgsConstructor
public class QuestionController {
    private final QuestionService s;
    @PostMapping("/create") public ResponseEntity<QuestionResponse> post(@Valid @RequestBody QuestionRequest r, @AuthenticationPrincipal UserDetails u) { return ResponseEntity.ok(s.createQuestionRecord(r, u.getUsername())); }
    @getMapping("/list") public ResponseEntity<List<QuestionResponse>> list() { return ResponseEntity.ok(s.readAllQuestions()); }
    @GetMapping("/{id}") public ResponseEntity<QuestionResponse> get(@PathVariable Long id) { return ResponseEntity.ok(s.fetchQuestionById(id)); }
}
```

### `AnswerController.java`
```java
package com.doconnect.controller;
import com.doconnect.dto.answer.*;
import com.doconnect.service.AnswerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/answers") @RequiredArgsConstructor
public class AnswerController {
    private final AnswerService s;
    @PostMapping("/post/{qId}") public ResponseEntity<AnswerResponse> post(@PathVariable Long qId, @Valid @RequestBody AnswerRequest r, @AuthenticationPrincipal UserDetails u) { return ResponseEntity.ok(s.createAnswerRecord(qId, r, u.getUsername())); }
    @GetMapping("/thread/{qId}") public ResponseEntity<List<AnswerResponse>> list(@PathVariable Long qId) { return ResponseEntity.ok(s.readAnswersForQuestion(qId)); }
}
```

### `AIController.java`
```java
package com.doconnect.controller;
import com.doconnect.dto.ai.*;
import com.doconnect.service.AIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/ai") @RequiredArgsConstructor
public class AIController {
    private final AIService s;
    @PostMapping("/suggest-answer/{qId}") public ResponseEntity<AIAnswerResponse> sugg(@PathVariable Long qId) { return ResponseEntity.ok(s.fetchAnswerSuggestion(qId)); }
    @PostMapping("/summarize") public ResponseEntity<AIAnswerResponse> sum(@RequestBody AIQuestionRequest r) { return ResponseEntity.ok(s.summarizeTextPayload(r.getTextContext())); }
}
```

### `ChatController.java`
```java
package com.doconnect.controller;
import com.doconnect.dto.chat.*;
import com.doconnect.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/chat") @RequiredArgsConstructor
public class ChatController {
    private final ChatService s;
    @GetMapping("/history/{room}") public ResponseEntity<List<ChatMessageDTO>> hist(@PathVariable String room) { return ResponseEntity.ok(s.extractRoomHistory(room)); }
    @GetMapping("/rooms") public ResponseEntity<List<ChatRoomDTO>> rms() { return ResponseEntity.ok(s.listActiveChatRooms()); }
}
```

### `NotificationController.java`
```java
package com.doconnect.controller;
import com.doconnect.entity.Notification;
import com.doconnect.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/notifications") @RequiredArgsConstructor
public class NotificationController {
    private final NotificationService s;
    @GetMapping("/unread") public ResponseEntity<List<Notification>> list(@AuthenticationPrincipal UserDetails u) { return ResponseEntity.ok(s.fetchUnreadNotifications(u.getUsername())); }
    @PutMapping("/read/{id}") public ResponseEntity<Void> read(@PathVariable Long id) { s.flagNotificationsAsRead(id); return ResponseEntity.noContent().build(); }
}
```

### `UserController.java`
```java
package com.doconnect.controller;
import com.doconnect.dto.user.*;
import com.doconnect.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/users") @RequiredArgsConstructor
public class UserController {
    private final UserService s;
    @GetMapping("/all") public ResponseEntity<List<UserDTO>> all() { return ResponseEntity.ok(s.pullAllRegisteredUsers()); }
    @GetMapping("/profile") public ResponseEntity<UserProfileDTO> prof(@AuthenticationPrincipal UserDetails u) { return ResponseEntity.ok(s.compileUserProfileData(u.getUsername())); }
}
```

### `ModerationController.java`
```java
package com.doconnect.controller;
import com.doconnect.dto.ai.*;
import com.doconnect.service.ModerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/moderation") @RequiredArgsConstructor
public class ModerationController {
    private final ModerationService s;
    @PostMapping("/inspect") @PreAuthorize("hasAnyRole('ADMIN','MODERATOR')")
    public ResponseEntity<ModerationResponse> insp(@RequestParam String type, @RequestParam Long entityId, @RequestBody AIQuestionRequest r) { return ResponseEntity.ok(s.inspectTextContent(r.getTextContext(), type, entityId)); }
}
```

### `AnalyticsController.java`
```java
package com.doconnect.controller;
import com.doconnect.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController @RequestMapping("/analytics") @RequiredArgsConstructor
public class AnalyticsController {
    private final AnalyticsService s;
    @GetMapping("/dashboard") @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<Map<String, Object>> metrics() { return ResponseEntity.ok(s.compileOperationalSystemMetrics()); }
}
```

---

## 14. Real-time Message Streaming Broker Gateway (`com.doconnect.websocket`)

### `ChatMessageHandler.java`
```java
package com.doconnect.websocket;
import com.doconnect.dto.chat.ChatMessageDTO;
import com.doconnect.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller @RequiredArgsConstructor
public class ChatMessageHandler {
    private final SimpMessagingTemplate template; private final ChatService service;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessageDTO dto) {
        ChatMessageDTO saved = service.persistChatMessage(dto);
        template.convertAndSend("/topic/room." + dto.getRoom(), saved);
    }
}
```

### `WebSocketEventListener.java`
```java
package com.doconnect.websocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.stereotype.Component;

@Component @Slf4j
public class WebSocketEventListener {
    @EventListener public void handleConnect(SessionConnectEvent e) { log.info("New live active websocket mapping connected."); }
}
```

### `ChatRoomManager.java`
```java
package com.doconnect.websocket;
import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentHashMap; import java.util.Set;
@Component
public class ChatRoomManager { 
    private final Set<String> rooms = ConcurrentHashMap.newKeySet();
    public void add(String id) { rooms.add(id); }
    public Set<String> getRooms() { return rooms; }
}
```
