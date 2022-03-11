package com.otdSolution.racineJcc.dao;

import com.otdSolution.racineJcc.entities.AppQuote;
import com.otdSolution.racineJcc.entities.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface AppQuoteRepository extends JpaRepository<AppQuote,Long> {
}
