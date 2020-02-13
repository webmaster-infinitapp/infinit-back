package com.paypro.wallet.wallet.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security
            .authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.paypro.wallet.wallet.model.User;
import com.paypro.wallet.wallet.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

import static java.util.Collections.emptyList;

import java.util.ArrayList;

class TokenAuthenticationService {
  static final long EXPIRATIONTIME = 60*60*24*10; // 10 day (segundos) 
  static final String SECRET = "paypr0s3cret!";
  static final String TOKEN_PREFIX = "Paypro";
  static final String HEADER_STRING = "Authorization";
  
  static void addAuthentication(HttpServletResponse res, String username) {
    String JWT = Jwts.builder()
        .setSubject(username)
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
        .signWith(SignatureAlgorithm.HS512, SECRET)
        .compact();
    res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + JWT);
  }

  static Authentication getAuthentication(HttpServletRequest request) {
    String token = request.getHeader(HEADER_STRING);
    if (token != null) {
      // parse the token.
      String user = Jwts.parser()
          .setSigningKey(SECRET)
          .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
          .getBody()
          .getSubject();
      
      
      
      if (user != null)
      {
    	  List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();

          
    	  try {
    		  
			
			if (!user.equals("Apk_Paypr0"))
			{
				list.add(new SimpleGrantedAuthority("ROLE_USER"));
				
			}
			else
			{
				list.add(new SimpleGrantedAuthority("ROLE_APK"));
				
			}
			return new UsernamePasswordAuthenticationToken(user, null, list);
		} catch (Exception e) {
			return null;
		}
    	  
      }
      else
      {
    	  return null;
      }
    }
    return null;
  }
}