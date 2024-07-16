package com.timetracker.utils;

import com.timetracker.model.Project;
import com.timetracker.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Component
public class ProjectUtils {
    private final ProjectRepository projectRepository;

    public Project getProject(Long id) {
        return projectRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project with id=" + id + " not found"));
    }
}
