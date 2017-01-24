package com.github.jwt.auth0.web.integration

import com.github.jwt.auth0.config.Auth0JwtConfig
import com.github.jwt.auth0.web.JwtHeaderMock
import spock.lang.Specification

class JwtHeaderMockIntegrationSpec extends Specification {
    private JwtHeaderMock jwtHeaderMock
    private Auth0JwtConfig auth0JwtConfig

    void setup() {
        auth0JwtConfig = Mock(Auth0JwtConfig)
    }

    def "Get default JWT token"() {
        given:
        jwtHeaderMock = new JwtHeaderMock(auth0JwtConfig: auth0JwtConfig)
        jwtHeaderMock.init()

        when:
        def jwt = jwtHeaderMock.getJwt()

        then:
        1 * auth0JwtConfig.getJwtSecret() >> "secret"
        jwt.get().length() > 0
    }

    def "Get JWT test token"() {
        given:
        jwtHeaderMock = new JwtHeaderMock(auth0JwtConfig: auth0JwtConfig, jwtTestToken: 'jwt-test-token')

        when:
        def jwt = jwtHeaderMock.getJwt()

        then:
        jwt.get() == 'jwt-test-token'
    }


}
