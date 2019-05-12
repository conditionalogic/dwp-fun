package com.bpdts.testapp.api.retrofit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitServiceGenerator
{
    private final String baseUri;

    private final Retrofit.Builder builder;
    private final OkHttpClient.Builder httpClient;
    private final HttpLoggingInterceptor logging;

    public RetrofitServiceGenerator( String baseUri ) {
        this( baseUri, HttpLoggingInterceptor.Level.NONE );
    }

    public RetrofitServiceGenerator( String baseUri, HttpLoggingInterceptor.Level logLevel ) {
        this.baseUri = baseUri;
        this.httpClient = new OkHttpClient.Builder();
        this.logging = new HttpLoggingInterceptor().setLevel( logLevel );
        this.builder = new Retrofit.Builder()
            .baseUrl( baseUri )
            .addConverterFactory( GsonConverterFactory.create() )
            .client( httpClient.build() );
    }

    public <T> T createService( Class<T> serviceClass ) {
        httpClient.addInterceptor( logging );
        builder.client( httpClient.build() );
        return builder.build().create( serviceClass );
    }
}
