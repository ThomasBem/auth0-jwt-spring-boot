package com.github.jwt.auth0.web

import com.github.jwt.auth0.config.Auth0JwtConfig
import spock.lang.Specification


class JwtHeaderMockSpec extends Specification {
    private JwtHeaderMock jwtHeader
    private Auth0JwtConfig auth0JwtConfig

    void setup() {
        auth0JwtConfig = Mock(Auth0JwtConfig)

        jwtHeader = new JwtHeaderMock(auth0JwtConfig: auth0JwtConfig)
        jwtHeader.init()
    }

    def "Get JWT claims"() {
        when:
        def jwt = jwtHeader.get()

        then:
        jwt.size() > 0
    }

    def "Get JWT String"() {
        when:
        def jwt = jwtHeader.getJwt()

        then:
        1 * auth0JwtConfig.getJwtSecret() >> "secret"
        jwt.isPresent()
    }
}
