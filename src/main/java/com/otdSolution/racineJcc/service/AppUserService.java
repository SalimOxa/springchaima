package com.otdSolution.racineJcc.service;

import com.otdSolution.racineJcc.dao.*;
import com.otdSolution.racineJcc.entities.*;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class AppUserService {
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
    private AppProjectRepository appProjectRepository;
    @Autowired
    private AppDocumentUserRepository appDocumentUserRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private ConfirmationCodeRepository confirmationCodeRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private WebSocketService webSocketService;

    public AccountService getAccountService() {
        return accountService;
    }



    public AppUser createUserAccordingToRole
            (AppCustomer user, String idUser) {
        AppUser appUser = (AppUser) appUserRepository.findById(idUser).get();
        user.setActivated(true);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        if (appUser.getRoles().get(0).getRoleName().equals("MASTER")) {
            AppMaster master = (AppMaster) appUser;
            if ("ADMIN".equals(user.getRoles().get(0).getRoleName())) {
                AppAdmin admin = AppUser.toAdminEntity(user);
                admin.setNameMaster(master.getUsername());
                admin.setIdMaster(master.getId());
                adminRepository.save(admin);

                master.getAppAdmins().add(admin);

                masterRepository.save(master);
            } else if ("CUSTOMER".equals(user.getRoles().get(0).getRoleName())) {
                customerRepository.save(user);
            }
            masterRepository.save(master);
        } else if (appUser.getRoles().get(0).getRoleName().equals("ADMIN")) {
            AppAdmin admin = (AppAdmin) appUser;
            if ("CUSTOMER".equals(user.getRoles().get(0).getRoleName())) {

            customerRepository.save(user);

            }
            adminRepository.save(admin);
        }
        return user;
    }

    // custommer create project
    public AppProject createProjectByCustomer(AppProject appProject, String idCustomer) {

        AppCustomer customer = customerRepository.findById(idCustomer).get();
        appProject.setAppCustomer(customer);
        appProject.setNameCustomer(customer.getUsername());
        appProject.setIdCustomer(idCustomer);
        AppProject project = appProjectRepository.save(appProject);
        customer.getProjects().add(appProject);
        customerRepository.save(customer);


        return project;
    }


    // admin create project
    public AppProject createProjectByAdmin(AppProject appProject, String idAdmin, String idCustomer) {
        AppAdmin admin = adminRepository.findById(idAdmin).get();
        appProject.setIdMaster(admin.getIdMaster());
        appProject.setNameAdmin(admin.getUsername());
        appProject.setIdAdmin(admin.getId());
        AppProject project = createProjectByCustomer(appProject, idCustomer);
        admin.getProjects().add(project);
        adminRepository.save(admin);


        return project;

    }

    // master create project
    public AppProject createProjectByMaster(AppProject appProject, String idMaster, String idAdmin, String idCustomer) {
        appProject.setIdMaster(idMaster);
        appProjectRepository.save(appProject);
        AppProject project = createProjectByAdmin(appProject, idAdmin, idCustomer);


        return project;

    }


    /// if custommer create project ==> master add project to admin
    public void addProjectToAdmin(String idAdmin, long idProject) {
        HashSet<String> listIdReceiver = new HashSet<String>();
        AppAdmin admin = adminRepository.findById(idAdmin).get();
        AppMaster master = masterRepository.findById(admin.getIdMaster()).get();
        AppProject project = appProjectRepository.findById(idProject).get();
        String msg = " " + "add " + " to " + admin.getUsername() + "the project : ";
        listIdReceiver.add(project.getIdCustomer());

        listIdReceiver.add(admin.getId());
        notificationService.sendNotificataion(listIdReceiver, admin.getIdMaster(), msg, project);
        admin.getProjects().add(project);
        project.setIdAdmin(admin.getId());
        project.setIdMaster(master.getId());
        project.setNameAdmin(admin.getUsername());

        appProjectRepository.save(project);


    }


    public void addAdminToMaster(String idAdmin, String idMaster) {
        AppAdmin admin = adminRepository.findById(idAdmin).get();
        AppMaster master = masterRepository.findById(idMaster).get();

        master.getAppAdmins().add(admin);
        admin.setNameMaster(master.getUsername());
        admin.setIdMaster(idMaster);
        adminRepository.save(admin);
        masterRepository.save(master);
    }

    public String deleteUserByAdmin(String idUser, String idAdmin) {

        AppAdmin admin = (AppAdmin) appUserRepository.findById(idAdmin).get();


        Iterator<AppProject> listProject = admin.getProjects().iterator();
        while (listProject.hasNext()) {
            AppProject appProject = listProject.next();
            if (appProject.getAppCustomer().getId().equals(idUser)) {
                appProject.setIdAdmin(null);
                appProject.setNameAdmin(null);
                listProject.remove();
            }
        }
        AppAdmin save = (AppAdmin) appUserRepository.save(admin);


        return "user successfully deleted";


    }

    public String deleteAccountUser(String idUser) {
        ConfirmationCode confirmationCode = confirmationCodeRepository.findConfirmationCodeBy(idUser);
        if (confirmationCode != null) {
            confirmationCodeRepository.delete(confirmationCode);
        }

        AppUser user = (AppUser) appUserRepository.findById(idUser).get();

        if ("CUSTOMER".equals(user.getRoles().get(0).getRoleName())) {
            AppCustomer customer = (AppCustomer) user;


            customer.getProjects().forEach(appProject -> {
                appProjectRepository.delete(appProject);
            });

            customerRepository.delete(customer);
        } else if ("ADMIN".equals(user.getRoles().get(0).getRoleName())) {
            AppAdmin admin = (AppAdmin) user;

            admin.getProjects().forEach(appProject -> {
                appProject.setIdAdmin(null);
                appProject.setNameAdmin(null);
                ;
            });
            admin.getProjects().removeAll(admin.getProjects());
            adminRepository.delete(admin);
        } else {
            AppMaster master = (AppMaster) user;
            master.getAppAdmins().forEach(appAdmin -> {
                appAdmin.getProjects().forEach(appProject -> {
                    appProject.setIdAdmin(null);
                    appProject.setNameAdmin(null);
                });


                adminRepository.delete(appAdmin);

            });
            masterRepository.delete(master);
        }


        return "success delete";
    }


    public boolean testExistPassword(String passWord, String id) {
        AppUser user = (AppUser) appUserRepository.findById(id).get();
        BCryptPasswordEncoder b = new BCryptPasswordEncoder();
        boolean test = b.matches(passWord, user.getPassword());
        if (test == false) {
            return false;

        } else {
            return true;
        }
    }

    // custommer create project
    public DocumentUser addDocumentToUser(DocumentUser documentUser, String idCustomer) {
        HashSet<String> listIdReceiver = new HashSet<String>();
        AppCustomer customer = customerRepository.findById(idCustomer).get();
        customer.getDocuments().add(documentUser);

        customer.getProjects().forEach(appProject -> {
            listIdReceiver.add(appProject.getIdAdmin());
        });


        notificationService.sendNotificataionOfDocument(listIdReceiver, customer.getId(), documentUser);
        appUserRepository.save(customer);
        return documentUser;
    }


}