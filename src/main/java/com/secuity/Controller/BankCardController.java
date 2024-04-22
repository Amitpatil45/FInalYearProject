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

import com.secuity.Service.impl.BankCardService;
import com.secuity.exception.GenericResponse;
import com.secuity.model.main.AadharCardEntity;
import com.secuity.model.main.BankCard;
import com.secuity.model.main.BankCardEntity;
import com.secuity.model.main.PanCardEntity;

import javassist.NotFoundException;

@RestController
@RequestMapping("/bankcard")
public class BankCardController {

    @Autowired
    private BankCardService bankCardService;

    // Create
    @PostMapping("/add")
    public ResponseEntity<GenericResponse> addBankCard(@RequestBody BankCard bankCard) {
    	GenericResponse response = bankCardService.addBankCard(bankCard);
    	return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Read
    @GetMapping("/all")
    public ResponseEntity<List<BankCard>> getAllBankCards() {
        List<BankCard> bankCards = bankCardService.getAllBankCards();
        return new ResponseEntity<>(bankCards, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BankCard> getBankCardById(@PathVariable("id") Long id) {
        Optional<BankCard> bankCard = bankCardService.getBankCardById(id);
        return bankCard.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
  
    // Update
    @PutMapping("/{id}")
    public ResponseEntity<GenericResponse> updateBankCard(@PathVariable("id") Long id,
                                                    @RequestBody BankCard updatedBankCard) {
    	GenericResponse response = bankCardService.updateBankCard(id, updatedBankCard);
    	return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse> deleteBankCard(@PathVariable("id") Long id) {
    	GenericResponse response = bankCardService.deleteBankCard(id);
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    
    @GetMapping("/entity/{id}")
    public ResponseEntity<BankCardEntity> getBankCardEntityById(@PathVariable Long id) throws NotFoundException {
        BankCardEntity bankCardEntity = bankCardService.getBankCardEntityById(id);
        return ResponseEntity.ok(bankCardEntity);
    }
}
