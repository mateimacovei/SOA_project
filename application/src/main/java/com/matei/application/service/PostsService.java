package com.matei.application.service;

import com.matei.application.model.Post;
import com.matei.application.repo.PostRepository;
import com.matei.application.rest.model.post.PostCreateDto;
import com.matei.application.rest.model.post.PostDto;
import com.matei.application.rest.model.post.PostUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.matei.application.service.TextBoardException.notFound;

@Component
@RequiredArgsConstructor
public class PostsService {

    private final PostRepository postRepository;

    private Post findOneAndAuthorize(Long postId) {
        var found = findOne(postId);
        UserAuthorizationService.checkIfUserNameMatchingOrRequireAdmin(found.getCreatedBy());
        return found;
    }

    Post findOne(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> notFound("Post with id " + postId + " not found"));
    }

    public PostDto mapToDto(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .text(post.getPostText())
                .createdBy(post.getCreatedBy())
                .createdDate(post.getCreatedDate())
                .build();
    }

    public List<PostDto> getPage(int pageIndex, int pageSize) {
        Sort sort = Sort.by(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);

        return postRepository.findAll(pageable).getContent().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public Long getCount() {
        return postRepository.count();
    }

    public PostDto getPost(Long postId) {
        return mapToDto(findOne(postId));
    }

    @Transactional
    public PostDto createPost(PostCreateDto dto) {
        var toSave = Post.builder().postText(dto.getText()).build();
        var saved = postRepository.save(toSave);
        return mapToDto(saved);
    }

    public PostDto updatePost(PostUpdateDto dto) {
        var found = findOneAndAuthorize(dto.getId());
        found.setPostText(dto.getText());
        return mapToDto(postRepository.save(found));
    }

    @Transactional
    public void deletePost(Long postId) {
        var found = findOneAndAuthorize(postId);
        // this will also delete comments
        postRepository.delete(found);
    }

    @Transactional
    public void handleDeletedUser(String username) {
        postRepository.deleteByCreatedBy(username); // this will also remove the comments
    }
}
