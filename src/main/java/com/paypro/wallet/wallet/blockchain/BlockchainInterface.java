package com.paypro.wallet.wallet.blockchain;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.EthBlock.Block;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetCode;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import com.gargoylesoftware.htmlunit.javascript.host.security.Credential;
import com.paypro.wallet.wallet.model.Blockchain;
import com.paypro.wallet.wallet.model.CuentaBlockchain;
import com.paypro.wallet.wallet.restmodel.DetalleTransaccion;
import com.paypro.wallet.wallet.restmodel.PagTransaccion;
import com.paypro.wallet.wallet.restmodel.Token;
import com.paypro.wallet.wallet.restmodel.Transaccion;
import com.paypro.wallet.wallet.restmodel.TransferToken;




public class BlockchainInterface {
	

	public static class TransferEventResponse {
	    public Address _from;

	    public Address _to;

	    public Uint256 _value;
	}
	
	private String isDes="S";
	
	private Blockchain blockchain;
	private String urlRPC;
	
	public BlockchainInterface() {
		
	}
	
	

	
	
	public BlockchainInterface(Blockchain blockchain) {
		
		
		this.blockchain = blockchain;
		this.urlRPC = "http://"+blockchain.getDireccion()+":"+String.valueOf(blockchain.getPuertoRPC());
		if (this.isDes == "S")
		{
			//TODO cambiar "ruta_keystore" por la ruta donde está el keystore de la cuenta
			this.blockchain.setRutaKeyStore("ruta_keystore");
		}
	}

	public Blockchain getBlockchain() {
		return blockchain;
	}

	public void setBlockchain(Blockchain blockchain) {
		this.blockchain = blockchain;
	}


	public String createAccount(String password) throws IOException
	{
		String cuentaETH;
		Admin web3j = Admin.build(new HttpService(urlRPC));
		cuentaETH = web3j.personalNewAccount(password).send().getResult();
		
		return cuentaETH;
	}
	
	public void changePassowordAccount(String cuenta, String oldPassword, String newPassword) throws Exception
	{
		Account cuentaBlockChain = new Account(cuenta);
		
		cuentaBlockChain.changePassword(oldPassword, newPassword,this.blockchain.getRutaKeyStore());
		//https://web3j.readthedocs.io/en/latest/getting_started.html
		
	}

	public String getBalance(String cuenta) throws IOException, InterruptedException, ExecutionException
	{
		
		Admin web3j = Admin.build(new HttpService(urlRPC));
		EthGetBalance ethGetBalance = web3j
	             .ethGetBalance(cuenta, DefaultBlockParameterName.LATEST)
	             .sendAsync().get();
		
		BigDecimal x18 = new BigDecimal("1000000000000000000");
		BigDecimal bigDecimal = new BigDecimal(ethGetBalance.getBalance());
		BigDecimal resultado = bigDecimal.divide(x18);
		return resultado.toString();
	}
	
