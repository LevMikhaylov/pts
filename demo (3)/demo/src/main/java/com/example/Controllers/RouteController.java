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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.Entities.Route;
import com.example.Services.RouteService;
@RestController
@RequestMapping("/routes")
@Validated
public class RouteController {
    @Autowired
    private RouteService rs;
    @GetMapping
    public List<Route> getAllRoutes() {
        return rs.findAll();
    }
    @PostMapping
    public Bus launchRoute(@RequestBody @Valid Route r){
        return rs.launcvhRoute(r);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelRoute(@PathVariable Long id) {
        try {
            rs.cancelRoute(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
