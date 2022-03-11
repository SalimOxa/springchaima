package com.otdSolution.racineJcc.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@SuperBuilder
@Table(name = "app_notification_user")
public class NotficationUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //name who did the task
    private String name;
    private String message;
    private Date createdDate;
    private String TypeNotification;
    private Boolean isShowed;
    private Long idProject;
    //update profile
    private String nameField;
    private String previousValue;
    private String updatedValue;
    private Boolean approved;
    private String idCustommer;
    private String idNotficationForApprove;


    private HashSet<String> receiver ;

    public static NotficationUser toEntity(NotficationUser notficationUser) {
        if (notficationUser == null) {
            return null;
        }
        return NotficationUser.builder()
                .id(notficationUser.getId())
                .name(notficationUser.getName())
                .message(notficationUser.getMessage())
                .createdDate(notficationUser.getCreatedDate())
                .TypeNotification(notficationUser.getTypeNotification())
                .isShowed(notficationUser.getIsShowed())
                .idProject(notficationUser.getIdProject())
                .nameField(notficationUser.getNameField())
                .previousValue(notficationUser.getPreviousValue())
                .updatedValue(notficationUser.getUpdatedValue())
                .approved(notficationUser.getApproved())
                .idCustommer(notficationUser.getIdCustommer())
                .receiver(notficationUser.getReceiver())
                .build();
    }

}
