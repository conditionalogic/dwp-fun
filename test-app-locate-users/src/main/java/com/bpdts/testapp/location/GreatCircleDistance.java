package com.bpdts.testapp.location;

/**
 * Somewhat opaque code adapted (copied) from https://www.geeksforgeeks.org/program-distance-two-points-earth
 */
public class GreatCircleDistance
{
    static final double EARTH_RADIUS_IN_MILES = 3958.8d;

    private final Location from;
    private final Location to;

    public GreatCircleDistance( Location from, Location to ) {
        this.from = from;
        this.to = to;
    }

    public double computeInMiles() {
        double fromLatitude = Math.toRadians( from.latitude() );
        double fromLongitude = Math.toRadians( from.longitude() );
        double toLatitude = Math.toRadians( to.latitude() );
        double toLongitude = Math.toRadians( to.longitude() );

        double dlatitude = toLatitude - fromLatitude;
        double dlongitude = toLongitude - fromLongitude;

        double a = Math.pow( Math.sin( dlatitude / 2 ), 2 )
            + Math.cos( fromLatitude ) * Math.cos( toLatitude )
            * Math.pow( Math.sin( dlongitude / 2 ), 2 );
        double c = 2 * Math.asin( Math.sqrt( a ) );

        return c * EARTH_RADIUS_IN_MILES;
    }

    public static void main( String[] args ) {
        Location london = new Location( 51.5074, 0.1278 );
        Location edinburgh = new Location( 55.9533, 3.1883 );
        System.out.println( new GreatCircleDistance( london, edinburgh ).computeInMiles() );
    }
}
