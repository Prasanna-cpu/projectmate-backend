package com.spring.backend.Service.Abstraction;

import com.spring.backend.Model.Issue;
import com.spring.backend.Model.User;
import com.spring.backend.Request.IssueRequest;

import java.util.List;

public interface IssueService {
    Issue getIssueById(Long issueId) ;

    List<Issue> getIssueByProjectId(Long projectId) ;

    Issue createIssue(IssueRequest issueRequest, Long userid) ;

    Issue updateIssue(Long issueId,IssueRequest updatedIssue,Long userid );

    void deleteIssue(Long issueId,Long userid) ;

    List<Issue> getIssuesByAssigneeId(Long assigneeId) ;

    List<Issue> searchIssues(String title, String status, String priority, Long assigneeId) ;

    List<User> getAssigneeForIssue(Long issueId) ;

    Issue addUserToIssue(Long issueId, Long userId) ;

    Issue updateStatus(Long issueId, String status) ;
}
