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
@Table(name="app_default_statement_of_work")
public class AppDefaultStatementOfWork {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    @Lob
    private byte[] data;
    private Date createdDate;

    public AppDefaultStatementOfWork(String fileName, byte[] data) {
        this.fileName = fileName;
        this.data = data;
    }
}
