package com.github.jwt.auth0.auth0

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.jwt.auth0.config.Auth0JwtConfig
import com.github.jwt.auth0.web.JwtHeader
import spock.lang.Specification

class Auth0JwtSpec extends Specification {
    private final String jwt = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoiMTIzIiwibmFtZSI6ImpvaG4uZG9lQHRlc3QuY29tIiwibmlja25hbWUiOiJqb2huLmRvZSIsImVtYWlsIjoiam9obi5kb2VAdGVzdC5jb20iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwicGljdHVyZSI6Imh0dHBzOi8vcy5ncmF2YXRhci5jb20vYXZhdGFyLyIsImFwcF9tZXRhZGF0YSI6eyJlbnZpcm9ubWVudCI6ImRlbW8iLCJjbGFpbXMiOnsicm9sZXMiOlsiVVNFUiJdLCJjb21wYW55IjoiU2hlbGwiLCJvcmdhbml6YXRpb24iOiJteS1vcmcxIn19LCJpc3MiOiJodHRwczovL3Rlc3QuY29tLyIsInN1YiI6IjEyMyIsImF1ZCI6IjIzNCIsImV4cCI6MTYwOTM3MjgwMCwiaWF0IjoxNDgwNDg5MzEwfQ.1XSF_RFMcqbzRZ3Wfs_kQiJWbaiGOwgl5dOWXzcP61E'

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

    def "Get name"() {
        when:
        def name = auth0Jwt.getName()

        then:
        name.get() == 'John Doe'

    }

    def "Return Optional.Empty when nickname not present"() {
        given:
        jwtHeader = Mock(JwtHeader) {
            getJwt() >> Optional.of('eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ')
        }
        auth0Jwt = new Auth0Jwt(jwtHeader: jwtHeader, config: config, objectMapper: new ObjectMapper())

        when:
        def name = auth0Jwt.getName()

        then:
        !name.isPresent()
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
        def permission = auth0Jwt.get(Permission)

        then:
        permission.environment == 'demo'
    }
}
