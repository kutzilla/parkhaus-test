package de.viadee.parkhaus.manager.repository;

import de.viadee.parkhaus.manager.entity.Parkticket;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Repository
@Transactional
public class ParkticketRepositoryImpl implements ParkticketRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void persist(Parkticket parkticket) {
        em.persist(parkticket);
    }

    @Override
    public Collection<Parkticket> findAll() {
        Query query = this.em.createQuery(
                "select s from Parkticket s");
        return query.getResultList();
    }

    @Override
    public Parkticket findById(String id) {
        Query query = this.em.createQuery("SELECT p FROM Parkticket p where p.id= :id");
        query.setParameter("id", id);
        return (Parkticket) query.getSingleResult();
    }
}
