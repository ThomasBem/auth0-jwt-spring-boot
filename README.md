# Auth0 JWT Spring Boot

[![Build Status](https://travis-ci.org/ThomasBem/auth0-jwt-spring-boot.svg?branch=master)](https://travis-ci.org/ThomasBem/auth0-jwt-spring-boot)
[ ![Download](https://api.bintray.com/packages/thomasbem/maven/auth0-jwt-spring-boot/images/download.svg) ](https://bintray.com/thomasbem/maven/auth0-jwt-spring-boot/_latestVersion)
[![codecov](https://codecov.io/gh/ThomasBem/auth0-jwt-spring-boot/branch/master/graph/badge.svg)](https://codecov.io/gh/ThomasBem/auth0-jwt-spring-boot)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/89ea619c2ad34aadaf89bab23afd7c91)](https://www.codacy.com/app/thomasbem/auth0-jwt-spring-boot?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=ThomasBem/auth0-jwt-spring-boot&amp;utm_campaign=Badge_Grade)
[![Codacy Badge](https://api.codacy.com/project/badge/Coverage/89ea619c2ad34aadaf89bab23afd7c91)](https://www.codacy.com/app/thomasbem/auth0-jwt-spring-boot?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=ThomasBem/auth0-jwt-spring-boot&amp;utm_campaign=Badge_Coverage)
[![codebeat badge](https://codebeat.co/badges/fdb29997-4808-4cd9-819e-01b9c909f181)](https://codebeat.co/projects/github-com-thomasbem-auth0-jwt-spring-boot)
[![Coverage Status](https://coveralls.io/repos/github/ThomasBem/auth0-jwt-spring-boot/badge.svg?branch=master)](https://coveralls.io/github/ThomasBem/auth0-jwt-spring-boot?branch=master)


Integrates [Auth0](https://auth0.com/) [JWT](https://jwt.io) with [Spring Boot](http://projects.spring.io/spring-boot).
 
## Installation

Gradle
```
compile('com.github.jwt.auth0:auth0-jwt-spring-boot:2.1.0')
```

## Usage

Add `@EnableAuth0JwtSpringBoot` to the Application-class (the class containing the `@SpringBootApplication`).  
This will enable the auth0 JWT integration and also include the [jasypt-spring-boot](https://github.com/ulisesbocchio/jasypt-spring-boot).

To test locally enable jwt-test-mode and configure jwt-secret. This will return a mock JWT that is encrypted with the configured secret.

### Auth0Jwt

This class helps you with extracting information from the JWT. It can be used in your Spring component by autowiring it in.

**Get property**  
To get a property in the JWT, for example nickname:
```
String nickname = auth0Jwt.getProperty("nickname");
```

**Get name**  
To get a formatted name from the Auth0 nickname:
```
String name = auth0Jwt.getName();
```

**Get object with JsonPath**  
Get object from app_metadata in the JWT using JsonPath. In this example an array of permissions is configured in auth0:
```
Permission[] permissions = auth0Jwt.get("$.permissions[*]", Permission[].class);
```
[JsonPath documentation](https://github.com/jayway/JsonPath)


## Configuration

| Key | Description | Default value |
|-----|-------------|---------------|
| basic-auth.username | Basic authentication header username | |
| basic-auth.password | Basic authentication header password | |
| jwt-secret | Secret used to verify JWT (from auth0) | |
| jwt-encoded-secret | If the secret configured with 'jwt-secret' is base64 url-safe encoded | true |
| jwt-key | The key used to store the jwt in the header. If the token is not found using 'jwt-key', it will try to use the standard Authorization-header | jwt |
| jwt-test-mode | Enable test mode to return mock JWT | false |
| jwt-test-token | If jwt-test-mode is enabled, return the configured token. If no token is configured, the default test-token is returned | |
| jwt-test-token-file | If jwt-test-mode is enabled, use the content in the file `test-jwt.json` in classpath (or other filename if configured). If the default filename is present, it will be used. If jwt-test-token is also configured this will be prioritized. | test-jwt.json | 

### Default test-token

 - user_id: 123
 - name: john.doe@test.com
 - nickname: john.doe
 - email: john.doe@test.com
 - email_verified: true
 - picture: https://s.gravatar.com/avatar
 
 
 ### Example test-jwt.json
 
 [To encode/decode jwt](https://jwt.io/)
 
 ```json
{
 "user_id": "auth0|1231232131231231",
 "name": "john.doe@mail.com",
 "nickname": "john.doe",
 "email": "john.doe@mail.com",
 "app_metadata": {
 "roles": [
   "ROLE_USER"
 ]}
}
 ```
