package com.otdSolution.racineJcc.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.nimbusds.oauth2.sdk.util.CollectionUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "userType", discriminatorType = DiscriminatorType.STRING)
@Table(name="app_user")
public class AppUser {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String birthday;
    private String phoneNumber;
    private boolean isActivated;
    private boolean isEnabledAccount;
    private Date dateCreation =new Date();
    private String adresse;
    @OneToMany(targetEntity = AppRole.class,fetch = FetchType.EAGER,cascade=CascadeType.ALL )
    @JoinColumn(name = "fk_app_user")
    private List<AppRole> roles = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_app_notif")
    private List<NotficationUser> notfications;



    public AppUser(String username) {
        this.username = username;
    }

    public static AppSuperMaster toSuperMasterEntity(AppUser user) {
        if (user == null) {
            return null;
        }
        return AppSuperMaster.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .birthday(user.getBirthday())
                .email(user.getEmail())

                .password(user.getPassword())
                .phoneNumber(user.getPhoneNumber())
                .isActivated(user.isActivated())
                .isEnabledAccount(user.isEnabledAccount())
                .roles(CollectionUtils.isEmpty(user.getRoles())  ? null :
                        user.getRoles().stream()
                                .map(AppRole::toEntity)
                                .collect(Collectors.toList())



                )
                .notfications(CollectionUtils.isEmpty(user.getNotfications())  ? null :
                        user.getNotfications().stream()
                                .map(NotficationUser::toEntity)
                                .collect(Collectors.toList())
                )
                .build();
    }

    public static AppMaster toMasterEntity(AppUser user) {
        if (user == null) {
            return null;
        }
        return AppMaster.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .birthday(user.getBirthday())
                .email(user.getEmail())

                .password(user.getPassword())
                .phoneNumber(user.getPhoneNumber())
                .isActivated(user.isActivated())
                .isEnabledAccount(user.isEnabledAccount())
                .roles(CollectionUtils.isEmpty(user.getRoles())  ? null :
                        user.getRoles().stream()
                                .map(AppRole::toEntity)
                                .collect(Collectors.toList())



                )
                .notfications(CollectionUtils.isEmpty(user.getNotfications())  ? null :
                        user.getNotfications().stream()
                                .map(NotficationUser::toEntity)
                                .collect(Collectors.toList())
                )
                .build();
    }
    public static AppAdmin toAdminEntity(AppUser user) {
        if (user == null) {
            return null;
        }
        return AppAdmin.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .birthday(user.getBirthday())
                .email(user.getEmail())
                .password(user.getPassword())
                .phoneNumber(user.getPhoneNumber())
                .isActivated(user.isActivated())
                .isEnabledAccount(user.isEnabledAccount())
                .roles(CollectionUtils.isEmpty(user.getRoles())  ? null :
                        user.getRoles().stream()
                                .map(AppRole::toEntity)
                                .collect(Collectors.toList())
                )
                .build();
    }
    public static AppCustomer toCustomerEntity(AppUser user) {
        if (user == null) {
            return null;
        }
        return AppCustomer.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .birthday(user.getBirthday())
                .email(user.getEmail())
                .password(user.getPassword())
                .phoneNumber(user.getPhoneNumber())
                .isActivated(user.isActivated())
                .isEnabledAccount(user.isEnabledAccount())

                .roles(CollectionUtils.isEmpty(user.getRoles())  ? null :
                        user.getRoles().stream()
                                .map(AppRole::toEntity)
                                .collect(Collectors.toList())
                )

                .build();
    }

}