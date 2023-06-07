package model.customer;

import java.util.regex.Pattern;

public class Customer {
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    private final String firstName;
    private final String lastName;
    private final String email;

    static {
        validateEmailPattern();
    }

    public Customer(final String firstName, final String lastName, final String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    private static void validateEmailPattern() {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    }

    public String getEmail() {
        return this.email;
    }

    @Override
    public String toString() {
        return "First Name: " + this.firstName
                + " Last Name: " + this.lastName
                + " Email Address: " + this.email;
    }
}
