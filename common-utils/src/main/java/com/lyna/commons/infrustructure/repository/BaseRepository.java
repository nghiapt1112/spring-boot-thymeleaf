package com.lyna.commons.infrustructure.repository;

import com.lyna.commons.infrustructure.object.AbstractEntity;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

public class BaseRepository<E extends AbstractEntity, ID extends Serializable> extends SimpleJpaRepository<E, ID> {

    @PersistenceContext
    protected EntityManager entityManager;

    public BaseRepository(Class domainClass, EntityManager em) {
        super(domainClass, em);
        this.entityManager = em;

    }
}
