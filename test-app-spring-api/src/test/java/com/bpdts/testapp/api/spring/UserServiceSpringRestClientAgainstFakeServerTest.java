package com.bpdts.testapp.api.spring;

import com.bpdts.testapp.client.model.spring.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith( SpringRunner.class )
@RestClientTest( UserServiceSpringRestClient.class )
public class UserServiceSpringRestClientAgainstFakeServerTest extends UserServiceSpringRestClientTest
{
    @Autowired
    MockRestServiceServer fakeServer;

    @Autowired
    ObjectMapper mapper;

    String userRepresentation;
    String populatedCitySpecificUserDataRepresentation;
    String cityAgnosticUserDataRepresentation;

    @Before
    public void setUp() throws Exception {
        userRepresentation = jsonFileToString("user.json" );
        populatedCitySpecificUserDataRepresentation = jsonFileToString( "londoners.json" );
        cityAgnosticUserDataRepresentation = jsonFileToString( "users.json" );
    }

    @Override
    protected void setUpForExistingUser( User user ) {
        fakeServer.expect( requestTo( serverBaseUri() + "/user/" + user.getId() ) )
                .andRespond( withSuccess( userRepresentation, MediaType.APPLICATION_JSON ) );
    }

    @Override
    public void setUpForNonexistentUser( User user ) {
        fakeServer.expect( requestTo( serverBaseUri() + "/user/" + user.getId() ) )
                .andRespond( withStatus( HttpStatus.NOT_FOUND ) );
    }

    @Override
    protected void setUpForPopulatedCitySpecificUserData(String city) {
        fakeServer.expect( requestTo( serverBaseUri() + "/city/" + city + "/users" ) )
                .andRespond( withSuccess( populatedCitySpecificUserDataRepresentation, MediaType.APPLICATION_JSON ) );
    }

    @Override
    protected void setUpForUnpopulatedCitySpecificUserData( String city ) {
        fakeServer.expect( requestTo( serverBaseUri() + "/city/" + city + "/users" ) )
                .andRespond( withSuccess( "[]", MediaType.APPLICATION_JSON ) );
    }

    @Override
    protected void setUpForCityAgnosticUserData() {
        fakeServer.expect( requestTo( serverBaseUri() + "/users" ) )
                .andRespond( withSuccess( cityAgnosticUserDataRepresentation, MediaType.APPLICATION_JSON ) );
    }

    private String jsonFileToString(String userJson ) throws IOException, URISyntaxException {
        return new String( Files.readAllBytes( Paths.get( getClass().getResource(userJson).toURI() ) ) );
    }
}
