package com.timetracker.service;

import com.timetracker.model.Project;
import com.timetracker.model.dto.ProjectCreateDto;
import com.timetracker.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    public Boolean createProject(ProjectCreateDto projectCreateDto) {
        Project project = new Project();
        project.setName(projectCreateDto.getName());
        Project createdProject = projectRepository.save(project);

        return getProjectById(createdProject.getId()).isPresent();
    }

    public Boolean deleteProject(Long id) {
        Optional<Project> projectFromDb = getProjectById(id);
        if (projectFromDb.isEmpty()) {
            return false;
        }
        projectRepository.deleteById(id);
        return getProjectById(id).isEmpty();
    }
}
