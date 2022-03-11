package com.otdSolution.racineJcc.entities;

import com.fasterxml.jackson.annotation.*;
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
@DiscriminatorValue("CUSTOMER")
public class AppCustomer extends AppUser{
    private String company;


    @JsonBackReference(value = "app-customer-project")
    @OneToMany(targetEntity = AppProject.class,fetch = FetchType.LAZY,cascade= CascadeType.ALL )
    @JoinColumn(name = "fk_app_project_customer",nullable = true)
    private Collection<AppProject> projects = new ArrayList<>();

    @JsonBackReference(value = "app-customer-document")
    @OneToMany(targetEntity = DocumentUser.class,fetch = FetchType.LAZY,cascade= CascadeType.ALL )
    @JoinColumn(name = "fk_app_documents_customer",nullable = true)
    private Collection<DocumentUser> documents = new ArrayList<>();

    public String getCompany() {
        return company;
    }

    public Collection<AppProject> getProjects() {
        return projects;
    }




    public void setProjects(Collection<AppProject> projects) {
        this.projects = projects;
    }

    public Collection<DocumentUser> getDocuments() {
        return documents;
    }
    public void setDocuments(Collection<DocumentUser> documents) {
        this.documents = documents;
    }
    public void setCompany (String company) {
        if(company==null){
            this.company = "none";}
        else{
            this.company=company;
        }

    }

}
