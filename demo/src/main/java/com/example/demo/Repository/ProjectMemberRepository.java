package com.example.demo.Repository;

import com.example.demo.Entity.ProjectMember;
import com.example.demo.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember,Long> {
    List<ProjectMember> findByUser(User user);
}
