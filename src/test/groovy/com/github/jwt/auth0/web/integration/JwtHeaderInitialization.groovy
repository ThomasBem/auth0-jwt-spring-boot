package com.github.jwt.auth0.web.integration

import com.github.jwt.auth0.config.Auth0JwtInitializer
import com.github.jwt.auth0.web.JwtHeaderImpl
import com.github.jwt.auth0.web.JwtHeaderMock
import spock.lang.Specification

class JwtHeaderInitialization extends Specification {

    private Auth0JwtInitializer auth0JwtInitializer

    def "Initialize JwtHeaderMock"() {
        given:
        auth0JwtInitializer = new Auth0JwtInitializer(jwtTestMode: 'true')

        when:
        def jwtHeader = auth0JwtInitializer.jwtHeader()

        then:
        jwtHeader as JwtHeaderMock
    }

    def "Initialize JwtHeaderImpl"() {
        given:
        auth0JwtInitializer = new Auth0JwtInitializer(jwtTestMode: 'false')

        when:
        def jwtHeader = auth0JwtInitializer.jwtHeader()

        then:
        jwtHeader as JwtHeaderImpl
    }

}
