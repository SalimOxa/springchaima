package com.otdSolution.racineJcc.dao;

import com.otdSolution.racineJcc.entities.AppProject;
import com.otdSolution.racineJcc.entities.AppTask;
import com.otdSolution.racineJcc.entities.TranchePayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface AppTranchePayementRepository extends JpaRepository<TranchePayment, Integer> {





}
