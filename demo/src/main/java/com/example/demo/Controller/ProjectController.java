package com.example.demo.Controller;

import com.example.demo.DTO.ProjectRequest;
import com.example.demo.DTO.ProjectResponse;
import com.example.demo.Entity.Projects;
import com.example.demo.Service.ProjectService;
import io.jsonwebtoken.Jwt;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/create")
    public ResponseEntity<ProjectResponse> createProject(@Valid @RequestBody ProjectRequest request,
                                                         @AuthenticationPrincipal UserDetails userDetails){

        return  ResponseEntity.status(HttpStatus.CREATED)
                .body(projectService.createProject(request, userDetails.getUsername()));
    }

    @PreAuthorize("hasAnyRole('MANAGER','MEMBER')")
    @GetMapping("/my")
    public  ResponseEntity<List<ProjectResponse>> getMyprojects(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(projectService.getMyProjects(userDetails.getUsername()));
    }
}
