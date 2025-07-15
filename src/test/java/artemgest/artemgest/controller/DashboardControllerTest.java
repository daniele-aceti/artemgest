package artemgest.artemgest.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import artemgest.artemgest.service.DashboardService;
import artemgest.artemgest.service.EventService;

@WebMvcTest(DashboardController.class)
public class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    DashboardService dashboardService;

    @MockitoBean
    EventService eventService;

    @Test
    void dashboardTest() throws Exception {
        Boolean dashboardMock = false;
        Double fatturatoMese = 1.0;

        Mockito.when(dashboardService.fattureDelMese(dashboardMock)).thenReturn(fatturatoMese);
        Mockito.when(dashboardService.fattureDelMese(dashboardMock)).thenReturn(fatturatoMese);
        Mockito.when(dashboardService.fattureDelMese(dashboardMock)).thenReturn(fatturatoMese);
        Mockito.when(dashboardService.fattureDelMese(dashboardMock)).thenReturn(fatturatoMese);
        Mockito.when(dashboardService.fattureDelMese(dashboardMock)).thenReturn(fatturatoMese);

        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andExpect(model().attributeExists("notifications"))
                .andExpect(model().attributeExists("fatturatoMese"))
                .andExpect(model().attributeExists("numeroFattureMese"))
                .andExpect(model().attributeExists("numeroFattureScaduteMese"))
                .andExpect(model().attributeExists("numeroFattureInAttesaMese"))
                .andExpect(model().attributeExists("fatturatoMeseInAttesa"));
    }

}
