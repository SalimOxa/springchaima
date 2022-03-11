package com.otdSolution.racineJcc.service;

import com.otdSolution.racineJcc.dao.AppTaskRepository;
import com.otdSolution.racineJcc.dao.CommentTaskRepository;
import com.otdSolution.racineJcc.entities.AppProject;
import com.otdSolution.racineJcc.entities.AppTask;
import com.otdSolution.racineJcc.entities.CommentTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class CommentTaskService {
    @Autowired
    private AppTaskRepository appTaskRepository;
    @Autowired
    private CommentTaskRepository commentTaskRepository;




}
