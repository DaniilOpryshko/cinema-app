package cinema.dao.impl;

import cinema.dao.OrderDao;
import cinema.exception.DataProcessingException;
import cinema.lib.Dao;
import cinema.model.Order;
import cinema.model.User;
import cinema.util.HibernateUtil;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

@Dao
public class OrderDaoImpl implements OrderDao {
    @Override
    public Order add(Order order) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(order);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't insert an order:" + order, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return order;
    }

    @Override
    public List<Order> getUserOrders(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Order> getUserOrdersQuery = session.createQuery("from Order o "
                    + "join fetch o.user "
                    + "join fetch o.tickets t "
                    + "join fetch t.movieSession ms "
                    + "join fetch ms.cinemaHall "
                    + "join fetch ms.movie "
                    + "where o.user = :user", Order.class);
            getUserOrdersQuery.setParameter("user", user);
            return getUserOrdersQuery.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get user's " + user + " orders", e);
        }
    }
}
