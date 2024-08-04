package com.evergreen_hotels.bmg.wuf1.util;
import java.security.*;

public class WUF1_HashAlgorithm {

	public static String MD5(String plainText, String key)
	{
		byte[] digest;

		try
		{
			MessageDigest mdAlgorithm = MessageDigest.getInstance("MD5");
			plainText = plainText + key;
			mdAlgorithm.update(plainText.getBytes());
			digest = mdAlgorithm.digest();
		}
		catch (NoSuchAlgorithmException e)
		{
			return e.getMessage();
		}
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < digest.length; i++)
			{
				plainText = Integer.toHexString(0xFF & digest[i]);
				if (plainText.length() < 2)
				{
					plainText = "0" + plainText;
				}
				hexString.append(plainText);
			}

			return hexString.toString().toUpperCase();
	}
}
