package com.dmc30.empruntservice.data.repository;

import com.dmc30.empruntservice.data.entity.Emprunt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmpruntRepository extends JpaRepository<Emprunt, Long> {

    @Query(value = "SELECT * FROM emprunt ORDER BY id_ouvrage", nativeQuery = true)
    List<Emprunt> findAll();

    @Query(value = "SELECT * FROM emprunt WHERE id_utilisateur=?1 ORDER BY date_restitution", nativeQuery = true)
    List<Emprunt> findEmpruntByUtilisateurId(Long utilisateurId);

    @Query(value = "(select * from emprunt where restitution = false " +
            "and date_restitution < now() group by id_utilisateur, id) UNION " +
            "(select * from emprunt where restitution = false " +
            "and prolongation = true and date_prolongation < now()group by id_utilisateur, id) " +
            "order by date_emprunt",
            nativeQuery = true)
    List<Emprunt> findExpiredEmprunts();

    @Query(value = "(select id_utilisateur from emprunt where restitution = false " +
            "and date_restitution < now() group by id_utilisateur, id) UNION " +
            "(select id_utilisateur from emprunt where restitution = false " +
            "and prolongation = true and date_prolongation < now()group by id_utilisateur, id) " +
            "order by id_utilisateur",
            nativeQuery = true)
    List<Long> findUtilisateurEnRetard();

    @Query(value = "(select * from emprunt where restitution = false " +
            "and date_restitution < now() and id_utilisateur=?1) UNION " +
            "(select * from emprunt where restitution = false " +
            "and prolongation = true and date_prolongation < now() " +
            "and id_utilisateur=?1)",
            nativeQuery = true)
    List<Emprunt> findExpiredEmpruntsByUtilisateurId(Long utilisateurId);

    @Query(value = "SELECT * FROM emprunt WHERE id_ouvrage=?1 AND restitution=false ORDER BY id_ouvrage", nativeQuery = true)
    Emprunt getEmpruntEnCoursByOuvrageId(Long ouvrageId);

    // ---------------------Mail Service Methodes --------------------------

    @Query(value = "SELECT * FROM public.emprunt WHERE restitution=true AND date_restitution=CURRENT_DATE", nativeQuery = true)
    List<Emprunt> getEmpruntsRestitues();
}