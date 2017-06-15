package com.github.jwt.auth0.web

import com.github.jwt.auth0.config.Auth0JwtConfig
import org.apache.commons.codec.binary.Base64
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

class RequestUtilSpec extends Specification {
    private RequestUtil requestUtil
    private JwtHeaderImpl jwtHeader
    private Auth0JwtConfig config
    private RestTemplate restTemplate

    void setup() {
        restTemplate = Mock(RestTemplate)
        jwtHeader = Mock(JwtHeaderImpl)
        config = Mock(Auth0JwtConfig)
        requestUtil = new RequestUtil(jwtHeader: jwtHeader, config: config, restTemplate: restTemplate)
    }

    def "Return status code on HttpClientErrorException"() {
        when:
        def responseEntity = requestUtil.exchange("http://localhost", HttpMethod.GET, String)

        then:
        1 * restTemplate.exchange(_ as String, _ as HttpMethod, _ as HttpEntity, _ as Class) >> {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND)
        }
        1 * jwtHeader.getJwt() >> Optional.empty()
        responseEntity.statusCode == HttpStatus.NOT_FOUND
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

    def "Create request entity with basic auth and jwt"() {
        given:
        def encodedAuthorization = Base64.encodeBase64String("test-user:test-password".getBytes())

        when:
        def httpEntity = requestUtil.createRequestEntity()

        then:
        1 * jwtHeader.getJwt() >> Optional.of("test-jwt")
        1 * config.getJwtKey() >> "jwt"
        2 * config.getUsername() >> "test-user"
        2 * config.getPassword() >> "test-password"
        httpEntity.getHeaders().getFirst(HttpHeaders.AUTHORIZATION) == "Basic " + encodedAuthorization
        httpEntity.getHeaders().getFirst("jwt") == "test-jwt"
    }
}
