package uk.ac.shef.oak.com6510.Models;

        import java.text.DateFormat;
        import java.text.SimpleDateFormat;
        import java.util.Date;
        import java.util.Locale;
        import java.util.TimeZone;

public class Utilities {

    /**
     * it converts a number of mseconds since 1.1.1970 (epoch) to a current string date
     *
     * @param actualTimeInMseconds a time in msecs for the UTC time zone
     * @return a time string of type HH:mm:ss such as 23:12:54.
     */
    public static String mSecsToString(long actualTimeInMseconds) {
        Date date = new Date(actualTimeInMseconds);
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.UK);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return (formatter.format(date));
    }
}