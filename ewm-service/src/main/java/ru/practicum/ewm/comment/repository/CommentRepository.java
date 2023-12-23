package ru.practicum.ewm.comment.repository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.comment.model.Comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByEventId(Long eventId, PageRequest request);
    Optional<Comment> findByIdAndAuthorIdAndEventId(Long commentId, Long authorId, Long eventId);

    List<Comment> findByCreatedBetween(LocalDateTime rangeStart, LocalDateTime rangeEnd, PageRequest request);
}
