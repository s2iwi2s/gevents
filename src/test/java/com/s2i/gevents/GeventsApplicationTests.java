package com.s2i.gevents;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.s2i.gevents.domain.Event;
import com.s2i.gevents.repositories.EventRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc

public class GeventsApplicationTests {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    ObjectMapper om = new ObjectMapper();
    @Autowired
    EventRepository eventRepository;
    @Autowired
    MockMvc mockMvc;

    Map<String, Event> testData;

    @Before
    public void setup() {
        eventRepository.deleteAll();
        testData = getTestData();
    }

    @Test
    public void testEventCreationValidData() throws Exception {
        Event expectedRecord = testData.get("event_01_push");
        Event actualRecord = om.readValue(mockMvc.perform(post("/api/events")
                        .contentType("application/json")
                        .content(om.writeValueAsString(expectedRecord)))
                .andDo(print())
                .andExpect(jsonPath("$.id", greaterThan(0)))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), Event.class);

        assertTrue(new ReflectionEquals(expectedRecord, "id").matches(actualRecord));
        assertTrue(eventRepository.findById(actualRecord.getId()).isPresent());

        expectedRecord = testData.get("event_02_watch");
        actualRecord = om.readValue(mockMvc.perform(post("/api/events")
                        .contentType("application/json")
                        .content(om.writeValueAsString(expectedRecord)))
                .andDo(print())
                .andExpect(jsonPath("$.id", greaterThan(0)))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), Event.class);

        assertTrue(new ReflectionEquals(expectedRecord, "id").matches(actualRecord));
        assertTrue(eventRepository.findById(actualRecord.getId()).isPresent());
    }

    @Test
    public void testGetAllEvents() throws Exception {
        Map<String, Event> testData = getTestData().entrySet().stream().filter(kv -> !"event_02_watch".contains(kv.getKey())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        List<Event> expectedRecords = new ArrayList<>();
        for (Map.Entry<String, Event> kv : testData.entrySet()) {
            expectedRecords.add(om.readValue(mockMvc.perform(post("/api/events")
                            .contentType("application/json")
                            .content(om.writeValueAsString(kv.getValue())))
                    .andDo(print())
                    .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), Event.class));
        }
        expectedRecords.sort(Comparator.comparing(Event::getId));

        List<Event> actualRecords = om.readValue(mockMvc.perform(get("/api/events"))
                .andDo(print())
                .andExpect(jsonPath("$.*", isA(ArrayList.class)))
                .andExpect(jsonPath("$.*", hasSize(expectedRecords.size())))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });

        for (int i = 0; i < expectedRecords.size(); i++) {
            Assert.assertTrue(new ReflectionEquals(expectedRecords.get(i)).matches(actualRecords.get(i)));
        }
    }

    @Test
    public void testGetEventRecordWithId() throws Exception {
        Event expectedRecord = getTestData().get("event_01_push");

        expectedRecord = om.readValue(mockMvc.perform(post("/api/events")
                        .contentType("application/json")
                        .content(om.writeValueAsString(expectedRecord)))
                .andDo(print())
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), Event.class);

        Event actualRecord = om.readValue(mockMvc.perform(get("/api/events/" + expectedRecord.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(), Event.class);

        Assert.assertTrue(new ReflectionEquals(expectedRecord).matches(actualRecord));

        // non-existing record test
        mockMvc.perform(get("/api/events/" + Integer.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetEventByRepository() throws Exception {
        Map<String, Event> eventsWithRepoId1 = getTestData().entrySet().stream().filter(kv -> "event_01_push,event_01_release".contains(kv.getKey())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        Map<String, Event> eventsWithRepoId2 = getTestData().entrySet().stream().filter(kv -> "event_02_watch".contains(kv.getKey())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        //1
        List<Event> expectedEventsWithRepoId1 = new ArrayList<>();
        for (Event event : eventsWithRepoId1.values()) {
            expectedEventsWithRepoId1.add(om.readValue(mockMvc.perform(post("/api/events")
                            .contentType("application/json")
                            .content(om.writeValueAsString(event)))
                    .andDo(print())
                    .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), Event.class));
        }
        expectedEventsWithRepoId1.sort(Comparator.comparing(Event::getId));

        //2
        List<Event> expectedEventsWithRepoId2 = new ArrayList<>();
        for (Event event : eventsWithRepoId2.values()) {
            expectedEventsWithRepoId2.add(om.readValue(mockMvc.perform(post("/api/events")
                            .contentType("application/json")
                            .content(om.writeValueAsString(event)))
                    .andDo(print())
                    .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), Event.class));
        }
        expectedEventsWithRepoId2.sort(Comparator.comparing(Event::getId));

        //get
        List<Event> actualEventsWithRepoId1 = om.readValue(mockMvc.perform(get("/api/repos/1/events"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(), new TypeReference<>() {
        });

        List<Event> actualEventsWithRepoId2 = om.readValue(mockMvc.perform(get("/api/repos/2/events"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(), new TypeReference<>() {
        });

        log.info("expectedEventsWithRepoId1 ==> {}", expectedEventsWithRepoId1);
        log.info("actualEventsWithRepoId1 ==> {}", actualEventsWithRepoId1);

        Assert.assertTrue(new ReflectionEquals(expectedEventsWithRepoId1).matches(actualEventsWithRepoId1));
        Assert.assertTrue(new ReflectionEquals(expectedEventsWithRepoId2).matches(actualEventsWithRepoId2));

        //non-existing record test
        mockMvc.perform(get("/api/repos/" + Integer.MAX_VALUE + "/events")
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    private Map<String, Event> getTestData() {
        Map<String, Event> data = new HashMap<>();

        Event event_01_push = new Event(
                "PushEvent",
                true,
                1,
                1);
        data.put("event_01_push", event_01_push);

        Event event_01_release = new Event(
                "ReleaseEvent",
                true,
                1,
                1);
        data.put("event_01_release", event_01_release);

        Event event_01_watch = new Event(
                "WatchEvent",
                true,
                1,
                1);
        data.put("event_01_watch", event_01_watch);

        Event event_02_watch = new Event(
                "PushEvent",
                true,
                2,
                1);
        data.put("event_02_watch", event_02_watch);


        return data;
    }
}
