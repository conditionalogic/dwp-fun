package com.bpdts.testapp.api.spring.live;

import com.bpdts.testapp.api.spring.UserServiceSpringRestClient;
import com.bpdts.testapp.api.spring.UserServiceSpringRestClientTest;
import com.bpdts.testapp.client.model.spring.User;
import org.junit.Before;
import org.springframework.boot.web.client.RestTemplateBuilder;

public class UserServiceSpringRestClientAgainstRealServerTest extends UserServiceSpringRestClientTest
{
    @Before
    public void setUp() {
        RestTemplateBuilder builder = new RestTemplateBuilder().rootUri( serverBaseUri() );
        sut = new UserServiceSpringRestClient( builder );
    }

    @Override
    protected void setUpForExistingUser( User user ) {
        // NOP
    }

    @Override
    protected void setUpForNonexistentUser( User user ) {
        // NOP
    }

    @Override
    protected void setUpForPopulatedCitySpecificUserData(String city) {
        // NOP
    }

    @Override
    protected void setUpForUnpopulatedCitySpecificUserData( String city ) {
        // NOP
    }

    @Override
    protected void setUpForCityAgnosticUserData() {
        // NOP
    }
}