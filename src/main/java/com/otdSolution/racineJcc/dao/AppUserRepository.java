package com.otdSolution.racineJcc.dao;

import com.otdSolution.racineJcc.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface  AppUserRepository <T extends AppUser>extends JpaRepository<T,String> {
     public AppUser findByUsername(String username);
     public AppUser findByEmail(String mail) ;

     @Query(value="SELECT * FROM app_user  WHERE user_type = 'MASTER'",nativeQuery = true)
     public List<AppMaster> findAllMasters();

     @Query(value="SELECT * FROM app_user  WHERE user_type = 'CUSTOMER'ORDER BY  date_creation  DESC ",nativeQuery = true)
     public List<AppCustomer> findAllCustomer();

     @Query(value="SELECT * FROM app_user WHERE  user_type = 'ADMIN' ORDER BY  date_creation  DESC ",nativeQuery = true)
     public List<AppAdmin> findAdmins();



}
