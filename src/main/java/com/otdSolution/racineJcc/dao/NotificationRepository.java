package com.otdSolution.racineJcc.dao;


import com.otdSolution.racineJcc.entities.NotficationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RepositoryRestResource
public interface NotificationRepository extends JpaRepository<NotficationUser,Long> {

    @Query(value= "SELECT * FROM   app_notification_user    WHERE   app_notification_user.fk_app_notif  = :idUser  AND app_notification_user.is_showed =true",nativeQuery = true)
    public List<NotficationUser> findAllNotifIsNotShowed(String idUser);






    @Query(value="SELECT * FROM app_notification_user WHERE   app_notification_user.fk_app_notif  = :idUser ORDER BY  created_date DESC ",nativeQuery = true)
    public List<NotficationUser> getAllNotficationsByUser(String idUser);



 /*/   @Query(value="DELETE FROM app_notification_user WHERE  app_notification_user.id_update_custommer = :idUpdate ",nativeQuery = true)
    public Void deleteApprovedNotfication(String idUpdate);*/



    @Transactional
    @Modifying
    @Query("delete from NotficationUser l WHERE l.idNotficationForApprove =:#{#idUpdate}")
    void deleteNotificationApprove( @Param("idUpdate") String idUpdate);
}
