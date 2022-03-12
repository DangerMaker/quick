package com.hundsun.zjfae.common.utils.aes;

public class EncDecUtil {


	private static final String KEY = "8D4F16E8F94796FC";
	private static final String ivParameterSpec = "0102030405060708";

	//protected static Logger logger = LoggerFactory.getLogger(EncDecUtil.class);

	/**
	 * @Description: 加密 -> AES ; 把字节数组转换成16进制字符串 ; base64
	 * 
	 *               <pre>
	 *               123qwe ZGI5M2ZhNmE0MTQ4YzExNmJmM2ZmYzhjYTBkODg4MGQ=
	 * @param data
	 * @return
	 */
	public static String AESEncrypt(String data) {
		if (data == null) {
			data = "";
		}

		String result = null;
		try {
			result = Base64Util.encode(Base64Util
					.bytesToHexString(AES.encrypt(data.getBytes(), KEY.getBytes(), ivParameterSpec.getBytes()))
					.toLowerCase().getBytes());
		} catch (Exception e) {
//			logger.error(
//					"服务器加密失败！ , data=【" + data + "】 , key=【" + key + "】 , ivParameterSpec=【" + ivParameterSpec + "】",
//					e);
		}
		return result == null ? "" : result;
	}

	/**
	 * @Description: 解密 -> base64 ; 把16进制字符串转换成字节数组 ; AES
	 * @param data
	 * @return
	 */
	public static String AESDecrypt(String data) {
		if (data == null) {
			data = "";
		}
		String result = null;
		try {
			result = new String(AES.decrypt(Base64Util.hexStringToBytes(new String(Base64Util.decode(data))),
					KEY.getBytes(), ivParameterSpec.getBytes()));
		} catch (Exception e) {
//			logger.error("服务器解密失败！ , data=【" + data + "】,key=【" + key + "】,ivParameterSpec=【" + ivParameterSpec + "】",
//					e);
		}
		return result == null ? "" : result;
	}
	
//	public static String Decrypt(String data) {
//		return jni.FlkWebCrypt.sm4Decrypt(data);
//	}
}
