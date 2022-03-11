package com.otdSolution.racineJcc.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "app_partner")

public class AppPartner implements Serializable {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @Column(unique=true)
        private String englishName;
        private String englishDescription;
        @Column(unique = true)
        private String frenchName;
        private String frenchDescription;
        @Column(unique = true)
        private String arabicName;
        private String arabicDescription;
        private String photoName;
        private Date partnershipDate;
        @Column(length =300000000)
        @Basic(fetch=FetchType.EAGER)
        private byte[] data;

}
