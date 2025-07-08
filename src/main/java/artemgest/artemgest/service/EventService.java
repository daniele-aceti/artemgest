package artemgest.artemgest.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import artemgest.artemgest.repository.EventRepository;

@Service
public class EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<String> getUpcomingEventNotifications() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime end = now.plusHours(24); // evento entro 24h da ora

        return eventRepository.findAll().stream()
                .filter(e -> {
                    LocalDateTime startTime = e.getDataInizio();
                    return startTime != null
                            && (startTime.isEqual(now) || (startTime.isAfter(now) && startTime.isBefore(end)));
                })
                .map(e -> "🔔 Memo: " + e.getTitle()
                + " il " + e.getDataInizio().toLocalDate()
                + " alle " + e.getDataInizio().toLocalTime())
                .collect(Collectors.toList());
    }

}
