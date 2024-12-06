package com.spring.backend.Controller;


import com.spring.backend.Model.Comment;
import com.spring.backend.Model.User;
import com.spring.backend.Request.CommentRequest;
import com.spring.backend.Response.ApiResponse;
import com.spring.backend.Service.Abstraction.CommentService;
import com.spring.backend.Service.Abstraction.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;

    private final UserService userService;


    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createComment(
            @RequestBody CommentRequest request,
            @RequestHeader("Authorization") String jwt
    ){
        User user=userService.findUserProfileByJwt(jwt);
        Comment createdComment=commentService.createComment(request.getIssueId(),
                user.getId(),
                request.getContent());

        return ResponseEntity.status(HttpStatus.CREATED).body(
               new ApiResponse(
                       "Comment created successfully",
                       createdComment,
                       HttpStatus.CREATED.value()
               )
        );
    }

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<ApiResponse> deleteComment(
            @PathVariable Long commentId,
            @RequestHeader("Authorization") String jwt
    ){
        User user=userService.findUserProfileByJwt(jwt);
        commentService.deleteComment(commentId, user.getId());
        log.info("Comment deleted successfully : {}", commentId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse(
                        "Comment deleted successfully",
                        null,
                        HttpStatus.OK.value()
                )
        );
    }

    @GetMapping("/issuer/{issueId}")
    public ResponseEntity<ApiResponse> findComments(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long issueId
    ){
        userService.findUserProfileByJwt(jwt);
        List<Comment> comments=commentService.findCommentByIssueId(issueId);
        return ResponseEntity.ok(
                new ApiResponse(
                        "Comments fetched successfully",
                        comments,
                        HttpStatus.OK.value()
                )
        );
    }

}
