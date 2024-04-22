package com.secuity.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.secuity.Service.impl.AadharCardService;
import com.secuity.exception.GenericResponse;
import com.secuity.model.main.AadharCard;
import com.secuity.model.main.AadharCardEntity;

import javassist.NotFoundException;

@RestController
@RequestMapping("/aadhar")
public class AadharCardController {

	@Autowired
	private AadharCardService aadharCardService;

	@PostMapping("/add")
	public ResponseEntity<GenericResponse> addAadharCard(@RequestBody AadharCard aadharCard) {
		GenericResponse response = aadharCardService.addAadharCard(aadharCard);
		return new ResponseEntity<>(response, HttpStatus.CREATED);

	}

	// Read
	@GetMapping("/all")
	public ResponseEntity<List<AadharCard>> getAllAadharCards() {
		List<AadharCard> aadharCards = aadharCardService.getAllAadharCards();
		return new ResponseEntity<>(aadharCards, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<AadharCard> getAadharCardById(@PathVariable("id") Long id) {
		Optional<AadharCard> aadharCard = aadharCardService.getAadharCardById(id);
		return aadharCard.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	// Update
	@PutMapping("/{id}")
	public ResponseEntity<GenericResponse> updateAadharCard(@PathVariable("id") Long id,
			@RequestBody AadharCard updatedAadharCard) {
		GenericResponse response = aadharCardService.updateAadharCard(id, updatedAadharCard);
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

	// Delete
	@DeleteMapping("/{id}")
	public ResponseEntity<GenericResponse> deleteAadharCard(@PathVariable("id") Long id) {
		GenericResponse response = aadharCardService.deleteAadharCard(id);
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@GetMapping("/entity/{id}")
	public ResponseEntity<AadharCardEntity> getAadharCardEntityById(@PathVariable Long id) throws NotFoundException {
		AadharCardEntity aadharCardEntity = aadharCardService.getAadharCardEntityById(id);
		return ResponseEntity.ok(aadharCardEntity);
	}
}
