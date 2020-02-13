package com.paypro.wallet.wallet.controller;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paypro.wallet.wallet.blockchain.BlockchainInterface;
import com.paypro.wallet.wallet.model.Blockchain;
import com.paypro.wallet.wallet.model.Campania;
import com.paypro.wallet.wallet.model.Contacto;
import com.paypro.wallet.wallet.model.CuentaBlockchain;
import com.paypro.wallet.wallet.model.Response;
import com.paypro.wallet.wallet.model.TokenMdl;
import com.paypro.wallet.wallet.model.User;
import com.paypro.wallet.wallet.restmodel.Balance;
import com.paypro.wallet.wallet.restmodel.ChangePassword;
import com.paypro.wallet.wallet.restmodel.DetalleTransaccion;
import com.paypro.wallet.wallet.restmodel.PagTransaccion;
import com.paypro.wallet.wallet.restmodel.Perfil;
import com.paypro.wallet.wallet.restmodel.SendTransaction;
import com.paypro.wallet.wallet.restmodel.Token;
import com.paypro.wallet.wallet.restmodel.TransferToken;
import com.paypro.wallet.wallet.security.AuthenticationFacade;
import com.paypro.wallet.wallet.service.UserService;
import com.paypro.wallet.wallet.twilio.TwilioAPI;




@RestController
public class WallerController {

	
	@Autowired
	private AuthenticationFacade authenticationFacade;
	
