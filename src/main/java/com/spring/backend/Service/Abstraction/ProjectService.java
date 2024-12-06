package com.spring.backend.Service.Abstraction;

import com.spring.backend.Model.Chat;
import com.spring.backend.Model.Project;
import com.spring.backend.Model.User;

import java.util.List;

public interface ProjectService {


    Project createProject(Project project , Long userId);

    List<Project> getProjectsByTeam(User user, String category, String tag);

    Project getProjectById(Long id);

    void deleteProject(Long projectId,Long userId);

    Project updateProject(Project projectToUpdate, Long userId);

    void addUserToProject(Long projectId,Long userId);

    void removeUserFromProject(Long projectId, Long userId);

    Chat getChatByProjectId(Long projectId);

    List<Project> searchProjects(String keyword,User user);


}
