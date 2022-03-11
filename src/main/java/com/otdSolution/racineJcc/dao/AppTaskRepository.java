package com.otdSolution.racineJcc.dao;

import com.otdSolution.racineJcc.entities.AppAdmin;
import com.otdSolution.racineJcc.entities.AppMaster;
import com.otdSolution.racineJcc.entities.AppTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface AppTaskRepository extends JpaRepository<AppTask, Integer> {



    @Query(value="SELECT * FROM app_task  WHERE state != 'BLOCKED' and fk_projects_tasks =:idProject ORDER BY  creation_date DESC",nativeQuery = true)
    public List<AppTask> findAllTasksOfProjectNotBlocked(long idProject);

    @Query(value="SELECT * FROM app_task  WHERE fk_projects_tasks =:idProject  ORDER BY  id  DESC",nativeQuery = true)
    public List<AppTask> findAllTasks(long idProject);
}
