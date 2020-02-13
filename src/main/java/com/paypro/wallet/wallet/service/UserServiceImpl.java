package com.paypro.wallet.wallet.service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.paypro.wallet.wallet.model.Campania;
import com.paypro.wallet.wallet.model.Contacto;
import com.paypro.wallet.wallet.model.CuentaBlockchain;
import com.paypro.wallet.wallet.model.User;
import com.paypro.wallet.wallet.repository.BlockChainRepository;
import com.paypro.wallet.wallet.repository.CuentaRepository;
import com.paypro.wallet.wallet.repository.TokenRepository;
import com.paypro.wallet.wallet.repository.UserRepository;
import com.paypro.wallet.wallet.restmodel.Perfil;    


@Service("userService")
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CuentaRepository cuentaRepository;

	@Autowired
	private BlockChainRepository blockChainRepository;
	
	@Autowired
	private TokenRepository tokenRepository;
	
	
	@Value("${spring.datasource.username}")
	private String user="";
	
	@Value("${spring.datasource.password}")
	private String pass="";
	
	@Value("${spring.datasource.url}")
	private String url;



	@Autowired
    private Environment env;	
	
	@Override
	public String getBlockChainDefault()
	{
		String valor = ""; 
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("bbdd");
	    
		EntityManager em = emf.createEntityManager();
		 //@Query("select c from Customer c where c.email = :email")
		 String query =
				"select valor from Sistema where Sistema.idParametro = 1 ";
		 
		 List<String> resultado = em.createNativeQuery(query).getResultList();
		 
		 valor = (String)resultado.get(0);
		 
		 
		 emf.close();
		 return  valor;
	}

	public UserRepository getUserRepository() throws Exception{
		return userRepository;
	}
	

	public BlockChainRepository getBlockChainRepository() {
		return blockChainRepository;
	}

	public CuentaRepository getCuentaRepository() {
		return cuentaRepository;
	}

	

	public TokenRepository getTokenRepository() {
		return tokenRepository;
	}

	@Override
	public String insertIntoBlockchain(String stringToUpload) throws Exception{
		
		String valRet = "";
		
		try (final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52)) {
				final HtmlPage page = webClient.getPage(url + stringToUpload);
				valRet = page.getBody().asText();
	    }
		catch (Exception ex)
		{
			throw ex;
		}
		
		return valRet;
	}
	
	public String readFromBlockchain(String txHash) throws Exception {

		String valRet = "";
		
		try (final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52)) {
				final HtmlPage page = webClient.getPage(url + txHash);
				valRet = page.getBody().asText();
	    }
		catch (Exception ex)
		{
			throw ex;
		}
		
		return valRet;
	}

	@Override
	public void saveUser(User user) {
	
		userRepository.save(user);
		
	}

	@Override
	public List<Contacto> getContactos(List<String> telefonos, int idBlockChainDefault) {
		List<Contacto> contactos = new ArrayList<Contacto>();
		String tlfs = "NULL";
		for (int i=0; i<telefonos.size();i++) {
			tlfs=tlfs+","+telefonos.get(i);
		}
		
		String valor = ""; 
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("bbdd");
	    
		EntityManager em = emf.createEntityManager();
		 String query =
				"SELECT u.telefono, c.cuenta\n" + 
				"FROM Sistema s, Usuario u, Cuenta c, Blockchain b\n" + 
				"WHERE\n" + 
				"u.telefono in ("+tlfs+")"+ 
				"and\n" + 
				"c.idBlockchain = b.idBlockchain\n" + 
				"and\n" + 
				"s.valor = " +idBlockChainDefault+ 
				" and\n" + 
				"c.telefono = u.telefono";
		 
		 List<Object[]> resultado = em.createNativeQuery(query).getResultList();
		 
		 
		 for (Object[] a : resultado) {
			 contactos.add(new Contacto(a[0].toString(), a[1].toString()));
		 }

		 
		 emf.close();
		
		return contactos;
	}
	
	@Override
    public Perfil getProfile(String telefono, int idBlockChainDefault) {
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("bbdd");
        EntityManager em = emf.createEntityManager();
        
        String query =
        		"SELECT u.nombre, c.gasPrice, c.gasLimit, u.imagen \n" + 
        		"from Usuario u, Cuenta c\n" + 
        		"where u.telefono = "+telefono+"\n" + 
        		"and u.telefono = c.telefono\n" + 
        		"and c.idBlockchain =" + idBlockChainDefault;
                
        List<Object[]> resultado = em.createNativeQuery(query).getResultList();
     
        Perfil cuenta = new Perfil(resultado.get(0)[0].toString(), (int) resultado.get(0)[1], (int) resultado.get(0)[2], (byte[]) resultado.get(0)[3]);
        	        
        return cuenta;
    }


	@Override
	public void saveAccount(CuentaBlockchain cuenta) {
	
		cuentaRepository.save(cuenta);
		
	}

	
	
	

	@Override
	public List<Campania> getCampanias(String fechaInicio, String fechaFin) {
		
		List<Campania> campanas = new ArrayList<Campania>();
	 
		
		String valor = ""; 
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("bbdd");
	    Date today = new Date();
		EntityManager em = emf.createEntityManager();
		
		String query =
				"SELECT c.idCampana, c.descripcion\n" + 
				"FROM Campana c\n" + 
				"WHERE\n" + 
				//Estado activas = 1
				"c.estado = 1";
			
		 
		 List<Object[]> resultado = em.createNativeQuery(query).getResultList();
				      
		 
		 for (Object[] a : resultado) {
			 
			 campanas.add(new Campania((int) a[0], a[1].toString(), '1'));
		 }

		 
		 emf.close();
		
		return campanas;
	}
	
	
	
}
