package service.reservation;

import model.customer.Customer;
import model.reservation.Reservation;
import model.room.IRoom;

import java.util.*;
import java.util.stream.Collectors;

public class ReservationService {
    private static final ReservationService INSTANCE = new ReservationService();
    private static final int RECOMMENDED_ROOMS_DEFAULT_PLUS_DAYS = 7;

    private final Map<String, IRoom> rooms = new HashMap<>();
    private final Map<String, Collection<Reservation>> reservations = new HashMap<>();

    private ReservationService() {}

    public static ReservationService getInstance() {
        return INSTANCE;
    }

    public void addRoom(IRoom room) {
        rooms.put(room.getRoomNumber(), room);
    }

    public void addRooms(List<IRoom> roomList) {
        for (IRoom room : roomList) {
            addRoom(room);
        }
    }

    public IRoom getARoom(String roomNumber) {
        return rooms.get(roomNumber);
    }

    public Collection<IRoom> getAllRooms() {
        return rooms.values();
    }

    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);
        reservations.computeIfAbsent(customer.getEmail(), k -> new LinkedList<>()).add(reservation);
        return reservation;
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
        return findAvailableRooms(checkInDate, checkOutDate);
    }

    public Collection<IRoom> findAlternativeRooms(Date checkInDate, Date checkOutDate) {
        return findAvailableRooms(addDefaultPlusDays(checkInDate), addDefaultPlusDays(checkOutDate));
    }

    public Collection<IRoom> findAvailableRooms(Date checkInDate, Date checkOutDate) {
        Set<IRoom> notAvailableRooms = reservations.values().stream()
                .flatMap(Collection::stream)
                .filter(reservation -> reservationOverlaps(reservation, checkInDate, checkOutDate))
                .map(Reservation::getRoom)
                .collect(Collectors.toSet());

        return rooms.values().stream()
                .filter(room -> !notAvailableRooms.contains(room))
                .collect(Collectors.toList());
    }

    public Date addDefaultPlusDays(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, RECOMMENDED_ROOMS_DEFAULT_PLUS_DAYS);
        return calendar.getTime();
    }

    private boolean reservationOverlaps(Reservation reservation, Date checkInDate, Date checkOutDate) {
        return checkInDate.before(reservation.getCheckOutDate()) && checkOutDate.after(reservation.getCheckInDate());
    }

    public Collection<Reservation> getCustomersReservation(Customer customer) {
        return reservations.getOrDefault(customer.getEmail(), Collections.emptyList());
    }

    public void printAllReservations() {
        Collection<Reservation> allReservations = reservations.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        if (allReservations.isEmpty()) {
            System.out.println("No reservations were found.");
        } else {
            allReservations.forEach(reservation -> System.out.println(reservation + "\n"));
        }
    }
}
