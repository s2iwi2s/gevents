package com.s2i.gevents.web;

import com.s2i.gevents.service.EventService;
import com.s2i.gevents.service.dto.EventDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@RestController()
@RequestMapping(path = "/api")
public class EventController {

    private final EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    @PostMapping("/events")
    public ResponseEntity<?> save(@RequestBody EventDTO dto) throws URISyntaxException {
        if (dto.getId() == null) {
            dto = service.save(dto);
            return ResponseUtil.created("/api/events", dto);
        }
        return ResponseUtil.badRequest(dto);
    }

    @GetMapping("/events")
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/repos/{first}/events")
    public ResponseEntity<?> findAllByFirst(@PathVariable(required = false, value = "first") Integer first) {
        return ResponseEntity.ok(service.findAll(first));
    }

    @GetMapping("/events/{eventId}")
    public ResponseEntity<?> save(@PathVariable("eventId") Long eventId) {
        return ResponseUtil.wrapOrNotFound(service.findById(eventId));
    }
}
