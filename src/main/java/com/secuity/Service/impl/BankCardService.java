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

import com.secuity.Repo.BankCardEntityRepository;
import com.secuity.Repo.BankCardRepository;
import com.secuity.exception.DataValidationException;
import com.secuity.exception.GenericResponse;
import com.secuity.model.main.AadharCardDto;
import com.secuity.model.main.BankCard;
import com.secuity.model.main.BankCardEntity;

import javassist.NotFoundException;

@Service
public class BankCardService {

	@Autowired
	private BankCardRepository bankCardRepository;

	@Autowired
	BankCardEntityRepository bankCardEntityRepository;

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

	private String rsaEncrypt(BankCard bankCard) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, rsaKeyPair.getPublic());
		byte[] encryptedBytes = cipher.doFinal(bankCard.toString().getBytes());
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

	// Create
	public GenericResponse addBankCard(BankCard bankCard) {
		Optional<BankCard> existingBankCard = bankCardRepository.findByAccountNumber(bankCard.getAccountNumber());
		if (existingBankCard.isPresent()) {
			throw new DataValidationException("Bank card with the same account number already exists.");
		}

		try {
			// Convert AadharCardDTO to entity
			BankCardEntity bankCardEntity = new BankCardEntity();

			// Encrypt sensitive data with RSA
			String encryptedData = rsaEncrypt(bankCard);
			bankCardEntity.setEncryptedData(encryptedData);

			// Generate MAC for data integrity
			String mac = generateMAC(encryptedData);
			bankCardEntity.setMac(mac);

			// Save AadharCard entity with encrypted data and MAC
			bankCardEntityRepository.save(bankCardEntity);
		} catch (Exception e) {
			e.printStackTrace();
			// Handle exceptions appropriately
		}

		bankCardRepository.save(bankCard);
		return new GenericResponse(201, "Created Succesfully");
	}

	// Read
	public List<BankCard> getAllBankCards() {
		return bankCardRepository.findAll();
	}

	public Optional<BankCard> getBankCardById(Long id) {
		return bankCardRepository.findById(id);
	}

	// Update
	public GenericResponse updateBankCard(Long id, BankCard updatedBankCard) {

		if (bankCardRepository.existsById(id)) {
			updatedBankCard.setId(id);
			bankCardRepository.save(updatedBankCard);
			return new GenericResponse(202, "Updated Successfully");
		}
		return new GenericResponse(400, "Error found");
	}

	// Delete
	public GenericResponse deleteBankCard(Long id) {
		bankCardEntityRepository.deleteById(id);
		bankCardRepository.deleteById(id);
		return new GenericResponse(204, "Deleted Successfully");
	}

	public BankCardEntity getBankCardEntityById(Long id) throws NotFoundException {
        return bankCardEntityRepository.findById(id)
                                  .orElseThrow(() -> new NotFoundException("Bank card not found"));
    }
}