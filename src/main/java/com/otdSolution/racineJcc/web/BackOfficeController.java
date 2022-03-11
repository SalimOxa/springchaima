package com.otdSolution.racineJcc.web;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.otdSolution.racineJcc.dao.*;
import com.otdSolution.racineJcc.dto.Model;
import com.otdSolution.racineJcc.entities.*;
import com.otdSolution.racineJcc.service.AppUserService;
import com.otdSolution.racineJcc.service.BacKOfficeService;
import com.otdSolution.racineJcc.service.WebSocketService;
import com.otdSolution.racineJcc.utils.messageNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.otdSolution.racineJcc.utils.ApplicationConstants.*;
import static com.otdSolution.racineJcc.utils.ApplicationConstants.DIRECTORY_PHOTO_SERVICE;
import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@RestController
@RequestMapping(APP_ROOT)
public class BackOfficeController {
    @Autowired
    private AppServiceRepository appServiceRepository;
    @Autowired
    private AppProjectRepository appProjectRepository;
    @Autowired
    private AppTranchePayementRepository appTranchePayementRepository;
    @Autowired
    private AppTaskRepository appTaskRepository;
    @Autowired
    ServletContext context;
    @Autowired
    AppDefaultServiceRepository appDefaultServiceRepository;
    @Autowired
    BacKOfficeService backOfficeService;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    AppStatementOfWorkRepository appStatementOfWorkRepository;
    @Autowired
    AppPartnerRepository appPartnerRepository;
    @Autowired
    AppUserService appUserService;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private WebSocketService webSocketService;
    //createphoto
    @PostMapping(value = "/SaveService")
    public ResponseEntity<Response> SaveUserProfilServer(@RequestParam("file") MultipartFile file, @RequestParam("service") String service, @RequestParam("fileSOW") MultipartFile fileSOW) throws JsonParseException, JsonMappingException, IOException {
        AppDefaultStatementOfWork appDefaultStatementOfWork = backOfficeService.saveFile(fileSOW);
        String filenameSOW = StringUtils.cleanPath(fileSOW.getOriginalFilename());
        Path fileStorage = get(DIRECTORY_STATEMENT_OF_WORK, filenameSOW).toAbsolutePath().normalize();
        copy(file.getInputStream(), fileStorage, REPLACE_EXISTING);
        AppDefaultService appDefaultService = new ObjectMapper().readValue(service, AppDefaultService.class);
        String modifierfilename = appDefaultService.getPhotoName();
        try {
            Files.write(Paths.get(DIRECTORY_PHOTO_SERVICE + modifierfilename), file.getBytes());

        } catch (Exception e) {
            e.printStackTrace();
        }

        appDefaultService.setPhotoName(modifierfilename);
        appDefaultService.setAppDefaultStatementOfWork(appDefaultStatementOfWork);
        AppDefaultService S = appServiceRepository.save(appDefaultService);

        if (S != null) {

            return new ResponseEntity(new Response(" service is saved successfully"), HttpStatus.OK);
        } else {

            return new ResponseEntity(new Response("service is  not saved"), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("services/all")
    public ResponseEntity<List<AppDefaultService>> findAll() {
        return ResponseEntity.ok(backOfficeService.getDefaultServices());
    }

    @GetMapping(path = "/photoService/{id}")
    public byte[] getPhoto(@PathVariable("id") long id) throws IOException {
        AppDefaultService service = appDefaultServiceRepository.findById(id).get();
        String modifierfilename = service.getPhotoName();
        return Files.readAllBytes(Paths.get(DIRECTORY_PHOTO_SERVICE + modifierfilename));
    }

    @GetMapping(path = "/getService/{id}")
    public ResponseEntity<AppDefaultService> getService(@PathVariable("id") long id) throws IOException {
        AppDefaultService service = appDefaultServiceRepository.findById(id).get();
        return ResponseEntity.ok(appServiceRepository.getById(id));
    }

    @DeleteMapping("/delete-service/{idService}")
    public ResponseEntity<String> deleteService(@PathVariable("idService") long idService) {

        backOfficeService.deleteService(idService);


        return ResponseEntity.ok("service successfully deleted");

    }

    @GetMapping(path = "/statementOfWorkService/{id}")
    public ResponseEntity<byte[]> getStatementOfWork(@PathVariable("id") long id) throws IOException {
        AppDefaultStatementOfWork StatementOfWork = appDefaultServiceRepository.findById(id).get().getAppDefaultStatementOfWork();



        return ResponseEntity.ok(StatementOfWork.getData());
    }


    @GetMapping("downloadFile/{fileId}")
    public ResponseEntity<byte[]> downloadFiles(@PathVariable long fileId, HttpServletResponse reponse) throws IOException {
        AppDefaultStatementOfWork doc = appDefaultServiceRepository.findById(fileId).get().getAppDefaultStatementOfWork();


        if (doc == null) {
            throw new FileNotFoundException(fileId + " was not found on the server");
        }
        reponse.setContentType(" application/octet-stream ");
        String headerKey = "Content-Disposition";
        String headerValue = "attachemen-filename=" + doc.getFileName();
        reponse.setHeader(headerKey, headerValue);
        ServletOutputStream servletOutputStream = reponse.getOutputStream();
        servletOutputStream.write(doc.getData());

        servletOutputStream.close();

        return ResponseEntity.ok().body(doc.getData());
    }


    @GetMapping("/projects-Without-admin")
    public ResponseEntity<List<AppProject>> projectsWithoutAdmin() {
        return ResponseEntity.ok(appProjectRepository.findProjectsWithoutAdmin());
    }


    @DeleteMapping("/delete-project-from-liste/{idProject}/{idUser}")
    public ResponseEntity<String> deleteProjectFromList(@PathVariable("idProject") long idProject, @PathVariable("idUser") String idUser) {

        backOfficeService.deleteProjectFromListUser(idProject, idUser);


        return ResponseEntity.ok("project successfully deleted");

    }

    @DeleteMapping("/delete-project/{idProject}/{idUser}")
    public ResponseEntity<String> deleteProject(@PathVariable("idProject") long idProject, @PathVariable("idUser") String idUser) {

        backOfficeService.deleteProject(idProject, idUser);

        return ResponseEntity.ok("project successfully deleted");

    }

    @PostMapping("/addStatementOfWork/{id}")
    public ResponseEntity<AppStatementOfWork> addStatementOfWorkToProject(@RequestParam("file") MultipartFile file, @PathVariable("id") long id) throws IOException {
        String fileName = file.getOriginalFilename();
        AppProject project = appProjectRepository.findById(id).get();
        AppStatementOfWork doc = new AppStatementOfWork(fileName, file.getBytes());
        appStatementOfWorkRepository.save(doc);
        String filenameSOW = StringUtils.cleanPath(file.getOriginalFilename());
        Path fileStorage = get(DIRECTORY_STATEMENT_OF_WORK_Of_Project, filenameSOW).toAbsolutePath().normalize();
        copy(file.getInputStream(), fileStorage, REPLACE_EXISTING);
        project.setAppStatementOfWork(doc);
        appProjectRepository.save(project);

        return new ResponseEntity<AppStatementOfWork>(doc, HttpStatus.OK);
    }


    @GetMapping("downloadFileOfProject/{fileId}")
    public ResponseEntity<byte[]> getFileOfProject(@PathVariable long fileId) throws IOException {
        AppProject project = appProjectRepository.findById(fileId).get();
        AppStatementOfWork doc = project.getAppStatementOfWork();


        if (doc == null) {
            throw new FileNotFoundException(fileId + " was not found on the server");
        }

        System.out.println(doc + "is document");
        return ResponseEntity.ok().body(doc.getData());
    }


    @GetMapping("/project/{idProject}")
    public ResponseEntity<AppProject> findProjectById(@PathVariable("idProject") Long idProject) {
        AppProject project = appProjectRepository.findById(idProject).get();
        if (project == null) {

            return new ResponseEntity<AppProject>(project, HttpStatus.BAD_REQUEST);
        } else {
            return ResponseEntity.ok(appProjectRepository.findById(idProject).get());

        }
    }


    @PostMapping("/addTrancheToPayForProject/{idProject}/{idUser}")
    public ResponseEntity<AppProject> addTrancheToPayForProject(@PathVariable("idProject") Long idProject, @RequestBody TranchePayment tranche, @PathVariable("idUser") String idUser) {
        AppTask task = new AppTask();
        task.setName("Payement Tranche");
        task.setState("IN_PROGRESS");
        task.setType("payTranche");
        task.setCreationDate(new Date());
        task.setEnableAccount(true);
        appTaskRepository.save(task);

        HashSet<String> listIdReceiverSuccessUpdate = new HashSet<String>();

        AppUser user = (AppUser) appUserRepository.findById(idUser).get();
        AppProject project =  appProjectRepository.findById(idProject).get();
        listIdReceiverSuccessUpdate.add(project.getIdCustomer());
        String msg =  user.getUsername() + " " + "add installment "+ tranche.getName()+ " to project "+project.getName();

        if(user.getRoles().get(0).getRoleName().equals("ADMIN")){
            AppAdmin admin = (AppAdmin) user;

            listIdReceiverSuccessUpdate.add(admin.getIdMaster());
        }else if(user.getRoles().get(0).getRoleName().equals("MASTER")) {
            listIdReceiverSuccessUpdate.add(project.getIdAdmin());

        }

        if (project != null) {
            tranche.setIdProject(idProject);
            tranche.setPaymentDate(new Date());
            tranche.setNumTranche(project.getTranchePayment().size() + 1);
            project.getTranchePayment().add(tranche);

            listIdReceiverSuccessUpdate.forEach(idReceiver -> {
                AppUser receiver = (AppUser) appUserRepository.findById(idReceiver).get();
                NotficationUser notfication = new NotficationUser(null, user.getUsername(), msg, new Date(), messageNotification.TYPE_CREATE_PROJECT, true, project.getId(), null, null, null, true, user.getId(),null, null);
                notificationRepository.save(notfication);
                receiver.getNotfications().add(notfication);
                appUserRepository.save(receiver);
                webSocketService.sendMessage(idReceiver,"payment_type");
            });

           tranche.setIdtask(task.getId());
            task.setTranchePayment(tranche);
            task.setDescription("Please  pay your tranche Number " +tranche.getNumTranche());
            project.getTasks().add(task);

            appProjectRepository.save(project);

            return ResponseEntity.ok(project);
        }

        return new ResponseEntity<AppProject>(project, HttpStatus.BAD_REQUEST);

    }

    @GetMapping("/tranche/{idTranche}")
    public ResponseEntity<TranchePayment> findTrancheById(@PathVariable("idTranche") Integer idTranche) {
        TranchePayment tranche = appTranchePayementRepository.findById(idTranche).get();
        if (tranche == null) {
            return new ResponseEntity<TranchePayment>(tranche, HttpStatus.BAD_REQUEST);
        } else {
            return ResponseEntity.ok(tranche);

        }
    }


    @PostMapping("/payTrancheProjectProject/{idTranche}/{idTask}/{idUser}")
    public ResponseEntity<TranchePayment> payTrancheProject(@PathVariable("idTranche") Integer idTranche,@PathVariable("idTask") Integer idTask,@PathVariable("idUser") String idUser) {
        AppUser user = (AppUser) appUserRepository.findById(idUser).get();
        HashSet<String> listIdReceiver = new HashSet<String>();
        TranchePayment tranche = appTranchePayementRepository.findById(idTranche).get();
        AppProject project =  appProjectRepository.findById(tranche.getIdProject()).get();
        AppAdmin admin = (AppAdmin) appUserRepository.findById(project.getIdAdmin()).get();
        if ((user.getRoles().get(0).getRoleName()).equals("CUSTOMER")){
            listIdReceiver.add(admin.getIdMaster());
            listIdReceiver.add(admin.getId());

        }else if((user.getRoles().get(0).getRoleName()).equals("MASTER")){
         listIdReceiver.add(project.getIdCustomer())  ;
         listIdReceiver.add(admin.getId());

        }else if((user.getRoles().get(0).getRoleName()).equals("ADMIN")){
            listIdReceiver.add(project.getIdCustomer())  ;
            listIdReceiver.add(admin.getIdMaster());

        }

        String msg =  user.getUsername() + " " + "was paid the installment "+ tranche.getName()+ " to project "+project.getName();


        if (tranche != null) {
            tranche.setEtatPayement(true);
            tranche.setPaymentDate(new Date());
            appTranchePayementRepository.save(tranche);
            project.setRestPayment(project.getRestPayment() - tranche.getPrice());
            if(project.getRestPayment()<=0){
                project.setStatus("Paid");
            }
            appProjectRepository.save(project);

            AppTask task =  appTaskRepository.findById(idTask).get();
            task.setState("DONE");
            task.setCloseDate(Calendar.getInstance().getTime());


            appTaskRepository.save(task);
            listIdReceiver.forEach(idReceiver -> {
                AppUser receiver = (AppUser) appUserRepository.findById(idReceiver).get();
                NotficationUser notfication = new NotficationUser(null, user.getUsername(), msg, new Date(), messageNotification.TYPE_CREATE_PROJECT, true, project.getId(), null, null, null, true,null, user.getId(), null);
                notificationRepository.save(notfication);
                receiver.getNotfications().add(notfication);
                appUserRepository.save(receiver);
                webSocketService.sendMessage(idReceiver,"payment_type");
            });

            return ResponseEntity.ok(tranche);
        }
        return new ResponseEntity<TranchePayment>(tranche, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/addStatementOfWorkToTranche/{idTranche}")
    public ResponseEntity<AppStatementOfWork> addStatementOfWorkToTranche(@RequestParam("file") MultipartFile file, @PathVariable("idTranche") Integer idTranche) throws IOException {

        String fileName = file.getOriginalFilename();
        TranchePayment tranchePayment = appTranchePayementRepository.findById(idTranche).get();
        AppStatementOfWork doc = new AppStatementOfWork(fileName, file.getBytes());
        appStatementOfWorkRepository.save(doc);
        String filenameSOW = StringUtils.cleanPath(file.getOriginalFilename());
        Path fileStorage = get(DIRECTORY_STATEMENT_OF_WORK_Of_Project, filenameSOW).toAbsolutePath().normalize();
        copy(file.getInputStream(), fileStorage, REPLACE_EXISTING);
        tranchePayment.setAppStatementOfWork(doc);
        appTranchePayementRepository.save(tranchePayment);

        return new ResponseEntity<AppStatementOfWork>(doc, HttpStatus.OK);
    }

    @GetMapping("downloadFileOfTranchePayment/{idTranche}")
    public ResponseEntity<byte[]> getFileOfTranche(@PathVariable Integer idTranche) throws IOException {
        TranchePayment tranchePayment = appTranchePayementRepository.findById(idTranche).get();
        AppStatementOfWork doc = tranchePayment.getAppStatementOfWork();

        if (doc == null) {
            throw new FileNotFoundException(idTranche + " was not found on the server");
        }
        return ResponseEntity.ok().body(doc.getData());
    }


    @PutMapping("/addPriceProject/{idProject}/{price}/{idUser}")
    public ResponseEntity<AppProject> addPriceToProject(@PathVariable("idProject") Long idProject, @PathVariable("price") long price , @PathVariable("idUser") String idUser) {
        AppProject project = (AppProject) appProjectRepository.findById(idProject).get();


        HashSet<String> listIdReceiver= new HashSet<String>();
        AppUser user = (AppUser) appUserRepository.findById(idUser).get();
        listIdReceiver.add(project.getIdCustomer());
        String msg =  user.getUsername() + " " + " the update was done to the price "+  " of project "+project.getName();

        if(user.getRoles().get(0).getRoleName().equals("ADMIN")){
            AppAdmin admin = (AppAdmin) user;

            listIdReceiver.add(admin.getIdMaster());
        }else if(user.getRoles().get(0).getRoleName().equals("MASTER")) {
            listIdReceiver.add(project.getIdAdmin());

        }

        if (project != null) {
           if(project.getTotalPayment() == null) {
               project.setTotalPayment(price);
               project.setRestPayment(price);
           }else if (  price < project.getTotalPayment() ) {

    if(project.getTranchePayment().isEmpty()) {
    project.setTotalPayment(price);
    project.setRestPayment(price);

}
            } else {

                long restPayment = project.getRestPayment() + (price - project.getTotalPayment());

                project.setRestPayment(restPayment);

                project.setTotalPayment(price);
                project.setStatus("Unpaid");
            }
            appProjectRepository.save(project);
            listIdReceiver.forEach(idReceiver -> {
                AppUser receiver = (AppUser) appUserRepository.findById(idReceiver).get();
                NotficationUser notfication = new NotficationUser(null, user.getUsername(), msg, new Date(), messageNotification.TYPE_CREATE_PROJECT, true, project.getId(), null, null, null, true, null,user.getId(), null);
                notificationRepository.save(notfication);
                receiver.getNotfications().add(notfication);
                webSocketService.sendMessage(idReceiver,"payment_type");
                appUserRepository.save(receiver);
            });

            return ResponseEntity.ok(project);
        }
        return new ResponseEntity<AppProject>(project, HttpStatus.BAD_REQUEST);
    }
    @GetMapping("/all-projects-by-master/{idMaster}")
    public ResponseEntity<List<AppProject>> projectsByMaster( @PathVariable("idMaster") String idMaster) {
        return ResponseEntity.ok(appProjectRepository.findProjectsByMaster(idMaster));
    }

    @PostMapping("/savePartner")
    public ResponseEntity<Response>  savePartner(@RequestParam("file")MultipartFile file,@RequestParam("partner") String partner) throws IOException {
        AppPartner appPartner= new ObjectMapper().readValue(partner,AppPartner.class);
        appPartner.setData(file.getBytes());
        AppPartner P=appPartnerRepository.save(appPartner);

        if(P!=null){
            return new ResponseEntity(new Response(" Partner is saved successfully"),HttpStatus.OK);
        } else
            return new ResponseEntity(new Response(" Partner is not saved "),HttpStatus.BAD_REQUEST);

    }
    @GetMapping("/getAllPartners")
    public ResponseEntity<List<AppPartner>> getAllPartners() {
        return ResponseEntity.ok(backOfficeService.getAppPartner());
    }

    @DeleteMapping("/deletePartner/{currentPartner}")
    public ResponseEntity<String> deletePartner(@PathVariable("currentPartner") long currentPartner) {
        //AppPartner appPartner= appPartnerRepository.findById(currentPartner).get();
        appPartnerRepository.deleteById(currentPartner);
        return ResponseEntity.ok("project successfully deleted");
    }






    @GetMapping("/all-projects-master-by-page/{idMaster}")
    public ResponseEntity<Model> findALLProjectByMaster(@PathVariable("idMaster") String idMaster,
                                                        @RequestParam(value = "numberPage",defaultValue = "0") String numberPage,
                                                        @RequestParam (value = "sizePage",defaultValue = "3") String sizePage,
                                                        @RequestParam (value = "nameProject",defaultValue = "") String nameProject,
                                                        @RequestParam (value = "nameCustomer",defaultValue = "") String nameCustomer,
                                                        @RequestParam (value = "nameAdmin",defaultValue = "") String nameAdmin){
        int number = Integer.parseInt(numberPage);
        int size=Integer.parseInt(sizePage);
       Page<AppProject> projects = appProjectRepository.findProjectsByMasterPagebleWithFilter(idMaster,nameProject,nameCustomer,nameAdmin, PageRequest.of(number,size, Sort.by("id").descending()));
        Model model= new Model();
       model.setProject(projects.getContent());
       model.setTotalPage(projects.getTotalPages());
        return new ResponseEntity<Model>(model, HttpStatus.OK);
    }
    @GetMapping("/all-projects-admin-by-page/{idAdmin}")
    public ResponseEntity<Model> findALLProjectByAdmin(@PathVariable("idAdmin") String idAdmin,
                                                       @RequestParam(value = "numberPage",defaultValue = "0") String numberPage,
                                                       @RequestParam (value = "sizePage",defaultValue = "3") String sizePage,
                                                       @RequestParam (value = "nameProject",defaultValue = "") String nameProject,
                                                       @RequestParam (value = "nameCustomer",defaultValue = "") String nameCustomer) {
        int number = Integer.parseInt(numberPage);
        int size=Integer.parseInt(sizePage);
        Page<AppProject> projects = appProjectRepository.findProjectsByAdminPagebleWithFilter(idAdmin,nameProject,nameCustomer, PageRequest.of(number,size,Sort.by("id").descending()));
        Model model= new Model();
        model.setProject(projects.getContent());
        model.setTotalPage(projects.getTotalPages());
        return new ResponseEntity<Model>(model, HttpStatus.OK);
    }



    @GetMapping("/all-projects-by-customer/{customer}")
    public ResponseEntity<List<AppProject>> findALLProjectByCustomer(@PathVariable("customer") String customer) {

        List<AppProject> projects = appProjectRepository.findProjectsByCustommer(customer);
        return new ResponseEntity<List<AppProject>>(projects, HttpStatus.OK);
    }
    @GetMapping("/all-tranche-by-project/{idProject}")
    public ResponseEntity<List<TranchePayment>> findALLTranchePayementByProject(@PathVariable("idProject") Long idProject) {
        List<TranchePayment> listTranchesPayments;
        AppProject project = appProjectRepository.findById(idProject).get();
        listTranchesPayments= (List<TranchePayment>) project.getTranchePayment();
        return new ResponseEntity<List<TranchePayment>>(listTranchesPayments, HttpStatus.OK);
    }


}
