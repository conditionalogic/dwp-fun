package com.bpdts.testapp.location;

import com.bpdts.testapp.api.retrofit.UserServiceException;
import com.bpdts.testapp.api.retrofit.UserServiceRetrofitRestClient;
import com.bpdts.testapp.client.model.retrofit.UserData;

import java.util.*;
import java.util.stream.Collectors;

public class UserLocator
{
    private static final Map<String, Location> locationByCity = new HashMap<String, Location>() {{
        put( "London", new Location(51.5074, 0.1278 ) );
    }};

    private static final Map<String, Double> radiusByCity = new HashMap<String, Double>() {{
        put( "London", 18.63d ); // length of M25 / 2 * Pi
    }};

    private final UserServiceRetrofitRestClient client;

    public UserLocator( String uri ) {
        client = new UserServiceRetrofitRestClient( uri );
    }

    public Set<UserData> listUsersAssociatedWithLondon() throws UserServiceException {
        return listUsersByDistance( locationByCity.get( "london" ),  radiusByCity.get( "london" ), 50 );
    }

    public Set<UserData> listUsersByDistance( Location location, double radius, int distance ) throws UserServiceException {
        Set<UserData> results = new HashSet<>();
        results.addAll( client.getUserDataByCity( "London" ) );
        Set<UserData> moreData = client.getAllUserData().stream()
            .filter( u -> new GreatCircleDistance( Location.from( u ), location ).computeInMiles() <= distance + radius )
            .collect( Collectors.toSet() );
        results.addAll( moreData );
        return results;
    }

    public static void main( String[] args ) throws Exception {
        String city = "London";
        int distance = 50;
        String uri = "https://bpdts-test-app.herokuapp.com/";
        if( args.length > 0 ) {
            city = args[0];
        }
        if( args.length > 1 ) {
            distance = Integer.parseInt( args[1] );
        }
        if( args.length > 2 ) {
            uri = args[2];
        }
        if( !locationByCity.containsKey( city ) ) {
            System.out.println( "The only supported cities at the moment are: " + locationByCity.keySet() );
            return;
        }
        if( distance < 0 ) {
            System.out.println( "Distance in miles: " + distance + " is not supported as it is negative" );
        }
        UserLocator userLocator = new UserLocator( uri );
        Collection<UserData> userData = userLocator.listUsersByDistance( locationByCity.get( city ), radiusByCity.get( city ), distance );
        System.out.println( userData.stream().map( u -> u.getFirstName() + " " + u.getLastName() ).collect( Collectors.toList() ) );
    }
}
