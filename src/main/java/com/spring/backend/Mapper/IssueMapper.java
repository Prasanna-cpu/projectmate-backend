package com.spring.backend.Mapper;

import com.spring.backend.DTO.IssueDTO;
import com.spring.backend.Model.Issue;

public class IssueMapper {
    public static IssueDTO mapToIssueDTO(Issue issue){
        IssueDTO issueDTO = new IssueDTO();

        issueDTO.setId(issue.getId());
        issueDTO.setTitle(issue.getTitle());
        issueDTO.setDescription(issue.getDescription());
        issueDTO.setStatus(issue.getStatus());
        issueDTO.setPriority(issue.getPriority());
        issueDTO.setAssignee(issue.getAssignee());
        issueDTO.setDueDate(issue.getDueDate());
        issueDTO.setProject(issue.getProject());
        return issueDTO;


    }
}
