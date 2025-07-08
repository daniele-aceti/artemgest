package artemgest.artemgest.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import artemgest.artemgest.model.Event;
import artemgest.artemgest.repository.EventRepository;

@Controller
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @GetMapping("/calendar")
    public String getCalendar(Model model) {
        model.addAttribute("eventList", eventRepository.findAll());
        return "calendar";
    }

    @PostMapping("/api/events")
    @ResponseBody
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        // Salva l'evento nel DB
        Event savedEvent = eventRepository.save(event);
        return ResponseEntity.ok(savedEvent);
    }

    @GetMapping("/api/events")
    @ResponseBody
    public List<Map<String, Object>> getEvent() {
        List<Event> eventi = eventRepository.findAll();
        List<Map<String, Object>> risposta = new ArrayList<>();

        for (Event e : eventi) {
            Map<String, Object> evento = new HashMap<>();
            evento.put("id", e.getId());
            evento.put("title", e.getTitle());
            evento.put("start", e.getDataInizio()); // FullCalendar vuole "start"
            evento.put("end", e.getDataFine());     // ...e "end"
            evento.put("allDay", false);
            risposta.add(evento);
        }

        return risposta;
    }
}
