package com.otdSolution.racineJcc.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@SuperBuilder
@Table(name="app_role")
public class AppRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String roleName;

    public static AppRole toEntity(AppRole role) {
        if (role == null) {
            return null;
        }
        return AppRole.builder()
                .id(role.getId())
                .roleName(role.getRoleName())
                .build();
    }


}
