package com.spring.backend.Service.Implemenatation;

import com.spring.backend.Exceptions.IssueResourceNotFoundException;
import com.spring.backend.Model.Issue;
import com.spring.backend.Model.Project;
import com.spring.backend.Model.User;
import com.spring.backend.Repository.IssueRepository;
import com.spring.backend.Request.IssueRequest;
import com.spring.backend.Service.Abstraction.IssueService;
import com.spring.backend.Service.Abstraction.ProjectService;
import com.spring.backend.Service.Abstraction.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class, IssueResourceNotFoundException.class})
public class IssueServiceImplementation implements IssueService {

    private final IssueRepository issueRepository;

    private final ProjectService projectService;

    private final UserService userService;

    @Override
    public Issue getIssueById(Long issueId) {
        Issue issue = issueRepository.findById(issueId).orElseThrow(
                () -> new IssueResourceNotFoundException("Issue not found with id: " + issueId)
        );
        return issue;
    }

    @Override
    public List<Issue> getIssueByProjectId(Long projectId) {

        Project project=projectService.getProjectById(projectId);

        List<Issue> issues=issueRepository.findByProjectId(projectId).orElseThrow(
                () -> new IssueResourceNotFoundException("Issue not found for project: " + projectId)
        );
        return issues;

    }

    @Override
    public Issue createIssue(IssueRequest issueRequest, Long userid) {
        Project project=projectService.getProjectById(issueRequest.getProjectId());
        Issue issue=new Issue();
        issue.setTitle(issueRequest.getTitle());
        issue.setDescription(issueRequest.getDescription());
        issue.setStatus(issueRequest.getStatus());
        issue.setProjectID(issueRequest.getProjectId());
        issue.setPriority(issueRequest.getPriority());
        issue.setDueDate(issueRequest.getDueDate());
        issue.setProject(project);

        Issue newIssue=issueRepository.save(issue);
        return newIssue;
    }

    @Override
    public Issue updateIssue(Long issueId, IssueRequest updatedIssue, Long userid) {
        User user=userService.findUserById(userid);
        Optional<Issue> existingIssue=issueRepository.findById(issueId);

        if(existingIssue.isPresent()){
            Project project=projectService.getProjectById(updatedIssue.getProjectId());
            User assignee = userService.findUserById(updatedIssue.getUserId());

            Issue issueToUpdate = getIssue(updatedIssue, existingIssue);

            Issue newIssue=issueRepository.save(issueToUpdate);
            return newIssue;
        }
        else{
            throw new IssueResourceNotFoundException("Issue not found with id: " + issueId);
        }


    }

    private static Issue getIssue(IssueRequest updatedIssue, Optional<Issue> existingIssue) {
        Issue issueToUpdate= existingIssue.get();


        if (updatedIssue.getDescription() != null) {
            issueToUpdate.setDescription(updatedIssue.getDescription());
        }

        if (updatedIssue.getDueDate() != null) {
            issueToUpdate.setDueDate(updatedIssue.getDueDate());
        }

        if (updatedIssue.getPriority() != null) {
            issueToUpdate.setPriority(updatedIssue.getPriority());
        }

        if (updatedIssue.getStatus() != null) {
            issueToUpdate.setStatus(updatedIssue.getStatus());
        }

        if (updatedIssue.getTitle() != null) {
            issueToUpdate.setTitle(updatedIssue.getTitle());
        }
        return issueToUpdate;
    }

    @Override
    public void deleteIssue(Long issueId, Long userid) {
        userService.findUserById(userid);
        issueRepository.findById(issueId).ifPresentOrElse(issueRepository::delete,()->{
            throw new IssueResourceNotFoundException("Issue not found with id: " + issueId);
        });
    }

    @Override
    public List<Issue> getIssuesByAssigneeId(Long assigneeId) {
        return List.of();
    }

    @Override
    public List<Issue> searchIssues(String title, String status, String priority, Long assigneeId) {
        return List.of();
    }

    @Override
    public List<User> getAssigneeForIssue(Long issueId) {
        return List.of();
    }

    @Override
    public Issue addUserToIssue(Long issueId, Long userId) {
        User user=userService.findUserById(userId);
        Issue issue=getIssueById(issueId);
        issue.setAssignee(user);
        Issue newIssue=issueRepository.save(issue);
        return newIssue;
    }

    @Override
    public Issue updateStatus(Long issueId, String status) {
       Issue issue=getIssueById(issueId);
       issue.setStatus(status);
       Issue newIssue=issueRepository.save(issue);
       return newIssue;
    }
}
