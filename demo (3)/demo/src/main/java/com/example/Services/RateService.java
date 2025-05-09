package com.example.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entities.Rate;
import com.example.Repositories.RateRepository;
import javax.validation.constraints.NotNull;
import jakarta.validation.Valid;

@Service
public class RateService {
    @Autowired
    private RateRepository rr;
    public Route addRate(@Valid @NotNull Rate rate){
        return rr.save(rate);
    }
    public void deleteRate(long id){
        if(rr.existsById(id)){
            rr.deleteById(id);
        }else{
            throw new RuntimeException("Rate is not found");
        }
    }
    public List<Rate> findAll(){
        return rr.findAll();
    }
    public Rate findByID(long id){
        return rr.findById(id).orElse(null);
    }
    public void updateRate(long id,@NotNull double price,@Valid @NotNull Rate updatedRate){
        if (rr.existsById(id)) {
            updatedRate.setPrice(price)
            rr.save(updatedbus);
        }else{
            throw new RuntimeException("Bus not found");
        } 
    }
}
