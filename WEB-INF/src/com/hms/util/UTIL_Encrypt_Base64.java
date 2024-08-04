package com.hms.util;

import java.io.UnsupportedEncodingException;
import java.security.*;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import org.apache.commons.codec.binary.Base64;

/**
 * 加密工具
 * 
 * @author 290098
 * 
 */
/**
 * BASE64的加密解密是雙向的，可以求反解.
 * BASE64Encoder和BASE64Decoder是非官方JDK實現類。雖然可以在JDK裡能找到並使用，但是在API裡查不到。
 * JRE 中 sun 和 com.sun 開頭包的類都是未被文檔化的，他們屬於 java, javax 類庫的基礎，其中的實現大多數與底層平臺有關，
 * 一般來說是不推薦使用的。 
 * BASE64 嚴格地說，屬於編碼格式，而非加密演算法 
 * 主要就是BASE64Encoder、BASE64Decoder兩個類，我們只需要知道使用對應的方法即可。
 * 另，BASE加密後產生的位元組位元數是8的倍數，如果不夠位元數以=符號填充。 
 * BASE64 按照RFC2045的定義，Base64被定義為：Base64內容傳送編碼被設計用來把任意序列的8位元位元組描述為一種不易被人直接識別的形式。
 * （The Base64 Content-Transfer-Encoding is designed to represent arbitrary sequences of octets in a form that need not be humanly readable.） 
 * 常見於郵件、http加密，截取http資訊，你就會發現登錄操作的用戶名、密碼欄位通過BASE64加密的。
 */
public class UTIL_Encrypt_Base64 {
	
	private static String algorithm = "DESede";
    private static Key key = null;
    private static Cipher cipher = null;

    private static void setUp() throws Exception {
        key = KeyGenerator.getInstance( algorithm ).generateKey();
        cipher = Cipher.getInstance( algorithm );
    }


	public static String encrypt(String pText) throws UnsupportedEncodingException {
		Base64 base64 = new Base64();
		// 使用Base64進行字串編碼
		pText = UTIL_String.nvl(pText, " ");
		String encodeString = new String(base64.encode(pText.getBytes("UTF-8")));
		// 輸出結果將為"VGhpcyBpcyBzb3VyY2Ugc3RyaW5nLg=="
		//return pText;
		return encodeString;
	}

	public static String decrypt(String cText) throws UnsupportedEncodingException {
		Base64 base64 = new Base64();
		// 使用Base64進行字串解碼
		String decodeString = new String(base64.decode(cText.getBytes("UTF-8")));
		// 輸出結果將為"This is source string."
		// return cText;
		return decodeString;
	}
	
	public static String getTab(String cText) throws UnsupportedEncodingException {
		Base64 base64 = new Base64();
		// 使用Base64進行字串解碼
		String decodeString = new String(base64.decode(cText.getBytes("UTF-8")));
		// 輸出結果將為"This is source string."
		// return cText;
		String loginID = decodeString.split(",")[2];		
		return loginID;
	}

}