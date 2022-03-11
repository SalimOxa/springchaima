package com.otdSolution.racineJcc.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "app_project")

public class AppProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name="description",length = 1024)
    private String description;
    private Date createdDate;
    private Long totalPayment;
    private Long restPayment;
    private String currencyCode;
    private String nameService;
    private String nameAdmin;
    private String nameCustomer;
    private String idAdmin;
    private String idMaster;
    private String idCustomer;
    private String company;
    private String status="Unpaid";
   @OneToOne(cascade = CascadeType.ALL)
            private AppStatementOfWork appStatementOfWork;
    @JsonBackReference(value = "app-projects-customer")
    @ManyToOne(fetch = FetchType.LAZY )
    private AppCustomer appCustomer;


    @JoinColumn(name = "fk_projects_tasks")
    @JsonBackReference(value = "app-projects-tasks")
    @OneToMany( targetEntity=AppTask.class, fetch = FetchType.LAZY,cascade={CascadeType.ALL} )
    private Collection<AppTask> tasks = new ArrayList<>();
    @JsonBackReference(value = "app-projects-tranche")
    @OneToMany( targetEntity=TranchePayment.class, fetch = FetchType.LAZY,cascade={CascadeType.ALL} )
    private Collection<TranchePayment> tranchePayment = new ArrayList<>();
    @JoinColumn(name = "fk_chat_message")
    @JsonBackReference(value = "app-projects-message")
    @OneToMany( targetEntity=MessageChat.class,fetch = FetchType.EAGER,cascade={CascadeType.ALL} )
    private Collection<MessageChat> messageChats = new ArrayList<>();


}
