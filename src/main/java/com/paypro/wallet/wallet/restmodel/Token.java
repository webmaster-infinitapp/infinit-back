package com.paypro.wallet.wallet.restmodel;

import java.io.UnsupportedEncodingException;

public class Token {
	
	private String smartContratAddress;
	private String tokenSymbol;
	private int decimals;
	private String password;
	private String tokenName;
	
	
	
	public Token(String smartContratAddress, String tokenSymbol, int decimals, String password, String tokenName) {
		super();
		this.smartContratAddress = smartContratAddress;
		this.tokenSymbol = tokenSymbol;
		this.decimals = decimals;
		this.password = password;
		this.tokenName = tokenName;
	}
	public Token() {
		super();
	}
	public String getSmartContratAddress() {
		return smartContratAddress;
	}
	public void setSmartContratAddress(String smartContratAddress) {
		this.smartContratAddress = smartContratAddress;
	}
	public String getTokenSymbol() {
		return tokenSymbol;
	}
	public void setTokenSymbol(String tokenSymbol) {
		this.tokenSymbol = tokenSymbol;
	}
	public int getDecimals() {
		return decimals;
	}
	public void setDecimals(int decimals) {
		this.decimals = decimals;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getTokenName() {
		return tokenName;
	}
	public void setTokenName(String tokenName) {
		this.tokenName = tokenName;
	}
	
	public String getPassDecode64() throws UnsupportedEncodingException
	  {
		  String retVal = "";
		  try {
			
			  byte[] asBytes = java.util.Base64.getDecoder().decode(password);
			  retVal = new String(asBytes, "utf-8");
			  
		  } catch (UnsupportedEncodingException e) {
			  throw e;
		  }
		  return retVal;
	  }
	
	

}
