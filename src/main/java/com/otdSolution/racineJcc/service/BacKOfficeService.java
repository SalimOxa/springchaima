package com.otdSolution.racineJcc.service;

import com.otdSolution.racineJcc.entities.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

public interface BacKOfficeService {
    public AppDefaultStatementOfWork saveFile(MultipartFile file);
    public AppDefaultStatementOfWork  getFile(Long fileId);
    public List<AppDefaultStatementOfWork> getFiles();
    public List<AppDefaultService> getDefaultServices();
    public String deleteProjectFromListUser(long idProject, String idUser);
    public  void deleteProject( long idproject, String idUser);
    AppProject addTaskToProject(AppTask appTask, Long idProject,String idEmmitor);
    public Collection<AppTask> findAllTaskByProject(Long idProject);
    public Collection<AppTask> findAllTaskISNotBlockedByProject(Long idProject,int numberTask);
    public  void deleteService( long idService);
    public List<AppPartner> getAppPartner();
}

