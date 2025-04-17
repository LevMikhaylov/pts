package com.example.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entities.Route;
import com.example.Repositories.RouteRepository;

import jakarta.validation.Valid;

@Service
public class RouteService {
    @Autowired
    private RouteRepository rrp;

    public Route launchRoute(@Valid Route route){
        return rrp.save(route);
    }
    public void cancelRoute(long id){
        if(rrp.existsById(id)){
            rrp.deleteById(id);
        }else{
            throw new RuntimeException("Route is not found");
        }
    }
    public List<Route> findAll(){
        return rrp.findAll();
    }
}
