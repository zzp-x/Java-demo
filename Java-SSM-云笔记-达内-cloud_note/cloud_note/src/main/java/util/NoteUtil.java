package util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;

public class NoteUtil {
	/** 利用MD5进行加密 */
	public static String EncoderByMd5(String str) {
		try {
			// 确定计算方法
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			Base64 base64 = new Base64();
			// 加密后的字符串
			String newstr = base64.encodeToString(md5.digest(str.getBytes("utf-8")));
			return newstr;
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("生成密码失败");
		}
	}

	/**判断用户密码是否正确
	   *newpasswd 用户输入的密码
	   *oldpasswd 正确密码*/
	  public static boolean checkpassword(String newpasswd,String oldpasswd) {
		  try {
			  if(EncoderByMd5(newpasswd).equals(oldpasswd))
			      return true;
			  else
				  return false;
		  } catch(Exception e) {
			  e.printStackTrace();
			  return false;
		  }
	  }
	  
	  /*
	   * 利用UUID算法生成主键
	   * */
	  public static String createId() {
		  UUID uuid = UUID.randomUUID();
		  String id = uuid.toString().replaceAll("-", "");
		  return id;
	  }
}
