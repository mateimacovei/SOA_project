package com.matei.application.service;

import com.matei.application.model.Comment;
import com.matei.application.repo.CommentRepository;
import com.matei.application.rest.model.comment.CommentCreateDto;
import com.matei.application.rest.model.comment.CommentDto;
import com.matei.application.service.external.ServerNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.matei.application.service.TextBoardException.notFound;


@Component
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostsService postsService;
    private final ServerNotificationService notificationService;

    private CommentDto mapToDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getCommentText())
                .createdBy(comment.getCreatedBy())
                .createdDate(comment.getCreatedDate())
                .build();
    }

    private Comment findOne(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> notFound("Comment with id " + commentId + " not found"));
    }

    public List<CommentDto> findAllForPost(Long postId) {
        var post = postsService.findOne(postId);
        return commentRepository.findByReplyingToOrderByIdDesc(post)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public CommentDto createOne(CommentCreateDto createDto) {
        var post = postsService.findOne(createDto.getPostId());
        var toSave = Comment.builder().commentText(createDto.getText()).replyingTo(post).build();
        CommentDto commentDto = mapToDto(commentRepository.save(toSave));
        var currentUser = UserAuthorizationService.getCurrentUserDetails().getUsername();
        var postCreator = post.getCreatedBy();
        if (!currentUser.equals(postCreator)) { // I don't want to get notifications for myself
            notificationService.sendNotification(postCreator, currentUser + " left a comment on your post");
        }
        return commentDto;
    }

    public void deleteOne(Long commentId) {
        var comment = findOne(commentId);
        UserAuthorizationService.checkIfUserNameMatchingOrRequireAdmin(comment.getCreatedBy());
        commentRepository.delete(comment);
    }
}
