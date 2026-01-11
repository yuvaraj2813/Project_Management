package com.example.demo.Service;

import com.example.demo.DTO.ProjectRequest;
import com.example.demo.DTO.ProjectResponse;
import com.example.demo.Entity.ProjectMember;
import com.example.demo.Entity.Projects;
import com.example.demo.Entity.User;
import com.example.demo.Exceptions.ResourceAlreadyExistException;
import com.example.demo.Repository.ProjectMemberRepository;
import com.example.demo.Repository.ProjectsRepository;
import com.example.demo.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectsRepository projectsRepository;
    private final UserRepository userRepository;


    public ProjectResponse createProject(ProjectRequest request,String email){

        log.info("{} user try to create Project",email);

        User manager=userRepository.findByEmail(email)
                .orElseThrow(()->{
                    log.warn("Invalid user :{}",email);
                    throw new RuntimeException("user not found");
                });

        if(projectsRepository.existsByname(request.name())){
            log.warn("'{}'project name already exists",request.name());
            throw new ResourceAlreadyExistException("Project with name "+request.name()+" already exist.");
        }

        Projects projects=new Projects();
        projects.setName(request.name());
        projects.setDescription(request.description());
        projects.setCreatedBy(manager);
        Projects savedproject;

        try{savedproject=projectsRepository.save(projects);
            log.info("Project created successfully with ID {}.",savedproject.getId());
        } catch (RuntimeException e) {
            log.error("Failed to create project '{}' for user '{}' ",projects.getName(),email);
            throw new RuntimeException(e);
        }

        ProjectMember projectMember=new ProjectMember();
        projectMember.setUser(manager);
        projectMember.setProject(savedproject);
        try{
            projectMemberRepository.save(projectMember);
            log.info("'{}' added as Member for project ID: {}",email,savedproject.getId());
        } catch (Exception e) {
            log.error("'{}' Failed to add as Member for project ID:{}",email,savedproject.getId());
            throw new RuntimeException(e);
        }
        return maptoResponse(savedproject);
    }

    public List<ProjectResponse> getMyProjects(String email){
        log.info("'{}' try to list my project.",email);
        User user=userRepository.findByEmail(email)
                .orElseThrow(()->{
                    log.warn("'{}' is not a valid user",email);
                    throw new RuntimeException("User not found");
                }
                );
        return projectMemberRepository.findByUser(user)
                .stream()
                .map(pm->maptoResponse(pm.getProject()))
                .toList();
    }

    private ProjectResponse maptoResponse(Projects project){
        return new ProjectResponse(project.getId(),project.getName(),project.getDescription(),project.getStatus());
    }

}
