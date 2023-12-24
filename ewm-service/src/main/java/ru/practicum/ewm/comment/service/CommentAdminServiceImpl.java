package ru.practicum.ewm.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.mapper.CommentMapper;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.comment.repository.CommentRepository;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentAdminServiceImpl implements CommentAdminService {
    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public void deleteCommentByAdmin(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new NotFoundException("Комментарий не найден");
        }
        commentRepository.deleteById(commentId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> findAllByAdmin(LocalDateTime rangeStart,
                                           LocalDateTime rangeEnd, Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        List<Comment> comments;

        if (rangeStart != null && rangeEnd != null) {
            if (rangeStart.isAfter(rangeEnd)) {
                throw new ConflictException("Дата окончания раньше даты начала.");
            }
            comments = commentRepository.findByCreatedBetween(rangeStart, rangeEnd, pageRequest);
        } else {
            comments = commentRepository.findAll();
        }
        return CommentMapper.toCommentDtoList(comments);
    }
}
