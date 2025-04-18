package com.example.demo;
import com.example.Entities.Bus;
import com.example.Services.BusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.example.Entities.Bus;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BusController.class)
public class BusControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BusService busService;

    private Bus bus1;
    private Bus bus2;

    @BeforeEach
    public void setUp() {
        bus1 = new Bus();
        bus1.setId(1L);
        bus1.setBrand("LIAZ");
        bus1.setBortNum("12345")

        bus2 = new Bus();
        bus2.setId(2L);
        bus2.setNumber("KAMAZ");
        bus2.setBortNum("345678");
    }

    @Test
    public void getAllBuses_ShouldReturnAllBuses() throws Exception {
        List<Bus> buses = Arrays.asList(bus1, bus2);
        Mockito.when(busService.findAll()).thenReturn(buses);

        mockMvc.perform(MockMvcRequestBuilders.get("/buses")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].brand", is(bus1.getBrand())))
                .andExpect(jsonPath("$[1].brand", is(bus2.getBrand())));
    }

    @Test
    public void getBusesById_ShouldReturnBus() throws Exception {
        Mockito.when(busService.findByID(1L)).thenReturn(Arrays.asList(bus1));

        mockMvc.perform(MockMvcRequestBuilders.get("/buses/id?id=1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(bus1.getId().intValue())))
                .andExpect(jsonPath("$[0].brand", is(bus1.getBrand())));
    }

    @Test
    public void addBus_ShouldCreateNewBus() throws Exception {
        Mockito.when(busService.addBus(any(Bus.class))).thenReturn(bus1);

        String busJson = """
        {
            "brand": "LIAZ",
            "bort_num": 12345
        }
        """;

        mockMvc.perform(MockMvcRequestBuilders.post("/buses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(busJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bus1.getId().intValue())))
                .andExpect(jsonPath("$.brand", is(bus1.getBrand())));
    }

    @Test
    public void deleteBus_WhenBusExists_ShouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(busService).deleteBus(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/buses/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteBus_WhenBusNotExists_ShouldReturnNotFound() throws Exception {
        Mockito.doThrow(new RuntimeException("Bus not found")).when(busService).deleteBus(99L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/buses/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateBus_WhenBusExists_ShouldReturnUpdatedBus() throws Exception {
        Bus updatedBus = new Bus();
        updatedBus.setId(1L);
        updatedBus.setBrand("KAMAZ");
        updatedBus.setBortNum("78945");

        Mockito.when(busService.updateBus(null, 1L, updatedBus)).thenReturn(updatedBus);

        String updatedBusJson = """
        {
            "brand": "RAMAZ",
            "bort_num": 78945
        }
        """;

        mockMvc.perform(MockMvcRequestBuilders.put("/buses/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedBusJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand", is(updatedBus.getBrand())))
                .andExpect(jsonPath("$.bortnum", is(updatedBus.getBortNum())));
    }

    @Test
    public void updateBus_WhenBusNotExists_ShouldReturnNotFound() throws Exception {
        Mockito.when(busService.updateBus(any(), anyLong(), any(Bus.class)))
                .thenThrow(new RuntimeException("Bus not found"));

        String updatedBusJson = """
        {
            "brand": "MAZ",
            "bort_num": 71945
        }
        """;

        mockMvc.perform(MockMvcRequestBuilders.put("/buses/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedBusJson))
                .andExpect(status().isNotFound());
    }
}