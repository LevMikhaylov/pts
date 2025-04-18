package com.example.demo;

import com.example.Entities.Route;
import com.example.Services.RouteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import com.example.Entities.Route;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RouteController.class)
public class RouteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RouteService routeService;

    private Route route1;
    private Route route2;

    @BeforeEach
    public void setUp() {
        route1 = new Route();
        route1.setId(1L);
        route1.setName("Route A");
        route1.setStartPoint("Point A");
        route1.setEndPoint("Point B");

        route2 = new Route();
        route2.setId(2L);
        route2.setName("Route B");
        route2.setStartPoint("Point C");
        route2.setEndPoint("Point D");
    }

    @Test
    public void getAllRoutes_ShouldReturnAllRoutes() throws Exception {
        List<Route> routes = Arrays.asList(route1, route2);
        Mockito.when(routeService.findAll()).thenReturn(routes);

        mockMvc.perform(get("/routes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(route1.getName())))
                .andExpect(jsonPath("$[1].name", is(route2.getName())));
    }

    @Test
    public void launchRoute_ShouldCreateNewRoute() throws Exception {
        Mockito.when(routeService.launchRoute(any(Route.class))).thenReturn(route1);

        String routeJson = """
        {
            "name": "Route A",
            "startPoint": "Point A",
            "endPoint": "Point B"
        }
        """;

        mockMvc.perform(post("/routes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(routeJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(route1.getId().intValue())))
                .andExpect(jsonPath("$.name", is(route1.getName())))
                .andExpect(jsonPath("$.startPoint", is(route1.getStartPoint())));
    }

    @Test
    public void cancelRoute_WhenRouteExists_ShouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(routeService).cancelRoute(1L);

        mockMvc.perform(delete("/routes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void cancelRoute_WhenRouteNotExists_ShouldReturnNotFound() throws Exception {
        Mockito.doThrow(new RuntimeException("Route not found")).when(routeService).cancelRoute(99L);

        mockMvc.perform(delete("/routes/99"))
                .andExpect(status().isNotFound());
    }

    // Тест на валидацию входных данных
    @Test
    public void launchRoute_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        String invalidRouteJson = """
        {
            "name": "",
            "startPoint": "",
            "endPoint": ""
        }
        """;

        mockMvc.perform(post("/routes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRouteJson))
                .andExpect(status().isBadRequest());
    }
}