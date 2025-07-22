package hms.reception.gui;

import hms.reception.database.TokenDBConnection;

public class UpdateCounterThread extends Thread {

	String counterSTR = "0";
	String token_type="0";
	

	public UpdateCounterThread(String counter,String tokenType) {

		counterSTR = counter;
		token_type=tokenType;
	}

	@SuppressWarnings("deprecation")
	public void run() {
		TokenDBConnection connection = new TokenDBConnection();
		try {
			connection.updateDataCounter(token_type, counterSTR);
			
			connection.updateDataScreenData(ReceptionMain._id, counterSTR,token_type);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			connection.closeConnection();
			e.printStackTrace();
		}
		connection.closeConnection();
		stop();
	}
}
