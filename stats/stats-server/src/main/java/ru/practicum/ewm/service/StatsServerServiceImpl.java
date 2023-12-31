package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.EndpointHit;
import ru.practicum.ewm.dto.ViewStats;
import ru.practicum.ewm.exception.BadRequestException;
import ru.practicum.ewm.exception.HitNotSaveException;
import ru.practicum.ewm.mapper.StatsServerMapper;
import ru.practicum.ewm.model.Hit;
import ru.practicum.ewm.repository.StatsServerRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServerServiceImpl implements StatsServerService {
    private final StatsServerRepository statsServerRepository;

    @Override
    public EndpointHit saveEndpointHit(EndpointHit endpointHit) {
        Hit hit = StatsServerMapper.INSTANCE.toHit(endpointHit);
        try {
            hit = statsServerRepository.save(hit);
        } catch (RuntimeException e) {
            throw new HitNotSaveException("Информация не сохранена: " + endpointHit);
        }
        return StatsServerMapper.INSTANCE.toEndpointHit(hit);
    }

    @Override
    public List<ViewStats> getAllStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (end.isBefore(start)) {
            throw new BadRequestException("Время конца должны быть после времени начала.");
        }
        if (unique) {
            if (uris == null) {
                return statsServerRepository.getAllUniqueStats(start, end);
            } else {
                return statsServerRepository.getAllUniqueStatsWithUris(start, end, uris);
            }
        } else {
            if (uris == null) {
                return statsServerRepository.getAllStats(start, end);
            } else {
                return statsServerRepository.getAllStatsWithUris(start, end, uris);
            }
        }
    }

    @Override
    public List<ViewStats> getFullStats() {
        return statsServerRepository.getAllStatsWithautTime();
    }

}
