package com.otdSolution.racineJcc.dao;

import com.otdSolution.racineJcc.entities.AppDefaultStatementOfWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface AppDefaultstatementOfWorkRepository extends JpaRepository<AppDefaultStatementOfWork,Long> {
}
