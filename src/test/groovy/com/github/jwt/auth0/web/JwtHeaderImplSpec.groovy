package com.github.jwt.auth0.web

import com.github.jwt.auth0.config.Auth0JwtConfig
import io.jsonwebtoken.JwtException
import org.apache.commons.codec.binary.Base64
import org.springframework.http.HttpHeaders
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest

class JwtHeaderImplSpec extends Specification {
    private final String jwt = 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0ZXN0LWtleSI6InRlc3QtdmFsdWUifQ.INFA_0gyIYnY7G_N8XzLaBxlE94YYRIX1Cgc76yVyOM'

    private JwtHeaderImpl jwtHeader
    private Auth0JwtConfig config
    private HttpServletRequest request

    void setup() {
        config = Mock(Auth0JwtConfig) {
            getJwtKey() >> "jwt"
        }
        request = Mock(HttpServletRequest)
        jwtHeader = new JwtHeaderImpl(config: config, request: request)
    }

    def "Get JWT"() {
        when:
        def jwt = jwtHeader.getJwt()

        then:
        1 * request.getHeader(HttpHeaders.AUTHORIZATION) >> this.jwt
        1 * config.getSecret() >> "secret".bytes
        jwt.isPresent()
    }

    def "Return empty claims map when unable to get JWT from header"() {
        when:
        def claims = jwtHeader.get()

        then:
        1 * request.getHeader(HttpHeaders.AUTHORIZATION) >>  { throw new IllegalStateException('test exception') }
        claims.isEmpty()
    }

    def "Get JWT with unencoded secret"() {
        when:
        def jwt = jwtHeader.getJwt()

        then:
        1 * request.getHeader(_ as String) >> this.jwt
        1 * config.getSecret() >> "secret".bytes
        jwt.isPresent()
    }

    def "Get JWT from authorization header"() {
        when:
        def jwt = jwtHeader.getJwt()

        then:
        1 * request.getHeader("jwt") >> null
        1 * request.getHeader(HttpHeaders.AUTHORIZATION) >> "Bearer ${this.jwt}"
        1 * config.getSecret() >> "secret".bytes
        jwt.isPresent()
        jwt.get() == this.jwt
    }

    def "Throw JwtException when unable to parse JWT"() {
        when:
        jwtHeader.getJwt()

        then:
        1 * request.getHeader(_ as String) >> jwt
        1 * config.getSecret() >> "notsecret".bytes
        thrown(JwtException)
    }

    def "Get claims"() {
        when:
        def claims = jwtHeader.get()

        then:
        1 * request.getHeader(_ as String) >> jwt
        2 * config.getSecret() >> "secret".bytes
        claims.size() == 1
    }
}
