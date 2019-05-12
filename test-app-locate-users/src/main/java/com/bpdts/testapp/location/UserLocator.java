package com.bpdts.testapp.location;

import com.bpdts.testapp.api.retrofit.UserServiceRetrofitRestClient;

public class UserLocator
{
    private final UserServiceRetrofitRestClient client;

    public UserLocator( String uri )    {
        client = new UserServiceRetrofitRestClient( uri );
    }

    public static void main( String[] args ) {
        System.out.println( "Hello, World" );
    }
}
