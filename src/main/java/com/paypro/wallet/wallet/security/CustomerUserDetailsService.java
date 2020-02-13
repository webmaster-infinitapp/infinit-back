package com.paypro.wallet.wallet.security;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.paypro.wallet.wallet.blockchain.BlockchainInterface;
import com.paypro.wallet.wallet.model.User;
import com.paypro.wallet.wallet.repository.UserRepository;

public class CustomerUserDetailsService implements AuthenticationProvider  {
      
    
	
    @Value("${paypro.data.roleapk}")
	private String roleAPK="";
	@Value("${paypro.data.roleuser}")
	private String roleUser="";
    
    @Resource
    private UserRepository userRepository;
    
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String name = authentication.getName();
        // You can get the password here
        String password = authentication.getCredentials().toString();
       
        
        if (name.equals("Apk_Paypr0") && password.equals(""))
        {
        	List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();

            list.add(new SimpleGrantedAuthority(roleAPK));
    		Authentication auth = new UsernamePasswordAuthenticationToken(name,
                    password,list);
            return auth;
        	
        }
        else {
        	User usuario=userRepository.findByTelefono(name);
        	
        	 //Esperamos la password en base64, así que d;ecodificamos antes de comprobar
            byte[] asBytes = java.util.Base64.getDecoder().decode(password);
            String passwordDecodificado = ""; 
            try {
            	BlockchainInterface hasher = new BlockchainInterface();
    			passwordDecodificado = new String(asBytes, "utf-8");
    			passwordDecodificado = hasher.getSHA512(passwordDecodificado).toLowerCase();
    			
    		} catch (UnsupportedEncodingException e) {
    			//Ha fallado la decodificación del password, no podemos dejar hacer login
    			return null;
    		}
             
            if(usuario == null) {
                throw new UsernameNotFoundException("Invalid username/password");
            }
            
            if (usuario != null)
            {
            	if(usuario.getPassword().equals(passwordDecodificado))
            	{
            		List<SimpleGrantedAuthority> list = new ArrayList<SimpleGrantedAuthority>();

                    list.add(new SimpleGrantedAuthority(roleUser));
            		Authentication auth = new UsernamePasswordAuthenticationToken(name,
                            password,list);
                    return auth;
            	}
            }
            
        	
        }
        
        
        return null;
	}
}