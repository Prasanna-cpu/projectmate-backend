package com.spring.backend.Service.Implemenatation;

import com.spring.backend.Exceptions.ProjectResourceNotFoundException;
import com.spring.backend.Model.Chat;
import com.spring.backend.Model.Project;
import com.spring.backend.Model.User;
import com.spring.backend.Repository.ProjectRepository;
import com.spring.backend.Service.Abstraction.ChatService;
import com.spring.backend.Service.Abstraction.ProjectService;
import com.spring.backend.Service.Abstraction.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class, UsernameNotFoundException.class, ProjectResourceNotFoundException.class})
public class ProjectServiceImplementation implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserService userService;
    private final ChatService chatService;


    @Override
    public Project createProject(Project project, Long userId) {


        User user=userService.findUserById(userId);

        Project newProject=new Project();

        newProject.setOwner(project.getOwner());
        newProject.setName(project.getName());
        newProject.setTags(project.getTags());
        newProject.setCategory(project.getCategory());
        newProject.setDescription(project.getDescription());
        newProject.getTeams().add(user);

        Project savedProject=projectRepository.save(newProject);

        Chat chat=new Chat();
        chat.setProject(savedProject);

        Chat projectChat=chatService.createChat(chat);
        savedProject.setChat(projectChat);


        return savedProject;


    }

    @Override
    public List<Project> getProjectsByTeam(User user, String category, String tag) {
        List<Project> projects=projectRepository.findByTeamsIsContainingOrOwner(user,user).orElseThrow(
                ()->new ProjectResourceNotFoundException("No projects found")
        );

        if(category!=null){
            projects=projects.stream().filter(p->p.getCategory().equalsIgnoreCase(category)).collect(Collectors.toList());
        }

        if(tag!=null){
            projects=projects.stream().filter(p->p.getTags().contains(tag)).collect(Collectors.toList());
        }

        return projects;
    }

    @Override
    public Project getProjectById(Long id) {
        Project project=projectRepository.findById(id).orElseThrow(
                ()->new ProjectResourceNotFoundException("Project not found")
        );
        return project;
    }

    @Override
    public void deleteProject(Long projectId, Long userId) {
        User user=userService.findUserById(userId);
        if(user!=null){
            projectRepository.findById(projectId).ifPresentOrElse(projectRepository::delete,()->{
                throw new ProjectResourceNotFoundException("Project not found");
            });
        }
    }

    @Override
    public Project updateProject(Project projectToUpdate, Long userId) {
        Project project=new Project();

        project.setName(projectToUpdate.getName());
        project.setTags(projectToUpdate.getTags());
        project.setDescription(projectToUpdate.getDescription());

        Project saveProject=projectRepository.save(project);

        return saveProject;
    }

    @Override
    public void addUserToProject(Long projectId, Long userId) {
        Project project=getProjectById(projectId);
        User user=userService.findUserById(userId);
        if(!project.getTeams().contains(user)){
            project.getChat().getUsers().add(user);
            project.getTeams().add(user);
            projectRepository.save(project);
        }
    }

    @Override
    public void removeUserFromProject(Long projectId, Long userId) {
        Project project=getProjectById(projectId);
        User user=userService.findUserById(userId);
        if(project.getTeams().contains(user)){
            project.getChat().getUsers().remove(user);
            project.getTeams().remove(user);
        }
        projectRepository.save(project);
    }

    @Override
    public Chat getChatByProjectId(Long projectId) {
        Project project=getProjectById(projectId);
        return project.getChat();

    }

    @Override
    public List<Project> searchProjects(String keyword, User user) {
        String partialName="%"+keyword+"%";
        List<Project> projects=projectRepository.findByNameIsContainingAndTeamsIsContaining(partialName,user).orElseThrow(
                ()->new ProjectResourceNotFoundException("No projects found")
        );
        return projects;
    }
}
