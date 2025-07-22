package hms.crypto;

public class CryptoInit {
	
	/*****PRODUCTIONS URL's****/
	public static final String ALGORITHM = "AES/CBC/PKCS5Padding";
	public static final String BOOKING_API_URL = "https://lb.mrlpay.com/pcpos4/TransactionRequest.php?source=556";
	public static final String CANCELLATION_API_URL = " https://lb.mrlpay.com/pcpos4/CancelTransactionRequest.php?source=556";
	public static final String STATUS_CHECK_API_URL= "https://lb.mrlpay.com/pcpos4/StatusCheck.php?source=556";
	public static final String Key = "Fp6dW9UTW1PyBm60dKDJgynzJitrWrxT";
	public static final String Iv = "r7zxnu77K9rl25da";
	
	/*****DEVELOPMENT URL's****/
//	public static final String ALGORITHM = "AES/CBC/PKCS5Padding";
//	public static final String BOOKING_API_URL = "https://bouat.mrlpay.com/pcpos4/TransactionRequest.php?source=629";
//	public static final String CANCELLATION_API_URL = "https://bouat.mrlpay.com/pcpos4/CancelTransactionRequest.php?source=629";
//	public static final String STATUS_CHECK_API_URL= "https://bouat.mrlpay.com/pcpos4/StatusCheck.php?source=629";
//	public static final String Key = "X5mUl3J1jneCd0adISoHWDTj7U8Rnhvd";
//	public static final String Iv = "1111111245683783";
}
