package lt.viko.eif.nlavkart.internetshop.util;

import lt.viko.eif.nlavkart.internetshop.models.Account;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HibernateTest {

    @Test
    void createSessionFactory() {
        /// Assign
        SessionFactory sessionFactory = null;
        StandardServiceRegistry registry = null;

        /// Act
        if (sessionFactory == null) {
            try {
                registry = new StandardServiceRegistryBuilder().configure().build();
                MetadataSources sources = new MetadataSources(registry);
                Metadata setData = sources.getMetadataBuilder().build();
                sessionFactory = setData.getSessionFactoryBuilder().build();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
            }
        }

        /// Assert
        assertNotNull(sessionFactory);
    }

    @Test
    void openTransaction() {
        /// Assign
        boolean isTransactionActive = false;
        SessionFactory sessionFactory = null;
        StandardServiceRegistry registry = null;
        Session session = null;
        Transaction transaction = null;

        if (sessionFactory == null) {
            try {
                registry = new StandardServiceRegistryBuilder().configure().build();
                MetadataSources sources = new MetadataSources(registry);
                Metadata setData = sources.getMetadataBuilder().build();
                sessionFactory = setData.getSessionFactoryBuilder().build();
            } catch (Exception e) {
                e.printStackTrace();
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
            }
        }


        /// Act
        if (!isTransactionActive) {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            isTransactionActive = true;
        }

        /// Assert
        assertTrue(isTransactionActive);
        if (transaction.getStatus() == TransactionStatus.ACTIVE) {
            assertTrue(true);
        } else {
            fail();
        }
        session.close();
    }

    @Test
    void closeTransaction() {
        /// Assign
        boolean isTransactionActive = false;
        SessionFactory sessionFactory = null;
        StandardServiceRegistry registry = null;
        Session session = null;
        Transaction transaction = null;

        if (sessionFactory == null) {
            try {
                registry = new StandardServiceRegistryBuilder().configure().build();
                MetadataSources sources = new MetadataSources(registry);
                Metadata setData = sources.getMetadataBuilder().build();
                sessionFactory = setData.getSessionFactoryBuilder().build();
            } catch (Exception e) {
                e.printStackTrace();
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
            }
        }
        if (!isTransactionActive) {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            isTransactionActive = true;
        }

        /// Act
        if (isTransactionActive) {
            transaction.commit();
            session.close();
            isTransactionActive = false;
        }

        /// Assert
        assertFalse(isTransactionActive);
        if (transaction.getStatus() == TransactionStatus.COMMITTED) {
            assertTrue(true);
        } else {
            fail();
        }
    }

    @Test
    void save() {
        /// Assign
        boolean isTransactionActive = false;
        SessionFactory sessionFactory = null;
        StandardServiceRegistry registry = null;
        Session session = null;
        Transaction transaction = null;

        if (sessionFactory == null) {
            try {
                registry = new StandardServiceRegistryBuilder().configure().build();
                MetadataSources sources = new MetadataSources(registry);
                Metadata setData = sources.getMetadataBuilder().build();
                sessionFactory = setData.getSessionFactoryBuilder().build();
            } catch (Exception e) {
                e.printStackTrace();
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
            }
        }
        if (!isTransactionActive) {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            isTransactionActive = true;
        }
        Account account = new Account();


        /// Act
        account.setId(0);
        session.save(account);


        /// Assert
        assertEquals(1, session.getStatistics().getEntityCount());
        if (isTransactionActive) {
            session.close();
            isTransactionActive = false;
        }
        assertFalse(isTransactionActive);
    }

    @Test
    public void query(){
        /// Assign
        boolean isTransactionActive = false;
        SessionFactory sessionFactory = null;
        StandardServiceRegistry registry = null;
        Session session = null;
        Transaction transaction = null;

        if (sessionFactory == null) {
            try {
                registry = new StandardServiceRegistryBuilder().configure().build();
                MetadataSources sources = new MetadataSources(registry);
                Metadata setData = sources.getMetadataBuilder().build();
                sessionFactory = setData.getSessionFactoryBuilder().build();
            } catch (Exception e) {
                e.printStackTrace();
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
            }
        }

        if (!isTransactionActive) {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            isTransactionActive = true;
        }

        Hibernate hibernate = new Hibernate();
        hibernate.createSessionFactory();
        hibernate.openTransaction();

        Account account = new Account();
        account.setUsername("QUICKTEST");
        account.setPassword("QUICKTEST");
        hibernate.save(account);
        hibernate.closeTransaction();

        hibernate.openTransaction();
        boolean resultType = true;
        String queryS = "FROM Account A WHERE A.username = \"QUICKTEST\"";
        List<Account> result = null;

        /// Act
        Query in;
        if (resultType) {
            in = session.createQuery(queryS, Account.class);
            result = in.list();
        } else {
            in = session.createQuery(queryS);
            in.executeUpdate();
        }

        /// Assert
        hibernate.query("DELETE FROM Account WHERE username = \"QUICKTEST\"", false);
        hibernate.closeTransaction();
        transaction.commit();
        session.close();
        assertEquals(1, result.size());
    }
}