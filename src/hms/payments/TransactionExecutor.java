package hms.payments;

import java.util.Arrays;

import javax.swing.JOptionPane;

import org.json.JSONObject;

import hms.amountreceipt.database.AmountReceiptDBConnection;
import hms.crypto.CryptoHandler;
import hms.payments.database.PaymentsDBConnection;

public class TransactionExecutor extends CryptoHandler {
	static int try_count=1;
	public static int doTransaction(String[] data) throws Exception {
		// Add values to the JSON object
		
		JSONObject ReqJson = new JSONObject();
		ReqJson.put("tid", data[0]);
		ReqJson.put("amount", data[1]);
		ReqJson.put("organization_code", data[2]+"Retail");
		ReqJson.put("additional_attribute1", data[3]);
		ReqJson.put("additional_attribute2", data[4]);
		ReqJson.put("additional_attribute3", data[5]);
		ReqJson.put("additional_attribute4", data[6]);
		ReqJson.put("additional_attribute5", data[7]);
		ReqJson.put("type",data[8]);
		ReqJson.put("actionId", data[9]);
		ReqJson.put("request_urn", data[10]);

		// Print the JSON object
		System.out.println("Req: "+ReqJson.toString());
		String encrypted = encrypt(ReqJson.toString());
		String ciphertext=new HttpRequestService().sendPOST(encrypted,BOOKING_API_URL);
		JSONObject ResJson = new JSONObject(decrypt(ciphertext));
		System.out.println("Res: " + ResJson.toString());

		if (ResJson.optString("urn", null)!=null) {
			int responseCode = ResJson.optInt("response_code", 0); 
			String responseMessage = ResJson.optString("response_message", null); 
			int urn = ResJson.optInt("urn", 0);
			String tid = ResJson.optString("tid", null);
			String amount = ResJson.optString("amount", null);
			String invoiceNumber = ResJson.optString("invoiceNumber", null);
			String rrn = ResJson.optString("rrn", null);
			String type = ResJson.optString("type", null);
			String cbAmt = ResJson.optString("cb_amt", null);
			String appCode = ResJson.optString("app_code", null);
			String tokenisedValue = ResJson.optString("tokenisedValue", null);
			String actionId = ResJson.optString("actionId", null);
			String[] arr = { String.valueOf(responseCode), responseMessage, urn+"",invoiceNumber, rrn,	cbAmt, appCode, tokenisedValue,data[10]+""};
			System.out.println(Arrays.toString(arr));
			setResponseData(arr);
			return urn;
		}else {
			JOptionPane.showMessageDialog(null, ResJson.optString("response_message", null), "Info",
					JOptionPane.ERROR_MESSAGE);
			return 0;
		}
	}

	public static void setResponseData(String[] data){
		PaymentsDBConnection PayDB = new PaymentsDBConnection();
		try {
			PayDB.UpdateResponseData(data);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}finally {
			PayDB.closeConnection();
		}

	}
	
}
