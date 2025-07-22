package hms.opd.gui;

import hms.reception.database.TokenDBConnection;
import hms.reception.gui.ReceptionMain;

public class CancelCounterThread extends Thread {

	String counterSTR = "0";

	public CancelCounterThread(String counter) {

		counterSTR = counter;
	}

	@SuppressWarnings("deprecation")
	public void run() {
		TokenDBConnection connection = new TokenDBConnection();
		try {
			Thread.sleep(2*1000);
			
			connection.CancelCounter(ReceptionMain._id, counterSTR);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			connection.closeConnection();
			e.printStackTrace();
		}
		connection.closeConnection();
		stop();
	}
}
