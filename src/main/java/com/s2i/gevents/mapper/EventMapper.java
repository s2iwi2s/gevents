package com.s2i.gevents.mapper;

import com.s2i.gevents.domain.Event;
import com.s2i.gevents.service.dto.EventDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventMapper extends EntityMapper<EventDTO, Event> {
}
