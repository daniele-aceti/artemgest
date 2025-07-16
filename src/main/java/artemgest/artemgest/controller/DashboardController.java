package artemgest.artemgest.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import artemgest.artemgest.service.DashboardService;
import artemgest.artemgest.service.EventService;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;

    private final EventService eventService;

    @Autowired
    public DashboardController(DashboardService dashboardService, EventService eventService) {
        this.dashboardService = dashboardService;
        this.eventService = eventService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        
        List<String> notifications = eventService.getUpcomingEventNotifications();
        model.addAttribute("notifications", notifications);

        Map<String, BigDecimal> statsPagato = dashboardService.filtraFatture("PAGATO");

        Map<String, BigDecimal> statsScadute = dashboardService.filtraFatture("SCADUTE");

        Map<String, BigDecimal> statsInAttesa = dashboardService.filtraFatture("IN_ATTESA");

        model.addAttribute("numeroFattureMese",
                statsPagato.getOrDefault("numeroFattureEmesse", BigDecimal.ZERO));

        model.addAttribute("fatturatoMese",
                statsPagato.getOrDefault("totale", BigDecimal.ZERO));

        model.addAttribute("numeroFattureScaduteMese",
                statsScadute.getOrDefault("numeroFattureScadute", BigDecimal.ZERO));

        model.addAttribute("numeroFattureInAttesaMese",
                statsInAttesa.getOrDefault("numeroFattureInAttesa", BigDecimal.ZERO));

        model.addAttribute("fatturatoMeseInAttesa",
                statsInAttesa.getOrDefault("fatturatoInAttesa", BigDecimal.ZERO));

        return "dashboard";
    }

}
