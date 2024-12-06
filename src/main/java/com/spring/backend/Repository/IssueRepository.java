package com.spring.backend.Repository;

import com.spring.backend.Model.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface IssueRepository extends JpaRepository<Issue,Long> {

    @Query("select i from Issue i where i.projectID = ?1")
    Optional<List<Issue>> findByProjectId(Long projectId);




}
