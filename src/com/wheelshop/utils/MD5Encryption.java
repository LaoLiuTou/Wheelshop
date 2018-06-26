package com.wheelshop.utils;
import java.security.MessageDigest;

 
public class MD5Encryption {
    private MD5Encryption() {
 
    }
   
    
    public static String getEncryption(String originString) {
    	String md5str = "";
    	try {
    	    // 1 创建一个提供信息摘要算法的对象，初始化为md5算法对象
    	    MessageDigest md = MessageDigest.getInstance("MD5");

    	    // 2 将消息变成byte数组
    	    byte[] input = originString.getBytes();

    	    // 3 计算后获得字节数组,这就是那128位了
    	    byte[] buff = md.digest(input);

    	    // 4 把数组每一字节（一个字节占八位）换成16进制连成md5字符串
    	    md5str = bytesToHex(buff);

    	} catch (Exception e) {
    	    e.printStackTrace();
    	}
    	return md5str;
    }
 
  public static void main(String[] args){
    	
    	System.out.println(getEncryption("384d57c6-da7e-4550-acca-ae4bbf99269c1504515378000!QAZXSW@"));
    	
    }
  

    /**
     * 二进制转十六进制
     * 
     * @param bytes
     * @return
     */
    public static String bytesToHex(byte[] bytes) {
	StringBuffer md5str = new StringBuffer();
	// 把数组每一字节换成16进制连成md5字符串
	int digital;
	for (int i = 0; i < bytes.length; i++) {
	    digital = bytes[i];

	    if (digital < 0) {
		digital += 256;
	    }
	    if (digital < 16) {
		md5str.append("0");
	    }
	    md5str.append(Integer.toHexString(digital));
	}
	return md5str.toString().toUpperCase();
    }
    
}

 