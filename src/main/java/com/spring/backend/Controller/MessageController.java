package com.spring.backend.Controller;

import java.util.List;

import com.spring.backend.Model.Chat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.spring.backend.Model.Message;
import com.spring.backend.Request.MessageRequest;
import com.spring.backend.Response.ApiResponse;
import com.spring.backend.Service.Abstraction.MessageService;
import com.spring.backend.Service.Abstraction.ProjectService;
import com.spring.backend.Service.Abstraction.UserService;

import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {
	
	private final MessageService messageService;
	private final UserService userService;
	private final ProjectService projectService;
	
	@PostMapping("/send")
	public ResponseEntity<ApiResponse> sendMessage(
			@RequestBody MessageRequest request
	){
		userService.findUserById(request.getSenderId());
		Chat chat=projectService.getProjectById(request.getProjectId()).getChat();

		if(chat==null){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse("Chat not found", null, HttpStatus.NOT_FOUND.value())
			);
		}
		
		Message sentMessage=messageService.sendMessage(request.getSenderId(), request.getProjectId(), request.getContent());
		
		return ResponseEntity.status(HttpStatus.OK).body(
				new ApiResponse("Message Sent", sentMessage,HttpStatus.OK.value())
		);
	}
	
	@GetMapping("/chat/{projectId}")
	public ResponseEntity<ApiResponse> getMessage(@PathVariable Long projectId){
		List<Message> messages=messageService.getMessageByProjectId(projectId);
		return ResponseEntity.status(HttpStatus.OK).body(
				new ApiResponse("Messages received", messages, HttpStatus.OK.value())
				);
	}
	
	
	

}
