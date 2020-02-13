package com.paypro.wallet.wallet.service;


import java.util.List;

import com.paypro.wallet.wallet.model.Campania;
import com.paypro.wallet.wallet.model.Contacto;
import com.paypro.wallet.wallet.model.CuentaBlockchain;
import com.paypro.wallet.wallet.model.User;
import com.paypro.wallet.wallet.repository.BlockChainRepository;
import com.paypro.wallet.wallet.repository.CuentaRepository;
import com.paypro.wallet.wallet.repository.TokenRepository;
import com.paypro.wallet.wallet.repository.UserRepository;
import com.paypro.wallet.wallet.restmodel.Perfil;




public interface UserService {
	
	public void saveUser(User user);
	
	public String insertIntoBlockchain(String stringToUpload) throws Exception;
	public String readFromBlockchain(String txHash) throws Exception;
	
	
	public UserRepository getUserRepository() throws Exception;

	public String getBlockChainDefault();
	public BlockChainRepository getBlockChainRepository();
	public CuentaRepository getCuentaRepository();
	public TokenRepository getTokenRepository();
	
	public List<Contacto> getContactos(List<String> telefonos, int idBlockChainDefault);
	
	public Perfil getProfile(String telefono, int idBlockChainDefault);

	void saveAccount(CuentaBlockchain cuenta);
	
	public List<Campania> getCampanias(String fechaInicio, String fechaFin);


	
}
