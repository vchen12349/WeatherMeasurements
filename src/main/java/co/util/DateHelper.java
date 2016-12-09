package co.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import co.exception.InvalidDateException;

public class DateHelper {
	
	public static Date formatJavaScriptDateString(String s) throws InvalidDateException{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Date d = null;
        try {
			d = sdf.parse(s);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new InvalidDateException();
		}
        return d;

	}

	public static String getDateInUTC(Date d) {
		TimeZone timeZone = TimeZone.getTimeZone("UTC");
		SimpleDateFormat sd = 
			       new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.US);
		sd.setTimeZone(timeZone);			
		String str = sd.format(d);
		return str;
	}
	public static String formatDateToJavascriptString(Date d) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        
		String s = sdf.format(d);
        return s;

	}

	public static Date fromDateToJavascriptStringToDate(Date d) {
		String s = formatDateToJavascriptString(d);
		Date dt = formatJavaScriptDateString(s);
		return dt;
	}
	public static Date stripDateOfHHMMSSMMM(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}
	/**
	 * When comparing dates only worry about the date hours, minutes, and seconds and obviously month, day, year.
	 * but no millis or second.
	 * @param d
	 * @param d2
	 * @return
	 */
	public static boolean isTimestampEqual(Date date1, Date date2) {
        Calendar c = Calendar.getInstance();
        c.setTime(date2);
        date2 = c.getTime();
        Calendar c2 = Calendar.getInstance();
        c2.setTime(date1);
        date1 = c2.getTime();
        if (date1.compareTo(date2) == 0) {
            return true;
        } 
		return false;
		
	}
	
	/**
	 * Ignoring millis, second, minute, hour is the date equal?  i.e. month, day, year
	 * @param date1
	 * @param date2
	 * @return boolean
	 */
	public static boolean isDateEqual(Date date1, Date date2) {
        Calendar c = Calendar.getInstance();
        c.setTime(date2);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);
        date2 = c.getTime();
        System.out.println(date2);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(date1);
        c2.set(Calendar.MILLISECOND, 0);
        c2.set(Calendar.SECOND, 0);
        c2.set(Calendar.MINUTE, 0);
        c2.set(Calendar.HOUR, 0);
        c2.set(Calendar.HOUR_OF_DAY, 0);
        date1 = c2.getTime();
		if (date1.compareTo(date2) == 0) {
            return true;
        } 
		return false;
	}
}
