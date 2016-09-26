# Auth0 JWT Spring Boot

[![Build Status](https://travis-ci.org/ThomasBem/auth0-jwt-spring-boot.svg?branch=master)](https://travis-ci.org/ThomasBem/auth0-jwt-spring-boot)

Integrates [Auth0](https://auth0.com/) [JWT](https://jwt.io) with [Spring Boot](http://projects.spring.io/spring-boot).
 
## Installation

Gradle
```
compile('com.github.jwt.auth0:auth0-jwt-spring-boot:0.0.10')
```

```
repositories {
    maven {
        url  "http://dl.bintray.com/thomasbem/maven"
    }
}
```

## Usage

Add `@EnableAuth0JwtSpringBoot` to the Application-class (the class containing the `@SpringBootApplication`).  
This will enable the auth0 JWT integration and also include the [jasypt-spring-boot](https://github.com/ulisesbocchio/jasypt-spring-boot).

## Configuration

| Key | Description | Default value |
|-----|-------------|---------------|
| basic-auth.username | Basic authentication header username | |
| basic-auth.password | Basic authentication header password | |
| jwt-secret | Secret used to verify JWT (from auth0) | |
| jwt-encoded-secret | If the secret configured with 'jwt-secret' is base64 url-safe encoded | true |
| jwt-key | The key used to store the jwt in the header. If the token is not found using 'jwt-key', it will try to use the standard Authorization-header | jwt |
