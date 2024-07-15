package com.timetracker.repository;

import com.timetracker.model.Users;
import com.timetracker.model.enums.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByLogin(String login);
    List<Users> findByRole(Roles role);
}
