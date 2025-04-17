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
import com.example.Entities.Bus;
import com.example.Services.BusService;
@RestController
@RequestMapping("/buses")
@Validated
public class BusController {
    @Autowired
    private BusService bs;
    @GetMapping
    public List<Bus> getAllBuses() {
        return bs.findAll();
    }
    @GetMapping("/id")
    public List<Bus> getBusesByID(long id) {
        return bs.findByID(id);
    }
    @PostMapping
    public Bus addBus(@RequestBody @Valid Bus bus){
        return bs.addBus(bus);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBus(@PathVariable Long id) {
        try {
            bs.deleteBus(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateBus(@PathVariable long id, @RequestBody @Valid Bus updatedBus) {
        try {
            return ResponseEntity.ok(bs.updateBus(null, id, updatedBus));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
