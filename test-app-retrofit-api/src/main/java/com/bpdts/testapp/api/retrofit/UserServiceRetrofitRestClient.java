package com.bpdts.testapp.api.retrofit;

import com.bpdts.testapp.client.api.retrofit.UsersApi;
import com.bpdts.testapp.client.model.retrofit.User;
import com.bpdts.testapp.client.model.retrofit.UserDataList;
import retrofit2.Response;

import java.io.IOException;
import java.util.Optional;

public class UserServiceRetrofitRestClient
{
    private static final int HTTP_NOT_FOUND = 404;

    private final UsersApi api;

    public UserServiceRetrofitRestClient( String uri ) {
        api = new RetrofitServiceGenerator( uri ).createService( UsersApi.class );
    }

    public Optional<User> getUser( String id ) throws UserServiceException {
        Response<User> response;
        try {
            response = api.getUser( id ).execute();
        }   catch( IOException e ) {
            throw new UserServiceException( "Error(s)[" + e.getMessage() + "] looking up user: " + id, e );
        }
        if( response.code() == HTTP_NOT_FOUND ) {
            return Optional.empty();
        }   else if( response.code() >= 400 ) {
            throw new UserServiceException( "Unexpected HTTP response: " + response.code() + " looking up user: " + id );
        }   else {
            return Optional.of( response.body() );
        }
    }

    public UserDataList getUserDataByCity( String city ) throws UserServiceException {
        try {
            return  api.listUsersByCity( city ).execute().body();
        }   catch( IOException e ) {
            throw new UserServiceException( "Error(s)[" + e.getMessage()
                + "] looking up user data for city: " + city, e );
        }
    }

    public UserDataList getAllUserData() throws UserServiceException {
        try {
            return api.listAllUsers().execute().body();
        } catch( IOException e ) {
            throw new UserServiceException( e );
        }
    }

    public static void main( String[] args ) throws Exception {
        UserServiceRetrofitRestClient client = new UserServiceRetrofitRestClient( "https://bpdts-test-app.herokuapp.com/" );
        System.out.println( client.getUser( "1" ) );
        System.out.println( client.getUser( "amadeus" ) );
        System.out.println( client.getUserDataByCity( "London" ) );
        System.out.println( client.getUserDataByCity( "Shangri-la" ) );
        System.out.println( client.getAllUserData().size() );
    }
}
