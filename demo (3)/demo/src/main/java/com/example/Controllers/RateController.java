package com.example.Controllers;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.Entities.Rate;
import com.example.Services.RateService;
@RestController
@RequestMapping("/rates")
@Validated
public class RateController {
    @Autowired
    private RateService rserv;
    @GetMapping
    public List<Rate> getAllRates() {
        return rserv.findAll();
    }
    @PostMapping
    public Rate addRate(@RequestBody @Valid Rate rate){
        return rserv.addRate(rate);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRate(@PathVariable Long id) {
        try {
            rserv.deleteRate(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<Rate> updateRate(@PathVariable long id, @RequestBody @Valid Rate updatedrate) {
        try {
            return ResponseEntity.ok(rserv.updateRate(null, id, updatedrate));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}