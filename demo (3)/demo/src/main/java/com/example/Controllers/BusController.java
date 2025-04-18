package com.example.Controllers;

import com.example.Entities.Bus;
import com.example.Services.BusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;

@Controller
public class BusController {

    @Autowired
    private BusService bs;

    @GetMapping("/allBuses")
    public String getAllBuses(Model model) {
        List<Bus> buses = bs.findAll();
        model.addAttribute("buses", buses);
        return "bus-list"; 
    }

    @GetMapping("/allBuses/{id}")
    public String getBusesByID(@PathVariable Long id, Model model) {
        Bus bus = bs.findByID(id);
        if (bus != null) {
            model.addAttribute("bus-found-by-id", bus);
        } else {
            model.addAttribute("error", "Bus not found with ID: " + id);
        }
        return "buses-found-by-id";
    }

    @PostMapping("/post")
    public String addBus(@RequestBody @Valid Bus bus, BindingResult result) {
        if (result.hasErrors()) {
            return "add-bus"; // Вернуться на форму при ошибке
        }
        bs.addBus(bus);
        return "redirect:/allBuses";
    }

    @DeleteMapping("/busDelete/{id}")
    public String deleteBus(@PathVariable Long id) {
        try {
            bs.deleteBus(id);
            return "redirect:/allBuses";
        } catch (RuntimeException e) {
            // Логирование или обработка ошибки
            return "error"; // Показать страницу ошибки
        }
    }

    @PutMapping("/busUpdate/{id}")
    public String updateBus(@PathVariable long id, @RequestBody @Valid Bus updatedBus) {
        try {
            bs.updateBus(id, updatedBus);
            return "redirect:/allBuses";
        } catch (RuntimeException e) {
            // Логирование или обработка ошибки
            return "error"; // Показать страницу ошибки
        }
    }
}
