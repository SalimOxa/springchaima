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
@Table(name="app_task_file")
public class TaskFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private byte[] data;
    private Date createdDate;

    public TaskFile (String fileName, byte[] data) {
        this.fileName = fileName;
        this.data = data;
    }
    @Column(length =30000000)
    @Basic(fetch=FetchType.EAGER)
    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
