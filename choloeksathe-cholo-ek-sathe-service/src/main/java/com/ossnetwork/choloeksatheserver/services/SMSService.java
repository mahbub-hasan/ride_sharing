package com.ossnetwork.choloeksatheserver.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.ws.rs.Encoded;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.aspectj.bridge.Version.text;

@Service
public class SMSService {

    URLConnection urlConnection = null;
    URL myURL = null;
    BufferedReader reader = null;
    //private final String token = "7cc50c030fa26aae37bc11a30cb58429";
    private final String token = "035bddc03f9d083eabd2793ae7653295";

    @Async
    public void sendSMS(String to, String code) throws IOException {
        try {
            String text = "Your application pass code: " + code + ".  We appreciate your kindness for helping people by choosing our services and sharing your car for others.";

            String encode_message = URLEncoder.encode(text, StandardCharsets.UTF_8.toString());
            String api = "https://sms.greenweb.com.bd/api.php?";

            api = api + "token=" + token +
                    "&to=" + to +
                    "&message=" + encode_message;

            myURL = new URL(api);
            urlConnection = myURL.openConnection();
            urlConnection.connect();
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String output;
            while ((output=reader.readLine()) != null){
                System.out.println("output "+ output);
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }finally {
            reader.close();
        }
    }
}
