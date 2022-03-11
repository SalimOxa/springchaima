package com.otdSolution.racineJcc.web;

import com.otdSolution.racineJcc.dao.AppUserRepository;
import com.otdSolution.racineJcc.dao.NotificationRepository;
import com.otdSolution.racineJcc.dto.Model;
import com.otdSolution.racineJcc.dto.ModelNotification;
import com.otdSolution.racineJcc.entities.AppCustomer;
import com.otdSolution.racineJcc.entities.AppUser;
import com.otdSolution.racineJcc.entities.MessageChat;
import com.otdSolution.racineJcc.entities.NotficationUser;
import com.otdSolution.racineJcc.service.AppUserService;
import com.otdSolution.racineJcc.service.NotificationService;
import com.otdSolution.racineJcc.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.*;

import static com.otdSolution.racineJcc.utils.ApplicationConstants.APP_ROOT;

@RestController
@RequestMapping(APP_ROOT)
public class NotificationUserController {

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    AppUserService appUserService;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private WebSocketService webSocketService;
    // obligatoire
    @GetMapping("/notifications-by-user-page/{idUser}/{numberListComment}")
    public ResponseEntity<ModelNotification> getNotficationsByUserByPage(@PathVariable("idUser") String idUser, @PathVariable("numberListComment") int numberListComment ) {
        List<NotficationUser> listComment = new ArrayList<NotficationUser>();
        //AppUser user = (AppUser) appUserRepository.findById(idUser).get();
      //  listComment=user.getNotfications();
        listComment=notificationRepository.getAllNotficationsByUser(idUser);

        if(numberListComment>listComment.size()){
            numberListComment=listComment.size();
        }
      //  Collections.reverse(listComment);
        listComment= listComment.subList(0,numberListComment);
        ModelNotification model= new  ModelNotification();
        model.setNotifications( listComment);
        model.setTotalNotifIsNotShowed( notificationRepository.findAllNotifIsNotShowed(idUser).size());
        return new ResponseEntity<ModelNotification>(model, HttpStatus.OK);
    }


    // obligatoire for adimin //fixmee
    @GetMapping("/notifications-by-user/{idUser}")
    public ResponseEntity<List<NotficationUser>> getNotficationsByUser(@PathVariable("idUser") String idUser) {
        AppUser user = (AppUser) appUserRepository.findById(idUser).get();

        return new ResponseEntity<List<NotficationUser>>(user.getNotfications(), HttpStatus.OK);
    }




    @GetMapping("/notification-is-showed/{idUser}")
    public ResponseEntity<Collection<NotficationUser>> findAllNotficationIsShowed(@PathVariable("idUser") String idUser) {
        HashSet<NotficationUser> listNotification= new HashSet<NotficationUser>();
        AppUser user = (AppUser) appUserRepository.findById(idUser).get();
        user.getNotfications().forEach(notif->{
            if(notif.getIsShowed()==true){
                listNotification.add(notif);
            }
        });
        return ResponseEntity.ok(listNotification);
    }


    @PutMapping("/update-etat-notfication/{idNotification}")
    public ResponseEntity<NotficationUser> updateEtatNotificationToShowed(@PathVariable("idNotification") Long idNotification) {
        NotficationUser notficationUser = notificationRepository.getById(idNotification);
        notficationUser.setIsShowed(false);
        notificationRepository.save(notficationUser);
        return new ResponseEntity<NotficationUser>(notficationUser, HttpStatus.OK);

    }


    @PutMapping("/update-customer-notfication")
    public ResponseEntity<AppCustomer> updateUser(@RequestBody AppCustomer customer)  {
        AppCustomer savedUser  = (AppCustomer) appUserRepository.save(customer);
        return new ResponseEntity<AppCustomer>(customer, HttpStatus.OK);
    }

    @PostMapping("/approve-update-profile-customer/{idUser}")
    public ResponseEntity<NotficationUser> approveUpdateUser(@RequestBody NotficationUser notficationUser ,@PathVariable("idUser") String idUser)  {
       NotficationUser notif = notificationService.approveUpdateProfile(notficationUser,idUser);
        return new ResponseEntity<NotficationUser>(notficationUser, HttpStatus.OK);
    }


    @GetMapping("/notifications-unread-by-user/{idUser}")
    public ResponseEntity<List<NotficationUser>> getNotficationsUnreadByUser(@PathVariable("idUser") String idUser) throws InterruptedException {

        List<NotficationUser> listNotficationUnread = new ArrayList<NotficationUser>();
        Thread.sleep(2000);
        listNotficationUnread= notificationRepository.findAllNotifIsNotShowed(idUser);

        return new ResponseEntity<List<NotficationUser>>(listNotficationUnread, HttpStatus.OK);
    }


    @PutMapping("/show-all-notfication-user/{idUser}")
    public ResponseEntity<Void> putMessagesOfProjectIsShowed(@PathVariable ("idUser") String idUser) {
        List<NotficationUser> listNotficationUnread = new ArrayList<NotficationUser>();
        listNotficationUnread= notificationRepository.findAllNotifIsNotShowed(idUser);


        listNotficationUnread.forEach((notfication) -> {
            notfication.setIsShowed(false);
            notificationRepository.save(notfication);
        });

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
