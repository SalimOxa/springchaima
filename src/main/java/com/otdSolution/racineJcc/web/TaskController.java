package com.otdSolution.racineJcc.web;


import com.otdSolution.racineJcc.dao.*;
import com.otdSolution.racineJcc.entities.*;
import com.otdSolution.racineJcc.service.BacKOfficeService;
import com.otdSolution.racineJcc.service.NotificationService;
import com.otdSolution.racineJcc.service.TaskService;
import com.otdSolution.racineJcc.utils.messageNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.otdSolution.racineJcc.utils.ApplicationConstants.APP_ROOT;



@RestController
@RequestMapping(APP_ROOT)
public class TaskController {
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private AppTaskRepository appTaskRepository;
    @Autowired
    private AppProjectRepository appProjectRepository;
    @Autowired
    private MailContentTaskRepository mailContentTaskRepository;
    @Autowired
    private AppTaskFileRepository appTaskFileRepository;
    @Autowired
    private TaskService taskService;
    @Autowired
    BacKOfficeService backOfficeService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private AppUserRepository<AppAdmin> adminRepository;
    Collection<AppTask> listTasks = new ArrayList<>();




    @PostMapping("/addTaskToProject/{idProject}/{idEmmitor}")
    public ResponseEntity<AppProject> addTaskToProject(@PathVariable("idProject") Long idProject,@PathVariable("idEmmitor") String idEmmitor, @RequestBody AppTask appTask) {
        AppProject project = backOfficeService.addTaskToProject(appTask, idProject,idEmmitor);
        return new ResponseEntity<AppProject>(project, HttpStatus.OK);

    }
    @PutMapping("/enableTaskForUser/{idEmmitor}/{idProject}")
    public ResponseEntity<AppTask> enableTaskForUser(@RequestBody int id,@PathVariable("idEmmitor") String idEmmitor,@PathVariable("idProject") Long idProject) throws InterruptedException {
        AppTask task = taskService.enableTaskForUser(id,idEmmitor,idProject);
        if (task != null) {
            return new ResponseEntity<AppTask>(task, HttpStatus.OK);
        }
        return new ResponseEntity<AppTask>(task, HttpStatus.BAD_REQUEST);
    }


/***/
    @GetMapping("/getAllTaskProject/{idProject}")
    public ResponseEntity<Collection<AppTask>> findAllTasksByProject(@PathVariable("idProject") Long idProject) {
        return ResponseEntity.ok(backOfficeService.findAllTaskByProject(idProject));
    }

    @GetMapping("/getAllTasksIsNotBlockedByProject/{idProject}/{numberTask}")
    public ResponseEntity<Collection<AppTask>> findAllTasksIsNotBlockedByProject(@PathVariable("idProject") Long idProject,@PathVariable("numberTask") int numberTask) {

        return ResponseEntity.ok(backOfficeService.findAllTaskISNotBlockedByProject(idProject,numberTask));
    }





    @DeleteMapping("/deleteTask/{idTask}/{idProject}/{idUser}")
    public ResponseEntity<String> deleteAccountUser(@PathVariable("idTask") int idTask, @PathVariable("idProject") Long idProject, @PathVariable("idUser") String idUser) {
        HashSet<String> listIdReceiver = new HashSet<String>();
        AppUser sender= (AppUser) appUserRepository.findById(idUser).get();
        AppProject project = appProjectRepository.findById(idProject).get();
        if ("ADMIN".equals(sender.getRoles().get(0).getRoleName())){
          AppAdmin admin= (AppAdmin) sender;
          listIdReceiver.add(admin.getIdMaster());
        }else if("MASTER".equals(sender.getRoles().get(0).getRoleName())) {
            listIdReceiver.add(project.getIdAdmin());
        }
        listIdReceiver.add(project.getIdCustomer());

        listTasks = project.getTasks();


        for (Iterator<AppTask> iterator = listTasks.iterator(); iterator.hasNext(); ) {
            AppTask task = iterator.next();

            if (task.getId()==idTask) {
                if(task.isEnableAccount()==true){
                    String msg =sender.getUsername() +" delete task :"+task.getName()+" of "+project.getName();
                    notificationService.sendNotificataionOfDelete(listIdReceiver,sender.getId(),msg,project);
                }

                iterator.remove();
            }
        }
        appProjectRepository.save(project);
        appTaskRepository.deleteById(idTask);
        return ResponseEntity.ok("task successfully deleted");
    }




