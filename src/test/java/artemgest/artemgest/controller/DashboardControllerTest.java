package artemgest.artemgest.controller;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
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
    private DashboardService dashboardService;

    @MockitoBean
    private EventService eventService;

    private Map<String, BigDecimal> pagatoStats;
    private Map<String, BigDecimal> scaduteStats;
    private Map<String, BigDecimal> inAttesaStats;

    @BeforeEach
    public void setUp() {
        pagatoStats = new HashMap<>();
        pagatoStats.put("numeroFattureEmesse", BigDecimal.valueOf(5));
        pagatoStats.put("totale", BigDecimal.valueOf(1000));

        scaduteStats = new HashMap<>();
        scaduteStats.put("numeroFattureScadute", BigDecimal.valueOf(2));

        inAttesaStats = new HashMap<>();
        inAttesaStats.put("numeroFattureInAttesa", BigDecimal.valueOf(3));
        inAttesaStats.put("fatturatoInAttesa", BigDecimal.valueOf(600));
    }

    @Test
    public void testDashboard() throws Exception {
        Mockito.when(eventService.getUpcomingEventNotifications()).thenReturn(Collections.emptyList());
        Mockito.when(dashboardService.filtraFatture("PAGATO")).thenReturn(pagatoStats);
        Mockito.when(dashboardService.filtraFatture("SCADUTE")).thenReturn(scaduteStats);
        Mockito.when(dashboardService.filtraFatture("IN_ATTESA")).thenReturn(inAttesaStats);

        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andExpect(model().attribute("numeroFattureMese", BigDecimal.valueOf(5)))
                .andExpect(model().attribute("fatturatoMese", BigDecimal.valueOf(1000)))
                .andExpect(model().attribute("numeroFattureScaduteMese", BigDecimal.valueOf(2)))
                .andExpect(model().attribute("numeroFattureInAttesaMese", BigDecimal.valueOf(3)))
                .andExpect(model().attribute("fatturatoMeseInAttesa", BigDecimal.valueOf(600)));
    }
}
