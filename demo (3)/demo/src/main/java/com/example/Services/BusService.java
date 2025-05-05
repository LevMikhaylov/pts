package com.example.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entities.Bus;
import com.example.Repositories.BusRepository;
import javax.validation.constraints.NotNull;
import jakarta.validation.Valid;

@Service
public class BusService {
    @Autowired
    private BusRepository bs;
    public Bus addBus(@Valid @NotNull Bus bus){
        return bs.save(bus);
    }
    public void updateBus(@NotNull String bort_num, long id, @NotNull String brand, @Valid @NotNull Bus updatedbus){
        if (bs.existsById(id)) {
            updatedbus.setBrand(brand);
            updatedbus.setBortNum(bort_num);
            bs.save(updatedbus);
        }else{
            throw new RuntimeException("Bus not found");
        }     
    }
    public List<Bus> findAll(){
        return bs.findAll();
    }
    public Bus findByID(long id){
        return bs.findById(id).orElse(null);
    }
    public void deleteBus(long id){
        if(bs.existsById(id)){
            bs.deleteById(id);
        }
        else{
            throw new RuntimeException("Bus not found");
        }
    }
}
