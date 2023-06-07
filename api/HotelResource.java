package api;

import model.customer.Customer;
import model.reservation.Reservation;
import model.room.IRoom;
import service.customer.CustomerService;
import service.reservation.ReservationService;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

public class HotelResource {
    private static final HotelResource SINGLETON = new HotelResource();

    private final CustomerService customerService = CustomerService.getInstance();
    private final ReservationService reservationService = ReservationService.getInstance();

    private HotelResource() {}

    public static HotelResource getSingleton() {
        return SINGLETON;
    }

    public Customer getCustomerByEmail(String email) {
        return customerService.getCustomer(email);
    }

    public void createACustomer(String email, String firstName, String lastName) {
        customerService.addCustomer(email, firstName, lastName);
    }

    public IRoom getRoom(String roomNumber) {
        return reservationService.getARoom(roomNumber);
    }

    public Reservation bookARoom(String customerEmail, IRoom room, Date checkInDate, Date checkOutDate) {
        Customer customer = getCustomerByEmail(customerEmail);
        return reservationService.reserveARoom(customer, room, checkInDate, checkOutDate);
    }

    public Collection<Reservation> getCustomersReservations(String customerEmail) {
        Optional<Customer> customer = Optional.ofNullable(getCustomerByEmail(customerEmail));
        return customer.map(reservationService::getCustomersReservation).orElse(Collections.emptyList());
    }

    public Collection<IRoom> findARoom(Date checkIn, Date checkOut) {
        return reservationService.findRooms(checkIn, checkOut);
    }

    public Collection<IRoom> findAlternativeRooms(Date checkIn, Date checkOut) {
        return reservationService.findAlternativeRooms(checkIn, checkOut);
    }

    public Date addDefaultPlusDays(Date date) {
        return reservationService.addDefaultPlusDays(date);
    }
}
