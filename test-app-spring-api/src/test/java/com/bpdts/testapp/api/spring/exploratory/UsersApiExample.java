package com.bpdts.testapp.api.spring.exploratory;

import com.bpdts.testapp.client.api.spring.UsersApi;
import com.bpdts.testapp.client.invoker.spring.ApiClient;
import com.bpdts.testapp.client.model.spring.NonExistentUser;
import com.bpdts.testapp.client.model.spring.User;
import com.bpdts.testapp.client.model.spring.UserDataList;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.ExtractingResponseErrorHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static java.util.Arrays.asList;

public class UsersApiExample
{
    public static void main( String[] args ) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept( asList(
                MediaType.APPLICATION_JSON,
                MediaType.APPLICATION_XML,
                MediaType.APPLICATION_PDF
        ));
        HttpEntity<?> request = new HttpEntity<>( headers );
        final RestTemplate template = new RestTemplate();
        ExtractingResponseErrorHandler errorHandler = new ExtractingResponseErrorHandler() {
            {
                setMessageConverters( template.getMessageConverters() );
            }
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                System.err.println( "Error: " + response );
                for( HttpMessageConverter converter : template.getMessageConverters() ) {
                    if( converter.canRead( NonExistentUser.class, response.getHeaders().getContentType() ) ) {
                        System.err.println( converter.read( NonExistentUser.class, response ) );
                        break;
                    }
                }
                super.handleError(response);
            }
        };
        template.setErrorHandler( errorHandler );

        exerciseRestTemplate( template, request, "https://bpdts-test-app.herokuapp.com" );
        exerciseApiClient( template );
    }

    private static void exerciseRestTemplate( RestTemplate template, HttpEntity<?> request, String baseUri ) {
        ResponseEntity<String> response = template.exchange( baseUri + "/user/1", HttpMethod.GET, request, String.class );
        System.out.println( response );
        try {
            response = template.exchange(baseUri + "/user/klingon", HttpMethod.GET, request, String.class);
            System.out.println(response);
        }   catch( HttpClientErrorException.NotFound e ) {
            System.err.println( e.getClass().getName() + " :: " + e.getResponseBodyAsString() );
            System.err.println( e );
            e.printStackTrace();
        }
    }

    private static void exerciseApiClient( RestTemplate template ) {
        ApiClient apiClient = new ApiClient( template );
        apiClient.setBasePath( "https://bpdts-test-app.herokuapp.com/" );

        UsersApi apiInstance = new UsersApi( apiClient );
        String city = "London"; // String
        System.out.println( apiInstance.getInstructions() );
        User user = apiInstance.getUser("1");
        System.out.println( user );
        UserDataList userDataList = apiInstance.listAllUsers();
        System.out.println( userDataList.size() );
        userDataList = apiInstance.listUsersByCity( "London" );
        System.out.println( userDataList );
        userDataList = apiInstance.listUsersByCity( "Klingon" );
        System.out.println( userDataList );
        user = apiInstance.getUser( "klingon" );
        System.out.println( user );
    }
}