import java.util.Calendar;


public class TimeCompare {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Calendar mytime = Calendar.getInstance();
		 
		// Set time of calendar to 18:00
		mytime.set(Calendar.HOUR_OF_DAY, 23);
		mytime.set(Calendar.MINUTE, 0);
		mytime.set(Calendar.SECOND, 0);
		mytime.set(Calendar.MILLISECOND, 0);

		
		
		
		Calendar cal = Calendar.getInstance();
		 
		// Set time of calendar to 18:00
		cal.set(Calendar.HOUR_OF_DAY, 20);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		 
		Calendar cal2 = Calendar.getInstance();
		 
		// Set time of calendar to 8:00
		//cal2.set(Calendar.DATE, 16);
		cal2.set(Calendar.HOUR_OF_DAY, 8);
		cal2.set(Calendar.MINUTE, 0);
		cal2.set(Calendar.SECOND, 0);
		cal2.set(Calendar.MILLISECOND, 0);
		
		// Check if current time is after 18:00 today
		boolean afterSix = mytime.after(cal);
		
		boolean beforetime = mytime.before(cal2);
		 
		if (afterSix&&beforetime) {
		    System.out.println("Go home, it's after 6 PM!");
		}
		else {
		    System.out.println("Hello!");
		}
		
//		Calendar calendar = Calendar.getInstance();
		//calendar.setTime(yourdate);
		int hours = mytime.get(Calendar.HOUR_OF_DAY);
		int minutes = mytime.get(Calendar.MINUTE);
		int seconds = mytime.get(Calendar.SECOND);
		
		
		 System.out.println(hours+"  "+minutes+"  "+seconds);
		 
		 if(hours<=24&&hours>=20)
		 {
			  System.out.println("OKKK"); 
		 }else  if(hours<=8&&hours>=0){
			  System.out.println("OKKKK 2");
		}
		 else {
			 System.out.println("Not OKKKK 2");
		}
	}

}
