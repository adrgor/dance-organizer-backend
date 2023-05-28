package org.example.event.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.event.model.Event;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventFilter {

    public static List<Event> filter(List<Event> events, Map<String, String> filter) {
        int eventsPerPage = parseInt(filter.get("events_per_page"), 10);
        int pageNumber = parseInt(filter.get("page_number"), 1);
        String eventName = filter.getOrDefault("event_name", "");
        String countries = filter.getOrDefault("countries", "");
        String cities = filter.getOrDefault("city", "");
        String eventTypes = filter.getOrDefault("event_types", "");
        String danceStyles = filter.getOrDefault("dance_styles", "");

        int firstEventIndex = (pageNumber - 1) * eventsPerPage;

        Stream<Event> eventsStream = events.stream()
                .filter(event -> event.getName().contains(eventName) || eventName.contains(event.getName()))
                .filter(event -> countries.toUpperCase().contains(event.getCountry().toUpperCase()) ||
                        event.getCountry().toUpperCase().contains(countries.toUpperCase()))
                .filter(event -> cities.toUpperCase().contains(event.getCity().toUpperCase()) ||
                        event.getCity().toUpperCase().contains(cities.toUpperCase()))
                .filter(event -> eventTypes.toUpperCase().contains(event.getEventType().toUpperCase()) ||
                        event.getEventType().toUpperCase().contains(eventTypes.toUpperCase()))
                .filter(event -> containsAny(danceStyles, event.getDanceStyles()) ||
                        String.join("", event.getDanceStyles()).toUpperCase().contains(danceStyles.toUpperCase()));

        try {
            Date fromDate = DateFormat.getDateInstance().parse(filter.get("from_date"));
            Date toDate = DateFormat.getDateInstance().parse(filter.get("to_date"));

            eventsStream = eventsStream
                    .filter(event -> event.getEndingDate().after(fromDate))
                    .filter(event -> event.getStartingDate().before(toDate));
        } catch (ParseException ignored) {

        }

        return eventsStream
                .skip(firstEventIndex).limit(eventsPerPage)
                .toList();
    }

    private static boolean containsAny(String str, List<String> substrings) {
        return substrings.stream().map(String::toUpperCase)
                .anyMatch(str.toUpperCase()::contains);
    }

    private static int parseInt(String val, int defaultVal) {
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }
}