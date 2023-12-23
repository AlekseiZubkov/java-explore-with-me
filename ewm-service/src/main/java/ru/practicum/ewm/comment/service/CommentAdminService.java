package ru.practicum.ewm.comment.service;

import ru.practicum.ewm.comment.dto.CommentDto;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentAdminService {
    void deleteCommentByAdmin(Long commentId);
    List<CommentDto> findAllByAdmin(LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);
}
