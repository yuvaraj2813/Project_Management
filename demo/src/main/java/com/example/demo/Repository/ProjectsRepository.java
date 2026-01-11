package com.example.demo.Repository;

import com.example.demo.Entity.Projects;
import com.example.demo.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectsRepository extends JpaRepository<Projects,Long> {
    List<Projects> findByCreatedBy(User user);
    boolean existsByname(String project_name);
}
