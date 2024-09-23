package core.basesyntax.dao.impl;

import core.basesyntax.dao.SmileDao;
import core.basesyntax.exception.DataProcessingException;
import core.basesyntax.model.Smile;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class SmileDaoImpl extends AbstractDao implements SmileDao {
    public SmileDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Smile create(Smile entity) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = factory.openSession();
            transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException(
                    "Can't add the smile " + entity + " to database", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return entity;
    }

    @Override
    public Smile get(Long id) {
        Session session = factory.openSession();
        Smile smile = session.get(Smile.class, id);
        session.close();
        return smile;
    }

    @Override
    public List<Smile> getAll() {
        Session session = null;
        try {
            session = factory.openSession();
            Query<Smile> allSmilesQuery = session.createQuery("from Smile", Smile.class);
            return allSmilesQuery.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException(
                    "Couldn't get all smiles from database", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
