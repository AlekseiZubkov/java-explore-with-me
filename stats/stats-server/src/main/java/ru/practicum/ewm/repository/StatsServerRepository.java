package ru.practicum.ewm.repository;

import ru.practicum.ewm.dto.ViewStats;
import ru.practicum.ewm.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StatsServerRepository extends JpaRepository<Hit, Long> {

    @Query("select new ru.practicum.ewm.dto.ViewStats(h.app, h.uri, COUNT(h.ip) as count_ip) " +
            "from Hit as h " +
            "where h.timestamp >= ?1  " +
            "and h.timestamp <= ?2 " +
            "and h.uri in ?3 " +
            "group by h.app, h.uri " +
            "order by count_ip desc ")
    List<ViewStats> getAllStatsWithUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.ewm.dto.ViewStats(h.app, h.uri, COUNT(DISTINCT(h.ip)) as count_ip) " +
            "from Hit as h " +
            "where h.timestamp >= ?1  " +
            "and h.timestamp <= ?2 " +
            "and h.uri in ?3 " +
            "group by h.app, h.uri " +
            "order by count_ip desc ")
    List<ViewStats> getAllUniqueStatsWithUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.ewm.dto.ViewStats(h.app, h.uri, COUNT(h.ip) as count_ip) " +
            "from Hit as h " +
            "where h.timestamp >= ?1  " +
            "and h.timestamp <= ?2 " +
            "group by h.app, h.uri " +
            "order by count_ip desc ")
    List<ViewStats> getAllStats(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.ewm.dto.ViewStats(h.app, h.uri, COUNT(DISTINCT(h.ip)) as count_ip) " +
            "from Hit as h " +
            "where h.timestamp >= ?1  " +
            "and h.timestamp <= ?2 " +
            "group by h.app, h.uri " +
            "order by count_ip desc ")
    List<ViewStats> getAllUniqueStats(LocalDateTime start, LocalDateTime end);

}