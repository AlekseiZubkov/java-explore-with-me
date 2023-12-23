package ru.practicum.ewm.comment.service;

import ru.practicum.ewm.comment.dto.CommentDto;

import java.util.List;

public interface CommentPrivateService {
    CommentDto addComment(Long userId, Long eventId, CommentDto commentDto);

    CommentDto updateComment(Long userId, Long commentId, Long eventId, CommentDto commentDto);

    void deleteCommentByUser(Long userId, Long eventId, Long commentId);
    List<CommentDto> findAllByEventId(Long eventId, Integer from, Integer size);
    CommentDto findCommentById(Long userId, Long eventId, Long commentId);

}
