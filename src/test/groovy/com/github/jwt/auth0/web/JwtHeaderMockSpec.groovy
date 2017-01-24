package com.github.jwt.auth0.web

import com.github.jwt.auth0.config.Auth0JwtConfig
import spock.lang.Specification


class JwtHeaderMockSpec extends Specification {
    private JwtHeaderMock jwtHeader
    private Auth0JwtConfig auth0JwtConfig
    private String testToken = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoiMTIzIiwibmFtZSI6ImpvaG4uZG9lQHRlc3QuY29tIiwibmlja25hbWUiOiJqb2huLmRvZSIsImVtYWlsIjoiam9obi5kb2VAdGVzdC5jb20iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwicGljdHVyZSI6Imh0dHBzOi8vcy5ncmF2YXRhci5jb20vYXZhdGFyLyJ9.pIb-kAhXykpP6f1fI7LbyDd0qGAJkQVJIn4yC0nTRqs'

    void setup() {
        auth0JwtConfig = Mock(Auth0JwtConfig)

        jwtHeader = new JwtHeaderMock(auth0JwtConfig: auth0JwtConfig)
        jwtHeader.init()
    }

    def "Get JWT claims"() {
        when:
        def claims = jwtHeader.get()

        then:
        claims.size() == 6
    }

    def "Get JWT test claims"() {
        given:
        jwtHeader = new JwtHeaderMock(auth0JwtConfig: auth0JwtConfig, jwtTestToken: testToken)
        jwtHeader.init()

        when:
        def claims = jwtHeader.get()

        then:
        1 * auth0JwtConfig.getJwtSecret() >> 'secret'
        claims.size() == 6
    }

    def "Get JWT String"() {
        when:
        def jwt = jwtHeader.getJwt()

        then:
        1 * auth0JwtConfig.getJwtSecret() >> "secret"
        jwt.isPresent()
    }
}
