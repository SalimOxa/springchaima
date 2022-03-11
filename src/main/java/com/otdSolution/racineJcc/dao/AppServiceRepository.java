package com.otdSolution.racineJcc.dao;

import com.otdSolution.racineJcc.entities.AppDefaultService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface AppServiceRepository extends JpaRepository<AppDefaultService,Long>{
}



