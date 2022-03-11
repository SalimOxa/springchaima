package com.otdSolution.racineJcc.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Table(name="app_pay_tranche_task")
public class PayTrancheTask {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;
    @OneToOne(cascade={CascadeType.ALL})
    @JoinColumn(name="tranchePayment")
    private TranchePayment tranchePayment;
}
