package dao;

import models.Kitten;
import config.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;

public class KittenDao implements KittenDaoInterface {
    @Override
    public Kitten save(Kitten kitten) {
        EntityManager entityManager = JpaUtil.getEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        entityManager.persist(kitten);
        tx.commit();
        entityManager.close();
        return kitten;
    }

    @Override
    public void deleteById(int id) {
        EntityManager entityManager = JpaUtil.getEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        Kitten kitten = entityManager.find(Kitten.class, id);
        if (kitten != null) entityManager.remove(kitten);
        tx.commit();
        entityManager.close();
    }

    @Override
    public void deleteByEntity(Kitten kitten) {
        EntityManager entityManager = JpaUtil.getEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        Kitten attached = entityManager.merge(kitten);
        entityManager.remove(attached);
        tx.commit();
        entityManager.close();
    }

    @Override
    public void deleteAll() {
        EntityManager entityManager = JpaUtil.getEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        entityManager.createQuery("DELETE FROM Kitten").executeUpdate();
        tx.commit();
        entityManager.close();
    }

    @Override
    public Kitten update(Kitten kitten) {
        EntityManager entityManager = JpaUtil.getEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        Kitten merged = entityManager.merge(kitten);
        tx.commit();
        entityManager.close();
        return merged;
    }

    @Override
    public Kitten getById(int id) {
        EntityManager entityManager = JpaUtil.getEntityManager();
        Kitten kitten = entityManager.find(Kitten.class, id);
        entityManager.close();
        return kitten;
    }

    @Override
    public List<Kitten> getAll() {
        EntityManager entityManager = JpaUtil.getEntityManager();
        List<Kitten> list = entityManager.createQuery("SELECT k FROM Kitten k", Kitten.class).getResultList();
        entityManager.close();
        return list;
    }
}
