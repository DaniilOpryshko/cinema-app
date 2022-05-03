package cinema.service.impl;

import cinema.dao.MovieSessionDao;
import cinema.dao.ShoppingCartDao;
import cinema.dao.TicketDao;
import cinema.model.MovieSession;
import cinema.model.ShoppingCart;
import cinema.model.Ticket;
import cinema.model.User;
import cinema.service.ShoppingCartService;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartDao shoppingCartDao;
    private final TicketDao ticketDao;
    private final MovieSessionDao movieSessionDao;

    public ShoppingCartServiceImpl(ShoppingCartDao shoppingCartDao,
                                   TicketDao ticketDao,
                                   MovieSessionDao movieSessionDao) {
        this.shoppingCartDao = shoppingCartDao;
        this.ticketDao = ticketDao;
        this.movieSessionDao = movieSessionDao;
    }

    @Override
    public String addSession(MovieSession movieSession, User user) {
        String message = "No available seats";
        if (movieSession.getAvailableSeats() > 0) {
            {
                Ticket ticket = new Ticket();
                ticket.setMovieSession(movieSession);
                ticket.setUser(user);
                ShoppingCart shoppingCart = shoppingCartDao.getByUser(user);
                ticketDao.add(ticket);
                shoppingCart.getTickets().add(ticket);
                movieSession.setAvailableSeats(movieSession.getAvailableSeats() - 1);
                movieSessionDao.update(movieSession);
                shoppingCartDao.update(shoppingCart);
                message = "Successfully added";
            }
        }
        return message;
    }

    @Override
    public String removeSession(MovieSession movieSession, User user) {
        String message = "No such session in shopping cart";
        ShoppingCart shoppingCart = shoppingCartDao.getByUser(user);
        Optional<Ticket> ticketOpt = shoppingCart.getTickets().stream()
                .filter(t -> t.getMovieSession().equals(movieSession))
                .findFirst();
        if (ticketOpt.isPresent()) {
            Ticket ticket = ticketOpt.get();
            shoppingCart.getTickets().remove(ticket);
            shoppingCartDao.update(shoppingCart);
            ticketDao.delete(ticket.getId());
            movieSession.setAvailableSeats(movieSession.getAvailableSeats() + 1);
            movieSessionDao.update(movieSession);
            message = "Successfully removed";
        }
        return message;
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDao.getByUser(user);
    }

    @Override
    public void registerNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartDao.add(shoppingCart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        shoppingCart.setTickets(null);
        shoppingCartDao.update(shoppingCart);
    }
}
