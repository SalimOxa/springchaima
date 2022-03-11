package com.otdSolution.racineJcc.dao;

import com.otdSolution.racineJcc.entities.CommentTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CommentTaskRepository extends JpaRepository<CommentTask,String> {
}
