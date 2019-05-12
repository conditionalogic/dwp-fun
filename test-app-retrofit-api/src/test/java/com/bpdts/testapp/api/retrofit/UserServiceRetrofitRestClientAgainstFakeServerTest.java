package com.bpdts.testapp.api.retrofit;

import ch.qos.logback.classic.Level;
import com.bpdts.testapp.client.model.retrofit.User;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.mockserver.configuration.ConfigurationProperties;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class UserServiceRetrofitRestClientAgainstFakeServerTest extends UserServiceRetrofitRestClientTest
{
    private static ClientAndServer fakeServer;

    private String userRepresentation;
    private String populatedCitySpecificUserDataRepresentation;
    String cityAgnosticUserDataRepresentation;

    public void moreSetUp() throws Exception {
        userRepresentation = jsonFileToString( "user.json" );
        populatedCitySpecificUserDataRepresentation = jsonFileToString( "londoners.json" );
        cityAgnosticUserDataRepresentation = jsonFileToString( "users.json" );
    }

    @BeforeClass
    public static void setUpBeforeClass() {
        configureMockServerLogging( Level.WARN, Logger.ROOT_LOGGER_NAME, "org.mockserver",
            "org.mockserver.mock" );
        fakeServer = ClientAndServer.startClientAndServer();
    }

    private static void configureMockServerLogging( Level level, String... loggerNames ) {
        for( String loggerName : loggerNames ) {
            ( (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger( loggerName ) ).setLevel( level );
        }
    }

    @AfterClass
    public static void tearDownAfterClass() {
        fakeServer.stop();
    }

    @Override
    protected UserServiceRetrofitRestClient createRestClient() {
        return new UserServiceRetrofitRestClient( "http://localhost:" + fakeServer.getLocalPort() );
    }

    @Override
    protected void setUpForExistingUser( User user ) {
        fakeServer.when( httpRequest("/user/" + user.getId() ) )
            .respond( httpResponse(200, this.userRepresentation ) );
    }

    @Override
    public void setUpForNonexistentUser( User user ) {
        fakeServer.when( httpRequest( "/user/" + user.getId() ) )
            .respond( httpResponse( 404, "" ) );
    }

    @Override
    protected void setUpForPopulatedCitySpecificUserData( String city) {
        fakeServer.when( httpRequest( "/city/" + city + "/users" ) )
            .respond( httpResponse( 200, populatedCitySpecificUserDataRepresentation ) );
    }

    @Override
    protected void setUpForUnpopulatedCitySpecificUserData( String city ) {
        fakeServer.when( httpRequest( "/city/" + city + "/users" ) )
            .respond( httpResponse( 200, "[]" ) );
    }

    @Override
    protected void setUpForCityAgnosticUserData() {
        fakeServer.when( httpRequest( "/users" ) )
            .respond( httpResponse( 200, cityAgnosticUserDataRepresentation ) );
    }

    private HttpRequest httpRequest(String path ) {
        return new HttpRequest()
                .withHeader( "Content-Type", "application/json" )
                .withMethod( "GET" )
                .withPath( path );
    }

    private HttpResponse httpResponse(int statusCode, String responseBody) {
        return new HttpResponse()
                .withHeader( "Content-Type", "application/json; charset=UTF-8" )
                .withStatusCode(statusCode)
                .withBody(responseBody);
    }

    private String jsonFileToString( String jsonFile ) throws IOException, URISyntaxException {
        return new String( Files.readAllBytes( Paths.get( getClass().getResource( jsonFile ).toURI() ) ) );
    }
}
