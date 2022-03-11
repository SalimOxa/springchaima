package com.otdSolution.racineJcc.dao;

import com.otdSolution.racineJcc.entities.ConfirmationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ConfirmationCodeRepository extends JpaRepository<ConfirmationCode,Long> {
     public ConfirmationCode findByConfirmationCode(String confirmationCode);

     @Query(value="SELECT * FROM confirmation_code  WHERE user_id = :idUser",nativeQuery = true)
     public ConfirmationCode  findConfirmationCodeBy(@Param("idUser") String idUser);


}