	public PagTransaccion getTransactionsByAddress(String cuenta,String pagina) throws IOException, InterruptedException, ExecutionException
	{
		
		String url = String.format(this.blockchain.getUrlWebDetail(),cuenta,pagina);
		Document doc = Jsoup.connect(url).get();
		
		String page = doc.getElementsByClass("profile container")
				.select("div").get(0)
				.getElementsByClass("col-md-6").select("span").get(2).select("b").get(1).text();
		
		
		Elements tabla = doc.getElementsByClass("table-hover");
		Elements tbody = tabla.select("tbody");
		Elements trs = tbody.select("tr");
		
		
		boolean hayTrs = true;
		int pos = 0;
		
		ArrayList<Transaccion> listaTransacciones = new ArrayList<>();
		while (hayTrs)
		{
			Element tr;
			try {
				tr = trs.get(pos);
			} catch (Exception e1) {
				hayTrs = false;
				break;
			}
			boolean vacio= false;
			Elements tds = tr.select("td");
			try {
				if (tds.get(1).select("span").first().text().contains("no matching"))
				{
					vacio=true;
				}
			} catch (Exception e) {
				
			}
			
			if (tds.isEmpty())
			{
				System.out.println("VACIO");
			}else
			if (vacio)
			{
				System.out.println("NO DATA");
			}else
			{
				Transaccion transaccion = new Transaccion();
				for (int i = 0; i<8;i++)
				{
					Element td = tds.get(i);
					if(i == 0)
					{
						transaccion.setHasTransaccion(td.select("a").first().text());
					}
					if(i == 1)
					{
						transaccion.setNumBloque(Integer.parseInt(td.select("a").first().text()));
					}
					if(i == 2)
					{
						transaccion.setFecha(td.select("span").first().attr("title"));
					}
					if(i == 3)
					{
						transaccion.setOrigen(td.select("span").first().text());
					}
					if(i == 4)
					{
						
						transaccion.setTipo(td.select("span").first().text().replaceAll(" ", "").trim());;
					}
					if(i == 5)
					{	
						try {
							transaccion.setDestino(td.select("span").first().text());
						} catch (Exception e) {
							transaccion.setDestino(td.select("a").first().attr("title"));
						}
					}
					if(i == 6)
					{
						transaccion.setEther(td.text().replaceAll(" Ether", ""));
					}
					if(i == 7)
					{
						transaccion.setTxFee(td.select("font").first().text().replaceAll("<b>", "").replaceAll("</b>", ""));
					}
				}
				listaTransacciones.add(transaccion);
			}
			
			pos++;
		}
		
		//añadimos las referentes a los tokens.
		listaTransacciones = this.getTransactionsTokensByAddress(listaTransacciones, cuenta, pagina);
		
		PagTransaccion paginado =  new PagTransaccion(pagina, page, listaTransacciones);
		
		return paginado;

	}
	
	
	public ArrayList<Transaccion> getTransactionsTokensByAddress(ArrayList<Transaccion> listaTransacciones, String cuenta,String pagina) throws IOException, InterruptedException, ExecutionException
	{

		String url = String.format(this.blockchain.getUrlWebDetail().replaceAll("txs",  "tokentxns"),cuenta,pagina);
	
		Document doc = Jsoup.connect(url).get();
		
		
		String page = doc.getElementsByClass("profile container")
				.select("div").get(0)
				.getElementsByClass("col-md-6").select("span").get(2).select("b").get(1).text();
		
		
		Elements tabla = doc.getElementsByClass("table-hover");
		Elements tbody = tabla.select("tbody");
		Elements trs = tbody.select("tr");
		
		
		boolean hayTrs = true;
		int pos = 0;
		
		while (hayTrs)
		{
			Element tr;
			try {
				tr = trs.get(pos);
			} catch (Exception e1) {
				hayTrs = false;
				break;
			}
			boolean vacio= false;
			Elements tds = tr.select("td");
			try {
				if (tds.get(1).select("span").first().text().contains("no matching"))
				{
					vacio=true;
				}
			} catch (Exception e) {
				
			}
			
			if (tds.isEmpty())
			{
				System.out.println("VACIO");
			}else
			if (vacio)
			{
				System.out.println("NO DATA");
			}else
			{
				Transaccion transaccion = new Transaccion();
				for (int i = 0; i<7;i++)
				{
					Element td = tds.get(i);
					if(i == 0)  //TXHASH
					{
						transaccion.setHasTransaccion(td.select("a").first().text());
					}
					if(i == 1)  // Age
					{
						transaccion.setFecha(td.select("span").first().attr("title"));
					}
					if(i == 2)  //From
					{
						transaccion.setOrigen(td.select("span").first().text());
					}
					if(i == 3) //Type 
					{
						
						transaccion.setTipo(td.select("span").first().text().replaceAll(" ", "").trim());
					}
					if(i == 4)  //TO 
					{	
						try {
							transaccion.setDestino(td.select("span").first().text());
						} catch (Exception e) {
							transaccion.setDestino(td.select("a").first().attr("title"));
						}
					}
					if(i == 5) //Value
					{
						transaccion.setEther(td.text());
					}
					if(i == 6) 
					{
						transaccion.setToken(td.select("a").text());
					}
				}
				listaTransacciones.add(transaccion);
			}
			
			pos++;
		}
		
		
		
		//PagTransaccion paginado =  new PagTransaccion(pagina, page, listaTransacciones);
		
		return listaTransacciones;

	}
	
