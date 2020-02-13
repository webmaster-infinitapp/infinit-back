package com.paypro.wallet.wallet.twilio;

import java.util.Random;

import org.springframework.beans.factory.annotation.Value;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class TwilioAPI {
	// Find your Account Sid and Auth Token at twilio.com/console
	@Value("${paypro.data.twilio.accountSid}")
    private String accountSid;
	@Value("${paypro.data.twilio.authToken}")
    private String authToken;
	@Value("${paypro.data.twilio.telFrom}")
    private String telFrom;
	
	@Value("${paypro.data.twilio.texto}")
    private String texto;

    public Message sendSMS(String telefono, String randomID) {
    	
        Twilio.init(this.accountSid, this.authToken);
        
        Message message = Message
                .creator(new PhoneNumber(telFrom), // to
                        new PhoneNumber(telefono), // from
                        String.format(texto, randomID))
                .create();

        return message;
    }
    
	public String genRandomID() {
		
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(10000);
        return String.valueOf(randomInt);
    }
}


