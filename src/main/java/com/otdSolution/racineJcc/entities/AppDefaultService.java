package com.otdSolution.racineJcc.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Table(name="app_default_service")
public class AppDefaultService implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String nameFrensh;
    private String nameArabic;
    @Column(name = "description", length = 1024)
    private String description;
    @Column(name = "descriptionFrensh", length = 1024)
    private String descriptionFrensh;
    @Column(name = "descriptionArabic", length = 1024)
    private String descriptionArabic;
    private String photoName;
    private String keyWords;
    private String videoURL;
    @OneToOne
    private AppDefaultStatementOfWork appDefaultStatementOfWork;
}