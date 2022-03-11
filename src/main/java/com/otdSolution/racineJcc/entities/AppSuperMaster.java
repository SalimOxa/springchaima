package com.otdSolution.racineJcc.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@DiscriminatorValue("SUPERMASTER")
public class AppSuperMaster extends AppUser{
    @OneToMany(targetEntity = AppMaster.class,fetch =FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_app_master")
    private Collection<AppMaster> appMasters= new ArrayList<>();


}
