package com.s2i.gevents.mapper;

import com.s2i.gevents.domain.Event;
import com.s2i.gevents.service.dto.EventDTO;
import org.mapstruct.Mapper;

import javax.swing.text.html.parser.Entity;

@Mapper(componentModel = "spring", uses = {})
public interface EventMapper extends EntityMapper<EventDTO, Event> {
}
