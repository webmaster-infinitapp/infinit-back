package com.paypro.wallet.wallet.restmodel;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChangePassword {
	
	private String password;
	private String newPassword;
	

	public String getPassDecode64(String pass) throws UnsupportedEncodingException
	  {
		  String retVal = "";
		  try {
			
			  byte[] asBytes = java.util.Base64.getDecoder().decode(pass);
			  retVal = new String(asBytes, "utf-8");
			  
		  } catch (UnsupportedEncodingException e) {
			  throw e;
		  }
		  return retVal;
	  }
	  


	  
	  public void encriptarPass() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		  String retVal = null;
			
		    try {
		         MessageDigest md = MessageDigest.getInstance("SHA-512");
		         //md.update(salt.getBytes("UTF-8")); // NO SALT!!!
		         byte[] bytes = md.digest(this.password.getBytes("UTF-8"));
		         StringBuilder sb = new StringBuilder();
		         for(int i=0; i< bytes.length ;i++){
		            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
		         }
		         retVal = sb.toString();
		        } 
		       catch (NoSuchAlgorithmException e){
		        e.printStackTrace();
		       }
		    this.password= retVal;
		  
		 
		  
	  }
	  public void encriptarNewPass() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		  String retVal;
		  try {
			  byte[] asBytes = java.util.Base64.getDecoder().decode(newPassword);
			  MessageDigest md = MessageDigest.getInstance("SHA-512");
			  String passwordDecodificado = new String(asBytes, "utf-8");
			  byte[] bytes = md.digest(passwordDecodificado.getBytes("UTF-8"));
			  StringBuilder sb = new StringBuilder();
			  for(int i=0; i< bytes.length ;i++){
				  sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			  }
			  retVal = sb.toString();
			  this.newPassword = retVal;
		  } 
		  catch (NoSuchAlgorithmException e){
			  throw e;
		  } catch (UnsupportedEncodingException e) {
			  throw e;
		}
		  
	  }

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	  
	  
	  
	

}
