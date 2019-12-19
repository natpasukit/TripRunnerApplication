package uk.ac.shef.oak.com6510.Utils;

import java.util.Date;

/**
 * Utils function to help in convert date time format for timestamp object
 */
public class TimestampConverter {

    /**
     * Convert time stamp into date
     *
     * @param timeStamp timeObject in long number
     * @return Date dat object of time stamp
     */
    public static Date timeStampToDate(long timeStamp) {
        Date date = new Date(timeStamp);
        return date;
    }

    /**
     * Convert time stamp into date
     *
     * @param timeStamp timeobject in String number
     * @return Date dat object of time stamp
     */
    public static Date timeStampToDate(String timeStamp) {
        Date date = new Date(Long.parseLong(timeStamp));
        return date;
    }
}