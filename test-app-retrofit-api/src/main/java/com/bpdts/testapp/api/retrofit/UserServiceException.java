package com.bpdts.testapp.api.retrofit;

public class UserServiceException extends Exception
{
    public UserServiceException( String message ) {
        super( message );
    }

    public UserServiceException( Exception e ) {
        super( e );
    }

    public UserServiceException( String message, Exception e ) {
        super( message, e );
    }
}
