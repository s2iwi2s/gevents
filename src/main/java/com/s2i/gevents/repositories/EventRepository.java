package com.s2i.gevents.repositories;

import com.s2i.gevents.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByFirstEquals(Integer first);

}
