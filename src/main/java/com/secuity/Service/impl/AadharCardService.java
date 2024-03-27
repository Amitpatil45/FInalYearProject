package com.secuity.Service.impl;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.secuity.Repo.AadharCardEntityRepo;
import com.secuity.Repo.AadharCardRepository;
import com.secuity.model.main.AadharCardDto;
import com.secuity.model.main.AadharCardEntity;

import javassist.NotFoundException;

@Service
public class AadharCardService {

	@Autowired
	private AadharCardRepository aadharCardRepository;

	@Autowired
	private AadharCardEntityRepo aadharCardEntityRepo;

	// Simulated keys (replace with proper key management)
	private static KeyPair rsaKeyPair;
	private static SecretKey macSecretKey;

	static {
		try {
			// Generate RSA key pair
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize(2048);
			rsaKeyPair = keyGen.generateKeyPair();

			// Generate MAC secret key
			KeyGenerator keyGenMac = KeyGenerator.getInstance("HmacSHA256");
			macSecretKey = keyGenMac.generateKey();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String rsaEncrypt(AadharCardDto aadharCardDTO) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, rsaKeyPair.getPublic());
		byte[] encryptedBytes = cipher.doFinal(aadharCardDTO.toString().getBytes());
		return Base64.getEncoder().encodeToString(encryptedBytes);
	}

	private String generateMAC(String data) throws Exception {
		Mac hmac = Mac.getInstance("HmacSHA256");
		hmac.init(macSecretKey);
		byte[] macBytes = hmac.doFinal(data.getBytes());
		return Base64.getEncoder().encodeToString(macBytes);
	}

	@Transactional
	public void saveAadharCard(AadharCardDto aadharCardDTO) {
		try {
			// Convert AadharCardDTO to entity
			AadharCardEntity aadharCardEntity = new AadharCardEntity();

			// Encrypt sensitive data with RSA
			String encryptedData = rsaEncrypt(aadharCardDTO);
			aadharCardEntity.setEncryptedData(encryptedData);

			// Generate MAC for data integrity
			String mac = generateMAC(encryptedData);
			aadharCardEntity.setMac(mac);

			// Save AadharCard entity with encrypted data and MAC
			aadharCardRepository.save(aadharCardEntity);
		} catch (Exception e) {
			e.printStackTrace();
			// Handle exceptions appropriately
		}
	}


	
	//...............................................................
	private String rsaDecrypt(String encryptedData, PrivateKey privateKey) throws Exception {
	    try {
	        Cipher cipher = Cipher.getInstance("HmacSHA256");
	        cipher.init(Cipher.DECRYPT_MODE, privateKey);
	        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
	        return new String(decryptedBytes);
	    } catch (BadPaddingException e) {
	        throw new SecurityException("Decryption error: Invalid padding");
	    } catch (Exception e) {
	        throw new SecurityException("Decryption error: " + e.getMessage());
	    }
	}

    public String getDecryptedAadharCardData(String encryptedData) throws Exception {
        AadharCardEntity aadharCardEntity = aadharCardEntityRepo.findById(encryptedData).orElse(null);
        if (aadharCardEntity == null) {
            throw new NotFoundException("Aadhar card not found");
        }

        // Decrypt encrypted data
        String decryptedData = rsaDecrypt(aadharCardEntity.getEncryptedData(), rsaKeyPair.getPrivate());

        // Verify MAC for data integrity
        String mac = generateMAC(decryptedData);
        if (!mac.equals(aadharCardEntity.getMac())) {
            throw new SecurityException("Data integrity compromised");
        }

        return decryptedData;
    }

}
