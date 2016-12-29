package xiaweizi.com.myapplication.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密工具包
 * @author Administrator
 *
 */
public class MD5Encoder {
	private static StringBuilder sb;

	public static String encode(String str){
		
		try {
			//获取MD5加密器
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] bytes = str.getBytes();
			byte[] digest = md.digest(bytes);
			sb = new StringBuilder();
			for (byte b : digest) {
				//把每个字节转成16进制数
				int d = b & 0xff;
				String hexString = Integer.toHexString(d);
				if (hexString.length() == 1) {
					hexString = "0" + hexString;
				}
				sb.append(hexString);
				System.out.println();
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return sb + "";
	}
}
