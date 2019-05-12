package com.bpdts.testapp.api.retrofit.live;

import com.bpdts.testapp.api.retrofit.UserServiceRetrofitRestClient;
import com.bpdts.testapp.api.retrofit.UserServiceRetrofitRestClientTest;
import com.bpdts.testapp.client.model.retrofit.User;

public class UserServiceRetrofitRestClientAgainstRealServerTest extends UserServiceRetrofitRestClientTest
{
    protected UserServiceRetrofitRestClient createRestClient() {
        return new UserServiceRetrofitRestClient( "https://bpdts-test-app.herokuapp.com" );
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