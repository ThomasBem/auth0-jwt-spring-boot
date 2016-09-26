package com.github.jwt.auth0.web.integration

import com.github.jwt.auth0.config.Auth0JwtInitializer
import com.github.jwt.auth0.web.JwtHeader
import com.github.jwt.auth0.web.JwtHeaderImpl
import com.github.jwt.auth0.web.JwtHeaderMock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.util.ReflectionTestUtils
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

@ContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JwtHeaderInitialization extends Specification {

    private Auth0JwtInitializer auth0JwtInitializer

    @Autowired
    WebApplicationContext context

    void setup() {
        auth0JwtInitializer = new Auth0JwtInitializer()
    }

    def "Initialize JwtHeaderMock" () {
        given:
        ReflectionTestUtils.setField(auth0JwtInitializer, "jwtTestMode", "true")

        when:
        def jwtHeader = auth0JwtInitializer.jwtHeader()

        then:
        jwtHeader as JwtHeaderMock
    }

    def "Initialize JwtHeaderImpl" () {
        given:
        ReflectionTestUtils.setField(auth0JwtInitializer, "jwtTestMode", "false")

        when:
        def jwtHeader = auth0JwtInitializer.jwtHeader()

        then:
        jwtHeader as JwtHeaderImpl
    }

}
