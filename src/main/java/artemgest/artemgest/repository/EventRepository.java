package artemgest.artemgest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import artemgest.artemgest.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {

}
