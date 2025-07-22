package hms.main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateFormatChange {

	public  DateFormatChange() {
		
	}
	public static String StringToMysqlDate(Date date)
	{
			String pattern = "yyyy-MM-dd";
	        SimpleDateFormat format = new SimpleDateFormat(pattern);
	        // formatting
	       // System.out.println(format.format(date));
	        return format.format(date).toString();
	
	}
	public static String getCurrentDate()
	{
			String pattern = "dd/MM/yyyy";
	        SimpleDateFormat format = new SimpleDateFormat(pattern);
	        // formatting
	       // System.out.println(format.format(date));
	        return format.format(new Date()).toString();
	
	}
	public static java.sql.Date addMonths(Date fch, int months) {
	    Calendar cal = new GregorianCalendar();
	    cal.setTimeInMillis(fch.getTime());
	    cal.add(Calendar.MONTH, months);
	    return new java.sql.Date(cal.getTimeInMillis());
	}
	public static Date MysqlDate(Date date)
	{
			String pattern = "yyyy-MM-dd";
	        SimpleDateFormat format = new SimpleDateFormat(pattern);
	        // formatting
	       // System.out.println(format.format(date));
	        return new Date(format.format(date));
	
	}
	public static String StringToDateFormat(String date)
	{
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
	    Date date1 = null;
		try {
			date1 = dt.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    // *** same for the format String below
	    SimpleDateFormat dt1 = new SimpleDateFormat("d MMMMMM, yyyy");
	        return dt1.format(date1);
	
	}
	public static String uniqueNumber()
	{
			String pattern = "yyyyMMddhhmmss";
	        SimpleDateFormat format = new SimpleDateFormat(pattern);
	        // formatting
	       // System.out.println(format.format(date));
	        return format.format(new Date()).toString();
	
	}
	 /*
     * calculate working hours
     * two parametrs are passed, first is toTime and second is 
     * fromTime
     * returns array of int first for day, second for hour, third for minutes
     */
    public static int[] calulateHoursAndDays_BetweenDates(Date fromTime, Date toTime) {
        int[] diff = new int[3];
        Calendar cFrom = Calendar.getInstance(), cTo = Calendar.getInstance();
        cFrom.setTime(fromTime);
        cTo.setTime(toTime);
        long difff = cTo.getTimeInMillis() - cFrom.getTimeInMillis();
        long seco = difff / 1000 % 60;
        long diffMinutes = difff / (60 * 1000) % 60;
        long diffHours = difff / (60 * 60 * 1000) % 24;
        long diffDays = difff / (24 * 60 * 60 * 1000);
        diff[0] = (int) diffDays;
        diff[1] = (int) diffHours;
        diff[2] = (int) diffMinutes;
        return diff;
    }
    
    /*
     * Returns sql date format having current date and selected time
     */
    public static Date javaDate(String datetimesql) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        Date dt = new Date();
        try {
            dt = sdf.parse(datetimesql);
        } catch (ParseException ex) {
           
        }
        return dt;
    }
    
    public static Date StringToDate(String datetimesql) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dt = new Date();
        try {
            dt = sdf.parse(datetimesql);
        } catch (ParseException ex) {
           
        }
        return dt;
    }
}
