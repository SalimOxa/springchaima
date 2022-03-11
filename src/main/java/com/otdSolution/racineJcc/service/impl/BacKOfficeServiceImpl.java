package com.otdSolution.racineJcc.service.impl;

import com.otdSolution.racineJcc.dao.*;
import com.otdSolution.racineJcc.entities.*;
import com.otdSolution.racineJcc.service.AccountService;
import com.otdSolution.racineJcc.service.BacKOfficeService;
import com.otdSolution.racineJcc.service.NotificationService;
import com.otdSolution.racineJcc.utils.messageNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class BacKOfficeServiceImpl implements BacKOfficeService {
    @Autowired
    private AppProjectRepository appProjectRepository;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AppUserRepository<AppCustomer> customerRepository;
    @Autowired
    private AppUserRepository<AppMaster> masterRepository;
    @Autowired
    private AppUserRepository<AppAdmin> adminRepository;
    @Autowired
    private AppDefaultstatementOfWorkRepository appDefaultstatementOfWorkRepository;
    @Autowired
    private AppDefaultServiceRepository appDefaultServiceRepository;
    @Autowired
    private AppTaskRepository appTaskRepository;
    @Autowired
    private NotificationService notificationService;
    Collection<AppProject> listAppProjectsAdmin = new ArrayList<AppProject>();
    Collection<AppAdmin> listAppAdmin = new ArrayList<AppAdmin>();
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired AppPartnerRepository appPartnerRepository;
    @Override
    public AppDefaultStatementOfWork saveFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        try {

            AppDefaultStatementOfWork doc = new AppDefaultStatementOfWork(fileName, file.getBytes());

            return appDefaultstatementOfWorkRepository.save(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public AppDefaultStatementOfWork getFile(Long fileId) {
        return appDefaultstatementOfWorkRepository.findById(fileId).get();
    }

    @Override
    public List<AppDefaultStatementOfWork> getFiles() {
        return appDefaultstatementOfWorkRepository.findAll();
    }

    @Override
    public List<AppDefaultService> getDefaultServices() {
        return appDefaultServiceRepository.findAll();
    }

    @Override
    public String deleteProjectFromListUser(long idProject, String idUser) {
        AppUser appUser = (AppUser) appUserRepository.findById(idUser).get();
        AppProject project = appProjectRepository.findById(idProject).get();
        HashSet<String> listIdReceiver = new HashSet<String>();
        listIdReceiver.add(project.getIdCustomer());

        if (appUser.getRoles().get(0).getRoleName().equals("MASTER")) {
            AppMaster master = (AppMaster) appUser;
            listIdReceiver.add(project.getIdAdmin());


                String msg= project.getNameAdmin()+" " +" has pulled out from a project" +"" +project.getName()+" of "+project.getNameCustomer()+" with "+ master.getUsername();
                notificationService.sendNotificataionOfDelete(listIdReceiver ,master.getId(),msg,project);
               AppAdmin appAdmin = ( AppAdmin) appUserRepository.findById(project.getIdAdmin()).get();
                deleteProjectOfAdmin(appAdmin,idProject);

            }else if (appUser.getRoles().get(0).getRoleName().equals("ADMIN")){
            AppAdmin appAdmin = (AppAdmin) appUser;
            deleteProjectOfAdmin(appAdmin,idProject);
            listIdReceiver.add(appAdmin.getIdMaster());
            String msg= appAdmin.getUsername()+" " +" has pulled out from a project"
                   +"" +project.getName()+" of "+project.getNameCustomer();
            notificationService. sendNotificataionOfDelete(listIdReceiver,appAdmin.getId(),msg,project);
        }

        return "delete";
    }

    private void deleteProjectOfAdmin(AppAdmin appAdmin, long idProject) {
        AppProject appProject = appProjectRepository.findById(idProject).get();
        listAppProjectsAdmin = appAdmin.getProjects();


        listAppProjectsAdmin.removeIf(item ->


                item.getId().equals(idProject)


        );
        appProject.setIdAdmin(null);
        appProject.setNameAdmin(null);
        appProjectRepository.save(appProject);
        adminRepository.save(appAdmin);
    }
    @Override
    public  void deleteProject( long idProject , String idUser){
        HashSet<String> listIdReceiver = new HashSet<String>();
        AppProject project =appProjectRepository.findById(idProject).get();



        String msg =messageNotification.DELETE_PROJECT +"" +project.getName() +" of "+ project.getNameCustomer();
        listIdReceiver.add(project.getIdCustomer());
        if(project.getIdAdmin()==null){
        masterRepository.findAllMasters().forEach(master->{
            if(!master.getId() .equals(idUser)){
                listIdReceiver.add(master.getId());
            }
        });}else {
            listIdReceiver.add(project.getIdAdmin());
        }
       appProjectRepository.delete(project);

        notificationService.sendNotificataionOfDelete(listIdReceiver,idUser,msg,project);



    }


    @Override
    public AppProject addTaskToProject(AppTask appTask, Long idProject,String idEmmitor) {
        Collection<String> listIdReceiver = new ArrayList<String>();
        AppProject appProject = appProjectRepository.findById(idProject).get();
        appTask.setCreationDate(Calendar.getInstance().getTime());
        appProject.getTasks().add(appTask);
        appProjectRepository.save(appProject);
    AppUser user = (AppUser) appUserRepository.findById(idEmmitor).get();
    if(user.getRoles().get(0).getRoleName().equals("MASTER")){

        listIdReceiver.add(appProject.getIdAdmin());

        notificationService.sendNotificataion(listIdReceiver,idEmmitor, messageNotification.CREATE_TASK_BY_MASTER,appProject);
    }else if(user.getRoles().get(0).getRoleName().equals("ADMIN")){

        listIdReceiver.add(appProject.getIdMaster());
        notificationService.sendNotificataion(listIdReceiver,idEmmitor, messageNotification.CREATE_TASK_BY_ADMIN,appProject);
    }

        return appProject;
    }



    @Override
    public Collection<AppTask> findAllTaskByProject(Long idProject) {


        Collection<AppTask> listTask = appTaskRepository.findAllTasks(idProject);
        return listTask;
    }
    @Override
    public Collection<AppTask> findAllTaskISNotBlockedByProject(Long idProject,int numberTask) {
        List<AppTask> SublistTask= new ArrayList<AppTask>();
        List<AppTask> listTask = new ArrayList<AppTask>();
        listTask=appTaskRepository.findAllTasksOfProjectNotBlocked(idProject);

        if(numberTask>listTask.size()){
            numberTask=listTask.size();
        }

        SublistTask= listTask.subList(0,numberTask);


        return   SublistTask;
    }
    @Override
    public  void deleteService( long idService){
        AppDefaultService service =appDefaultServiceRepository.findById(idService).get();
        appDefaultServiceRepository.delete(service );
        appDefaultstatementOfWorkRepository.deleteById(service.getAppDefaultStatementOfWork().getId());
    }
    public List<AppPartner> getAppPartner(){
        return appPartnerRepository.findAll();
    }





}