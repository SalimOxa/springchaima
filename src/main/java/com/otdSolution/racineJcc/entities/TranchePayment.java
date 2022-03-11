package com.otdSolution.racineJcc.entities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "tranche_payement")

public class TranchePayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id =0;
    private String name;
    @Column(name="description",length = 1024)
    private String description;
    private Date deadline;
    private Date paymentDate;
    private Long price;
    private String currencyCode;
    private Integer numTranche;
    private Boolean etatPayement;
    private Boolean isLastPayement;
    private Integer idtask;
    private long idProject;
    @OneToOne(cascade = CascadeType.ALL)
    private AppStatementOfWork appStatementOfWork;


 




}
