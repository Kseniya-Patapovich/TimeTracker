package com.timetracker.repository;

import com.timetracker.model.UserTimeTracker;
import com.timetracker.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserTimeTracker, Long> {
    Optional<UserTimeTracker> findByLogin(String login);
    List<UserTimeTracker> findByRole(Role role);
    Optional<UserTimeTracker> findByFullName(String fullName);
}
