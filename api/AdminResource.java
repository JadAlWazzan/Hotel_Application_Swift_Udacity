package api;

import model.customer.Customer;
import model.room.IRoom;
import service.customer.CustomerService;
import service.reservation.ReservationService;

import java.util.Collection;
import java.util.List;

public class AdminResource {

    private static final AdminResource SINGLETON = new AdminResource();

    private final CustomerService customerService = CustomerService.getInstance();
    private final ReservationService reservationService = ReservationService.getInstance();

    private AdminResource() {}

    public static AdminResource getSingleton() {
        return SINGLETON;
    }

    public Customer getCustomer(String email) {
        return customerService.getCustomer(email);
    }

    public void addRooms(List<IRoom> rooms) {
        reservationService.addRooms(rooms);
    }

    public Collection<IRoom> getAllRooms() {
        return reservationService.getAllRooms();
    }

    public Collection<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    public void displayAllReservations() {
        reservationService.printAllReservations();
    }
}
