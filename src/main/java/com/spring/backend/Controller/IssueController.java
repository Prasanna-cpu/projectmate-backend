package com.spring.backend.Controller;


import com.spring.backend.DTO.IssueDTO;
import com.spring.backend.Mapper.IssueMapper;
import com.spring.backend.Model.Issue;
import com.spring.backend.Model.User;
import com.spring.backend.Request.IssueRequest;
import com.spring.backend.Response.ApiResponse;
import com.spring.backend.Service.Abstraction.IssueService;
import com.spring.backend.Service.Abstraction.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/issues")
public class IssueController {

    private final UserService userService;

    private final IssueService issueService;


    @GetMapping("/issue/{issueId}")
    public ResponseEntity<ApiResponse> getIssue(@PathVariable Long issueId){
        Issue issue=issueService.getIssueById(issueId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse(
                        "Issue fetched successfully",
                        issue,
                        HttpStatus.OK.value()
                )
        );
    }

    @GetMapping("/issue/project/{projectId}")
    public ResponseEntity<ApiResponse> getIssueByProjectId(@PathVariable Long projectId){
        List<Issue> issues=issueService.getIssueByProjectId(projectId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse(
                        "Issues fetched successfully",
                        issues,
                        HttpStatus.OK.value()
                )
        );

    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createIssue(
            @RequestBody IssueRequest issue,
            @RequestHeader("Authorization") String jwt
            ){
        User tokenUser=userService.findUserProfileByJwt(jwt);

        Issue createdIssue=issueService.createIssue(issue, tokenUser.getId());
        IssueDTO issueDTO= IssueMapper.mapToIssueDTO(createdIssue);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse(
                        "Issue created successfully",
                        issueDTO,
                        HttpStatus.CREATED.value()
                )
        );


    }
    @PutMapping("/update/{issueId}")
    public ResponseEntity<ApiResponse> updateIssue(
            @PathVariable Long issueId,
            @RequestBody IssueRequest issue,
            @RequestHeader("Authorization") String jwt
    ){
        User user=userService.findUserProfileByJwt(jwt);
        Issue updatedIssue=issueService.updateIssue(issueId,issue, user.getId());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                new ApiResponse(
                        "Issue updated successfully",
                        updatedIssue,
                        HttpStatus.ACCEPTED.value()
                )
        );
    }

    @DeleteMapping("/delete/{issueId}")
    public ResponseEntity<ApiResponse>  deleteIssue(@PathVariable Long issueId,@RequestHeader("Authorization") String jwt){
        User user=userService.findUserProfileByJwt(jwt);
        issueService.deleteIssue(issueId,user.getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                new ApiResponse(
                        "Issue deleted successfully",
                        null,
                        HttpStatus.NO_CONTENT.value()
                )
        );
    }

    @PutMapping("/{issueId}/assignee/{userId}")
    public ResponseEntity<ApiResponse> addUserToIssue(
            @PathVariable Long issueId,
            @PathVariable Long userId
    ){
        Issue issue=issueService.addUserToIssue(issueId, userId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                new ApiResponse(
                        "User added to issue successfully",
                        issue,
                        HttpStatus.ACCEPTED.value()
                )
        );
    }

    @PutMapping("/{issueId}/status/{status}")
    public ResponseEntity<ApiResponse>updateIssueStatus(
            @PathVariable String status,
            @PathVariable Long issueId)  {
        Issue issue = issueService.updateStatus(issueId,status);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                new ApiResponse(
                        "Issue status updated successfully",
                        issue,
                        HttpStatus.ACCEPTED.value()
                )
        );
    }


}
