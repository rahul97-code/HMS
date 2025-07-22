package hms.payments;

import org.json.JSONObject;

import hms.crypto.CryptoHandler;
import hms.payments.database.PaymentsDBConnection;

public class TransactionStatus extends CryptoHandler {

	public static void main(String[] args) {
		try {
			getTransactionStatus("", "0917030A", "2759");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String[] getTransactionStatus(String Urn,String Tid,String req_urn) throws Exception {
		JSONObject ReqJson = new JSONObject();
		ReqJson.put("urn", Urn);
		ReqJson.put("tid", Tid);
		ReqJson.put("request_urn", req_urn);
		System.out.println(ReqJson.toString());
		String encrypted = encrypt(ReqJson.toString());
		String ciphertext=new HttpRequestService().sendPOST(encrypted,STATUS_CHECK_API_URL);
		JSONObject ResJson = new JSONObject(decrypt(ciphertext));
		System.out.println(ResJson.toString()); 

		if (ResJson.optString("urn", null)!=null) {
			int responseCode = ResJson.getInt("response_code");
			String cardHolderName = ResJson.optString("card_holder_name", null);
			String TP_TXN_ID = ResJson.optString("TP_TXN_ID", null);
			String mid = ResJson.optString("mid", null);
			String tsi = ResJson.optString("tsi", null);
			String type = ResJson.optString("type", null);
			String upiTxnId = ResJson.optString("upi_txn_id", "");
			String tid = ResJson.optString("tid", null);
			String tokenisedValueOfCardNumber = ResJson.optString("tokenised_value_of_card_number", "");
			String requestUrn = ResJson.optString("request_urn", null);
			String txnTime = ResJson.optString("txn_time", null);
			String additionalAttribute1 = ResJson.optString("additional_attribute1", "");
			String additionalAttribute2 = ResJson.optString("additional_attribute2", "");
			String additionalAttribute3 = ResJson.optString("additional_attribute3", "");
			String additionalAttribute4 = ResJson.optString("additional_attribute4", "");
			String additionalAttribute5 = ResJson.optString("additional_attribute5", "");
			String batchNumber = ResJson.optString("batchNumber", null);
			String appCode = ResJson.optString("app_code", null);
			String invoiceNumber = ResJson.optString("invoicenumber", null);
			String captureMethod = ResJson.optString("capture_method", "");
			String amount = ResJson.optString("amount", null);
			String chip = ResJson.optString("chip", null);
			String cbAmt = ResJson.optString("cb_amt", null);
			String maskedCardNumber = ResJson.optString("masked_card_number", null);
			String rrn = ResJson.optString("rrn", null);
			String tc = ResJson.optString("tc", null);
			String urn = ResJson.optString("urn", null);
			String cardScheme = ResJson.optString("card_scheme", null);
			String txnDate = ResJson.optString("txn_date", null);
			String fetchCount = ResJson.optString("fetch_count", "0");
			String tvr = ResJson.optString("tvr", null);
			String responseMessage = ResJson.optString("response_message", "No Records Found");
			String txnType = ResJson.optString("txn_type", null);
			String billingNumber = ResJson.optString("billing_number", null);
			String actionId = ResJson.optString("actionId", null);
			String aid = ResJson.optString("aid", null);
			String status = ResJson.optString("status", null);
			if(!status.contains("INITIATE")) {
				setResponseData( new String[]{requestUrn, ""+responseCode, cardHolderName, TP_TXN_ID, mid, tsi, type, upiTxnId, tid, tokenisedValueOfCardNumber, txnTime, additionalAttribute1, additionalAttribute2, additionalAttribute3, additionalAttribute4, additionalAttribute5, batchNumber, appCode, invoiceNumber, captureMethod, amount, chip, cbAmt, maskedCardNumber, rrn, tc, urn, cardScheme, txnDate, fetchCount, tvr, responseMessage, txnType, billingNumber, actionId, aid, status});
			}
			return new String[]{status,requestUrn,cardHolderName,additionalAttribute2,amount,responseMessage};
		}else {
			return null;
		}

	}
	public static void setResponseData(String[] data){
		PaymentsDBConnection PayDB = new PaymentsDBConnection();
		try {
			PayDB.inserPaymentStatusData(data);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}finally {
			PayDB.closeConnection();
		}

	}

}
