package com.otdSolution.racineJcc.dao;

import com.otdSolution.racineJcc.entities.TaskFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface AppTaskFileRepository extends JpaRepository<TaskFile,Long> {
}
