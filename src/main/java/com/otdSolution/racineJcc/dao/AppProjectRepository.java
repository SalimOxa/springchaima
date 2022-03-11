package com.otdSolution.racineJcc.dao;

import com.otdSolution.racineJcc.entities.AppProject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface AppProjectRepository extends JpaRepository<AppProject,Long> {

    @Query(value="SELECT * FROM app_project  WHERE id_admin IS NULL",nativeQuery = true)
public List<AppProject> findProjectsWithoutAdmin();

    @Query(value="SELECT * FROM app_project  WHERE id_master = :id_master",nativeQuery = true)
    public Page<AppProject> findProjectsByMasterPageble(@Param("id_master") String id_master, Pageable pageable);

    @Query(value="SELECT * FROM app_project  WHERE id_admin = :id_admin",nativeQuery = true)
    public Page<AppProject> findProjectsByAdminPageble(@Param("id_admin") String id_admin, Pageable pageable);


    @Query(value="SELECT * FROM app_project  WHERE id_master = :id_master",nativeQuery = true)
    public List<AppProject> findProjectsByMaster(@Param("id_master") String id_master);
    @Query(value="SELECT * FROM app_project  WHERE id_admin = :id_admin",nativeQuery = true)
    public List<AppProject> findProjectsByAdmin(@Param("id_admin") String id_admin);




    @Query (value = "SELECT * FROM app_project WHERE id_master = :id_master and name like %:nameProject% and name_customer like %:nameCustomer% and name_admin like %:nameAdmin%",nativeQuery = true)
    public Page<AppProject> findProjectsByMasterPagebleWithFilter(
            @Param("id_master")String id_master,
            @Param("nameProject") String nameProject,
            @Param("nameCustomer") String nameCustomer,
            @Param("nameAdmin") String nameAdmin,
            Pageable pageable);



    @Query (value = "SELECT * FROM app_project WHERE id_admin = :id_admin and name like %:nameProject% and name_customer like %:nameCustomer%",nativeQuery = true)
    public Page<AppProject> findProjectsByAdminPagebleWithFilter(
            @Param("id_admin")String id_admin,
            @Param("nameProject") String nameProject,
            @Param("nameCustomer") String nameCustomer,
            Pageable pageable);



    @Query(value="SELECT * FROM app_project  WHERE id_customer = :id_customer",nativeQuery = true)
    public List<AppProject> findProjectsByCustommer(@Param("id_customer") String id_customer);

}
