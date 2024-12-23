package com.spring.backend.Repository;

import com.spring.backend.Model.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface InvitationRepository extends JpaRepository<Invitation,Long> {


    @Query("select p from Invitation p where  p.token=?1")
    Optional<Invitation> findByToken(String token);

    @Query("select p from Invitation p where  p.email=?1")
    Optional<Invitation> findByEmail(String email);


}
