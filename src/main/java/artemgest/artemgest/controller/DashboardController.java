package artemgest.artemgest.controller;

import java.util.List;

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

    /* 
     * nei metodi fattureDelMese e fattureInAttesa viene usato un booleano
     * true per l'importo false per il numero
     */

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<String> notifications = eventService.getUpcomingEventNotifications();
        model.addAttribute("notifications", notifications);
        model.addAttribute("fatturatoMese", dashboardService.fattureDelMese(true));
        model.addAttribute("numeroFattureMese", dashboardService.fattureDelMese(false));
        model.addAttribute("numeroFattureScaduteMese", dashboardService.fattureScadute());
        model.addAttribute("numeroFattureInAttesaMese", dashboardService.fattureInAttesa(false));
        model.addAttribute("fatturatoMeseInAttesa", dashboardService.fattureInAttesa(true));
        return "dashboard";
    }

}
