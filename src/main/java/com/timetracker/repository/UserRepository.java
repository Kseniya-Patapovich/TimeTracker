package com.timetracker.repository;

import com.timetracker.model.TimeTrackerUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<TimeTrackerUser, Long> {
    Optional<TimeTrackerUser> findByLogin(String login);

    @Query("SELECT u FROM time_tracker_user u JOIN u.projects p WHERE p.id = :projectId")
    List<TimeTrackerUser> getAllByProjectId(@Param("projectId") Long projectId);
}
