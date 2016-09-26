package com.github.jwt.auth0.web

import com.github.jwt.auth0.config.Auth0JwtConfig
import io.jsonwebtoken.JwtException
import org.apache.commons.codec.binary.Base64
import org.springframework.http.HttpHeaders
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest

class JwtHeaderSpec extends Specification {
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
        1 * request.getHeader(_ as String) >> "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0ZXN0LWtleSI6InRlc3QtdmFsdWUifQ.INFA_0gyIYnY7G_N8XzLaBxlE94YYRIX1Cgc76yVyOM"
        1 * config.isJwtSecretEncoded() >> true
        1 * config.getJwtSecret() >> Base64.encodeBase64URLSafeString("secret".bytes)
        jwt.isPresent()
    }

    def "Get JWT with unencoded secret"() {
        when:
        def jwt = jwtHeader.getJwt()

        then:
        1 * request.getHeader(_ as String) >> "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0ZXN0LWtleSI6InRlc3QtdmFsdWUifQ.INFA_0gyIYnY7G_N8XzLaBxlE94YYRIX1Cgc76yVyOM"
        1 * config.isJwtSecretEncoded() >> false
        1 * config.getJwtSecret() >> "secret"
        jwt.isPresent()
    }

    def "Get JWT from authorization header"() {
        given:
        def jwtString = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0ZXN0LWtleSI6InRlc3QtdmFsdWUifQ.INFA_0gyIYnY7G_N8XzLaBxlE94YYRIX1Cgc76yVyOM"

        when:
        def jwt = jwtHeader.getJwt()

        then:
        1 * request.getHeader("jwt") >> null
        1 * request.getHeader(HttpHeaders.AUTHORIZATION) >> "Bearer ${jwtString}"
        1 * config.isJwtSecretEncoded() >> true
        1 * config.getJwtSecret() >> Base64.encodeBase64URLSafeString("secret".bytes)
        jwt.isPresent()
        jwt.get() == jwtString
    }

    def "Throw JwtException when unable to parse JWT"() {
        when:
        jwtHeader.getJwt()

        then:
        1 * request.getHeader(_ as String) >> "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0ZXN0LWtleSI6InRlc3QtdmFsdWUifQ.INFA_0gyIYnY7G_N8XzLaBxlE94YYRIX1Cgc76yVyOM"
        1 * config.isJwtSecretEncoded() >> true
        1 * config.getJwtSecret() >> Base64.encodeBase64URLSafeString("notsecret".bytes)
        thrown(JwtException)
    }

    def "Get claims"() {
        when:
        def claims = jwtHeader.get()

        then:

        1 * request.getHeader(_ as String) >> "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0ZXN0LWtleSI6InRlc3QtdmFsdWUifQ.INFA_0gyIYnY7G_N8XzLaBxlE94YYRIX1Cgc76yVyOM"
        2 * config.isJwtSecretEncoded() >> true
        2 * config.getJwtSecret() >> Base64.encodeBase64URLSafeString("secret".bytes)
        claims.size() == 1
    }
}
