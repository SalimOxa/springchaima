package com.otdSolution.racineJcc.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString


@Table(name="app_task")
public class AppTask {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Integer id;
        private String name;
        @Column(name="description",length = 1024)
        private String description;
        private Date creationDate =new Date();
        private Date closeDate ;
        private String type;
        private String state;

        private boolean enableAccount;
        @OneToMany( targetEntity=CommentTask.class, fetch = FetchType.LAZY,cascade={CascadeType.ALL} )
        private Collection<CommentTask> comments = new ArrayList<>();
        @OneToOne(cascade={CascadeType.ALL})
        @JoinColumn(name="mail")
        private MailContentTask mail;
        @OneToOne(cascade={CascadeType.ALL})
        @JoinColumn(name="file")
        private TaskFile file;
        @OneToOne(cascade={CascadeType.ALL})
        @JoinColumn(name="tranchePayment")
        private TranchePayment tranchePayment;



}

