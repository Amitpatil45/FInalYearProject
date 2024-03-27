package com.secuity.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.secuity.Service.impl.AadharCardService;
import com.secuity.model.main.AadharCardDto;

import javassist.NotFoundException;

@RestController
@RequestMapping("/aadhar")
public class AadharCardController {

	@Autowired
	private AadharCardService aadharCardService;

	@PostMapping("/save")
	public ResponseEntity<String> saveAadharCard(@RequestBody AadharCardDto aadharCardDTO) {
		try {
			aadharCardService.saveAadharCard(aadharCardDTO);
			return new ResponseEntity<>("AadharCard saved successfully", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error saving AadharCard", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/all/{id}")

	public ResponseEntity<String> decryptAadharCard(@RequestBody String encryptedData) {
		try {
			String decryptedAadharCard = aadharCardService.getDecryptedAadharCardData(encryptedData);
			return ResponseEntity.ok(decryptedAadharCard);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
}