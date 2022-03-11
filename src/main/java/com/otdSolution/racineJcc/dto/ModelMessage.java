package com.otdSolution.racineJcc.dto;

import com.otdSolution.racineJcc.entities.MessageChat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelMessage {
    private Collection<MessageChat> messages = new ArrayList<>();
    private int totalMessageIsNotShowed;
}
