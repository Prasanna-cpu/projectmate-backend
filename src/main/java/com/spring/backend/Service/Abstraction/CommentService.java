package com.spring.backend.Service.Abstraction;

import com.spring.backend.Model.Comment;

import java.util.List;

public interface CommentService {
    Comment createComment(Long issueId,Long userId,String content);

    void deleteComment(Long commentId,Long userId);

    List<Comment> findCommentByIssueId(Long issueId);
}
