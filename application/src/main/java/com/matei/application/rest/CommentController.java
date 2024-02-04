package com.matei.application.rest;

import com.matei.application.rest.model.comment.CommentCreateDto;
import com.matei.application.rest.model.comment.CommentDto;
import com.matei.application.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin("*")
@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/post/{postId}")
    public List<CommentDto> getForPost(@PathVariable Long postId) {
        return commentService.findAllForPost(postId);
    }

    @PostMapping
    public CommentDto createComment(@RequestBody @Valid CommentCreateDto dto) {
        return commentService.createOne(dto);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        commentService.deleteOne(commentId);
    }
}
