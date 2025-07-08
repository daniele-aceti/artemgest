package artemgest.artemgest.controller;

import java.util.List;

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
    public List<Event> getEvents() {
        return eventRepository.findAll();
    }
}
