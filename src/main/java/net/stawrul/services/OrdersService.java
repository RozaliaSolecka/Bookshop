package net.stawrul.services;

import net.stawrul.model.Book;
import net.stawrul.model.Order;
import net.stawrul.model.OrderedBooks;
import net.stawrul.services.exceptions.OrderedEmptyException;
import net.stawrul.services.exceptions.OutOfStockException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Komponent (serwis) biznesowy do realizacji operacji na zamówieniach.
 */
@Service
public class OrdersService extends EntityService<Order> {

    //Instancja klasy EntityManger zostanie dostarczona przez framework Spring
    //(wstrzykiwanie zależności przez konstruktor).
    public OrdersService(EntityManager em) {

        //Order.class - klasa encyjna, na której będą wykonywane operacje
        //Order::getId - metoda klasy encyjnej do pobierania klucza głównego
        super(em, Order.class, Order::getId);
    }

    /**
     * Pobranie wszystkich zamówień z bazy danych.
     *
     * @return lista zamówień
     */
    public List<Order> findAll() {
        return em.createQuery("SELECT o FROM Order o", Order.class).getResultList();
    }

    /**
     * Złożenie zamówienia w sklepie.
     * <p>
     * Zamówienie jest akceptowane, jeśli wszystkie objęte nim produkty są dostępne (przynajmniej 1 sztuka). W wyniku
     * złożenia zamówienia liczba dostępnych sztuk produktów jest zmniejszana o jeden. Metoda działa w sposób
     * transakcyjny - zamówienie jest albo akceptowane w całości albo odrzucane w całości. W razie braku produktu
     * wyrzucany jest wyjątek OutOfStockException.
     *
     * @param order zamówienie do przetworzenia
     */

    @Transactional
    public void placeOrder(Order order) throws OrderedEmptyException, OutOfStockException {

        if (order == null) {
            throw new OrderedEmptyException();
        } else if (order.getOrderedBooks().size() == 0) {
            throw new OrderedEmptyException();
        } else if (order.totalAmount() == 0 || order.totalAmount() > 2) {
            throw new OrderedEmptyException();
        }

        for (OrderedBooks orderedBooks : order.getOrderedBooks()) {
            Book book = em.find(Book.class, orderedBooks.getBook().getId());

            if (book.getAmount() < orderedBooks.getAmount()) {
                //wyjątek z hierarchii RuntineException powoduje wycofanie transakcji (rollback)
                throw new OutOfStockException();
            } else {
                int newAmount = book.getAmount() - orderedBooks.getAmount();
                book.setAmount(newAmount);
            }
        }

        //jeśli wcześniej nie został wyrzucony wyjątek OutOfStockException, zamówienie jest zapisywane w bazie danych
        save(order);
    }

}
