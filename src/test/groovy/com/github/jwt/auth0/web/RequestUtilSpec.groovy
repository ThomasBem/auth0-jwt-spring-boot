package com.github.jwt.auth0.web

import com.github.jwt.auth0.config.Auth0JwtConfig
import org.springframework.http.HttpHeaders
import spock.lang.Specification

class RequestUtilSpec extends Specification {
    private RequestUtil requestUtil
    private JwtHeader jwtHeader
    private Auth0JwtConfig config

    void setup() {
        jwtHeader = Mock(JwtHeader)
        config = Mock(Auth0JwtConfig)
        requestUtil = new RequestUtil(jwtHeader: jwtHeader, config: config)
    }

    def "No authorization header when missing username/password"() {
        when:
        def entity = requestUtil.createRequestEntity()
        def headers = entity.getHeaders()

        then:
        1 * jwtHeader.getJwt() >> Optional.empty()
        headers.get(HttpHeaders.AUTHORIZATION) == null
    }

    def "Add authorization header when username/password is configured"() {
        when:
        def entity = requestUtil.createRequestEntity()
        def headers = entity.getHeaders()

        then:
        1 * jwtHeader.getJwt() >> Optional.empty()
        2 * config.getUsername() >> "test-user"
        2 * config.getPassword() >> "test-password"
        headers.get(HttpHeaders.AUTHORIZATION) != null
    }

    def "Add JWT to header"() {
        when:
        def entity = requestUtil.createRequestEntity()
        def headers = entity.getHeaders()

        then:
        1 * jwtHeader.getJwt() >> Optional.of("test-jwt")
        1 * config.getJwtKey() >> "jwt"
        headers.getFirst("jwt") == "test-jwt"
    }
}
