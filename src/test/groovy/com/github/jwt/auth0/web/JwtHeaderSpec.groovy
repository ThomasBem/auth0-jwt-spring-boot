package com.github.jwt.auth0.web

import com.github.jwt.auth0.config.Auth0JwtConfig
import org.apache.commons.codec.binary.Base64
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest

class JwtHeaderSpec extends Specification {
    private JwtHeader jwtHeader
    private Auth0JwtConfig config
    private HttpServletRequest request

    void setup() {
        config = Mock(Auth0JwtConfig)
        request = Mock(HttpServletRequest)
        jwtHeader = new JwtHeader(config: config, request: request)
    }

    def "Get JWT"() {
        when:
        def jwt = jwtHeader.getJwt()

        then:
        1 * config.getJwtKey() >> "jwt"
        1 * request.getHeader(_ as String) >> "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0ZXN0LWtleSI6InRlc3QtdmFsdWUifQ.INFA_0gyIYnY7G_N8XzLaBxlE94YYRIX1Cgc76yVyOM"
        1 * config.getJwtSecret() >> Base64.encodeBase64URLSafeString("secret".bytes)
        jwt.isPresent()
    }


}
