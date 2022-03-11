package com.otdSolution.racineJcc.service;

import com.otdSolution.racineJcc.dao.AppProjectRepository;
import com.otdSolution.racineJcc.dao.AppUserRepository;
import com.otdSolution.racineJcc.dao.NotificationRepository;
import com.otdSolution.racineJcc.entities.*;
import com.otdSolution.racineJcc.utils.messageNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.Charset;
import java.util.*;


@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    AppUserService appUserService;
    @Autowired
    private AppUserRepository<AppCustomer> customerRepository;
    @Autowired
    private AppUserRepository<AppMaster> masterAppUserRepository;
    @Autowired
    private WebSocketService webSocketService;
    @Autowired
    private AppProjectRepository appProjectRepository;

    public void sendNotificataionAddCostumer(Collection<String> listIdReceiver, AppUser emmitor) {

        String msg = emmitor.getUsername() + " " +
                "Create account";
        listIdReceiver.forEach(idReceiver -> {
            NotficationUser notficationUser = new NotficationUser(null, emmitor.getUsername(), msg, new Date(), messageNotification.TYPE_CREATE_PROJECT, true,null, null, null, null, true,null, null, null);
            notificationRepository.save(notficationUser);


            if (idReceiver != null) {
                AppUser receiver = (AppUser) appUserRepository.findById(idReceiver).get();

                try {
                    Thread.sleep(2000);
                    receiver.getNotfications().add(notficationUser);
                    appUserRepository.save(receiver);
                    webSocketService.sendMessage(idReceiver, "user_type");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        });
    }
    public void sendNotificataion(Collection<String> listIdReceiver, String idEmmitor, String message, AppProject project) {
        AppUser emmitor = (AppUser) appUserRepository.findById(idEmmitor).get();
        String msg= emmitor.getUsername()+" "+message+" "+project.getName();
        listIdReceiver.forEach(idReceiver->{
            NotficationUser notficationUser = new NotficationUser(null,emmitor.getUsername(),msg,new Date(), messageNotification.TYPE_CREATE_PROJECT,true,project.getId(),null,null,null,true,null,null,null);
            notificationRepository.save(notficationUser);


            if (idReceiver != null) {
                AppUser receiver = (AppUser) appUserRepository.findById(idReceiver).get();

                try {
                    Thread.sleep(2000);
                    receiver.getNotfications().add(notficationUser);
                    appUserRepository.save(receiver);
                    webSocketService.sendMessage(idReceiver,"project_type");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        });




    }
    public void sendNotificataionOfDocument(Collection<String> listIdReceiver, String idEmmitor, DocumentUser doc) {
        AppUser emmitor = (AppUser) appUserRepository.findById(idEmmitor).get();
        String msg= emmitor.getUsername()+" "+" add doocument "+" "+doc.getDocumentType();
        listIdReceiver.forEach(idReceiver->{
            NotficationUser notficationUser = new NotficationUser(null,emmitor.getUsername(),msg,new Date(), messageNotification.TYPE_ADD_DOCUMENT,true,doc.getId(),null,null,null,true,null,null,null);


            if (idReceiver != null) {
                AppUser receiver = (AppUser) appUserRepository.findById(idReceiver).get();
                try {
                    Thread.sleep(2000);
                    receiver.getNotfications().add(notficationUser);
                    appUserRepository.save(receiver);
                    notificationRepository.save(notficationUser);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            webSocketService.sendMessage(idReceiver,"document_type");
        });




    }

    public Boolean sendNotficationForUpdateProfileOrUpdating (AppCustomer customer,String nameFeild,String updatedFeild ,String previousValue ) {
        HashSet<String> listIdReceiver = new HashSet<String>();
        String msg = "Customer " + customer.getUsername() + " " + messageNotification.UPDATE_PROFILE_USER + ": " + nameFeild + " from :" + previousValue + " to :" + updatedFeild;

      if(!appProjectRepository.findProjectsByCustommer(customer.getId()).isEmpty()){

          appProjectRepository.findProjectsByCustommer(customer.getId()).forEach(appProject -> {
        if(appProject.getIdAdmin()!=null){

            listIdReceiver.add(appProject.getIdAdmin());
            listIdReceiver.add(appProject.getIdMaster());}

    });
          int leftLimit = 97; // letter 'a'
          int rightLimit = 122; // letter 'z'
          int targetStringLength = 10;
          Random random = new Random();

          String generatedString = random.ints(leftLimit, rightLimit + 1)
                  .limit(targetStringLength)
                  .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                  .toString();
          System.out.println("///////////////"+listIdReceiver);
          if(!listIdReceiver.isEmpty()){

        listIdReceiver.forEach(idReceiver -> {

            NotficationUser notficationUser = new NotficationUser(null, customer.getUsername(), msg, new Date(), messageNotification.UPDATE_PROFILE_USER, true, null, nameFeild, previousValue, updatedFeild, false, customer.getId(), generatedString,listIdReceiver);
            notificationRepository.save(notficationUser);

                AppUser receiver = (AppUser) appUserRepository.findById(idReceiver).get();
                try {
                   // notificationRepository.save(notficationUser);
                    receiver.getNotfications().add(notficationUser);
                    appUserRepository.save(receiver);

                    webSocketService.sendMessage(idReceiver,"profile_type");
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


        });

    }else{
        return true;
    }

}else{

          return true;
      }
      return false;
    }


    public NotficationUser approveUpdateProfile(NotficationUser notficationUser, String idUser){
        HashSet<String> listIdReceiverSuccessUpdate = new HashSet<String>();
        notficationUser.setIsShowed(false);
        notificationRepository.save(notficationUser);
        listIdReceiverSuccessUpdate=  notficationUser.getReceiver();
        listIdReceiverSuccessUpdate.add(notficationUser.getIdCustommer());
        listIdReceiverSuccessUpdate.remove(idUser);

        AppUser sender = (AppUser) appUserRepository.findById(idUser).get();

        String msg =  sender.getUsername() + " " + messageNotification.APPROVE_UPDATE_USER ;
        String msg2 = notficationUser.getMessage()+" " + messageNotification.APPROVE_UPDATE_USER_Back_Office+" "+" with"+" " +sender.getUsername() ;

        notificationRepository.deleteNotificationApprove(notficationUser.getIdNotficationForApprove());


        listIdReceiverSuccessUpdate.forEach(idReceiver -> {
            if (idReceiver != null) {
                AppUser receiver = (AppUser) appUserRepository.findById(idReceiver).get();

                if("CUSTOMER".equals(receiver.getRoles().get(0).getRoleName())){

                    NotficationUser notficationForUppdate = new NotficationUser(null, sender.getUsername(), msg, new Date(), messageNotification.UPDATE_PROFILE_USER, true, null, null, null, null, true,null, sender.getId(), null);
                    notificationRepository.save(notficationForUppdate);
                receiver.getNotfications().add(notficationForUppdate);
                appUserRepository.save(receiver);

                }
               else if(!("CUSTOMER".equals(receiver.getRoles().get(0).getRoleName()))) {
                    NotficationUser notficationForApproved2= new NotficationUser(null, sender.getUsername(), msg2, new Date(), messageNotification.UPDATE_PROFILE_USER, true, null, null, null, null, true, null,sender.getId(), null);
                    notificationRepository.save(notficationForApproved2);
                   // receiver.getNotfications().remove(notficationUser);

                    receiver.getNotfications().add(notficationForApproved2);
                    appUserRepository.save(receiver);

                }
                webSocketService.sendMessage(idReceiver,"profile_type");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }

        });





        return  new NotficationUser();
    }

    public void sendNotificataionOfDelete( Collection<String> listIdReceiver,String idEmmitor, String msg, AppProject project) {
        AppUser emmitor = (AppUser) appUserRepository.findById(idEmmitor).get();


        listIdReceiver.forEach(idReceiver->{
            NotficationUser notficationUser = new NotficationUser(null,emmitor.getUsername(),msg,new Date(), messageNotification.TYPE_DELETE_PROJECT,true,project.getId(),null,null,null,true,null,null,null);
            notificationRepository.save(notficationUser);
            if (idReceiver != null) {
                AppUser receiver = (AppUser) appUserRepository.findById(idReceiver).get();
                receiver.getNotfications().add(notficationUser);
                appUserRepository.save(receiver);
                webSocketService.sendMessage(idReceiver,"project_type");

            }
        });




    }
    public void sendNotificataionOfDeleteDocument( Collection<String> listIdReceiver,String idEmmitor, DocumentUser doc) {
        AppUser emmitor = (AppUser) appUserRepository.findById(idEmmitor).get();

        String msg= emmitor.getUsername()+" "+" delete doocument : "+" "+doc.getDocumentType();
        listIdReceiver.forEach(idReceiver->{
            NotficationUser notficationUser = new NotficationUser(null,emmitor.getUsername(),msg,new Date(), messageNotification.TYPE_DELETE_PROJECT,true,doc.getId(),null,null,null,true,null,null,null);
            notificationRepository.save(notficationUser);
            if (idReceiver != null) {
                AppUser receiver = (AppUser) appUserRepository.findById(idReceiver).get();
                receiver.getNotfications().add(notficationUser);
                appUserRepository.save(receiver);
                webSocketService.sendMessage(idReceiver,"document_type");

            }
        });




    }

    }

