package com.matei.application.rest;

import com.matei.application.rest.model.post.PostCreateDto;
import com.matei.application.rest.model.post.PostDto;
import com.matei.application.rest.model.post.PostUpdateDto;
import com.matei.application.service.PostsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@CrossOrigin("*")
@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostsService postsService;

    @GetMapping("/count")
    public Map<String, Long> getCount() {
        return Map.of("count", postsService.getCount());
    }

    @GetMapping("/")
    public List<PostDto> getPage(@RequestParam(required = false, defaultValue = "0") int pageIndex,
                                 @RequestParam(required = false, defaultValue = "50") int pageSize) {
        return postsService.getPage(pageIndex, pageSize);
    }

    @GetMapping("/{postId}")
    public PostDto getOne(@PathVariable Long postId) {
        return postsService.getPost(postId);
    }

    @PostMapping
    public PostDto createPost(@RequestBody @Valid PostCreateDto dto) {
        return postsService.createPost(dto);

    }

    @PutMapping
    public PostDto updatePost(@RequestBody @Valid PostUpdateDto dto) {
        return postsService.updatePost(dto);

    }

    @DeleteMapping("/{postId}")
    public void deletePost(@PathVariable Long postId) {
        postsService.deletePost(postId);
    }
}
