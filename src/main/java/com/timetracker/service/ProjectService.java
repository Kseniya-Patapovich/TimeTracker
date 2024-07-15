package com.timetracker.service;

import com.timetracker.exception.BlockedUserException;
import com.timetracker.exception.DeleteProjectException;
import com.timetracker.exception.ProjectIsNotInProgressException;
import com.timetracker.exception.ProjectNotFoundException;
import com.timetracker.exception.UserNotFoundException;
import com.timetracker.model.Project;
import com.timetracker.model.Users;
import com.timetracker.model.dto.ProjectCreateDto;
import com.timetracker.model.enums.ProjectStatus;
import com.timetracker.repository.ProjectRepository;
import com.timetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        }
        return List.of();
    }

    public List<Users> getUsersByProjectId(Long id) {
        Optional<Project> projectFromDb = getProjectById(id);
        if (projectFromDb.isPresent()) {
            Project project = projectFromDb.get();
            if (project.getProjectStatus() == ProjectStatus.DRAFT) {
                return project.getUsers();
            }
        }
        return List.of();
    }

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
                throw new DeleteProjectException(id.toString());
            }
        } else {
            throw new ProjectNotFoundException(id.toString());
        }
    }

    public void addUserToProject(Long projectId, Long userId) {
        Optional<Project> projectFromDb = projectRepository.findById(projectId);
        if (projectFromDb.isPresent()) {
            Project project = projectFromDb.get();
            Optional<Users> userFromDb = userRepository.findById(userId);
            if (userFromDb.isPresent()) {
                Users user = userFromDb.get();
                if (user.getLocked() == false) {
                    project.getUsers().add(user);
                    user.getProjects().add(project);
                    userRepository.saveAndFlush(user);
                } else {
                    throw new BlockedUserException(userId.toString());
                }
            } else {
                throw new UserNotFoundException(userId.toString());
            }
            projectRepository.saveAndFlush(project);
        } else {
            throw new ProjectNotFoundException(projectId.toString());
        }
    }

    public void doneProject(Long id) {
        Optional<Project> projectFromDb = projectRepository.findById(id);
        if (projectFromDb.isPresent()) {
            Project project = projectFromDb.get();
            if (project.getProjectStatus() == ProjectStatus.IN_PROGRESS) {
                project.setProjectStatus(ProjectStatus.DONE);
                projectRepository.saveAndFlush(project);
            } else {
                throw new ProjectIsNotInProgressException(id.toString());
            }
        } else {
            throw new ProjectNotFoundException(id.toString());
        }
    }
}
