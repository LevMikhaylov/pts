package com.example.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.Entities.Route;
import com.example.Services.RouteService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/routes")
@Validated
public class RouteController {
    @Autowired
    private RouteService rs;

    @GetMapping("/all")
    public String getAllRoutes(Model model) {
        List<Route> routes = rs.findAll();
        model.addAttribute("routes", routes);
        return "allRoutes"; // Название HTML страницы для отображения всех маршрутов
    }

    @GetMapping("/add")
    public String showAddRouteForm(Model model) {
        model.addAttribute("route", new Route());
        return "addRoute"; // Название HTML страницы для добавления маршрута
    }

    @PostMapping("/add")
    public String addRoute(@Valid Route route, BindingResult result) {
        if (result.hasErrors()) {
            return "addRoute"; // В случае ошибок возвращаем на ту же страницу
        }
        rs.launchRoute(route);
        return "redirect:/routes/all"; // Перенаправляем на страницу со всеми маршрутами
    }

    @DeleteMapping("/{id}")
    public String cancelRoute(@PathVariable Long id) {
        try {
            rs.cancelRoute(id);
            return "redirect:/routes/all"; // После удаления перенаправляем на страницу со всеми маршрутами
        } catch (RuntimeException e) {
            return "error"; // Отобразить страницу ошибки
        }
    }
}