import api.HotelResource;
import model.reservation.Reservation;
import model.room.IRoom;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

public class MainMenu {
    private static final String DEFAULT_DATE_FORMAT = "MM/dd/yyyy";
    private static final HotelResource hotelResource = HotelResource.getSingleton();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        startMainMenu();
    }

    public static void startMainMenu() {
        printMainMenu();

        String line = scanner.nextLine();
        while (!line.equals("5")) {
            switch (line) {
                case "1":
                    findAndReserveRoom();
                    break;
                case "2":
                    seeMyReservation();
                    break;
                case "3":
                    createAccount();
                    break;
                case "4":
                    AdminMenu.adminMenu();
                    break;
                default:
                    System.out.println("Unknown command.\n");
            }
            printMainMenu();
            line = scanner.nextLine();
        }
        System.out.println("Exit");
    }

    private static void findAndReserveRoom() {
        System.out.println("Please enter your Check In Date (MM/dd/yyyy):");
        Date checkIn = getInputDate();

        System.out.println("Please enter your Check Out Date (MM/dd/yyyy):");
        Date checkOut = getInputDate();

        if (checkIn != null && checkOut != null) {
            Collection<IRoom> availableRooms = hotelResource.findARoom(checkIn, checkOut);

            if (availableRooms.isEmpty()) {
                Collection<IRoom> alternativeRooms = hotelResource.findAlternativeRooms(checkIn, checkOut);

                if (alternativeRooms.isEmpty()) {
                    System.out.println("Unfortunately, no rooms were found.");
                } else {
                    final Date alternativeCheckIn = hotelResource.addDefaultPlusDays(checkIn);
                    final Date alternativeCheckOut = hotelResource.addDefaultPlusDays(checkOut);
                    System.out.println("We've only found some rooms on some alternative dates:" +
                            "\nCheck In Date: " + formatDate(alternativeCheckIn) +
                            "\nCheck Out Date: " + formatDate(alternativeCheckOut));

                    printRooms(alternativeRooms);
                    reserveRoom(alternativeCheckIn, alternativeCheckOut, alternativeRooms);
                }
            } else {
                printRooms(availableRooms);
                reserveRoom(checkIn, checkOut, availableRooms);
            }
        }
    }

    private static Date getInputDate() {
        try {
            String inputDate = scanner.nextLine();
            return new SimpleDateFormat(DEFAULT_DATE_FORMAT).parse(inputDate);
        } catch (ParseException ex) {
            System.out.println("Error: Invalid Date. Please enter a valid date.");
            return getInputDate();
        }
    }

    private static void reserveRoom(Date checkInDate, Date checkOutDate, Collection<IRoom> rooms) {
        System.out.println("Would you like to book a room with us? (y/n)");
        String bookRoom = scanner.nextLine();

        if ("y".equalsIgnoreCase(bookRoom)) {
            System.out.println("Do you already have an account with us? (y/n)");
            String haveAccount = scanner.nextLine();

            if ("y".equalsIgnoreCase(haveAccount)) {
                System.out.println("Enter your Email.");
                String customerEmail = scanner.nextLine();

                if (hotelResource.getCustomerByEmail(customerEmail) == null) {
                    System.out.println("Customer email not found.\nYou may need to create a new account.");
                } else {
                    System.out.println("What is the room number that you would like to reserve?");
                    String roomNumber = scanner.nextLine();

                    if (rooms.stream().anyMatch(room -> room.getRoomNumber().equals(roomNumber))) {
                        IRoom room = hotelResource.getRoom(roomNumber);

                        Reservation reservation = hotelResource.bookARoom(customerEmail, room, checkInDate, checkOutDate);
                        System.out.println("Your Reservation was created successfully!");
                        System.out.println(reservation);
                    } else {
                        System.out.println("Error: the room number is not available!\nPlease start the reservation again.");
                    }
                }

                printMainMenu();
            } else {
                System.out.println("Please create an account with us.");
                printMainMenu();
            }
        } else if ("n".equalsIgnoreCase(bookRoom)) {
            printMainMenu();
        } else {
            reserveRoom(checkInDate, checkOutDate, rooms);
        }
    }

    private static void seeMyReservation() {
        System.out.println("Please enter your Email address: ");
        String customerEmail = scanner.nextLine();

        printReservations(hotelResource.getCustomersReservations(customerEmail));
    }

    private static void printReservations(Collection<Reservation> reservations) {
        if (reservations == null || reservations.isEmpty()) {
            System.out.println("Unfortunately, no reservations were found.");
        } else {
            reservations.forEach(reservation -> System.out.println("\n" + reservation));
        }
    }

    private static void createAccount() {
        System.out.println("Enter an Email address:");
        String email = scanner.nextLine();

        System.out.println("Enter your first name:");
        String firstName = scanner.nextLine();

        System.out.println("Enter your last Name:");
        String lastName = scanner.nextLine();

        try {
            hotelResource.createACustomer(email, firstName, lastName);
            System.out.println("Your account was created successfully!");

            printMainMenu();
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getLocalizedMessage());
            createAccount();
        }
    }

    private static void printRooms(Collection<IRoom> rooms) {
        if (rooms.isEmpty()) {
            System.out.println("No rooms were found.");
        } else {
            rooms.forEach(System.out::println);
        }
    }

    static void printMainMenu() {
        System.out.println("\nWelcome to Hotello, the Hotel Reservation Application");
        System.out.println("--------------------------------------------");
        System.out.println("1. Find or reserve a room");
        System.out.println("2. See your reservations");
        System.out.println("3. Create an Account");
        System.out.println("4. Admin settings");
        System.out.println("5. Exit application");
        System.out.println("--------------------------------------------");
        System.out.println("Please select a number from the menu option:");
    }

    private static String formatDate(Date date) {
        return new SimpleDateFormat(DEFAULT_DATE_FORMAT).format(date);
    }
}
