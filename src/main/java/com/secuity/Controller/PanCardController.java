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

import com.secuity.Service.impl.PanCardService;
import com.secuity.exception.GenericResponse;
import com.secuity.model.main.PanCard;
import com.secuity.model.main.PanCardEntity;

@RestController
@RequestMapping("/pancard")
public class PanCardController {

	@Autowired
	private PanCardService panCardService;

	// Create
	@PostMapping("/add")
	public ResponseEntity<GenericResponse> addPanCard(@RequestBody PanCard panCard) {
		GenericResponse response = panCardService.addPanCard(panCard);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	// Read
	@GetMapping("/all")
	public ResponseEntity<List<PanCard>> getAllPanCards() {
		List<PanCard> panCards = panCardService.getAllPanCards();
		return new ResponseEntity<>(panCards, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PanCard> getPanCardById(@PathVariable("id") Long id) {
		Optional<PanCard> panCard = panCardService.getPanCardById(id);
		return panCard.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	// Update
	@PutMapping("/{id}")
	public ResponseEntity<GenericResponse> updatePanCard(@PathVariable("id") Long id,
			@RequestBody PanCard updatedPanCard) {
		GenericResponse response = panCardService.updatePanCard(id, updatedPanCard);

		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

	// Delete
	@DeleteMapping("/{id}")
	public ResponseEntity<GenericResponse> deletePanCard(@PathVariable("id") Long id) {
		GenericResponse response = panCardService.deletePanCard(id);
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("/entiry/{id}")
    public ResponseEntity<PanCardEntity> getPanCardEntityById(@PathVariable Long id) {
        PanCardEntity panCardEntity = panCardService.getPanCardEntityById(id);
        return ResponseEntity.ok(panCardEntity);
    }
}