package com.otdSolution.racineJcc.entities;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@DiscriminatorValue("MASTER")
public class AppMaster  extends AppUser{
    @OneToMany(targetEntity = AppAdmin.class,fetch =FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_app_admin")
    private Collection<AppAdmin> appAdmins = new ArrayList<>();


}
