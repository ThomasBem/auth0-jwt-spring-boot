package com.github.jwt.auth0.web

import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Specification


class JwtTestFileSpec extends Specification {

    def "Return empty map if file is not found"() {
        given:
        JwtTestFile jwtTokenFile = new JwtTestFile(objectMapper: new ObjectMapper(), tokenFile: 'file-not-found.json')

        when:
        def claims = jwtTokenFile.getClaims()

        then:
        claims.isEmpty()
    }

    def "Get claims map test-jwt.json"() {
        given:
        JwtTestFile jwtTokenFile = new JwtTestFile(objectMapper: new ObjectMapper(), tokenFile: 'test-jwt.json')

        when:
        def content = jwtTokenFile.getClaims()

        then:
        content.size() == 5
    }

}
