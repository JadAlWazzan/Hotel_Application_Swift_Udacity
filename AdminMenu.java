import api.AdminResource;
import model.customer.Customer;
import model.room.IRoom;
import model.room.Room;
import model.room.enums.RoomType;

import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;

public class AdminMenu {
    private static final AdminResource adminResource = AdminResource.getSingleton();
    private static final Scanner scanner = new Scanner(System.in);

    public static void adminMenu() {
        printMenu();

        while (true) {
            String line = scanner.nextLine();

            if (line.length() != 1) {
                System.out.println("Error: Invalid command\n");
                continue;
            }

            char action = line.charAt(0);

            switch (action) {
                case '1':
                    displayAllCustomers();
                    break;
                case '2':
                    displayAllRooms();
                    break;
                case '3':
                    displayAllReservations();
                    break;
                case '4':
                    addRoom();
                    break;
                case '5':
                    MainMenu.printMainMenu();
                    return;
                default:
                    System.out.println("Unknown command\n");
                    break;
            }
        }
    }

    private static void printMenu() {
        System.out.print("\nAdmin Menu\n" +
                "--------------------------------------------\n" +
                "1. See all the Customers\n" +
                "2. See all the Rooms\n" +
                "3. See all the Reservations\n" +
                "4. Add a Room to the hotel\n" +
                "5. Go back to Main Menu\n" +
                "--------------------------------------------\n" +
                "Please select a number from the menu option:\n");
    }

    private static void addRoom() {
        System.out.println("Enter a room number:");
        String roomNumber = scanner.nextLine();

        System.out.println("Enter a price per night:");
        double roomPrice = enterRoomPrice();

        System.out.println("Enter the room type: 1 for single a bed, 2 for double a bed:");
        RoomType roomType = enterRoomType();

        Room room = new Room(roomNumber, roomPrice, roomType);

        adminResource.addRooms(Collections.singletonList(room));
        System.out.println("Awesome. Room added successfully!");

        System.out.println("Would you like to add another room now? Y/N");
        addAnotherRoom();
    }

    private static double enterRoomPrice() {
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException exp) {
            System.out.println("This is not a valid price for the room! Please enter a valid number. Example: 100.00");
            return enterRoomPrice();
        }
    }

    private static RoomType enterRoomType() {
        try {
            return RoomType.valueOfLabel(scanner.nextLine());
        } catch (IllegalArgumentException exp) {
            System.out.println("This is not a valid room type! please choose 1 for a single bed room or 2 for a double bed room:");
            return enterRoomType();
        }
    }

    private static void addAnotherRoom() {
        String answer = scanner.nextLine();

        while (!answer.equalsIgnoreCase("Y") && !answer.equalsIgnoreCase("N")) {
            System.out.println("Please enter Y for (Yes) or N for (No)");
            answer = scanner.nextLine();
        }

        if (answer.equalsIgnoreCase("Y")) {
            addRoom();
        } else {
            printMenu();
        }
    }

    private static void displayAllRooms() {
        Collection<IRoom> rooms = adminResource.getAllRooms();

        if (rooms.isEmpty()) {
            System.out.println("No rooms were found.");
        } else {
            rooms.forEach(System.out::println);
        }
    }

    private static void displayAllCustomers() {
        Collection<Customer> customers = adminResource.getAllCustomers();

        if (customers.isEmpty()) {
            System.out.println("No customers were found.");
        } else {
            customers.forEach(System.out::println);
        }
    }

    private static void displayAllReservations() {
        adminResource.displayAllReservations();
    }
}
