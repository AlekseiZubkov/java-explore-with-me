package ru.practicum.ewm.comment.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.mapper.UserMapper;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CommentMapper {
    public Comment toComment(CommentDto commentDto, User user, Event event) {
        return Comment.builder()
                .text(commentDto.getText())
                .author(user)
                .event(event)
                .edited(false)
                .created(LocalDateTime.now())
                .build();
    }

    public CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                UserMapper.INSTANCE.toUserShortDto(comment.getAuthor()),
                comment.getEvent().getId(),
                comment.getCreated(),
                comment.getEdited()
        );
    }

    public List<CommentDto> toCommentDtoList(List<Comment> comments) {
        return comments.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }
}
