package com.otdSolution.racineJcc.web;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.otdSolution.racineJcc.dao.AppDocumentUserRepository;
import com.otdSolution.racineJcc.dao.AppProjectRepository;
import com.otdSolution.racineJcc.dao.AppUserRepository;
import com.otdSolution.racineJcc.dao.MessageChatRepository;
import com.otdSolution.racineJcc.dto.Contact;

import com.otdSolution.racineJcc.dto.ModelMessage;
import com.otdSolution.racineJcc.entities.*;
import com.otdSolution.racineJcc.service.AppUserService;
import com.otdSolution.racineJcc.service.MailService;
import com.otdSolution.racineJcc.service.NotificationService;
import com.otdSolution.racineJcc.service.WebSocketService;
import com.otdSolution.racineJcc.utils.messageNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.otdSolution.racineJcc.utils.ApplicationConstants.*;


@RestController
@RequestMapping(APP_ROOT)
public class AppUserController {
    @Autowired
    AppDocumentUserRepository documentUserRepository;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private AppUserService userService;
    @Autowired
    private MailService mailService;
    @Autowired
    private AppUserRepository<AppCustomer> customerRepository;
    @Autowired
    private AppUserRepository<AppMaster> masterRepository;
    @Autowired
    private AppUserRepository<AppAdmin> adminRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private WebSocketService webSocketService;
    @Autowired
    private AppProjectRepository appProjectRepository;

