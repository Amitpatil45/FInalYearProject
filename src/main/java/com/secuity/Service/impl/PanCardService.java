package com.secuity.Service.impl;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.secuity.Repo.PanCardEntityRepository;
import com.secuity.Repo.PanCardRepository;
import com.secuity.exception.DataValidationException;
import com.secuity.exception.GenericResponse;
import com.secuity.model.main.AadharCardDto;
import com.secuity.model.main.PanCard;
import com.secuity.model.main.PanCardEntity;

import javassist.NotFoundException;

@Service
public class PanCardService {

	@Autowired
	private PanCardRepository panCardRepository;

	@Autowired
	private PanCardEntityRepository panCardEntityRepository;
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

	private String rsaEncrypt(PanCard panCard) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, rsaKeyPair.getPublic());
		byte[] encryptedBytes = cipher.doFinal(panCard.toString().getBytes());
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

	}

	// ...............................................................
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
		PanCardEntity panCardEntity = panCardEntityRepository.findById(encryptedData).orElse(null);
		if (panCardEntity == null) {
			throw new NotFoundException("Aadhar card not found");
		}

		// Decrypt encrypted data
		String decryptedData = rsaDecrypt(panCardEntity.getEncryptedData(), rsaKeyPair.getPrivate());

		// Verify MAC for data integrity
		String mac = generateMAC(decryptedData);
		if (!mac.equals(panCardEntity.getMac())) {
			throw new SecurityException("Data integrity compromised");
		}

		return decryptedData;
	}

	// Create
	public GenericResponse addPanCard(PanCard panCard) {
		Optional<PanCard> existingPanCard = panCardRepository.findByPanNumber(panCard.getPanNumber());
		if (existingPanCard.isPresent()) {
			throw new DataValidationException("PAN card with the same number already exists.");
		}

		try {
			// Convert AadharCardDTO to entity
			PanCardEntity panCardEntity = new PanCardEntity();

			// Encrypt sensitive data with RSA
			String encryptedData = rsaEncrypt(panCard);
			panCardEntity.setEncryptedData(encryptedData);

			// Generate MAC for data integrity
			String mac = generateMAC(encryptedData);
			panCardEntity.setMac(mac);

			// Save AadharCard entity with encrypted data and MAC
			panCardEntityRepository.save(panCardEntity);
		} catch (Exception e) {
			e.printStackTrace();
			// Handle exceptions appropriately
		}

		panCardRepository.save(panCard);
		return new GenericResponse(201, "Created Succesfully");
	}

	// Read
	public List<PanCard> getAllPanCards() {
		return panCardRepository.findAll();
	}

	public Optional<PanCard> getPanCardById(Long id) {
		return panCardRepository.findById(id);
	}

	// Update
	public GenericResponse updatePanCard(Long id, PanCard updatedPanCard) {

		if (panCardRepository.existsById(id)) {
			updatedPanCard.setId(id);
			panCardRepository.save(updatedPanCard);

			return new GenericResponse(202, "Updated Successfully");
		}
		return new GenericResponse(400, "Error found");
	}

	// Delete
	public GenericResponse deletePanCard(Long id) {
		panCardEntityRepository.deleteById(id);
		panCardRepository.deleteById(id);
		return new GenericResponse(204, "Deleted Successfully");

	}

	public PanCardEntity getPanCardEntityById(Long id) {
		// TODO Auto-generated method stub
		return panCardEntityRepository.getById(id);
	}
}