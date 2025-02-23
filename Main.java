import java.util.*;
import java.util.regex.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class Main {
    private static Scanner sc = new Scanner(System.in);
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Booking> bookings = new ArrayList<>();
    boolean isLoggedin = false;
    User loggedInUser = null;
    int count = 0;

    public boolean isValidDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        try {
            LocalDate.parse(date, formatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isValidUserId(String userId) {
        if (userId.length() < 5 || userId.length() > 20) {
            System.out.println("Invalid User Id");
            return false;
        }
        return true;
    }

    public boolean isValidUserName(String userName) {
        if (userName.length() < 1 || userName.length() > 50) {
            System.out.println("Invalide Customer Name!.");
            return false;
        }
        return true;
    }

    public boolean validateRole(String role) {
        if (role.equalsIgnoreCase("customer") || role.equalsIgnoreCase("officer")) {
            return true;
        }
        System.out.println("Invalide Role!.");
        System.out.println("Please select valid role.");
        return false;
    }

    public boolean validateEmail(String email) {
        try {
            if (email == null || email.isEmpty()) {
                throw new IllegalArgumentException("Email cannot be null or empty");
            }
            String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
            Pattern pattern = Pattern.compile(emailRegex);
            Matcher matcher = pattern.matcher(email);
            if (!matcher.matches()) {
                throw new IllegalArgumentException("Invalid email format");
            }
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public boolean validatePassword(String password) {
        if (password.length() < 8 || password.length() > 30) {
            return false;
        }
        boolean hasLower = password.matches(".*[a-z].*");
        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasSymbol = password.matches(".*[\\W_].*");
        return hasLower && hasUpper && hasDigit && hasSymbol;
    }

    public boolean validateConfirmPassword(String confirmPassword, String password) {
        if (password.equals(confirmPassword)) {
            return true;
        }
        System.out.println("Password and confirm password is different!.");
        return false;
    }

    public void displayUserDetails(String userId) {
        for (User user : users) {
            String newUserId = user.getUserId();
            if (newUserId.equals(userId)) {
                user.displayDetails();
            }
        }
        return;
    }

    public boolean registration() {
        String password = "";
        String role = "";
        String email = "";
        String name = "";
        String confirmPassword = "";
        String mailingAddress = "";
        String zipcode = "";
        long mobileNumber = 0;
        String userld = "";
        boolean passwordCheck = false;
        boolean confirmPasswordCheck = false;
        boolean emailCheck = false;
        boolean roleCheck = false;
        System.out.println();
        System.out.println("Registation Page.");
        System.out.println("Please specify your role\nCustomer\nOfficer");
        do {
            role = sc.nextLine();
            if (validateRole(role)) {
                roleCheck = true;
            } else {
                roleCheck = false;
            }
        } while (!roleCheck);
        boolean userNameCheck = true;
        System.out.println("Enter your name");
        do {
            name = sc.nextLine();
            if (isValidUserName(name)) {
                userNameCheck = true;
            } else {
                userNameCheck = false;
            }
        } while (!userNameCheck);

        System.out.println("Enter your User ID");
        boolean userldCheck = true;
        do {
            userld = sc.nextLine();
            if (isValidUserId(userld)) {
                userldCheck = true;
            } else {
                userldCheck = false;
            }
        } while (!userldCheck);

        System.out.println("Enter your email.");
        do {
            email = sc.nextLine();
            if (validateEmail(email)) {
                emailCheck = true;
            } else {
                emailCheck = false;
            }
        } while (!emailCheck);
        System.out.println("Enter you password");
        System.out.println(
                "Password should contains atleast one upper character, one lower character, one number and one special character.");
        do {
            password = sc.nextLine();
            if (validatePassword(password)) {
                passwordCheck = true;
            } else {
                System.out.println("Invalid Password");
                passwordCheck = false;
            }
        } while (!passwordCheck);
        System.out.println("Enter your Confirm password");
        do {
            confirmPassword = sc.nextLine();
            if (validateConfirmPassword(confirmPassword, password)) {
                confirmPasswordCheck = true;
            } else {
                confirmPasswordCheck = false;
            }
        } while (!confirmPasswordCheck);
        System.out.println("Enter your mobile number");
        mobileNumber = sc.nextLong();
        sc.nextLine();
        System.out.println("Mailing Address");
        mailingAddress = sc.nextLine();
        System.out.println("Enter your Zip Code Name");
        zipcode = sc.nextLine();
        users.add(new User(email, name, mobileNumber, new Address(mailingAddress, zipcode), userld, password, role));
        System.out.println("Registration successfull");
        return true;
    }

    public boolean validateLogin(String email, String password) {
        for (User user : users) {
            if (email.equals(user.getEmail()) && password.equals(user.getPassword())) {
                return true;
            }
        }
        return false;
    }

    public boolean login() {
        if (isLoggedin) {
            return true;
        }
        String email = "";
        String password = "";
        boolean passwordCheck = false;
        System.out.println();
        System.out.println("Login Page");
        System.out.println("Enter your registerd email");
        email = sc.nextLine();
        System.out.println("Enter your password");
        do {
            password = sc.nextLine();
            if (validatePassword(password)) {
                passwordCheck = true;
            } else {
                System.out.println("Invalid Password!.");
                passwordCheck = false;
            }
        } while (!passwordCheck);
        if (!validateLogin(email, password)) {
            isLoggedin = false;
            System.out.println("LoginIn Failed!..");
            System.out.println("No User found for this credential");
            return false;
        }
        isLoggedin = true;
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                loggedInUser = user;
            }
        }
        System.out.println("LoggedIn Successfull!..");
        return true;
    }

    public void bookingService() {
        String recName = "";
        String recAddress = "";
        String recZipCode = "";
        long recMobile = 0;
        double parWeight = 0;
        double cost = 0;
        String parDescription = "";
        String parDeliveryType = "";
        String parPackingPreference = "";
        String parPickupTime = "";
        String parDropTime = "";
        boolean pickUpCheck = true;
        boolean dropUpCheck = true;
        System.out.println();
        System.out.println("Welcome to Booking Services Pages");
        System.out.println("Enter Receiver's Name: ");
        recName = sc.nextLine();
        System.out.println("Enter Receiver's Address");
        recAddress = sc.nextLine();
        System.out.println("Enter Receiver's Zip Code: ");
        recZipCode = sc.nextLine();
        System.out.println("Enter Receiver's Mobile: ");
        recMobile = sc.nextLong();
        sc.nextLine();
        System.out.println("Enter Weight of the Parcel in grams: ");
        parWeight = sc.nextDouble();
        cost = cost + parWeight * 0.25;
        sc.nextLine();
        System.out.println("Enter Description of the Parcel: ");
        parDescription = sc.nextLine();
        boolean type = true;
        do {
            System.out.println("Enter Delivery Type: ");
            System.out.println("[1] Standard");
            System.out.println("[2] Express");
            int choiceType = sc.nextInt();
            sc.nextLine();
            switch (choiceType) {
                case 1:
                    parDeliveryType = "Standard";
                    type = false;
                    break;
                case 2:
                    parDeliveryType = "Express";
                    cost = cost + 100;
                    type = false;
                    break;
                default:
                    System.out.println("Invalid Response");
                    type = true;
                    break;
            }
        } while (type);
        boolean packType = true;
        do {
            System.out.println("Enter Packing preference Parcel : ");
            System.out.println("[1] Standard Box");
            System.out.println("[2] Waterproof");
            System.out.println("[3] Tamper-Proof");
            System.out.println("[4] Eco-Friendly");
            int packingType = sc.nextInt();
            sc.nextLine();
            switch (packingType) {
                case 1:
                    parPackingPreference = "Normal";
                    packType = false;
                    break;
                case 2:
                    parPackingPreference = "Waterproof";
                    cost = cost + 50;
                    packType = false;
                    break;
                case 3:
                    parPackingPreference = "Tamper-Proof";
                    cost = cost + 70;
                    packType = false;
                    break;
                case 4:
                    parPackingPreference = "Eco-Friendly";
                    cost = cost + 80;
                    packType = false;
                    break;
                default:
                    System.out.println("Invalid Response");
                    packType = true;
                    break;
            }
        } while (packType);
        System.out.println("Enter Pickup Date of the Parcel (dd-MM-yyyy): ");
        do {
            parPickupTime = sc.nextLine();
            if (isValidDate(parPickupTime)) {
                pickUpCheck = false;
            } else {
                System.out.println("Invalid format!..");
                pickUpCheck = true;
            }
        } while (pickUpCheck);
        System.out.println("Enter Drop Date of the Parcel (dd-MM-yyyy): ");
        do {
            parDropTime = sc.nextLine();
            if (isValidDate(parDropTime)) {
                dropUpCheck = false;
            } else {
                System.out.println("Invalid format!..");
                dropUpCheck = true;
            }
        } while (dropUpCheck);
        LocalDateTime paymentTime = LocalDateTime.now();
        ;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");
        String payment_time = paymentTime.format(formatter);
        String bookingId = "BookingId" + (count++);
        bookings.add(new Booking(loggedInUser.getUserId(), recName, new Address(recAddress, recZipCode), recMobile,
                parWeight, parDescription, parDeliveryType, parPackingPreference, parPickupTime, parDropTime, cost,
                payment_time, bookingId, payment_time, "Booked"));
        System.out.println("Booking is Successfull!!!");
        generateInvoice(bookingId);
    }

    public void bookingServiceOfficer() {
        String recName = "";
        String recAddress = "";
        String recZipCode = "";
        long recMobile = 0;
        double parWeight = 0;
        double cost = 0;
        String parDescription = "";
        String parDeliveryType = "";
        String parPackingPreference = "";
        String parPickupTime = "";
        String parDropTime = "";
        boolean pickUpCheck = true;
        boolean dropUpCheck = true;
        System.out.println();
        System.out.println("Welcome to Booking Services Pages");
        System.out.println("Enter Sender's Name: ");
        String senderName = sc.nextLine();
        System.out.println("Enter Sender's Address");
        String senderAddress = sc.nextLine();
        System.out.println("Enter Sender's Zip Code: ");
        String senderZipCode = sc.nextLine();
        System.out.println("Enter Sender's Mobile: ");
        long senderMobile = sc.nextLong();
        sc.nextLine();
        boolean validUser = false;
        for (User tempUser : users) {
            if (tempUser.getName().equalsIgnoreCase(senderName)) {
                validUser = true;
            }
        }
        if (!validUser) {
            return;
        }
        System.out.println("Enter Receiver's Name: ");
        recName = sc.nextLine();
        System.out.println("Enter Receiver's Address");
        recAddress = sc.nextLine();
        System.out.println("Enter Receiver's Zip Code: ");
        recZipCode = sc.nextLine();
        System.out.println("Enter Receiver's Mobile: ");
        recMobile = sc.nextLong();
        sc.nextLine();
        System.out.println("Enter Weight of the Parcel in grams: ");
        parWeight = sc.nextDouble();
        cost = cost + parWeight * 0.25;
        sc.nextLine();
        System.out.println("Enter Description of the Parcel: ");
        parDescription = sc.nextLine();
        boolean type = true;
        do {
            System.out.println("Enter Delivery Type: ");
            System.out.println("[1] Standard");
            System.out.println("[2] Express");
            int choiceType = sc.nextInt();
            sc.nextLine();
            switch (choiceType) {
                case 1:
                    parDeliveryType = "Standard";
                    type = false;
                    break;
                case 2:
                    parDeliveryType = "Express";
                    cost = cost + 100;
                    type = false;
                    break;
                default:
                    System.out.println("Invalid Response");
                    type = true;
                    break;
            }
        } while (type);
        boolean packType = true;
        do {
            System.out.println("Enter Packing preference Parcel : ");
            System.out.println("[1] Standard Box");
            System.out.println("[2] Waterproof");
            System.out.println("[3] Tamper-Proof");
            System.out.println("[4] Eco-Friendly");
            int packingType = sc.nextInt();
            sc.nextLine();
            switch (packingType) {
                case 1:
                    parPackingPreference = "Normal";
                    packType = false;
                    break;
                case 2:
                    parPackingPreference = "Waterproof";
                    cost = cost + 50;
                    packType = false;
                    break;
                case 3:
                    parPackingPreference = "Tamper-Proof";
                    cost = cost + 70;
                    packType = false;
                    break;
                case 4:
                    parPackingPreference = "Eco-Friendly";
                    cost = cost + 80;
                    packType = false;
                    break;
                default:
                    System.out.println("Invalid Response");
                    packType = true;
                    break;
            }
        } while (type);
        System.out.println("Enter Pickup Date of the Parcel (dd-MM-yyyy): ");
        do {
            parPickupTime = sc.nextLine();
            if (isValidDate(parPickupTime)) {
                pickUpCheck = false;
            } else {
                System.out.println("Invalid format!..");
                pickUpCheck = true;
            }
        } while (pickUpCheck);
        System.out.println("Enter Drop Date of the Parcel (dd-MM-yyyy): ");
        do {
            parDropTime = sc.nextLine();
            if (isValidDate(parDropTime)) {
                dropUpCheck = false;
            } else {
                System.out.println("Invalid format!..");
                dropUpCheck = true;
            }
        } while (dropUpCheck);
        LocalDateTime paymentTime = LocalDateTime.now();
        ;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");
        String payment_time = paymentTime.format(formatter);
        String bookingId = "BookingId" + (count++);
        bookings.add(new Booking(loggedInUser.getUserId(), recName, new Address(recAddress, recZipCode), recMobile,
                parWeight, parDescription, parDeliveryType, parPackingPreference, parPickupTime, parDropTime, cost,
                payment_time, bookingId, payment_time, "Booked"));
        System.out.println("Booking is Successfull!!!");
        generateInvoice(bookingId);
    }

    public void generateInvoice(String bookingId) {
        for (Booking book : bookings) {
            if (book.getBookingId().equals(bookingId)) {
                book.displayInvoice();
            }
        }

    }

    public void displaySenderDetails(String userId) {
        for (User tempUser : users) {
            if (tempUser.getUserId().equals(userId)) {
                System.out.println();
                System.out.println("Sender Details");
                System.out.println("Name : " + tempUser.getName());
                System.out.println("Address :");
                System.out.println(tempUser.address.mailingAddress + " " + tempUser.address.zipcode);
            }
        }
    }

    public void home() {
        System.out.println("Home");
    }

    public void tracking(String bookingId) {
        if (bookings.size() == 0) {
            System.out.println("No record available");
            return;
        }
        for (Booking booking : bookings) {
            if (booking.getBookingId().equals(bookingId)) {
                if (!booking.getUserId().equals(loggedInUser.getUserId())) {
                    System.out.println("Customer is only allowed to track their own parcel");
                    return;
                }
                System.out.println("Tracking Details: ");
                System.out.println();
                displaySenderDetails(booking.getUserId());
                booking.displayTrackingDetails();
                return;
            }
        }
        System.out.println("No record found");
    }

    public void trackingOfficer(String bookingId) {
        if (bookings.size() == 0) {
            System.out.println("No recorde found");
            return;
        }
        for (Booking booking : bookings) {
            if (booking.getBookingId().equals(bookingId)) {
                System.out.println("Tracking Details: ");
                System.out.println();
                displaySenderDetails(booking.getUserId());
                booking.displayTrackingDetails();
                return;
            }
        }
        System.out.println("No record found");
    }

    public void previousBooking(String userld) {
        if (bookings.size() == 0) {
            System.out.println("No recorde found");
            return;
        }
        ArrayList<Booking> tempBooking = new ArrayList<>();
        for (Booking booking : bookings) {
            String tempUserld = booking.getUserId();
            if (tempUserld.equals(userld)) {
                tempBooking.add(booking);
            }
        }
        if (tempBooking.size() > 0) {
            System.out.println("Customer Id: " + loggedInUser.getUserId());
        }
        for (int i = tempBooking.size() - 1; i >= 0; i--) {
            tempBooking.get(i).displayPreviosBookings();
            System.out.println();
        }
    }

    public void previousBookingOfficer(String userld) {
        if (bookings.size() == 0) {
            System.out.println("No recorde found");
            return;
        }
        ArrayList<Booking> tempBooking = new ArrayList<>();
        for (Booking booking : bookings) {
            String tempUserld = booking.getUserId();
            if (tempUserld.equals(userld)) {
                tempBooking.add(booking);
            }
        }
        if (tempBooking.size() > 0) {
            System.out.println("Customer Id: " + loggedInUser.getUserId());
        }
        for (int i = tempBooking.size() - 1; i >= 0; i--) {
            tempBooking.get(i).displayPreviosBookings();
            System.out.println();
        }
    }

    public void updateStatus(String bookingId) {
        if (bookings.size() == 0) {
            System.out.println("No recorde found");
            return;
        }
        for (Booking booking : bookings) {
            if (booking.getBookingId().equals(bookingId)) {
                System.out.println("Select the status of Parcel ");
                System.out.println("[1] Booked\n[2] In Transit\n[3] Delivered\n[4] Returned");
                boolean updateCheck = true;
                do {
                    int updateChoice = sc.nextInt();
                    sc.nextLine();
                    switch (updateChoice) {
                        case 1:
                            booking.setStatus("Booked");
                            updateCheck = false;
                            break;
                        case 2:
                            booking.setStatus("In Transit");
                            updateCheck = false;
                            break;
                        case 3:
                            booking.setStatus("Delivered");
                            updateCheck = false;
                            break;
                        case 4:
                            booking.setStatus("Returned");
                            updateCheck = false;
                            break;
                        default:
                            System.out.println("Invalide response.!!");
                            updateCheck = true;
                            break;
                    }
                } while (updateCheck);
                displaySenderDetails(booking.getUserId());
                booking.displayTrackingDetails();
                return;
            }
        }
        System.out.println("No record found");
    }

    public void updateTime(String bookingId) {
        if (bookings.size() == 0) {
            System.out.println("No recorde found");
            return;
        }
        for (Booking booking : bookings) {
            if (booking.getBookingId().equals(bookingId)) {
                System.out.println("Enter Pick Up Time: ");
                var pickUp = sc.nextLine();
                System.out.println("Enter Drop Off Time: ");
                var dropOff = sc.nextLine();
                booking.setParPickupTime(pickUp);
                booking.setParDropoffTime(dropOff);
                displaySenderDetails(booking.getUserId());
                booking.displayTrackingDetails();
                return;
            }
        }
        System.out.println("No record found");
    }

    public void contactSupport() {
        System.out.println();
        System.out.println("Contact Details");
        System.out.println("Email: abc@gmail.com");
        System.out.println("Mobile No: 9090909090");
        System.out.println("WhiteField, Banglore-500031");
    }

    public void logout() {
        System.out.println("Logout");
        loggedInUser = null;
        isLoggedin = false;
    }

    public static void main(String[] args) {
        Main app = new Main();
        boolean flag = true;
        boolean dashFlag = false;
        System.out.println("Parcel Management");
        while (flag) {
            System.out.println();
            System.out.println("Menu");
            System.out.println("[1] Registration.\n[2] Login.\n[3] Exit.");
            int option = sc.nextInt();
            sc.nextLine();
            switch (option) {
                case 1:
                    app.registration();
                    break;
                case 2:
                    if (app.login()) {
                        dashFlag = true;
                    }
                    ;
                    break;
                case 3:
                    System.out.println("Exit");
                    flag = false;
                    break;
                default:
                    System.out.println("Invalid option please choose a valid option.");
                    flag = true;
                    break;
            }
            while (dashFlag) {
                if (app.loggedInUser.getRole().equalsIgnoreCase("customer")) {
                    System.out.println();
                    System.out.println("Welcome " + app.loggedInUser.getName());
                    System.out.println(
                            "[1] Home\n[2] Booking service\n[3] Tracking\n[4] Previous Booking\n[5] Contact Support\n[6] Logout");
                    int customerChoice = sc.nextInt();
                    sc.nextLine();
                    switch (customerChoice) {
                        case 1:
                            app.home();
                            break;
                        case 2:
                            app.bookingService();
                            break;
                        case 3:
                            System.out.println("Enter booking Id to Track: ");
                            var bid = sc.nextLine();
                            app.tracking(bid);
                            break;
                        case 4:
                            app.previousBooking(app.loggedInUser.getUserId());
                            break;
                        case 5:
                            app.contactSupport();
                            break;
                        case 6:
                            app.logout();
                            dashFlag = false;
                            break;
                    }
                } else {
                    System.out.println();
                    System.out.println("Welcome " + app.loggedInUser.getName());
                    System.out.println(
                            "[1] Home\n[2] Tracking\n[3] Delivery Status\n[4] Booking Service\n[5] Pickup Scheduling\n[6] Previous Booking\n[7] Logout");
                    int officerChoice = sc.nextInt();
                    sc.nextLine();
                    switch (officerChoice) {
                        case 1:
                            app.home();
                            break;
                        case 2:
                            System.out.println("Enter booking Id to Track: ");
                            var bld = sc.nextLine();
                            app.trackingOfficer(bld);
                            break;
                        case 3:
                            System.out.println("Enter booking Id to update status: ");
                            var b_id = sc.nextLine();
                            app.updateStatus(b_id);
                            break;
                        case 4:
                            app.bookingServiceOfficer();
                            break;
                        case 5:
                            System.out.println("Enter booking Id to update pick up and DropOff time");
                            var b_Id = sc.nextLine();
                            app.updateTime(b_Id);
                            break;
                        case 6:
                            System.out.println("Enter user Id to see previous booking : ");
                            var uld = sc.nextLine();
                            app.previousBookingOfficer(uld);
                            break;
                        case 7:
                            // logout
                            app.logout();
                            dashFlag = false;
                            break;
                    }
                }
            }
        }
    }
}

class User {
    private String email;
    private String name;
    private long mobile;
    protected Address address;
    private String userId;
    private String password;
    private String role;

    User(String email, String name, long mobile, Address address,
            String userId, String password, String role) {
        this.email = email;
        this.name = name;
        this.mobile = mobile;
        this.address = address;
        this.userId = userId;
        this.password = password;
        this.role = role;
    }

    void displayDetails() {
        System.out.println(this.email);
        System.out.println(this.mobile);
        System.out.println(this.name);
        System.out.println(this.userId);
        System.out.println(this.password);
        System.out.println(this.role);
        System.out.println(this.address.mailingAddress);
        System.out.println(this.address.zipcode);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getMobile() {
        return mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

class Address {
    protected String mailingAddress;
    protected String zipcode;

    public Address(String mailingAddress, String zipcode) {
        this.mailingAddress = mailingAddress;
        this.zipcode = zipcode;
    }
}

class Booking {
    protected String userId;
    protected String recName;
    protected Address recAddress;
    protected long recMobile;
    protected double parWeightGram;
    protected String parContentsDescription;
    protected String parDeliveryType;
    protected String parPackingPreference;
    protected String parPickupTime;
    protected String parDropoffTime;
    protected double parServiceCost;
    protected String parPaymentTime;
    protected String bookingId;
    protected String bookingDate;
    protected String status;

    public Booking(String userId, String recName, Address recAddress, long recMobile, double parWeightGram,
            String parContentsDescription, String parDeliveryType, String parPackingPreference, String parPickupTime,
            String parDropoffTime, double parServiceCost, String parPaymentTime, String bookingId, String bookingDate,
            String status) {
        this.userId = userId;
        this.recName = recName;
        this.recAddress = recAddress;
        this.recMobile = recMobile;
        this.parWeightGram = parWeightGram;
        this.parContentsDescription = parContentsDescription;
        this.parDeliveryType = parDeliveryType;
        this.parPackingPreference = parPackingPreference;
        this.parPickupTime = parPickupTime;
        this.parDropoffTime = parDropoffTime;
        this.parServiceCost = parServiceCost;
        this.parPaymentTime = parPaymentTime;
        this.bookingId = bookingId;
        this.bookingDate = bookingDate;
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public void setRecMobile(long recMobile) {
        this.recMobile = recMobile;
    }

    public String getRecName() {
        return recName;
    }

    public void setRecName(String recName) {
        this.recName = recName;
    }

    public Address getRecAddress() {
        return recAddress;
    }

    public void setRecAddress(Address recAddress) {
        this.recAddress = recAddress;
    }

    public long getRecMobile() {
        return recMobile;
    }

    public double getParWeightGram() {
        return parWeightGram;
    }

    public void setParWeightGram(double parWeightGram) {
        this.parWeightGram = parWeightGram;
    }

    public String getParContentsDescription() {
        return parContentsDescription;
    }

    public void setParContentsDescription(String parContentsDescription) {
        this.parContentsDescription = parContentsDescription;
    }

    public String getParDeliveryType() {
        return parDeliveryType;

    }

    public void setParDeliveryType(String parDeliveryType) {
        this.parDeliveryType = parDeliveryType;

    }

    public String getParPackingPreference() {
        return parPackingPreference;

    }

    public void setParPackingPreference(String parPackingPreference) {
        this.parPackingPreference = parPackingPreference;
    }

    public String getParPickupTime() {
        return parPickupTime;
    }

    public void setParPickupTime(String parPickupTime) {
        this.parPickupTime = parPickupTime;
    }

    public String getParDropoffTime() {
        return parDropoffTime;
    }

    public void setParDropoffTime(String parDropoffTime) {
        this.parDropoffTime = parDropoffTime;
    }

    public double getParServiceCost() {
        return parServiceCost;
    }

    public void setParServiceCost(double parServiceCost) {
        this.parServiceCost = parServiceCost;

    }

    public String getParPaymentTime() {
        return parPaymentTime;

    }

    public void setParPaymentTime(String parPaymentTime) {
        this.parPaymentTime = parPaymentTime;
    }

    protected void displayInvoice() {
        System.out.println();
        System.out.println("Invoice: ");
        System.out.println("Booking ID :" + bookingId);
        System.out.println("Receiver Name: " + recName);
        System.out.println("Receiver Address: " + recAddress.mailingAddress + " " + recAddress.zipcode);
        System.out.println("Receiver Mobile Number: " + recMobile);
        System.out.println("Parcel Weight: " + parWeightGram);
        System.out.println("Parcel Description: " + parContentsDescription);
        System.out.println("Parcel Delivery Type: " + parDeliveryType);
        System.out.println("Parcel Packing Preference: " + parPackingPreference);
        System.out.println("Parcel Pickup Time: " + parPickupTime);
        System.out.println("Parcel Dropoff Time: " + parDropoffTime);
        System.out.println("Parcel Service Cost: " + parServiceCost);
        System.out.println("Parcel Payment Time: " + parPaymentTime);
    }

    protected void displayTrackingDetails() {
        System.out.println("Booking ID :" + bookingId);
        System.out.println("Receiver Name: " + recName);
        System.out.println("Receiver Address: " + recAddress.mailingAddress + " " + recAddress.zipcode);
        System.out.println("Date of Booking:" + bookingDate);
        System.out.println("Parcel Status :" + status);
    }

    protected void displayPreviosBookings() {
        System.out.println("Booking ID :" + bookingId);
        System.out.println("Booking Date: " + bookingDate);
        System.out.println("Receiver Name:" + recName);
        System.out.println("Receiver Address: " + recAddress.mailingAddress + " " + recAddress.zipcode);
        System.out.println("Parcel Service Cost: " + parServiceCost);
        System.out.println("Status: " + status);
    }
}
