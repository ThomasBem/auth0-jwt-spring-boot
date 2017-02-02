package com.github.jwt.auth0.config

import spock.lang.Specification

class Auth0JwtConfigSpec extends Specification {

    def "Get secret with base64 encoding"() {
        given:
        def config = new Auth0JwtConfig(jwtSecretEncoded: 'true', jwtSecret: 'c2VjcmV0')

        when:
        def secret = config.getSecret()

        then:
        new String(secret) == 'secret'
    }

    def "Get secret without encoding"() {
        given:
        def config = new Auth0JwtConfig(jwtSecretEncoded: 'false', jwtSecret: 'secret')

        when:
        def secret = config.getSecret()

        then:
        new String(secret) == 'secret'
    }

}
