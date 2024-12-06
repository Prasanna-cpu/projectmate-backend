package com.spring.backend.Service.Implemenatation;

import com.spring.backend.Exceptions.CommentResourceNotFoundException;
import com.spring.backend.Exceptions.ForbiddenOperationException;
import com.spring.backend.Exceptions.IssueResourceNotFoundException;
import com.spring.backend.Model.Comment;
import com.spring.backend.Model.Issue;
import com.spring.backend.Model.User;
import com.spring.backend.Repository.CommentRepository;
import com.spring.backend.Repository.IssueRepository;
import com.spring.backend.Repository.UserRepository;
import com.spring.backend.Service.Abstraction.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
@Transactional(rollbackFor = {Exception.class,
        IssueResourceNotFoundException.class, UsernameNotFoundException.class
        , CommentResourceNotFoundException.class, ForbiddenOperationException.class})
@RequiredArgsConstructor
public class CommentServiceImplementation implements CommentService {

    private final CommentRepository commentRepository;

    private final IssueRepository issueRepository;

    private final UserRepository userRepository;

    @Override
    public Comment createComment(Long issueId, Long userId, String content) {
        Issue issue=issueRepository.findById(issueId).orElseThrow(
                ()-> new IssueResourceNotFoundException("Issue not found")
        );

        User user=userRepository.findById(userId).orElseThrow(
                ()-> new UsernameNotFoundException("User not found")
        );

        Comment comment=new Comment();

        comment.setIssue(issue);
        comment.setUser(user);
        comment.setContent(content);
        comment.setCreatedDateTime(LocalDateTime.now());

        Comment saveComment = commentRepository.save(comment);
        issue.getComments().add(saveComment);
        return saveComment;
    }

    @Override
    public void deleteComment(Long commentId, Long userId) {
        Comment comment=commentRepository.findById(commentId).orElseThrow(
                ()->new CommentResourceNotFoundException("Comment not found")
        );
        User user=userRepository.findById(userId).orElseThrow(
                ()->new UsernameNotFoundException("User not found")
        );

        if(comment.getUser().equals(user)){
            commentRepository.delete(comment);
        }
        else{
            throw new ForbiddenOperationException("Operation forbidden");
        }
    }

    @Override
    public List<Comment> findCommentByIssueId(Long issueId) {
        List<Comment> comments=commentRepository.findCommentByIssueId(issueId).orElseThrow(
                ()->new CommentResourceNotFoundException("Comment not found")
        );
        return comments;
     }
}
