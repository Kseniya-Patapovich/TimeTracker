package com.timetracker.repository;

import com.timetracker.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT p FROM project p JOIN p.users u WHERE u.id = :userId")
    List<Project> findProjectsByUserId(Long userId);
}
