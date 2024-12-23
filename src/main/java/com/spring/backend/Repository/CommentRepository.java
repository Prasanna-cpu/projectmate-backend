package com.spring.backend.Repository;

import com.spring.backend.Model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {

    @Query("select c from Comment c where c.issue.id = ?1")
    Optional<List<Comment>> findCommentByIssueId(Long issueId);


}
