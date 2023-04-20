package com.s2i.gevents.service;

import com.s2i.gevents.domain.Event;
import com.s2i.gevents.mapper.EventMapper;
import com.s2i.gevents.repositories.EventRepository;
import com.s2i.gevents.service.dto.EventDTO;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private EventRepository repository;
    private EventMapper eventMapper;

    public EventService(EventRepository repository, EventMapper eventMapper) {
        this.repository = repository;
        this.eventMapper = eventMapper;
    }

    public EventDTO save(EventDTO dto) {
        Event event = eventMapper.toEntity(dto);
        return eventMapper.toDto(repository.save(event));
    }

    public List<EventDTO> findAll() {
        return this.eventMapper.toDto(this.repository.findAll());
    }

    public List<EventDTO> findAll(Integer first) {
        return this.eventMapper.toDto(repository.findAllByFirstEquals(first));
    }

    public Optional<EventDTO> findById(Long id) {
        return repository.findById(id).map(eventMapper::toDto);
    }

}
