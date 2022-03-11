package com.otdSolution.racineJcc.service;

import com.otdSolution.racineJcc.dao.*;
import com.otdSolution.racineJcc.entities.*;
import com.otdSolution.racineJcc.utils.messageNotification;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TaskService {
    @Autowired
    private AppTaskRepository appTaskRepository;
    @Autowired
    private CommentTaskRepository commentTaskRepository;
    @Autowired
    private MailContentTaskRepository mailContentTaskRepository;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private AppProjectRepository appProjectRepository;
    @Autowired
    private AppUserRepository<AppCustomer> customerRepository;
    @Autowired
    private AppUserRepository<AppMaster> masterRepository;
    @Autowired
    private AppUserRepository<AppAdmin> adminRepository;
    @Autowired
    private NotificationService notificationService;

    public AppTask enableTaskForUser(int id, String idEmmitor, long idProject) throws InterruptedException {
        HashSet<String> listIdReceiver = new HashSet<String>();


        AppTask task = appTaskRepository.findById(id).get();
     
        if (task != null) {
            task.setEnableAccount(true);
            task.setCreationDate(new Date());
            task.setState("IN_PROGRESS");
            appTaskRepository.save(task);
            AppProject appProject = appProjectRepository.findById(idProject).get();
            AppUser user = (AppUser) appUserRepository.findById(idEmmitor).get();
            listIdReceiver.add(appProject.getIdCustomer());
            if (user.getRoles().get(0).getRoleName().equals("MASTER")) {
                listIdReceiver.add(appProject.getIdAdmin());

            } else if (user.getRoles().get(0).getRoleName().equals("ADMIN")) {
                listIdReceiver.add(appProject.getIdMaster());
            }
            notificationService.sendNotificataion(listIdReceiver, idEmmitor, messageNotification.ENABLE_TASK_FOR_CUSTOMER, appProject);
        }
        return task;

    }


    public Collection<CommentTask> findAllCommentByTasks(int idTask) {
        AppTask appTask = appTaskRepository.findById(idTask).get();
        Collection<CommentTask> listComment = appTask.getComments();
        return listComment;
    }


    public AppTask addCommentToTask(CommentTask commentTask, int idTask, long idProject) {
        HashSet<String> listIdReceiver = new HashSet<String>();
        AppTask appTask = appTaskRepository.findById(idTask).get();

        commentTask.setCreationDate( Calendar.getInstance().getTime());
        commentTaskRepository.save(commentTask);
        appTask.getComments().add(commentTask);
        appTask.setEnableAccount(true);
        appTask.setState("DONE");
        appTask.setCloseDate(Calendar.getInstance().getTime());
        AppProject appProject = appProjectRepository.findById(idProject).get();
        String idAdmin = appProject.getIdAdmin();
        String idMaster = adminRepository.findById(idAdmin).get().getIdMaster();
        listIdReceiver.add(idMaster);
        listIdReceiver.add(idAdmin);
        notificationService.sendNotificataion(listIdReceiver, appProject.getIdCustomer(), messageNotification.ANSWER_COMMENT, appProject);
        appTaskRepository.save(appTask);;


        return appTask;
    }

    public AppTask sendMailToAdminTAsk(int idTask,long idProject, MailContentTask mail) {
        HashSet<String> listIdReceiver = new HashSet<String>();
        AppTask appTask = appTaskRepository.findById(idTask).get();
        appTask.setState("DONE");
        appTask.setCloseDate(Calendar.getInstance().getTime());
        mail.setCreationDate(  Calendar.getInstance().getTime());
        mailContentTaskRepository.save(mail);
        appTask.setMail(mail);
        AppProject appProject = appProjectRepository.findById(idProject).get();
        String idAdmin = appProject.getIdAdmin();
        String idMaster = adminRepository.findById(idAdmin).get().getIdMaster();
        listIdReceiver.add(idMaster);
        listIdReceiver.add(idAdmin);
        notificationService.sendNotificataion(listIdReceiver, appProject.getIdCustomer(), messageNotification.SEND_MAIL, appProject);
        appTaskRepository.save(appTask);;
        return appTask;
    }

/*

    public TaskFile uploadFile(MultipartFile file, long idProject, String idTask){
        String fileName = file.getOriginalFilename();
        AppTask task = appTaskRepository.findById(idTask).get();
        task.setState("DONE");
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        task.setCloseDate(dateFormat.format(date));
        TaskFile taskfile= new TaskFile(fileName, file.getBytes());
        appTaskFileRepository.save(taskfile);
        task.setFile(taskfile);
        appTaskRepository.save(task);




        return null;

    }*/

    public MailContentTask getMailContentBytask(int idTask) {
        AppTask appTask = appTaskRepository.findById(idTask).get();
        MailContentTask mail = appTask.getMail();
        return mail;
    }


}
