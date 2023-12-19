package ru.practicum.ewm.event.repository;

import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.StateEvent;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByInitiatorId(Long userId, Pageable page);

    @Query("select e " +
            "from Event e " +
            "where ((:users is null or e.initiator.id IN :users) " +
            "and (:states is null or e.state IN :states) " +
            "and (:categories is null or e.category.id IN :categories) " +
            "and (e.eventDate BETWEEN :rangeStart and :rangeEnd)) ")
    List<Event> getAllEventsByAdmin(
            @Param("users") List<Long> users,
            @Param("states") List<StateEvent> states,
            @Param("categories") List<Long> categories,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd,
            Pageable page);

    @Query("select e " +
            "from Event e " +
            "where (e.state = 'PUBLISHED') " +
            "and (LOWER(e.annotation) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "OR LOWER(e.description) LIKE LOWER(CONCAT('%', :text, '%'))) " +
            "and ((:categories) IS NULL OR e.category.id IN :categories) " +
            "and ((:paid) IS NULL OR e.paid = :paid) " +
            "and (e.eventDate BETWEEN :rangeStart AND :rangeEnd) " +
            "and ((:onlyAvailable IS TRUE AND (e.participantLimit > e.confirmedRequests OR e.participantLimit = 0)) " +
            "    OR :onlyAvailable IS FALSE) ")
    List<Event> getAllEvents(@Param("text") String text,
                             @Param("categories") List<Long> categories,
                             @Param("paid") Boolean paid,
                             @Param("rangeStart") LocalDateTime rangeStart,
                             @Param("rangeEnd") LocalDateTime rangeEnd,
                             @Param("onlyAvailable") Boolean onlyAvailable,
                             Pageable page);
}