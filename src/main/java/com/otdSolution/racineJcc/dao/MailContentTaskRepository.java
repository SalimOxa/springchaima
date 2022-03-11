package com.otdSolution.racineJcc.dao;

import com.otdSolution.racineJcc.entities.MailContentTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface MailContentTaskRepository extends JpaRepository<MailContentTask,String> {
}
