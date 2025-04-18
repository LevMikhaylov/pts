package com.example.Controllers;

import java.util.List;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.Entities.Rate;
import com.example.Services.RateService;

@Controller
@RequestMapping("/rates")
public class RateController {

    @Autowired
    private RateService rateService;

    @GetMapping("/allRates")
    public String getAllRates(Model model) {
        List<Rate> rates = rateService.findAll();
        model.addAttribute("rates", rates);
        return "allRates"; // Возвращаем имя HTML страницы for Thymeleaf
    }

    @GetMapping("/add")
    public String showAddRateForm(Model model) {
        model.addAttribute("rate", new Rate());
        return "addRate"; // Возвращаем форму добавления тарифа
    }

    @PostMapping("/add")
    public String addRate(@Valid @ModelAttribute Rate rate) {
        rateService.addRate(rate);
        return "redirect:/rates/allRates"; // Перенаправление на страницу со всеми тарифами
    }

    @GetMapping("/edit/{id}")
    public String showEditRateForm(@PathVariable Long id, Model model) {
        Rate rate = rateService.findById(id); 
        if (rate == null) {
            return "404"; // Возвращаем 404, если не найдено
        }
        model.addAttribute("rate", rate);
        return "editRate"; // Возвращаем форму редактирования тарифа
    }

    @PostMapping("/edit/{id}")
    public String updateRate(@PathVariable Long id, @Valid @ModelAttribute Rate updatedRate) {
        rateService.updateRate(id, updatedRate); // Обновляем тариф
        return "redirect:/rates/allRates"; // Перенаправление на страницу со всеми тарифами
    }

    @DeleteMapping("/delete/{id}")
    public String deleteRate(@PathVariable Long id) {
        rateService.deleteRate(id);
        return "redirect:/rates/allRates"; // Перенаправление на страницу со всеми тарифами
    }
    @GetMapping("/confirmDelete/{id}")
    public String showDeleteRateConfirmation(@PathVariable Long id, Model model) {
        Rate rate = rateService.findById(id);
        if (rate == null) {
            return "404"; // Возвращаем 404, если не найдено
        }
        model.addAttribute("rate", rate);
        return "deleteRate"; // Возвращаем страницу подтверждения удаления
    }
}