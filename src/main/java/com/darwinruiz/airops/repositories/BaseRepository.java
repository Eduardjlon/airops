package com.darwinruiz.airops.repositories;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.function.Consumer;

public abstract class BaseRepository<T, ID> {

    @Inject
    protected EntityManager em;

    protected abstract Class<T> entity();

    public T find(ID id) {
        return em.find(entity(), id);
    }

    public List<T> findAll() {
        String entityName = em.getMetamodel().entity(entity()).getName();

        return em.createQuery("select e from " + entityName + " e", entity())
                .getResultList();
    }

    public void tx(Consumer<EntityManager> work) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            work.accept(em);
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        }
    }

    public T save(T e) {
        tx(entityManager -> {
            Object id = entityManager.getEntityManagerFactory()
                    .getPersistenceUnitUtil()
                    .getIdentifier(e);
            if (id == null) {
                entityManager.persist(e);   // nuevo
            } else {
                entityManager.merge(e);     // existente
            }
        });
        return e;
    }

    public void delete(T e) {
        tx(entityManager -> entityManager.remove(
                entityManager.contains(e) ? e : entityManager.merge(e)
        ));
    }
}