    @GetMapping("/getAllCommentByTaskProject/{idTask}")
    public ResponseEntity<Collection<CommentTask>> findAllCommenyByTask(@PathVariable("idTask") int idTask) {
        return ResponseEntity.ok(taskService.findAllCommentByTasks(idTask));
    }

    @GetMapping("/getMailContentBytask/{idTask}")
    public ResponseEntity<MailContentTask> getMailContentBytask(@PathVariable("idTask") int idTask) {
        return ResponseEntity.ok(taskService.getMailContentBytask(idTask));
    }





    @GetMapping("downloadFileTask/{fileId}")
    public  ResponseEntity<byte[]> downloadFiles(@PathVariable long fileId, HttpServletResponse reponse) throws IOException {
        TaskFile doc = appTaskFileRepository.findById(fileId).get();
        System.out.println(doc);

        if (doc == null) {
            throw new FileNotFoundException(fileId + " was not found on the server");
        }
        reponse.setContentType(" application/octet-stream ");
        String headerKey = "Content-Disposition";
        String headerValue = "attachemen-filename=" + doc.getFileName();
        reponse.setHeader(headerKey, headerValue);
        return ResponseEntity.ok().body(doc.getData());
    }











    @PostMapping("/addCommentToTask/{idTask}/{idProject}")
    public ResponseEntity<AppTask> addCommentToTask(@PathVariable("idTask") int idTask, @RequestBody CommentTask commentTask, @PathVariable("idProject") Long idProject) {
        AppTask task = taskService.addCommentToTask(commentTask, idTask,idProject);
        return new ResponseEntity<AppTask>(task, HttpStatus.OK);

    }



    @PutMapping("/sendMailToAdminTAsk/{idTask}/{idProject}")
    public ResponseEntity<AppTask> sendMailToAdminTAsk(@PathVariable("idTask") int idTask,@PathVariable("idProject") Long idProject,@RequestBody MailContentTask mail) {
        AppTask task = taskService.sendMailToAdminTAsk(idTask,idProject,mail);
        return new ResponseEntity<AppTask>(task, HttpStatus.OK);

    }


    @PostMapping("/addFileUploadTask/{idTask}/{idProject}")
    public ResponseEntity<TaskFile> addStatementOfWorkToProject(@RequestParam("file") MultipartFile file, @PathVariable("idTask") int idTask,@PathVariable("idProject") Long idProject) throws IOException {
        Collection<String> listIdReceiver = new ArrayList<String>();
        String fileName = file.getOriginalFilename();
        AppTask task = appTaskRepository.findById(idTask).get();
        task.setState("DONE");
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        task.setCloseDate(Calendar.getInstance().getTime());
        TaskFile taskfile= new TaskFile(fileName, file.getBytes());
        appTaskFileRepository.save(taskfile);
        task.setFile(taskfile);
        AppProject appProject = appProjectRepository.findById(idProject).get();
        String idAdmin = appProject.getIdAdmin();
        String idMaster = adminRepository.findById(idAdmin).get().getIdMaster();
        listIdReceiver.add(idMaster);
        listIdReceiver.add(idAdmin);
        notificationService.sendNotificataion(listIdReceiver, appProject.getIdCustomer(), messageNotification.UPLOAD_FILE, appProject);
        notificationService.sendNotificataion(listIdReceiver, appProject.getIdCustomer(), messageNotification.UPLOAD_FILE, appProject);
        appTaskRepository.save(task);
        return new ResponseEntity<TaskFile>(taskfile, HttpStatus.OK);
    }


}