	public BigInteger getBalanceFromToken(String contractAdress, CuentaBlockchain cuenta) throws Exception
	{
		//Account cuentaBlockChain = new Account(cuenta.getCuenta());
		//Credentials credentials = cuentaBlockChain.loadCredentials(token.getPassDecode64(),this.blockchain.getRutaKeyStore());
		Web3j web3j = Web3j.build(new HttpService(this.urlRPC));
		
		BigInteger gasPrice = BigInteger.valueOf(cuenta.getGasPrice());
		BigInteger gasLimit = BigInteger.valueOf(cuenta.getGasLimit()); 
		
		Credentials credentials = Credentials.create("0","0");
		EIP20 myToken1 = EIP20.load(contractAdress, web3j, credentials, gasPrice, gasLimit);
		BigInteger balance = myToken1.balanceOf(cuenta.getCuenta()).send();
		String name = myToken1.name().send();
		return balance;
		
	}
	 	
	
	public String addToken(Token token, CuentaBlockchain cuenta) throws Exception
	{
		Account cuentaBlockChain = new Account(cuenta.getCuenta());
		Credentials credentials = cuentaBlockChain.loadCredentials(token.getPassDecode64(),this.blockchain.getRutaKeyStore());
		Web3j web3j = Web3j.build(new HttpService(this.urlRPC));
		
		EthGetCode ethGetCode = web3j.ethGetCode(token.getSmartContratAddress(),  DefaultBlockParameterName.LATEST).send();
		if (ethGetCode.hasError()) {
	        throw new IllegalStateException("Failed to get code for " + token.getSmartContratAddress() + ": " + ethGetCode.getError().getMessage());
	    }
		
		
		BigInteger gasPrice = BigInteger.valueOf(cuenta.getGasPrice()); ;
		BigInteger gasLimit = BigInteger.valueOf(cuenta.getGasLimit()); 
		
		EIP20 myToken1 = EIP20.load(token.getSmartContratAddress(), web3j, credentials, gasPrice, gasLimit);
		
		String name = myToken1.name().send();
		
		return name;
		
	}
	
	
	public String transferToken(TransferToken transfer, CuentaBlockchain cuenta) throws Exception
	{
		Account cuentaBlockChain = new Account(cuenta.getCuenta());
		Credentials credentials = cuentaBlockChain.loadCredentials(transfer.getPassword(),this.blockchain.getRutaKeyStore());
		Web3j web3j = Web3j.build(new HttpService(this.urlRPC));
		
		EthGetCode ethGetCode = web3j.ethGetCode(transfer.getOrigen(),  DefaultBlockParameterName.LATEST).send();
		if (ethGetCode.hasError()) {
	        throw new IllegalStateException("Failed to get code for " + transfer.getOrigen() + ": " + ethGetCode.getError().getMessage());
	    }
		
		BigInteger saldo = this.getBalanceFromToken(transfer.getOrigen(), cuenta);
		
		if (transfer.getValor().compareTo(saldo) == 1 )
		{
			 throw new IllegalStateException("saldo de  " + transfer.getOrigen() + " es " + saldo.toString());
		}
		
		
		//TODO ajustar gasprice para que no dé error la transacción
		BigInteger gasPrice =  web3j.ethGasPrice().send().getGasPrice(); //BigInteger.valueOf(cuenta.getGasPrice()); ;
		BigInteger gasLimit = BigInteger.valueOf(cuenta.getGasLimit()); 
		
		
		
		EIP20 myToken = EIP20.load(transfer.getOrigen(), web3j, credentials, gasPrice, gasLimit);
		TransactionReceipt transaccion = myToken.transfer(transfer.getDestino(), transfer.getValor()).send();
		String idTransaccion = transaccion.getTransactionHash();
		
		
		return idTransaccion;
		
	}
	
	public String getPrivateKey(String password, CuentaBlockchain cuenta) throws Exception
	{
		Account cuentaBlockChain = new Account(cuenta.getCuenta());
		Credentials credentials = cuentaBlockChain.loadCredentials(password,this.blockchain.getRutaKeyStore());
		String privateKey= credentials.getEcKeyPair().getPrivateKey().toString(16);
		
		return privateKey;
		
	}
	 	
