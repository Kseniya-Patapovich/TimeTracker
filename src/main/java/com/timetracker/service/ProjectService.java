package com.timetracker.service;

import com.timetracker.model.Project;
import com.timetracker.model.Users;
import com.timetracker.model.dto.ProjectCreateDto;
import com.timetracker.model.enums.ProjectStatus;
import com.timetracker.repository.ProjectRepository;
import com.timetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    public List<Project> getAllByUserId(Long id) {
        Optional<Users> userFromDb = userRepository.findById(id);
        if (userFromDb.isPresent()) {
            Users user = userFromDb.get();
            return user.getProjects();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id=" + id + " not found!");
        }
    }

    @Transactional
    public Long createProject(ProjectCreateDto projectCreateDto) {
        Project project = new Project();
        project.setProjectName(projectCreateDto.getProjectName());
        project.setProjectStatus(ProjectStatus.DRAFT);
        Project createdProject = projectRepository.save(project);
        return createdProject.getId();
    }

    public Boolean deleteProject(Long id) {
        Optional<Project> projectFromDb = getProjectById(id);
        if (projectFromDb.isPresent()) {
            if (projectFromDb.get().getProjectStatus() == ProjectStatus.DRAFT) {
                projectRepository.deleteById(id);
                return getProjectById(id).isEmpty();
            } else {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Project with id=" + id + " is not DRAFT status!");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project with id=" + id + " not found!");
        }
    }

    @Transactional
    public void addUserToProject(Long projectId, Long userId) {
        Optional<Project> projectFromDb = projectRepository.findById(projectId);
        if (projectFromDb.isPresent()) {
            Project project = projectFromDb.get();
            Optional<Users> userFromDb = userRepository.findById(userId);
            if (userFromDb.isPresent()) {
                Users user = userFromDb.get();
                if (user.getLocked()) {
                    project.getUsers().add(user);
                    user.getProjects().add(project);
                    userRepository.save(user);
                } else {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "User with id=" + userId + " is block");
                }
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id=" + userId + " not found!");
            }
            projectRepository.save(project);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project with id=" + projectId + " not found!");
        }
    }

    @Transactional
    public void updateProjectStatus(Long id, ProjectStatus projectStatus) {
        Optional<Project> projectFromDb = projectRepository.findById(id);
        if (projectFromDb.isPresent()) {
            Project project = projectFromDb.get();
            if (project.getProjectStatus().ordinal() < projectStatus.ordinal()) {
                project.setProjectStatus(projectStatus);
                projectRepository.save(project);
            } else {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Project with id=" + id + " is in the wrong status!");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project with id=" + id + " not found!");
        }
    }
}