    @Autowired
    MessageChatRepository messageChatRepository;
    @PostMapping("/new-user/{idUser}")
    public ResponseEntity<AppUser> createAdminToMaster(@RequestBody AppCustomer user, @PathVariable("idUser") String idUser) {
        AppUser appUser = userService. createUserAccordingToRole(user, idUser);
        if (appUser != null) {
            return new ResponseEntity<AppUser>(appUser, HttpStatus.OK);
        }
        return new ResponseEntity<AppUser>(appUser, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/all-users/{idUser}")
    public ResponseEntity<?> getAllUsers(@PathVariable("idUser") String idUser) {
        Collection<AppCustomer> customers = new HashSet<AppCustomer>();

        AppUser appUser = (AppUser) appUserRepository.findById(idUser).get();

        if (appUser.getRoles().get(0).getRoleName().equals("MASTER")) {
            AppMaster master = (AppMaster) appUser;
            List<AppAdmin> admins = (List<AppAdmin>) master.getAppAdmins();
            return new ResponseEntity<List<AppAdmin>>(admins, HttpStatus.OK);
        } else if (appUser.getRoles().get(0).getRoleName().equals("ADMIN")) {
            AppAdmin admin = (AppAdmin) appUser;
            for (AppProject project : admin.getProjects()) {
                AppCustomer customer = customerRepository.findById(project.getIdCustomer()).get();
                customers.add(customer);
                if (project.getAppCustomer() == null) {
                    customers.removeIf(appCustomer -> appCustomer == null);
                }
            }


            return new ResponseEntity<Collection<AppCustomer>>(customers, HttpStatus.OK);
        } else

            return new ResponseEntity<Error>(HttpStatus.BAD_REQUEST);
    }
/**/
    @GetMapping("/user/{idUser}")
    public ResponseEntity<?> findById(@PathVariable("idUser") String idUser) {

        AppUser appUser = (AppUser) appUserRepository.findById(idUser).get();
        if (appUser.getRoles().get(0).getRoleName().equals("MASTER")) {
            AppMaster master = (AppMaster) appUser;
            if (appUser != null) {
                return new ResponseEntity<AppMaster>(master, HttpStatus.OK);
            }
        } else if (appUser.getRoles().get(0).getRoleName().equals("ADMIN")) {
            AppAdmin admin = (AppAdmin) appUser;
            if (appUser != null) {
                return new ResponseEntity<AppAdmin>(admin, HttpStatus.OK);
            }
        } else {
            AppCustomer customer = (AppCustomer) appUser;
            if (appUser != null) {
                return new ResponseEntity< AppCustomer>(customer, HttpStatus.OK);
            }
        }
        return new ResponseEntity<AppUser>(appUser, HttpStatus.BAD_REQUEST);
    }
// a effacer
    @GetMapping("/all-admins/master/{idMaster}")
    public ResponseEntity<List<AppAdmin>> findAdmins(@PathVariable("idMaster") String idMaster) {
        AppMaster master = (AppMaster) appUserRepository.findById(idMaster).get();
        List<AppAdmin> admins = (List<AppAdmin>) master.getAppAdmins();

        return ResponseEntity.ok(admins);
    }



    @PutMapping("/add-project/{idAdmin}/{idProject}")
    public ResponseEntity<AppAdmin> addProjectToAdmin(@PathVariable("idAdmin") String idAdmin, @PathVariable("idProject") long idProject) {

        userService.addProjectToAdmin(idAdmin, idProject);

        return new ResponseEntity<AppAdmin>(HttpStatus.OK);
    }


    @DeleteMapping("/delete-user-by-admin/{idUser}/{idAdmin}")
    public ResponseEntity<String> deleteUserByAdmin(@PathVariable("idUser") String idUser, @PathVariable("idAdmin") String idAdmin) {

        userService.deleteUserByAdmin(idUser, idAdmin);
        return ResponseEntity.ok("user successfully deleted");


    }


    @GetMapping("/all-customers")
    public ResponseEntity<List<AppCustomer>> getAllCustomer() {
        return new ResponseEntity<List<AppCustomer>>(appUserRepository.findAllCustomer(), HttpStatus.OK);
    }



    @GetMapping("/admins-without-master")
    public ResponseEntity<List<AppAdmin>> getadmins() {
        return new ResponseEntity<List<AppAdmin>>(appUserRepository.findAdmins(), HttpStatus.OK);

    }

    @GetMapping("/all-master")
    public ResponseEntity<List<AppMaster>> getMAsters() {
        return new ResponseEntity<List<AppMaster>>(appUserRepository.findAllMasters()
                , HttpStatus.OK);
    }

    @PutMapping("/add-admin-to-master/{idAdmin}/{idMaster}")
    public ResponseEntity<AppAdmin> addAdminTomaster(@PathVariable("idAdmin") String idAdmin, @PathVariable("idMaster") String idMaster) {

        userService.addAdminToMaster(idAdmin, idMaster);

        return new ResponseEntity<AppAdmin>(HttpStatus.OK);
    }

    @PostMapping("/sendMailContact")
    public ResponseEntity<Contact> sendMailContact(@RequestBody Contact contact) {
        System.out.println(contact);
        mailService.sendEmail(contact);
        return new ResponseEntity<Contact>(contact, HttpStatus.OK);
    }

    @GetMapping("/one/{email}")
    public ResponseEntity< AppCustomer> findByEmail(@PathVariable("email") String email) {


        AppCustomer user = (AppCustomer) customerRepository.findByEmail(email);

            if (user == null) {
                System.out.println(user);
                return new ResponseEntity<AppCustomer>(user, HttpStatus.BAD_REQUEST);
            } else {
                return ResponseEntity.ok(user);

            }


    }

    @PutMapping("/update-customer")
    public ResponseEntity<AppCustomer> updateUser(@RequestBody AppCustomer user) {
        if (user.getPassword() != null) {
            System.out.println(user.getPassword());
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
            AppCustomer savedUser  = (AppCustomer) appUserRepository.save(user);
            return new ResponseEntity<AppCustomer>(savedUser, HttpStatus.OK);
    }

    @PutMapping("/update-master")
    public ResponseEntity<AppMaster> updateMaster(@RequestBody AppMaster user) {
        if (user.getPassword() != null) {


            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }



        masterRepository.save(user);
            return new ResponseEntity< AppMaster>( user, HttpStatus.OK);


    }
    @PutMapping("/update-admin")
    public ResponseEntity<AppAdmin> updateAdmin(@RequestBody AppAdmin user) {
        if (user.getPassword() != null) {
            System.out.println(user.getPassword());
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
              adminRepository.save(user);
            return new ResponseEntity<AppAdmin>(user, HttpStatus.OK);


    }
    @GetMapping(value = "/exist/{text}/{id}")

    public boolean testPasswordExist(@PathVariable String text, @PathVariable String id) {


        boolean resultas = userService.testExistPassword(text, id);

        return resultas;

    }


    @GetMapping("/all-users")
    public ResponseEntity<List<AppUser>> findALLUsers() {

        List<AppUser> users = appUserRepository.findAll();
        return new ResponseEntity<List<AppUser>>(users, HttpStatus.OK);
    }

    @PutMapping("/approve-account-user")
    public ResponseEntity<String> approveAccount(@RequestBody String id) {
        AppUser user = (AppUser) appUserRepository.findById(id).get();
        if (user != null) {
            user.setEnabledAccount(true);
            appUserRepository.save(user);

            return new ResponseEntity<String>("ok", HttpStatus.OK);
        }
        return new ResponseEntity<String>("no", HttpStatus.BAD_REQUEST);
    }


    @DeleteMapping("/deleteAccountUser/{idUser}")
    public ResponseEntity<String> deleteAccountUser(@PathVariable("idUser") String idUser) {
        AppUser user = (AppUser) appUserRepository.findById(idUser).get();


        if (user != null) {
            userService.deleteAccountUser(idUser);
            return ResponseEntity.ok("master successfully deleted");
        }
        return new ResponseEntity<String>("no", HttpStatus.BAD_REQUEST);


    }




// custommer create project
    @PostMapping("/new-project-customer/{idCustomer}")
    public ResponseEntity<AppProject> createProjectbycostomer(@RequestBody AppProject appProject, @PathVariable("idCustomer") String idCustomer) {
        Collection<String> listIdReceiverFromCustomer = new ArrayList<String>();

        AppProject project = userService.createProjectByCustomer(appProject, idCustomer);

        if (project != null) {
          masterRepository.findAllMasters().forEach(appMaster -> {
                listIdReceiverFromCustomer.add(appMaster.getId())  ;

            });
          //add socket
            notificationService.sendNotificataion(listIdReceiverFromCustomer,idCustomer, messageNotification.CREATE_PROJECT_BY_CUSTOMER,project);



            return new ResponseEntity<AppProject>(project, HttpStatus.OK);
        }
        return new ResponseEntity<AppProject>(project, HttpStatus.BAD_REQUEST);
    }
    // admin create project

    @PostMapping("/new-project-admin/{idAdmin}/{idCustomer}")
    public ResponseEntity<AppProject> createUser(@RequestBody AppProject appProject, @PathVariable("idAdmin") String idAdmin, @PathVariable("idCustomer") String idCustomer) {
        Collection<String> listIdReceiverFromAdmin = new ArrayList<String>();
        AppProject project = userService.createProjectByAdmin(appProject, idAdmin, idCustomer);
        AppAdmin admin =adminRepository.findById(idAdmin).get();
              //////add SOCKET///////////////////////////

        if (project != null) {
            listIdReceiverFromAdmin.add(admin.getIdMaster());
            listIdReceiverFromAdmin.add(idCustomer);
            notificationService.sendNotificataion(listIdReceiverFromAdmin,idAdmin, messageNotification.CREATE_PROJECT_BY_ADMIN,project);

            return new ResponseEntity<AppProject>(project, HttpStatus.OK);
        }
        return new ResponseEntity<AppProject>(project, HttpStatus.BAD_REQUEST);
    }
// master create project
    @PostMapping("/new-project-master/{idMaster}/{idAdmin}/{idCustomer}")
    public ResponseEntity<AppProject> addNewProjectToCustomeraByMaster(@RequestBody AppProject appProject, @PathVariable("idMaster") String idMaster,@PathVariable("idAdmin") String idAdmin, @PathVariable("idCustomer") String idCustomer) {
        Collection<String> listIdReceiver = new ArrayList<String>();
        AppProject project = userService.createProjectByMaster(appProject,idMaster,idAdmin, idCustomer);
                //////add SOCKET///////////////////////////
        if (project != null) {
            // send notification for admin and customer
            listIdReceiver.add(idAdmin);
            listIdReceiver.add(idCustomer);
            notificationService.sendNotificataion(listIdReceiver,idMaster, messageNotification.CREATE_PROJECT_BY_MASTER,project);

            return new ResponseEntity<AppProject>(project, HttpStatus.OK);
        }
        return new ResponseEntity<AppProject>(project, HttpStatus.BAD_REQUEST);
    }







    // add documentUser
    @PostMapping("/add-document-to-customer/{idCustomer}")
    public ResponseEntity <DocumentUser>addDocumentToUser( @PathVariable("idCustomer") String idCustomer,@RequestParam ("customer") String customer ,@RequestParam("file") MultipartFile file) throws IOException  {
        DocumentUser documentUser =  new ObjectMapper().readValue(customer,  DocumentUser.class);
        //////add SOCKET///////////////////////////
        documentUser.setData(file.getBytes());
        DocumentUser document = userService.addDocumentToUser(documentUser, idCustomer);


        if (document != null) {
            return new ResponseEntity<DocumentUser>(document, HttpStatus.OK);
        }
        return new ResponseEntity<DocumentUser>(document, HttpStatus.BAD_REQUEST);
    }
    @GetMapping("getFilDocument/{fileId}")
    public ResponseEntity<byte[]> getFileOfdocument (@PathVariable long fileId) throws IOException {
        DocumentUser document = documentUserRepository.findById(fileId).get();


        if (document == null) {
            throw new FileNotFoundException(fileId + " was not found on the server");
        }


        return ResponseEntity.ok().body(document.getData());
    }

    @DeleteMapping("/delete-document/{id}")
    public ResponseEntity<String> deleteDocument(@PathVariable("id") long id) {
        DocumentUser document = documentUserRepository.findById(id).get();
        AppCustomer customer = customerRepository.findById(document.getId_customer()).get();
        HashSet<String> listIdReceiver = new HashSet<String>();

        customer.getProjects().forEach(appProject -> {
            listIdReceiver.add(appProject.getIdAdmin());
        });


        notificationService.sendNotificataionOfDeleteDocument(listIdReceiver, customer.getId(), document);
        documentUserRepository.deleteById(id);


        return  ResponseEntity.ok("document successfully deleted");

    }
//send notfication updating or update profile if custommer not has propject

    @PostMapping("/send-updated-feild-to-reciver/{nameFeild}/{updatedFeild}/{previousValue}")
    public ResponseEntity<Boolean> sendNotfication(@PathVariable("nameFeild") String nameFeild,  @PathVariable("updatedFeild") String updatedFeild,@PathVariable("previousValue")  String previousValue ,@RequestBody AppCustomer customer) {
        Boolean customerUpdated = notificationService.sendNotficationForUpdateProfileOrUpdating(customer,nameFeild,updatedFeild,previousValue);
        return new ResponseEntity<Boolean>(customerUpdated, HttpStatus.OK);

    }



    @GetMapping("/list-document-by-custommer/{idCustomer}")
    public ResponseEntity<List<DocumentUser>> getListDocument(@PathVariable("idCustomer") String idCustomer) {
        AppCustomer customer = (AppCustomer) appUserRepository.findById(idCustomer).get();
        List<DocumentUser> documents = (List<DocumentUser>) customer.getDocuments();

        return ResponseEntity.ok(documents);
    }

    @MessageMapping("/resume/{id}")
    @SendTo("/topic/initial")
    public  MessageChat  chat(@Payload MessageChat msg,@DestinationVariable  String id) {
        long idProject= Long.parseLong( id, 10);
        AppProject project = appProjectRepository.findById(idProject).get();
        messageChatRepository.save(msg);
         project.getMessageChats().add(msg);
        appProjectRepository.save(project);
        return msg;
    }
    @GetMapping("/all-message/{id}/{numberMessage}/{idSender}")
    public ResponseEntity<ModelMessage> findAllMessagesOfProject(@PathVariable ("id") long id, @PathVariable("numberMessage") int numberMessage,@PathVariable("idSender") String idSender) {
        List<MessageChat> listMessageProject = new ArrayList<MessageChat>();
        List<MessageChat> SublistMessage= new ArrayList<>();
        AppProject project = appProjectRepository.findById(id).get();

        listMessageProject= messageChatRepository.findAllMessages(id);
        if(numberMessage>listMessageProject.size()){
            numberMessage=listMessageProject.size();
        }
       /* List<MessageChat> sortedList = listMessageProject.stream()
                .sorted(Comparator.comparingInt(MessageChat::getId))
                .collect(Collectors.toList());
        Collections.reverse(sortedList)
        sortedList= sortedList.subList(0,numberMessage);
        Collections.reverse(sortedList);*/
        Collections.reverse(listMessageProject);
        listMessageProject=    listMessageProject.subList(0,numberMessage);
        Collections.reverse(listMessageProject);
        ModelMessage model= new  ModelMessage();
        model.setMessages(   listMessageProject);
        model.setTotalMessageIsNotShowed(messageChatRepository.findAllMessagesIsNotShowed(idSender,project.getId()).size());

        return new ResponseEntity<ModelMessage>(model, HttpStatus.OK);
    }

    @PutMapping("/all-message-is-showed/{id}/{idSender}")
    public ResponseEntity<Void> putMessagesOfProjectIsShowed(@PathVariable ("id") long id, @PathVariable("idSender") String idSender) {
        List<MessageChat> listMessageProject = new ArrayList<MessageChat>();



        listMessageProject=messageChatRepository.findAllMessagesIsNotShowed(idSender,id);
        listMessageProject.forEach((message) -> {
            message.setShowed(true);
            messageChatRepository.save(message);
        });

        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