	public String getSHA512(String inputString) throws UnsupportedEncodingException
	{
		String retVal = null;
		
	    try {
	         MessageDigest md = MessageDigest.getInstance("SHA-512");
	         //md.update(salt.getBytes("UTF-8")); // NO SALT!!!
	         byte[] bytes = md.digest(inputString.getBytes("UTF-8"));
	         StringBuilder sb = new StringBuilder();
	         for(int i=0; i< bytes.length ;i++){
	            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
	         }
	         retVal = sb.toString();
	        } 
	       catch (NoSuchAlgorithmException e){
	        e.printStackTrace();
	       }
	    return retVal;
	}
	
	public boolean unlockAccount(CuentaBlockchain cuenta, String password) {
		Account cuentaBlockChain = new Account(cuenta.getCuenta());
		boolean unlock= false;

		Admin web3j = Admin.build(new HttpService(urlRPC));
		PersonalUnlockAccount personalUnlockAccount;
		try {
			personalUnlockAccount = web3j.personalUnlockAccount(cuenta.getCuenta(), password).send();
			if (personalUnlockAccount.accountUnlocked()) {
				   unlock=true;
				}
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return unlock;

	}
	
	
	
	 
	

	public String sendFunds(String origen, String destino, BigDecimal valor, String password, BigInteger gasPrice, BigInteger gasLimit, String data) throws Exception {
		
		
		Account cuentaBlockChain = new Account(origen);
		Credentials credentials = cuentaBlockChain.loadCredentials(password,this.blockchain.getRutaKeyStore());
		
		
		Web3j web3 = Web3j.build(new HttpService(this.urlRPC));  
		
		Transfer transferencia = new Transfer(web3, new RawTransactionManager(web3, credentials));
		//TODO ajustar gasprice para que no dé error la transacción
		gasPrice =  web3.ethGasPrice().send().getGasPrice(); //BigInteger.valueOf(cuenta.getGasPrice()); ;
		TransactionReceipt transactionReceipt = transferencia.sendFunds(destino, valor, Convert.Unit.ETHER, gasPrice, gasLimit, data).send();
		return transactionReceipt.getTransactionHash();


	}
	
	public DetalleTransaccion getDetalleTransaccion(String idTransaccion) {
        
        DetalleTransaccion transaccion = new DetalleTransaccion();
        
        try {
            // Crear la conexion
            Admin web3j = Admin.build(new HttpService(urlRPC));
            
            // Obtenemos detalles de la transaccion
            Transaction tx = web3j.ethGetTransactionByHash(idTransaccion).send().getResult();
            
            // Con el Receipt obtenemos el estado
            Optional<TransactionReceipt> txReceiptOptional =
                     web3j.ethGetTransactionReceipt(tx.getHash()).send().getTransactionReceipt();
            TransactionReceipt txReceipt = txReceiptOptional.get();
            
            // Con el bloque obtenemos la fecha de la transaccion
            Block block = web3j.ethGetBlockByHash(tx.getBlockHash(), false).send().getResult();
                        
            // Obtencion del numero de confirmaciones
            // Nº confirmaciones = Nº bloque actual - Nº bloque donde se encuentra la transaccion
            EthBlockNumber blockNumber = web3j.ethBlockNumber().send();
            BigInteger currentBlockNumber = blockNumber.getBlockNumber();
            BigInteger confirmations = currentBlockNumber.subtract(tx.getBlockNumber());
                
            // Establecemos los detalles de la transaccion
            transaccion.setBloque(tx.getBlockHash());
            transaccion.setCantidad(tx.getValue());
            transaccion.setConfirmaciones(confirmations);
            transaccion.setDestino(tx.getTo());
            transaccion.setEstado(Integer.parseInt(txReceipt.getStatus().substring(2)));
            transaccion.setFecha(new java.util.Date(block.getTimestamp().longValue()));
            transaccion.setMensaje(tx.getInput()); 
            transaccion.setTransaccion(tx.getHash());
            transaccion.setMoneda("ETH");
            
        }
        catch ( Exception e) {
            System.err.println(e);
        }
        
        return transaccion;
        
    }
}
