package com.timetracker.service;

import com.timetracker.model.Project;
import com.timetracker.model.TimeTrackerUser;
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

    public Project getProjectById(long id) {
        return projectRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project " + id + " not found"));
    }

    public List<Project> getAllByUserId(long id) {
        return projectRepository.findProjectsByUserId(id);
    }

    @Transactional
    public Long createProject(ProjectCreateDto projectCreateDto) {
        Project project = new Project();
        project.setProjectName(projectCreateDto.getProjectName());
        project.setProjectStatus(ProjectStatus.DRAFT);
        List<TimeTrackerUser> users = userRepository.findAllById(projectCreateDto.getUsersId());
        project.setUsers(users);
        Project createdProject = projectRepository.save(project);
        return createdProject.getId();
    }

    @Transactional
    public void deleteProject(Long id) {
        Project project = projectUtils.getProject(id);
        if (project.getProjectStatus() == ProjectStatus.DRAFT) {
            projectRepository.delete(project);
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Possible to delete only DRAFT projects!");
        }
    }

    @Transactional
    public void addUserToProject(long projectId, long userId) {
        Project project = projectUtils.getProject(projectId);
        TimeTrackerUser user = userUtils.getUser(userId);

        user.getProjects().add(project);
        userRepository.save(user);
    }

    @Transactional
    public void updateProjectStatus(long id, String projectStatus) {
        Project project = projectUtils.getProject(id);
        ProjectStatus newStatus = ProjectStatus.getByName(projectStatus);
        if (project.getProjectStatus().ordinal() < newStatus.ordinal()) {
            project.setProjectStatus(newStatus);
            projectRepository.save(project);
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Project with id=" + id + " is in the wrong status!");
        }
    }
}