	@Autowired
	private UserService userService;
	
	
	
	
	@RequestMapping(method=RequestMethod.POST, path="/registro", produces = "application/json")
	@Secured("ROLE_APK")
	public ResponseEntity<?> registro(@RequestBody User usuario, HttpServletRequest request) {

		try {
			String isOTPOK = (String) request.getSession().getAttribute("isOTPOK");
			if (isOTPOK == null | !isOTPOK.equals("S"))
			{
				return new ResponseEntity<Response>(new Response(1, "Error creación cuenta"), HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			String telefono = usuario.getTelefono();
			User usuarioAux = userService.getUserRepository().findByTelefono(telefono);
			if (usuarioAux != null)
			{
				return new ResponseEntity<Response>(new Response(1, "Error creación cuenta"), HttpStatus.INTERNAL_SERVER_ERROR);
			}
			usuario.setEstado("H");
			usuario.setFechaCreacion(new Date());
			usuario.setFechaUltimoAcceso(new Date());
			usuario.setCodPais(this.getCodPais(telefono));
			usuario.setNombre(usuario.getNombre());
			usuario.setAlias(usuario.getAlias());
			String password = usuario.getPassDecode64();
			if (usuario.getImagen()!=null) {
				usuario.setImagen(usuario.getImagen());	
			}
			
			
			
			if(usuario.isValidUser())
			{
				String valorSis = userService.getBlockChainDefault();
				int idBlockChainDefault = Integer.parseInt(valorSis);
				
				CuentaBlockchain cuenta = new CuentaBlockchain();
				
				Blockchain block = userService.getBlockChainRepository().findByIdBlockchain(idBlockChainDefault);
				
				BlockchainInterface blkInterface = new BlockchainInterface(block);
				
				
				String cuentaETH = blkInterface.createAccount(password);
				
				cuenta.setCuenta(cuentaETH);
				cuenta.setIdBlockchain(idBlockChainDefault);
				cuenta.setTelefono(telefono);
				cuenta.setDescripcion(block.getDescripcion());
				cuenta.setFechaCreacion(new Date());
				cuenta.setGasLimit(block.getGasLimit());
				cuenta.setGasPrice(block.getGasPrice());
				
				
				usuario.encriptarPass();
				
				userService.getUserRepository().save(usuario);
				userService.getCuentaRepository().save(cuenta);
				
			}
			else
			{
				return new ResponseEntity<Response>(new Response(1, "Error creación cuenta. Usuario no válido"), HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			
			
		} catch (Exception e) {
			return new ResponseEntity<Response>(new Response(1, "Error creación cuenta"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
			
		return new ResponseEntity<Response>(new Response(0, "OK"), HttpStatus.OK);
	}
	
	
	@RequestMapping(method=RequestMethod.GET, path="/otp", produces = "application/json")
	@Secured("ROLE_APK")
	public ResponseEntity<?> verifyOTP(@RequestParam("otp") String otp,HttpServletRequest request) {

		try {
			
			String otpContext=(String) request.getSession().getAttribute("OTP");
			if (otpContext==null || !otpContext.equals(otp))
			{
				return new ResponseEntity<Response>(new Response(1, "Error OTP"), HttpStatus.INTERNAL_SERVER_ERROR);
			}
			request.getSession().setAttribute("isOTPOK","S");
		} catch (Exception e) {
			return new ResponseEntity<Response>(new Response(1, "Error OTP"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
			
		return new ResponseEntity<Response>(new Response(0, "OK"), HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.POST, path="/otp", produces = "application/json")
	@Secured("ROLE_APK")
	public ResponseEntity<?> sendOPT(@RequestBody User usuario, HttpServletRequest request) {

		try {
			String telefono = usuario.getTelefono();
			TwilioAPI apiTwilio = new TwilioAPI();
			
			//TODO Una vez que se ha dado de alta la cuenta twilio, meter los datos aquí
			String randomID = apiTwilio.genRandomID();
			
			//FIXME para pruebas en local se puede poner el randomID estático 
			//String randomID = "1111";
			System.out.println(randomID);
			request.getSession().setAttribute("OTP",randomID);
			request.getSession().setAttribute("isOTPOK","N");

			apiTwilio.sendSMS(telefono, randomID);
			
			
		} catch (Exception e) {
			return new ResponseEntity<Response>(new Response(1, "Error OTP"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
			
		return new ResponseEntity<Response>(new Response(0, "OK"), HttpStatus.OK);
	}
	
	
	@RequestMapping(method=RequestMethod.POST, path="/transferToken", produces = "application/json")
	@Secured("ROLE_USER")
	public ResponseEntity<?> transferToken(@RequestBody TransferToken transferToken) {

		try {
			String telefono = authenticationFacade.getAuthentication().getName();
			
			User usuarioAux = userService.getUserRepository().findByTelefono(telefono);
			if (usuarioAux != null  && usuarioAux.getEstado().equals("H"))
			{
				String valorSis = userService.getBlockChainDefault();
				int idBlockChainDefault = Integer.parseInt(valorSis);
				
				CuentaBlockchain cuenta = userService.getCuentaRepository().findByTelefonoAndIdBlockchain(telefono, idBlockChainDefault);
				Blockchain block = userService.getBlockChainRepository().findByIdBlockchain(idBlockChainDefault);
				BlockchainInterface blkInterface = new BlockchainInterface(block);
				
				usuarioAux.setPassword(transferToken.getPassword());
				transferToken.setPassword(usuarioAux.getPassDecode64());
				String idTx = null;
				
				idTx = blkInterface.transferToken(transferToken, cuenta);	
				
				
					
				return new ResponseEntity<String>(idTx, HttpStatus.OK);
			}
			else
			{
				return new ResponseEntity<Response>(new Response(1, "Error al realizar la transacción"), HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
		} catch (Exception e) {
			return new ResponseEntity<Response>(new Response(1, "Error al realizar la transacción"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
			
	}
	
	
	
	@RequestMapping(method=RequestMethod.POST, path="/changepass", produces = "application/json")
	@Secured("ROLE_USER")
	public ResponseEntity<?> changepass(@RequestBody ChangePassword usuario) {

		try {
			//http://www.joseluisestevez.com/index.php/2017/07/16/creando-aplicaciones-la-blockchain-ethereum-usando-java-web3j/
			String telefono = authenticationFacade.getAuthentication().getName();
			usuario.setPassword(usuario.getPassDecode64(usuario.getPassword()));
			String passwordOld = usuario.getPassword();
			usuario.encriptarPass();
			
			User usuarioAux = userService.getUserRepository().findByTelefono(telefono);
			if (usuarioAux != null 
					&& usuarioAux.getEstado().equals("H")
					&& usuario.getPassword().equals(usuarioAux.getPassword())
					)
			{
				String valorSis = userService.getBlockChainDefault();
				int idBlockChainDefault = Integer.parseInt(valorSis);
				
				CuentaBlockchain cuenta = userService.getCuentaRepository().findByTelefonoAndIdBlockchain(telefono, idBlockChainDefault);
				
				Blockchain block = userService.getBlockChainRepository().findByIdBlockchain(idBlockChainDefault);
				
				BlockchainInterface blkInterface = new BlockchainInterface(block);
				usuarioAux.setPassword(usuario.getPassDecode64(usuario.getNewPassword()));
				String newPass = usuarioAux.getPassDecode64();
				blkInterface.changePassowordAccount(cuenta.getCuenta(), passwordOld, newPass);
				usuarioAux.encriptarPass();
				userService.getUserRepository().save(usuarioAux);
	
			}
			
			else
			{
				return new ResponseEntity<Response>(new Response(1, "Error cambiar password"), HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
		} catch (Exception e) {
			return new ResponseEntity<Response>(new Response(1, "Error cambiar password"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
			
		return new ResponseEntity<Response>(new Response(0, "OK"), HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.GET, path="/balance", produces = "application/json")
	@Secured("ROLE_USER")
	public ResponseEntity<?> balance() {

		try {
			
			String telefono = authenticationFacade.getAuthentication().getName();
			
			
			User usuarioAux = userService.getUserRepository().findByTelefono(telefono);
			if (usuarioAux != null  && usuarioAux.getEstado().equals("H"))
			{
				String valorSis = userService.getBlockChainDefault();
				int idBlockChainDefault = Integer.parseInt(valorSis);
				
				CuentaBlockchain cuenta = userService.getCuentaRepository().findByTelefonoAndIdBlockchain(telefono, idBlockChainDefault);
				Blockchain block = userService.getBlockChainRepository().findByIdBlockchain(idBlockChainDefault);
				BlockchainInterface blkInterface = new BlockchainInterface(block);
				String balanceStr = blkInterface.getBalance(cuenta.getCuenta());
				
				
					
				
				Balance balance = new Balance(
						cuenta.getCuenta(), 
						cuenta.getDescripcion(), 
						balanceStr,
						block.getSiglaBlockchain()
				);
				ArrayList<Balance> listaBalances = new ArrayList<>();
				listaBalances.add(balance);
				
				List<TokenMdl> tokens =userService.getTokenRepository().findByTelefonoAndIdBlockchain(telefono, idBlockChainDefault);
				for (int i = 0; i< tokens.size();i++)
				{
					TokenMdl token = tokens.get(i);
					BigInteger balanceToken = blkInterface.getBalanceFromToken(token.getContracAddres(), cuenta);
					Balance balancetoken = new Balance(
							token.getContracAddres(), 
							token.getTokenName(), 
							balanceToken.toString(),
							token.getTokenSymbol()
							);
					listaBalances.add(balancetoken);
					
				}
								
				return new ResponseEntity<ArrayList<Balance>>(listaBalances,HttpStatus.OK);
			}
			else
			{
				return new ResponseEntity<Response>(new Response(1, "Error balance"), HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
		} catch (Exception e) {
			return new ResponseEntity<Response>(new Response(1, "Error balance"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
			
		
	}
	
	
	@RequestMapping(method=RequestMethod.GET, path="/transacHist", produces = "application/json")
	@Secured("ROLE_USER")
	public ResponseEntity<?> transacHist(@RequestParam("cuenta") String cuenta, @RequestParam("pagina") String pagina) {

		try {
			
			String telefono = authenticationFacade.getAuthentication().getName();
			
			
			User usuarioAux = userService.getUserRepository().findByTelefono(telefono);
			if (usuarioAux != null  && usuarioAux.getEstado().equals("H"))
			{
				String valorSis = userService.getBlockChainDefault();
				int idBlockChainDefault = Integer.parseInt(valorSis);
				
				CuentaBlockchain cuentaUser = userService.getCuentaRepository().findByTelefonoAndCuenta(telefono, cuenta);
				if(cuentaUser!=null)
				{
					Blockchain block = userService.getBlockChainRepository().findByIdBlockchain(idBlockChainDefault);
					BlockchainInterface blkInterface = new BlockchainInterface(block);
					
					
					PagTransaccion paginado=blkInterface.getTransactionsByAddress(cuentaUser.getCuenta(),pagina);
					
					if (paginado.getListaTransacciones().size()==0)
					{
						return new ResponseEntity<Response>(new Response(1, "Error transhist"), HttpStatus.NO_CONTENT);
					}
					
					return new ResponseEntity<PagTransaccion>(paginado,HttpStatus.OK);
				}
				else {
					return new ResponseEntity<Response>(new Response(1, "Error transhist"), HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
			else
			{
				return new ResponseEntity<Response>(new Response(1, "Error transhist"), HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
		} catch (Exception e) {
			return new ResponseEntity<Response>(new Response(1, "Error transhist"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
			
		
	}
	
	@RequestMapping(method=RequestMethod.POST, path="/contactos", produces = "application/json")
	@Secured("ROLE_USER")
	public ResponseEntity<?> contactos(@RequestBody List<String> telefonos, @RequestParam("fechaInicio") String fechaInicio,
			  @RequestParam("fechaFin") String fechaFin) {

		try {
			
			String telefono = authenticationFacade.getAuthentication().getName();
			
			
			User usuarioAux = userService.getUserRepository().findByTelefono(telefono);
			if (usuarioAux != null  && usuarioAux.getEstado().equals("H"))
			{
				String valorSis = userService.getBlockChainDefault();
				int idBlockChainDefault = Integer.parseInt(valorSis);
			
		
				List<String> tels = this.formatTelefonos(telefonos, telefono, usuarioAux.getCodPais());
				
				
				List<Contacto> listaContactos = userService.getContactos(tels, idBlockChainDefault);
				
				//Recogemos también las campañas para mostrarlas a continuacion:
				ResponseEntity<List<Campania>> listCampanas = (ResponseEntity<List<Campania>>) this.campanas(fechaInicio,fechaFin);
				
				for (int i=0; i< listCampanas.getBody().size();i++) {
					
					System.out.println(listCampanas.getBody().get(i).getIdCampania());
					System.out.println(listCampanas.getBody().get(i).getDescripcion());
					Contacto nuevo = new Contacto("Campaña_ "+listCampanas.getBody().get(i).getDescripcion(), "XXXX");
					listaContactos.add(nuevo);
				}
				
				
				
				return new ResponseEntity<List<Contacto>>(listaContactos,HttpStatus.OK);
				
				
				
			}
			else
			{
				return new ResponseEntity<Response>(new Response(1, "Error contactos"), HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
		} catch (Exception e) {
			return new ResponseEntity<Response>(new Response(1, "Error contactos"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
			
		
	}
	
	
	@RequestMapping(method=RequestMethod.POST, path="/campanas", produces = "application/json")
	@Secured("ROLE_USER")
	public ResponseEntity<?> campanas(@RequestParam("fechaInicio") String fechaInicio,
									  @RequestParam("fechaFin") String fechaFin){

		try {
			
			String telefono = authenticationFacade.getAuthentication().getName();
			
			
			User usuarioAux = userService.getUserRepository().findByTelefono(telefono);
			if (usuarioAux != null  && usuarioAux.getEstado().equals("H"))
				{
					List<Campania> listaCampanas = userService.getCampanias(fechaInicio, fechaFin);
					
					return new ResponseEntity<List<Campania>>(listaCampanas,HttpStatus.OK);
				}
			else
				{
					return new ResponseEntity<Response>(new Response(1, "Error campanias"), HttpStatus.INTERNAL_SERVER_ERROR);
				}
				
		} catch (Exception e) {
			return new ResponseEntity<Response>(new Response(1, "Error campanias"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
			
		
	}
	
	
	@RequestMapping(method=RequestMethod.POST, path="/addNewToken", produces = "application/json")
	@Secured("ROLE_USER")
	public ResponseEntity<?> addNewToken(@RequestBody Token token) {

		try {
			
			String telefono = authenticationFacade.getAuthentication().getName();
			
			
			User usuarioAux = userService.getUserRepository().findByTelefono(telefono);
			if (usuarioAux != null  && usuarioAux.getEstado().equals("H"))
			{
				String valorSis = userService.getBlockChainDefault();
				int idBlockChainDefault = Integer.parseInt(valorSis);
				CuentaBlockchain cuenta = userService.getCuentaRepository().findByTelefonoAndIdBlockchain(telefono, idBlockChainDefault);
				Blockchain block = userService.getBlockChainRepository().findByIdBlockchain(idBlockChainDefault);
				BlockchainInterface blkInterface = new BlockchainInterface(block);
				String tokenName = blkInterface.addToken(token, cuenta);
				TokenMdl tokenMdl =  new TokenMdl(telefono, idBlockChainDefault, token.getSmartContratAddress(), token.getDecimals(), 
						token.getTokenSymbol(), tokenName, new Date());
				
				userService.getTokenRepository().save(tokenMdl);
				
				return new ResponseEntity<Response>(new Response(0, "OK"), HttpStatus.OK);
			}
			else
			{
				return new ResponseEntity<Response>(new Response(1, "Error addNewToken"), HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
		} catch (Exception e) {
			return new ResponseEntity<Response>(new Response(1, "Error addNewToken"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
			
		
	}


	private List<String> formatTelefonos(List<String> telefonos, String telefono, String codPais) {
		List<String> telefonosFormat = new ArrayList<String>();
		for (int i= 0; i<telefonos.size(); i++) {
				//si ya tiene prefijo, se deja
				if (telefonos.get(i).startsWith("00")) {
					//Cambiar el 00 por un +
					telefonosFormat.add(telefonos.get(i).replace("00", "+"));
				}else if(telefonos.get(i).startsWith("+")) {
					telefonosFormat.add(telefonos.get(i));
				}else { //No tiene prefijo, SE AÑADE con codPais
					telefonosFormat.add(codPais+telefonos.get(i));
				}
		}	  
		
		return telefonosFormat;
				
	}
	
	private String getCodPais(String telefono) {
		
		String codPais="+34";
		
		if (telefono.length()==12) { // Tlf con prefijo +34666887744
			codPais = telefono.substring(0, 3);
		}
		else {
			if (telefono.length()==11) { //tlf  con prefijo 34666887744
				codPais = "+" + telefono.substring(0, 2);
			}
		}
		
		return codPais;
		
		
		
	}
	
	
	@RequestMapping(method=RequestMethod.POST, path="/recibir", produces = "application/json")
	@Secured("ROLE_USER")
	public ResponseEntity<?> recibir() {

		try {
			
			String telefono = authenticationFacade.getAuthentication().getName();
			
			
			User usuarioAux = userService.getUserRepository().findByTelefono(telefono);
			if (usuarioAux != null  && usuarioAux.getEstado().equals("H"))
			{
				String valorSis = userService.getBlockChainDefault();
				int idBlockChainDefault = Integer.parseInt(valorSis);
				
				CuentaBlockchain cuenta = userService.getCuentaRepository().findByTelefonoAndIdBlockchain(telefono, idBlockChainDefault);
				
				return new ResponseEntity<String>(cuenta.getCuenta(), HttpStatus.OK);
			}
			else
			{
				return new ResponseEntity<Response>(new Response(1, "Error recibir"), HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
		} catch (Exception e) {
			return new ResponseEntity<Response>(new Response(1, "Error recibir"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
			
		
	}
	
	
	@RequestMapping(method=RequestMethod.POST, path="/transaccion", produces = "application/json")
	@Secured("ROLE_USER")
	public ResponseEntity<?> transaccion(@RequestBody SendTransaction transaccion
		){
	
		
		//Salida: Salida Código verificación y Código transacción
		try {
			
			String telefono = authenticationFacade.getAuthentication().getName();
			
			
			User usuarioAux = userService.getUserRepository().findByTelefono(telefono);
			if (usuarioAux != null  && usuarioAux.getEstado().equals("H"))
			{
				String valorSis = userService.getBlockChainDefault();
				int idBlockChainDefault = Integer.parseInt(valorSis);
				
				CuentaBlockchain cuenta = userService.getCuentaRepository().findByTelefonoAndIdBlockchain(telefono, idBlockChainDefault);
				Blockchain block = userService.getBlockChainRepository().findByIdBlockchain(idBlockChainDefault);
				BlockchainInterface blkInterface = new BlockchainInterface(block);
				
				//String send = blkInterface.sendEthers(cuenta, destino, valor);
				//Credentials credentials = WalletUtils.loadCredentials("password", cuenta.getPassword());
				
				usuarioAux.setPassword(transaccion.getPassword());
				String idTx = null;
				if (blkInterface.unlockAccount(cuenta, usuarioAux.getPassDecode64())) {
					idTx = blkInterface.sendFunds(cuenta.getCuenta(), transaccion.getDestino(), transaccion.getValor(),  usuarioAux.getPassDecode64(), BigInteger.valueOf(cuenta.getGasPrice()), BigInteger.valueOf(cuenta.getGasLimit()), transaccion.getDescripcion());	
				}
				
					
				return new ResponseEntity<String>(idTx, HttpStatus.OK);
			}
			else
			{
				return new ResponseEntity<Response>(new Response(1, "Error al realizar la transacción"), HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
		} catch (Exception e) {
			return new ResponseEntity<Response>(new Response(1, "Error al realizar la transacción"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
			
		
	}

	
	
	
	@RequestMapping(method=RequestMethod.GET, path="/profile", produces = "application/json")
    @Secured("ROLE_USER")
    public ResponseEntity<?> profile() {

        try {
            
            String telefono = authenticationFacade.getAuthentication().getName();
            
            User usuarioAux = userService.getUserRepository().findByTelefono(telefono);
            if (usuarioAux != null  && usuarioAux.getEstado().equals("H"))
            {
                String valorSis = userService.getBlockChainDefault();
                int idBlockChainDefault = Integer.parseInt(valorSis);
                
                Perfil datos = userService.getProfile(telefono, idBlockChainDefault);
                
                return new ResponseEntity<Perfil>(datos,HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<Response>(new Response(1, "Error profile"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            
        } catch (Exception e) {
            return new ResponseEntity<Response>(new Response(1, "Error profile"), HttpStatus.INTERNAL_SERVER_ERROR);
        }    
    }
	
	@RequestMapping(method=RequestMethod.GET, path="/transaccion", produces = "application/json")
    @Secured("ROLE_USER")
    public ResponseEntity<?> detalleTransaccion(@RequestParam String idTransaccion) {

        try {
            
            String telefono = authenticationFacade.getAuthentication().getName();
            
            User usuarioAux = userService.getUserRepository().findByTelefono(telefono);
            if (usuarioAux != null  && usuarioAux.getEstado().equals("H"))
            {
                String valorSis = userService.getBlockChainDefault();
                int idBlockChainDefault = Integer.parseInt(valorSis);
                
                Blockchain block = userService.getBlockChainRepository().findByIdBlockchain(idBlockChainDefault);
                BlockchainInterface blkInterface = new BlockchainInterface(block);
                
                DetalleTransaccion response = blkInterface.getDetalleTransaccion(idTransaccion);
                
                if (response.getTransaccion() == null) {
                    return new ResponseEntity<Response>(new Response(1, "No se ha encontrado la transaccion con id "+idTransaccion), HttpStatus.INTERNAL_SERVER_ERROR);
                }
                
                return new ResponseEntity<DetalleTransaccion>(response,HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<Response>(new Response(1, "Error detalle transaccion"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            
        } catch (Exception e) {
            System.err.println(e);
            return new ResponseEntity<Response>(new Response(1, "Error detalle transaccion"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
            
        
    }
	
	@RequestMapping(method=RequestMethod.POST, path="/updateGasPrice", produces = "application/json")
	@Secured("ROLE_USER")
	public ResponseEntity<?> updateGasPrice(@RequestParam int newGasPrice){
	
		try {
			
			String telefono = authenticationFacade.getAuthentication().getName();
			
			
			User usuarioAux = userService.getUserRepository().findByTelefono(telefono);
			if (usuarioAux != null  && usuarioAux.getEstado().equals("H"))
			{
				String valorSis = userService.getBlockChainDefault();
				int idBlockChainDefault = Integer.parseInt(valorSis);
				
				CuentaBlockchain cuenta = userService.getCuentaRepository().findByTelefonoAndIdBlockchain(telefono, idBlockChainDefault);

				//Se setea el nuevo campo de gasprice
				cuenta.setGasPrice(newGasPrice);
		
				userService.getCuentaRepository().save(cuenta);
				
				
				return new ResponseEntity<>(newGasPrice, HttpStatus.OK);
			}
 		else
			{
				return new ResponseEntity<Response>(new Response(1, "Error al actualizar la cuenta"), HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
		} catch (Exception e) {
			return new ResponseEntity<Response>(new Response(1, "Error al actualizar la cuenta"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
				
			
	}
	
	
	@RequestMapping(method=RequestMethod.POST, path="/updateGasLimit", produces = "application/json")
	@Secured("ROLE_USER")
	public ResponseEntity<?> updateGasLimit(@RequestParam int newGasLimit){
	
		try {
			
			String telefono = authenticationFacade.getAuthentication().getName();
			
			
			User usuarioAux = userService.getUserRepository().findByTelefono(telefono);
			if (usuarioAux != null  && usuarioAux.getEstado().equals("H"))
			{
				String valorSis = userService.getBlockChainDefault();
				int idBlockChainDefault = Integer.parseInt(valorSis);
				
				CuentaBlockchain cuenta = userService.getCuentaRepository().findByTelefonoAndIdBlockchain(telefono, idBlockChainDefault);

				//Se setea el nuevo campo de gasprice
				cuenta.setGasLimit(newGasLimit);
		
				userService.getCuentaRepository().save(cuenta);
				
				
				return new ResponseEntity<>(newGasLimit, HttpStatus.OK);
			}
 		else
			{
				return new ResponseEntity<Response>(new Response(1, "Error al actualizar la cuenta"), HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
		} catch (Exception e) {
			return new ResponseEntity<Response>(new Response(1, "Error al actualizar la cuenta"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
				
			
	}
	
	
	@RequestMapping(method=RequestMethod.POST, path="/getPrivKey", produces = "application/json")
	@Secured("ROLE_USER")
	public ResponseEntity<?> getPrivateKey(@RequestBody ChangePassword password) {

		try {
			String telefono = authenticationFacade.getAuthentication().getName();
			
			User usuarioAux = userService.getUserRepository().findByTelefono(telefono);
			if (usuarioAux != null  && usuarioAux.getEstado().equals("H"))
			{
				String valorSis = userService.getBlockChainDefault();
				int idBlockChainDefault = Integer.parseInt(valorSis);
				
				CuentaBlockchain cuenta = userService.getCuentaRepository().findByTelefonoAndIdBlockchain(telefono, idBlockChainDefault);
				Blockchain block = userService.getBlockChainRepository().findByIdBlockchain(idBlockChainDefault);
				BlockchainInterface blkInterface = new BlockchainInterface(block);
				
				usuarioAux.setPassword(password.getPassword());
				String pass = usuarioAux.getPassDecode64();
				String privateKey = null;
				privateKey = blkInterface.getPrivateKey(pass, cuenta);	
				
				return new ResponseEntity<String>(privateKey, HttpStatus.OK);
			}
			else
			{
				return new ResponseEntity<Response>(new Response(1, "Error al realizar la transacción"), HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
		} catch (Exception e) {
			return new ResponseEntity<Response>(new Response(1, "Error al realizar la transacción"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
			
	}
	
	
	@RequestMapping(method=RequestMethod.POST, path="/updatePhoto", produces = "application/json")
	@Secured("ROLE_USER")
	public ResponseEntity<?> updatePhoto(@RequestBody byte[] imagen){
	
		try {
			
			String telefono = authenticationFacade.getAuthentication().getName();
			
			
			User usuarioAux = userService.getUserRepository().findByTelefono(telefono);
			if (usuarioAux != null  && usuarioAux.getEstado().equals("H"))
			{
				
				//Se setea el nuevo campo de foto
			 
				usuarioAux.setImagen(imagen);
				userService.getUserRepository().save(usuarioAux);
				
				
				return new ResponseEntity<>(imagen, HttpStatus.OK);
			}
 		else
			{
				return new ResponseEntity<Response>(new Response(1, "Error al actualizar la cuenta"), HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
		} catch (Exception e) {
			return new ResponseEntity<Response>(new Response(1, "Error al actualizar la cuenta"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
				
			
	}
	

}
