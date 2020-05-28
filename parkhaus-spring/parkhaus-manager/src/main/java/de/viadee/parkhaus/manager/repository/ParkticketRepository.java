package de.viadee.parkhaus.manager.repository;

import de.viadee.parkhaus.manager.entity.Parkticket;

import java.util.Collection;

public interface ParkticketRepository {
    void persist(Parkticket parkticket);

    Collection<Parkticket> findAll();

    Parkticket findById(String id);
}
