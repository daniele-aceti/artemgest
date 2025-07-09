package artemgest.artemgest.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import artemgest.artemgest.model.Event;
import artemgest.artemgest.repository.EventRepository;

@Service
public class EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    /**
     * Restituisce notifiche per eventi che iniziano entro le prossime 24 ore,
     * @return appuntamento con data formattata
     */
    public List<String> getUpcomingEventNotifications() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime in24Hours = now.plusHours(24);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy 'alle ore' HH:mm", Locale.ITALIAN);

        List<String> notifiche = new ArrayList<>();

        List<Event> eventi = eventRepository.findAll();

        for (Event event : eventi) {
            LocalDateTime startTime = event.getDataInizio();

            if (startTime == null) {
                continue; // salto evento senza data
            }

            if (startTime.isBefore(now)) {
                continue; // salto evento già passato
            }

            if (startTime.isAfter(in24Hours)) {
                continue; // salto evento troppo distante
            }

            String titolo = event.getTitle();
            String dataFormattata = startTime.format(formatter);

            String notifica = "🔔 Memo: " + titolo + " il " + dataFormattata;

            notifiche.add(notifica);
        }

        return notifiche;
    }
}
