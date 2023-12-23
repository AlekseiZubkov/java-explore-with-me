package ru.practicum.ewm.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.mapper.CommentMapper;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.comment.repository.CommentRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentPrivateServiceImpl implements CommentPrivateService {
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public CommentDto addComment(Long userId, Long eventId, CommentDto commentDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Событие не найдено"));

        Comment savedComment = commentRepository.save(CommentMapper.toComment(commentDto, user, event));

        return CommentMapper.toCommentDto(savedComment);
    }

    @Transactional
    @Override
    public CommentDto updateComment(Long userId, Long commentId, Long eventId, CommentDto commentDto) {
        checkEventAvailability(eventId);

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Комментарий не найден"));
        checkAuthorComment(userId, comment);

        comment.setText(commentDto.getText());
        comment.setEdited(true);
        Comment savedComment = commentRepository.save(comment);

        return CommentMapper.toCommentDto(savedComment);

    }

    @Transactional
    @Override
    public void deleteCommentByUser(Long userId, Long eventId, Long commentId) {
        checkEventAvailability(eventId);
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Комментарий не найден"));
        checkAuthorComment(userId, comment);
        commentRepository.deleteById(commentId);

    }


    @Override
    public List<CommentDto> findAllByEventId(Long eventId, Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        List<Comment> comments = commentRepository.findByEventId(eventId, pageRequest);
        return CommentMapper.toCommentDtoList(comments);
    }

    @Override
    public CommentDto findCommentById(Long userId, Long eventId, Long commentId) {
        checkEventAvailability(eventId);

        Comment comment = commentRepository.findByIdAndAuthorIdAndEventId(commentId, userId, eventId)
                .orElseThrow(() -> new NotFoundException("Комментарий не найден"));
        return CommentMapper.toCommentDto(comment);
    }

    private void checkEventAvailability(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("Событие не найдено");
        }
    }

    private static void checkAuthorComment(Long userId, Comment comment) {
        if (!Objects.equals(userId, comment.getAuthor().getId())) {
            throw new ConflictException("Изменение может вносить только автор комментария.");
        }
    }
}
