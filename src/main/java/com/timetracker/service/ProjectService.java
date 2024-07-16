package com.timetracker.service;

import com.timetracker.model.Project;
import com.timetracker.model.UserTimeTracker;
import com.timetracker.model.dto.ProjectCreateDto;
import com.timetracker.model.enums.ProjectStatus;
import com.timetracker.repository.ProjectRepository;
import com.timetracker.repository.UserRepository;
import com.timetracker.utils.ProjectUtils;
import com.timetracker.utils.UserUtils;
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
    private final UserUtils userUtils;
    private final ProjectUtils projectUtils;

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    public List<Project> getAllByUserId(Long id) {
        return projectRepository.findProjectsByUserId(id);
    }

    @Transactional
    public Long createProject(ProjectCreateDto projectCreateDto) {
        Project project = new Project();
        project.setProjectName(projectCreateDto.getProjectName());
        project.setProjectStatus(ProjectStatus.DRAFT);
        List<UserTimeTracker> users = userRepository.findAllById(projectCreateDto.getUsersId());
        project.setUsers(users);
        Project createdProject = projectRepository.save(project);
        return createdProject.getId();
    }

    public Boolean deleteProject(Long id) {
        Project project = projectUtils.getProject(id);
        if (project.getProjectStatus() == ProjectStatus.DRAFT) {
            projectRepository.deleteById(id);
            return getProjectById(id).isEmpty();
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Project with id=" + id + " is not DRAFT status!");
        }
    }

    @Transactional
    public void addUserToProject(Long projectId, Long userId) {
        Project project = projectUtils.getProject(projectId);
        UserTimeTracker user = userUtils.getUser(userId);
        if (!user.getLocked()) {
            project.getUsers().add(user);
            user.getProjects().add(project);
            userRepository.save(user);
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with id=" + userId + " is block");
        }
        projectRepository.save(project);
    }

    @Transactional
    public void updateProjectStatus(Long id, ProjectStatus projectStatus) {
        Project project = projectUtils.getProject(id);
        if (project.getProjectStatus().ordinal() < projectStatus.ordinal()) {
            project.setProjectStatus(projectStatus);
            projectRepository.save(project);
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Project with id=" + id + " is in the wrong status!");
        }
    }
}
