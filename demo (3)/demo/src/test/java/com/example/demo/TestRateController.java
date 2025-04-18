package com.example.demo;

import com.example.Entities.Rate;
import com.example.Services.RateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.example.Entities.Rate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RateController.class)
public class TestRateController{

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RateService rateService;

    private Rate rate1;
    private Rate rate2;

    @BeforeEach
    public void setUp() {
        rate1 = new Rate();
        rate1.setId(1L);
        rate1.setPrice(100.0);

        rate2 = new Rate();
        rate2.setId(2L);
        rate2.setPrice(200.0);
    }

    @Test
    public void getAllRates_ShouldReturnAllRates() throws Exception {
        List<Rate> rates = Arrays.asList(rate1, rate2);
        Mockito.when(rateService.findAll()).thenReturn(rates);

        mockMvc.perform(get("/rates")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(rate1.getName())))
                .andExpect(jsonPath("$[1].name", is(rate2.getName())));
    }

    @Test
    public void addRate_ShouldCreateNewRate() throws Exception {
        Mockito.when(rateService.addRate(any(Rate.class))).thenReturn(rate1);

        String rateJson = """
        {
            "price": 100.0
        }
        """;

        mockMvc.perform(post("/rates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(rateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(rate1.getId().intValue())))
                .andExpect(jsonPath("$.name", is(rate1.getName())))
                .andExpect(jsonPath("$.price", is(rate1.getPrice())));
    }

    @Test
    public void deleteRate_WhenRateExists_ShouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(rateService).deleteRate(1L);

        mockMvc.perform(delete("/rates/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteRate_WhenRateNotExists_ShouldReturnNotFound() throws Exception {
        Mockito.doThrow(new RuntimeException("Rate not found")).when(rateService).deleteRate(99L);

        mockMvc.perform(delete("/rates/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateRate_WhenRateExists_ShouldReturnUpdatedRate() throws Exception {
        Rate updatedRate = new Rate();
        updatedRate.setId(1L);
        updatedRate.setName("Standard Updated");
        updatedRate.setPrice(150.0);

        Mockito.when(rateService.updateRate(null, 1L, updatedRate)).thenReturn(updatedRate);

        String updatedRateJson = """
        {
            "price": 150.0
        }
        """;

        mockMvc.perform(put("/rates/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedRateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(updatedRate.getName())))
                .andExpect(jsonPath("$.price", is(updatedRate.getPrice())));
    }

    @Test
    public void updateRate_WhenRateNotExists_ShouldReturnNotFound() throws Exception {
        Mockito.when(rateService.updateRate(any(), anyLong(), any(Rate.class)))
                .thenThrow(new RuntimeException("Rate not found"));

        String updatedRateJson = """
        {
            "price": 999.0
        }
        """;

        mockMvc.perform(put("/rates/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedRateJson))
                .andExpect(status().isNotFound());
    }

    @Test
    public void addRate_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        String invalidRateJson = """
        {
            "price": -100.0
        }
        """;

        mockMvc.perform(post("/rates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRateJson))
                .andExpect(status().isBadRequest());
    }
}