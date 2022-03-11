package com.otdSolution.racineJcc.dao;

import com.otdSolution.racineJcc.entities.DocumentUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface AppDocumentUserRepository extends JpaRepository<DocumentUser,Long> {
}
