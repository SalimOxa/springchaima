package com.otdSolution.racineJcc.dto;

import com.otdSolution.racineJcc.entities.AppProject;
import com.otdSolution.racineJcc.entities.AppTask;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Model {
    @Id @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;
    private Collection<AppProject> project = new ArrayList<>();
    private int totalPage;

}
