package com.matei.application.repo;

import com.matei.application.model.Comment;
import com.matei.application.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByReplyingToOrderByIdDesc(Post replyingTo);
}
