package com.dmc30.livreservice.data.repository;

import com.dmc30.livreservice.data.entity.bibliotheque.Bibliotheque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BibliothequeRepository extends JpaRepository<Bibliotheque, Long> {

    List<Bibliotheque> findAll();

    Bibliotheque findBibliothequeById(Long bibliothequeId);
}
