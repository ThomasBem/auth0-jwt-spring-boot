package com.github.jwt.auth0

import com.github.jwt.auth0.web.JwtHeader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.boot.test.WebIntegrationTest
import spock.lang.Specification

@SpringApplicationConfiguration(classes = TestApplication)
@WebIntegrationTest(randomPort = true)
class TestApplicationSpec extends Specification {

    @Autowired
    private JwtHeader jwtHeader


    def "Initialize Auth0 JWT Spring beans"() {
        when:
        def claims = jwtHeader.get()

        then:
        claims.size() == 0
    }
}
