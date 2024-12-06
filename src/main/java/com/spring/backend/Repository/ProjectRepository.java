package com.spring.backend.Repository;

import com.spring.backend.Model.Project;
import com.spring.backend.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {

    @Query("select p from Project p where p.owner = ?1")
    Optional<List<Project>> findByOwner(User user);

    @Query("select p from Project p where p.name like concat('%', ?1, '%') and ?2 member of p.teams")
    Optional<List<Project>> findByNameIsContainingAndTeamsIsContaining(String partialName, User user);

    @Query("select p from Project p join p.teams t where t=:user")
    Optional<List<Project>> findProjectByTeams(@Param("user") User user);

    @Query("select p from Project p where ?1 member of p.teams or p.owner = ?2")
    Optional<List<Project>> findByTeamsIsContainingOrOwner(User user, User owner);


}
