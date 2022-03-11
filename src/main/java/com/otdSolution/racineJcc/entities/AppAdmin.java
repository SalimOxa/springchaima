package com.otdSolution.racineJcc.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;


@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
@Data
@DiscriminatorValue("ADMIN")
public class AppAdmin extends AppUser{



    private  String nameMaster;
    private String idMaster;
    @JsonBackReference
    @OneToMany(targetEntity = AppProject.class,fetch = FetchType.LAZY,cascade=CascadeType.ALL )
    @JoinColumn(name = "fk_app_project_admin",nullable =true)
    private Collection<AppProject> projects = new ArrayList<>();

}
