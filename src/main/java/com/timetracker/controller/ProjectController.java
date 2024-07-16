package com.timetracker.controller;

import com.timetracker.model.Project;
import com.timetracker.model.dto.ProjectCreateDto;
import com.timetracker.model.enums.ProjectStatus;
import com.timetracker.service.ProjectService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping
    public List<Project> getAllProject() {
        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    public Project getProjectById(@PathVariable("id") long id) {
        return projectService.getProjectById(id);
    }

    @GetMapping("/user/{id}")
    public List<Project> getAllByUserId(@PathVariable long id) {
        return projectService.getAllByUserId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createProject(@RequestBody ProjectCreateDto projectCreateDto) {
        return projectService.createProject(projectCreateDto);
    }

    @PutMapping("/{projectId}/user/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addUserToProject(@PathVariable("projectId") long projectId, @PathVariable("userId") long userId) {
        projectService.addUserToProject(projectId, userId);
    }

    @PutMapping("/changeStatus/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeProjectStatus(@PathVariable long id, @PathParam("projectStatus") String projectStatus) {
        projectService.updateProjectStatus(id, projectStatus);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProject(@PathVariable("id") long id) {
        projectService.deleteProject(id);
    }
}
