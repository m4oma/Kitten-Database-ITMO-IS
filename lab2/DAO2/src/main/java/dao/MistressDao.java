package dao;

import models.Mistress;
import config.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class MistressDao implements MistressDaoInterface {
    @Override
    public Mistress save(Mistress mistress) {
        EntityManager entityManager = JpaUtil.getEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        entityManager.persist(mistress);
        tx.commit();
        entityManager.close();
        return mistress;
    }

    @Override
    public void deleteById(int id) {
        EntityManager entityManager = JpaUtil.getEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        Mistress mistress = entityManager.find(Mistress.class, id);
        if (mistress != null) entityManager.remove(mistress);
        tx.commit();
        entityManager.close();
    }

    @Override
    public void deleteByEntity(Mistress mistress) {
        EntityManager entityManager = JpaUtil.getEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        Mistress attached = entityManager.merge(mistress);
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
        entityManager.createQuery("DELETE FROM Mistress").executeUpdate();
        tx.commit();
        entityManager.close();
    }

    @Override
    public Mistress update(Mistress mistress) {
        EntityManager entityManager = JpaUtil.getEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        Mistress merged = entityManager.merge(mistress);
        tx.commit();
        entityManager.close();
        return merged;
    }

    @Override
    public Mistress getById(int id) {
        EntityManager entityManager = JpaUtil.getEntityManager();
        Mistress mistress = entityManager.find(Mistress.class, id);
        entityManager.close();
        return mistress;
    }

    @Override
    public List<Mistress> getAll() {
        EntityManager entityManager = JpaUtil.getEntityManager();
        List<Mistress> list = entityManager.createQuery("SELECT m FROM Mistress m", Mistress.class).getResultList();
        entityManager.close();
        return list;
    }
}
