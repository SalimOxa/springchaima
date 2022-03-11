package com.otdSolution.racineJcc.dao;


import com.otdSolution.racineJcc.entities.MessageChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface MessageChatRepository extends JpaRepository<MessageChat,Integer> {


    @Query(value= "SELECT * FROM   message_chat    WHERE   message_chat.fk_chat_message  = :idProject  AND  message_chat.id_receiver  = :idUser AND message_chat.is_showed =false  ",nativeQuery = true)
    public List<MessageChat> findAllMessagesIsNotShowed(String idUser , long idProject );


    @Query(value= "SELECT * FROM   message_chat    WHERE   message_chat.fk_chat_message  = :idProject   ORDER BY  message_chat.id  ASC ",nativeQuery = true)
    public List<MessageChat> findAllMessages( long idProject );
}
