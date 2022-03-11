package com.otdSolution.racineJcc.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Table(name="app_document_user")
public class DocumentUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String numero;
    private String documentType;
    private Date expirationDate;
    private byte[] data;
    private String id_customer;


    @Column(length =300000000)
    @Basic(fetch=FetchType.EAGER)
    public byte[] getData() {
        return data;
    }
    public void setData(byte[] data) {
        this.data = data;
    }
}
