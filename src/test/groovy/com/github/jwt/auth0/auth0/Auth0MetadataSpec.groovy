package com.github.jwt.auth0.auth0

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.jwt.auth0.web.JwtHeader
import spock.lang.Specification

class Auth0MetadataSpec extends Specification {
    def permissionsMetadata = "{permissions=[{app=assettracker, role=USER, company=FMC, organisation=WAS}, {app=well-life, role=USER, company=FMC, organisation=WAS}, {app=sds, role=USER, company=FMC, organisation=WAS}, {app=frm, role=USER, company=FMC, organisation=WAS}], roles=[ROLE_USER]}"

    private Auth0Metadata auth0Metadata
    private JwtHeader jwtHeader

    void setup() {
        jwtHeader = Mock(JwtHeader)
        auth0Metadata = new Auth0Metadata(jwtHeader: jwtHeader)
    }

    def "Get token without auth0 metadata"() {
        when:
        def metadata = auth0Metadata.get()

        then:
        1 * jwtHeader.get() >> ["test-key": "test-value"]
        !metadata.isPresent()
    }

    def "Get token with auth0 metadata"() {
        when:
        def metadata = auth0Metadata.get()

        then:
        1 * jwtHeader.get() >> ["app_metadata": permissionsMetadata]
        metadata.isPresent()
        new ObjectMapper().readValue(metadata.get(), Permissions).permissions.size() > 0
    }

    def "Get permissions"() {
        when:
        def permissions = auth0Metadata.getPermissions()

        then:
        1 * jwtHeader.get() >> ["app_metadata": permissionsMetadata]
        permissions.size() > 0
    }

    def "Is authorized"() {
        when:
        def authorized = auth0Metadata.isAuthorized("assettracker")

        then:
        1 * jwtHeader.get() >> ["app_metadata": permissionsMetadata]
        authorized
    }

    def "Is not authorized"() {
        when:
        def authorized = auth0Metadata.isAuthorized("unknown-app")

        then:
        1 * jwtHeader.get() >> ["app_metadata": permissionsMetadata]
        !authorized
    }
}
