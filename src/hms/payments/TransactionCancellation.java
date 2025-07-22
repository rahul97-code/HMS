package hms.payments;

import javax.swing.JOptionPane;

import org.json.JSONObject;

import hms.crypto.CryptoHandler;
import hms.payments.database.PaymentsDBConnection;

public class TransactionCancellation extends CryptoHandler {

	public static void main(String[] args) throws Exception {
		cancelTransaction(null);
	}
	public static int cancelTransaction(String[] data) throws Exception {
		JSONObject ReqJson = new JSONObject();    // Add values to the JSON object
		ReqJson.put("tid", data[0]);
		ReqJson.put("amount", data[1]);
		ReqJson.put("organization_code", data[2]);
		ReqJson.put("type", data[3]); 
		ReqJson.put("actionId", data[4]);
		ReqJson.put("urn", data[5]);
		ReqJson.put("request_urn", data[6]);

		// Print the JSON object
		System.out.println(ReqJson.toString());
		String encrypted = encrypt(ReqJson.toString());
		String ciphertext=new HttpRequestService().sendPOST(encrypted,CANCELLATION_API_URL);
		JSONObject ResJson = new JSONObject(decrypt(ciphertext));

		System.out.println("Res: " + ResJson.toString());
		if (ResJson.optString("urn", null)!=null) {
			int responseCode = ResJson.getInt("response_code");
			String responseMessage = ResJson.getString("response_message");
			String urn = ResJson.getString("urn");
			String tid = ResJson.getString("tid");
			String amount = ResJson.getString("amount");
			String invoiceNumber = ResJson.getString("invoiceNumber");
			String rrn = ResJson.getString("rrn");
			String type = ResJson.getString("type");
			String cbAmt = ResJson.getString("cb_amt");
			String appCode = ResJson.getString("app_code");
			String tokenisedValue = ResJson.getString("tokenisedValue");
			String actionId = ResJson.getString("actionId");
			String[] res=new String[] {data[6],responseCode+"",responseMessage,urn,tid,amount,invoiceNumber,rrn,type,cbAmt,appCode,tokenisedValue,actionId};
			if(responseMessage.contains("Success")) {
				return 1;
			}else if(responseMessage.contains("Transaction in process it will not cancelled")){
				setResponseData(res);
		        JOptionPane.showMessageDialog(null, responseMessage, "Error", JOptionPane.ERROR_MESSAGE);
				return 2;
			}else {
				setResponseData(res);
		        JOptionPane.showMessageDialog(null, responseMessage, "Error", JOptionPane.ERROR_MESSAGE);
				return 0;
			}
		}
		return 0;
	}
	public static void setResponseData(String[] data){
		PaymentsDBConnection PayDB = new PaymentsDBConnection();
		try {
			PayDB.inserPaymentCancelData(data);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}finally {
			PayDB.closeConnection();
		}

	}
}
