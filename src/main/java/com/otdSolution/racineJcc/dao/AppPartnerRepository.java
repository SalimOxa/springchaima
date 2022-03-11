package com.otdSolution.racineJcc.dao;


import com.otdSolution.racineJcc.entities.AppPartner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface AppPartnerRepository extends JpaRepository<AppPartner,Long> {
}