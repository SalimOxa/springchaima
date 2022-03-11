package com.otdSolution.racineJcc.dao;


import com.otdSolution.racineJcc.entities.AppStatementOfWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface AppStatementOfWorkRepository extends JpaRepository<AppStatementOfWork,Long> {
}
