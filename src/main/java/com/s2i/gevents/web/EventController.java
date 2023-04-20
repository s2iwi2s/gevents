package com.s2i.gevents.web;

import com.s2i.gevents.domain.Event;
import com.s2i.gevents.repositories.EventRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping(path = "/api")
public class EventController {

    private EventRepository repository;
    public EventController(EventRepository repository){
        this.repository = repository;
    }

    @PostMapping("/events")
    public ResponseEntity<?> save(@RequestBody Event event) throws URISyntaxException {
        if(event.getId() == null) {
            event = repository.save(event);
            return ResponseUtil.created("/api/events", event);
        }
        return ResponseUtil.badRequest(event);
    }

    @GetMapping("/events")
    public ResponseEntity<?> getAll(){
        List<Event> events = repository.findAll();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/repos/{first}/events")
    public ResponseEntity<?> getAllById(@PathVariable(required = false, value = "first") Integer first){
        List<Event> events = repository.findAllByFirstEquals(first);
        return ResponseEntity.ok(events);
    }
    @GetMapping("/events/{eventId}")
    public ResponseEntity<?> save(@PathVariable("eventId") Long eventId){
        Optional<Event> optionalEvent = repository.findById(eventId);
        return ResponseUtil.wrapOrNotFound(optionalEvent);
    }
}
