package com.spring.backend.Controller;


import com.spring.backend.Model.Chat;
import com.spring.backend.Model.Invitation;
import com.spring.backend.Model.Project;
import com.spring.backend.Model.User;
import com.spring.backend.Request.InviteRequest;
import com.spring.backend.Response.ApiResponse;
import com.spring.backend.Service.Abstraction.InvitationService;
import com.spring.backend.Service.Abstraction.ProjectService;
import com.spring.backend.Service.Abstraction.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Slf4j
public class ProjectController {

    private final ProjectService projectService;

    private final UserService userService;

    private final InvitationService invitationService;


    @GetMapping("/get-projects")
    public ResponseEntity<ApiResponse> getProjects(
            @RequestParam(required=false) String category,
            @RequestParam(required=false) String tag,
            @RequestHeader("Authorization") String jwt
    ){
        User user=userService.findUserProfileByJwt(jwt);
        List<Project> projects=projectService.getProjectsByTeam(user,category,tag);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse(
                        "Projects fetched successfully",
                        projects,
                        HttpStatus.OK.value()
                )
        );
    }

    @GetMapping("/get-project/{id}")
    public ResponseEntity<ApiResponse> getProjectById(
            @PathVariable Long id,
            @RequestHeader("Authorization") String jwt
    ){
        User user=userService.findUserProfileByJwt(jwt);
        Project project=projectService.getProjectById(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse(
                        "Project fetched successfully",
                        project,
                        HttpStatus.OK.value()
                )
        );
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createProject(
            @RequestBody Project project,
            @RequestHeader("Authorization") String jwt
    ){
        User user=userService.findUserProfileByJwt(jwt);
        project.setOwner(user);
        Project newProject=projectService.createProject(project,user.getId());
        userService.updateUsersProjectSize(user,1);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse(
                        "Project created successfully",
                        newProject,
                        HttpStatus.CREATED.value()
                )
        );
    }
    @PatchMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateProject(
            @PathVariable Long id,
            @RequestBody Project project,
            @RequestHeader("Authorization") String jwt
    ){
        User user=userService.findUserProfileByJwt(jwt);
        Project projectToUpdate=projectService.updateProject(project,user.getId());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                new ApiResponse(
                        "Project updated successfully",
                        projectToUpdate,
                        HttpStatus.ACCEPTED.value()
                        )
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteProject(
            @PathVariable Long id,
            @RequestHeader("Authorization") String jwt
    ){
        User user=userService.findUserProfileByJwt(jwt);
        log.info(String.valueOf(user.getId()));
        projectService.deleteProject(id,user.getId());
        userService.updateUsersProjectSize(user,-1);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                new ApiResponse(
                        "Project deleted successfully",
                        null,
                        HttpStatus.NO_CONTENT.value()
                )
        );
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchProjects(
            @RequestParam(required = false) String keyword,
            @RequestHeader("Authorization") String jwt
    ){
        User user=userService.findUserProfileByJwt(jwt);
        List<Project> projects=projectService.searchProjects(keyword,user);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse(
                        "Projects fetched successfully",
                        projects,
                        HttpStatus.OK.value()
                )
        );
    }

    @PostMapping("/{userId}/add-to-project/{projectId}")
    public ResponseEntity<ApiResponse> addUserToProject(
            @PathVariable Long projectId,
            @PathVariable Long userId
    ){
        projectService.addUserToProject(projectId, userId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                new ApiResponse(
                        "User added to project successfully",
                        null,
                        HttpStatus.ACCEPTED.value()
                )
        );
    }

    @DeleteMapping("/{userId}/remove-from-project/{projectId}")
    public ResponseEntity<ApiResponse> removeUserFromProject(
            @PathVariable Long projectId,
            @PathVariable Long userId){
        projectService.removeUserFromProject(projectId, userId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                new ApiResponse(
                        "User removed from project successfully",
                        null,
                        HttpStatus.ACCEPTED.value()
                )
        );
    }

    @GetMapping("/{projectId}/get-chat")
    public ResponseEntity<ApiResponse> getChatByProjectId(
            @PathVariable Long projectId,
            @RequestHeader("Authorization") String jwt
    ){
        User user=userService.findUserProfileByJwt(jwt);
        Chat chat=projectService.getChatByProjectId(projectId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse(
                        "Chat fetched successfully",
                        chat,
                        HttpStatus.OK.value()
                )
        );
    }
   @PostMapping("/invite")
    public ResponseEntity<ApiResponse> invitationHandler(
            @RequestBody InviteRequest inviteRequest
    ) throws MessagingException {

        invitationService.sendInvitation(inviteRequest.getEmail(),inviteRequest.getProjectId());

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse(
                        "Invitation sent successfully",
                        null,
                        HttpStatus.OK.value()
                )
        );

    }

    @GetMapping("/accept")
    public ResponseEntity<ApiResponse> acceptInvitation(
            @RequestParam String token,
            @RequestHeader("Authorization") String jwt
    ) {
        User user=userService.findUserProfileByJwt(jwt);

        Invitation invitation = invitationService.acceptInvitation(token,user.getId());
        projectService.addUserToProject(invitation.getProjectId(),user.getId());

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                new ApiResponse(
                        "Invitation accepted successfully",
                        invitation,
                        HttpStatus.ACCEPTED.value()
                )
        );
    }

}
