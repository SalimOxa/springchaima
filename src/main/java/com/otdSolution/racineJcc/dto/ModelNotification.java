package com.otdSolution.racineJcc.dto;
import com.otdSolution.racineJcc.entities.NotficationUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelNotification {
    private Collection<NotficationUser> notifications = new ArrayList<>();
    private int totalNotifIsNotShowed;
}
