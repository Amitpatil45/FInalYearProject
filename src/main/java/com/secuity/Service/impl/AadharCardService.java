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

import com.secuity.Repo.AadharCardEntityRepo;
import com.secuity.Repo.AadharCardRepository;
import com.secuity.exception.DataValidationException;
import com.secuity.exception.GenericResponse;
import com.secuity.model.main.AadharCard;
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

	private String rsaEncrypt(AadharCard aadharCard) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, rsaKeyPair.getPublic());
		byte[] encryptedBytes = cipher.doFinal(aadharCard.toString().getBytes());
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

	// Create
	public GenericResponse addAadharCard(AadharCard aadharCard) {
		Optional<AadharCard> existingAadharCard = aadharCardRepository.findByCardNumber(aadharCard.getCardNumber());
		if (existingAadharCard.isPresent()) {
			throw new DataValidationException("Aadhar card with the same number already exists.");
		}

		try {
			// Convert AadharCardDTO to entity
			AadharCardEntity aadharCardEntity = new AadharCardEntity();

			// Encrypt sensitive data with RSA
			String encryptedData = rsaEncrypt(aadharCard);
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
		aadharCardRepository.save(aadharCard);
		return new GenericResponse(201, "Created Succesfully");
	}

	// Read
	public List<AadharCard> getAllAadharCards() {
		return aadharCardRepository.findAll();
	}

	public Optional<AadharCard> getAadharCardById(Long id) {
		return aadharCardRepository.findById(id);
	}

	// Update
	public GenericResponse updateAadharCard(Long id, AadharCard updatedAadharCard) {
		Optional<AadharCard> existingAadharCard = aadharCardRepository
				.findByCardNumber(updatedAadharCard.getCardNumber());

		if (aadharCardRepository.existsById(id)) {
			updatedAadharCard.setId(id);
			aadharCardRepository.save(updatedAadharCard);
			return new GenericResponse(202, "Updated Successfully");
		}
		return new GenericResponse(400, "Error found");
	}

	// Delete
	public GenericResponse deleteAadharCard(Long id) {
		Optional<AadharCard> aadharOptional = aadharCardRepository.findById(id);
		if (aadharOptional.isEmpty()) {
			throw new DataValidationException("Adhar not found");
		}
		aadharCardEntityRepo.deleteById(id);
		aadharCardRepository.deleteById(id);
		return new GenericResponse(204, "Deleted Successfully");
	}

	public AadharCardEntity getAadharCardEntityById(Long id) throws NotFoundException {
		return aadharCardEntityRepo.findById(id).orElseThrow(() -> new NotFoundException("Aadhar card not found"));
	}

}
