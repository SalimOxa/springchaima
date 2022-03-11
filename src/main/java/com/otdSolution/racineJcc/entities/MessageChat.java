package com.otdSolution.racineJcc.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;


import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@ToString
@Table(name="message_chat")
public class MessageChat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String senderName;
    private String idReceiver;
    private String message;
    private Date date =new Date();
    private  boolean isShowed ;


}
