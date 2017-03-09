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

    def "Return empty map when unable to read the file"() {
        given:
        def objectMapper = Mock(ObjectMapper)
        JwtTestFile jwtTokenFile = new JwtTestFile(objectMapper: objectMapper, tokenFile: 'test-jwt.json')

        when:
        def claims = jwtTokenFile.getClaims()

        then:
        1 * objectMapper.readValue(_ as byte[], _ as Class) >> { throw new IOException('test exception') }
        claims.isEmpty()
    }

}
