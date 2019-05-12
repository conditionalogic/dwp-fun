package com.bpdts.testapp.api.retrofit;


import com.bpdts.testapp.client.model.retrofit.User;
import com.bpdts.testapp.client.model.retrofit.UserData;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class UserServiceRetrofitRestClientTest
{
    static final String POPULATED_CITY = "London";
    static final String UNPOPULATED_CITY = "Camelot";

    @Autowired
    protected UserServiceRetrofitRestClient sut;

    @Before
    public void setUp() throws Exception {
        moreSetUp();
        sut = createRestClient();
    }

    protected void moreSetUp() throws Exception {
        // NOP in base class
    }

    @Test
    public void testExistingUserShouldReturnUser() throws Exception {
        User user = existingUser();
        setUpForExistingUser( user );
        Optional<User> possibleUser = sut.getUser("1");
        assertThat( possibleUser.isPresent() ).isTrue();
        assertThat( possibleUser.get() ).isEqualTo( user );
    }

    @Test
    public void testNonexistentUserShouldNotBeFound() throws Exception {
        User user = nonExistentUser();
        setUpForNonexistentUser( user );
        Optional<User> possibleUser = sut.getUser( user.getId() );
        assertThat( possibleUser.isPresent() ).isFalse();
    }

    @Test
    public void testCitySpecificUserDataForPopulatedCity() throws Exception {
        setUpForPopulatedCitySpecificUserData( POPULATED_CITY );
        List<UserData> userDataList = sut.getUserDataByCity( POPULATED_CITY );
        assertThat( userDataList ).hasAtLeastOneElementOfType( UserData.class );
        assertThat( userDataList ).contains( singleUserDataFromPopulatedCity() );
    }

    @Test
    public void testCitySpecificUserDataForUnpopulatedCity() throws Exception {
        setUpForUnpopulatedCitySpecificUserData( UNPOPULATED_CITY );
        List<UserData> userDataList = sut.getUserDataByCity( UNPOPULATED_CITY  );
        assertThat( userDataList ).isEmpty();
    }

    @Test
    public void testCityAgnosticUserData() throws Exception {
        setUpForCityAgnosticUserData();
        List<UserData> userDataList = sut.getAllUserData();
        assertThat( userDataList ).isNotEmpty();
        assertThat( userDataList ).contains( singleUserDataFromPopulatedCity() );
    }

    protected abstract UserServiceRetrofitRestClient createRestClient();

    protected abstract void setUpForExistingUser( User user );

    protected abstract void setUpForNonexistentUser( User user );

    protected abstract void setUpForPopulatedCitySpecificUserData( String city );

    protected abstract void setUpForUnpopulatedCitySpecificUserData( String city );

    protected abstract void setUpForCityAgnosticUserData();

    protected String serverBaseUri() {
        return "https://bpdts-test-app.herokuapp.com";
    }

    protected User existingUser() {
        User result = new User();
        result.setId( "1" );
        result.setFirstName( "Maurise" );
        result.setLastName( "Shieldon" );
        result.setEmail( "mshieldon0@squidoo.com" );
        result.setIpAddress( "192.57.232.111" );
        result.setLatitude( 34.003135d );
        result.setLongitude( -117.7228641d );
        result.setCity( "Kax" );
        return result;
    }

    protected User nonExistentUser() {
        User result = new User();
        result.setId( "amadeus" );
        return result;
    }

    protected UserData singleUserDataFromPopulatedCity() {
        UserData result = new UserData();
        result.setId( "135" );
        result.setFirstName( "Mechelle" );
        result.setLastName( "Boam" );
        result.setEmail( "mboam3q@thetimes.co.uk" );
        result.setIpAddress( "113.71.242.187" );
        result.setLatitude( -6.5115909d );
        result.setLongitude( 105.652983 );
        return result;
    }
}

