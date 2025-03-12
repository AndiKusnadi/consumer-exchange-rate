package bni.co.id.consumer.exchange_rate.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class GeneralFacility {

    private  final ObjectMapper m_objectMapper = new ObjectMapper();

    @Bean
    public ObjectMapper getObjectMapper() {
        m_objectMapper.registerModule(new JavaTimeModule());
        m_objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return m_objectMapper;
    }

    public String encrypt(String pValue, String pSecretKey) throws IllegalBlockSizeException,
            BadPaddingException, InvalidKeySpecException, IOException, GeneralSecurityException {
        DESKeySpec keySpec = new DESKeySpec(pSecretKey.getBytes("UTF8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(keySpec);
        // ENCODE plainTextPassword String
        byte[] cleartext = pValue.getBytes("UTF8");

        Cipher cipher = Cipher.getInstance("DES"); // cipher is not thread safe
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return Base64.getEncoder().encodeToString(cipher.doFinal(cleartext));
    }

    public String decrypt(String pValue, String pSecretKey)
            throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeySpecException,
            NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        byte[] encrypedPwdBytes = Base64.getDecoder().decode(pValue);

        DESKeySpec keySpec = new DESKeySpec(pSecretKey.getBytes("UTF8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(keySpec);

        Cipher cipher = Cipher.getInstance("DES");// cipher is not thread safe
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] plainTextPwdBytes = (cipher.doFinal(encrypedPwdBytes));
        return new String(plainTextPwdBytes);
    }

}
