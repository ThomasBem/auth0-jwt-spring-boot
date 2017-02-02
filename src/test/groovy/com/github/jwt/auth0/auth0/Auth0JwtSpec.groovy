package com.github.jwt.auth0.auth0

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.jwt.auth0.config.Auth0JwtConfig
import com.github.jwt.auth0.web.JwtHeader
import spock.lang.Specification

class Auth0JwtSpec extends Specification {
    private final String jwt = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoiMTIzIiwibmFtZSI6ImpvaG4uZG9lQHRlc3QuY29tIiwibmlja25hbWUiOiJqb2huLmRvZSIsImVtYWlsIjoiam9obi5kb2VAdGVzdC5jb20iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwicGljdHVyZSI6Imh0dHBzOi8vcy5ncmF2YXRhci5jb20vYXZhdGFyLyIsImFwcF9tZXRhZGF0YSI6eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwicGVybWlzc2lvbnMiOlt7ImFwcCI6Im15LWFwcCIsInJvbGVzIjpbIlVTRVIiXSwiY29tcGFueSI6Im15LWNvbXBhbnkiLCJvcmdhbmlzYXRpb24iOiJteS1vcmcifV19LCJpc3MiOiJodHRwczovL3Rlc3QuY29tLyIsInN1YiI6IjEyMyIsImF1ZCI6IjIzNCIsImV4cCI6MTYwOTM3MjgwMCwiaWF0IjoxNDgwNDg5MzEwfQ.pX6fsmwzOlJjJ-Igj1-FEPT9vJ0BVxbdMfKojMaxE5I'

    private Auth0Jwt auth0Jwt
    private Auth0JwtConfig config
    private JwtHeader jwtHeader

    void setup() {
        config = Mock(Auth0JwtConfig) {
            getSecret() >> 'secret'.bytes
        }
        jwtHeader = Mock(JwtHeader) {
            getJwt() >> Optional.of(jwt)
        }
        auth0Jwt = new Auth0Jwt(jwtHeader: jwtHeader, config: config, objectMapper: new ObjectMapper())
    }

    def "Get property"() {
        when:
        def nickname = auth0Jwt.getProperty('nickname')

        then:
        nickname.isPresent()
        nickname.get() == 'john.doe'
    }

    def "Return empty when property is not found"() {
        when:
        def property = auth0Jwt.getProperty('invalid-key')

        then:
        !property.isPresent()
    }

    def "Return empty when no JWT is found"() {
        given:
        def header = Mock(JwtHeader) {
            getJwt() >> Optional.empty()
        }
        auth0Jwt = new Auth0Jwt(jwtHeader: header, config: config, objectMapper: new ObjectMapper())

        when:
        def nickname = auth0Jwt.getProperty('nickname')

        then:
        !nickname.isPresent()
    }

    def "Return Permissions from JWT"() {
        when:
        def permissions = auth0Jwt.get('$.permissions[*]', Permission[])

        then:
        permissions.size() == 1
        permissions[0].app == 'my-app'
    }

    def "Return null if JsonPath does not exist"() {
        when:
        def permissions = auth0Jwt.get('$.invalid', Permission[])

        then:
        permissions == null
    }

}
