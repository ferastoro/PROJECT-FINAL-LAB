package projek.data;

import projek.model.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class DataStore {
    private static DataStore instance;

    private List<User> users;
    private List<Kamar> roomList;
    private List<Reservasi> reservationList;

    private final String userFile = "data/users.json";
    private final String roomFile = "data/kamar.json";
    private final String reservationFile = "data/reservasi.json";

    private final Gson gson;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    private DataStore(){
        users = new ArrayList<>();
        roomList = new ArrayList<>();
        reservationList = new ArrayList<>();
        gson = new GsonBuilder().setPrettyPrinting().create();

        createDataDirectory();
        loadAllData();
    }

    public static synchronized DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }


    private void createDataDirectory() {
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            if (dataDir.mkdirs()) {
            System.out.println("Directory 'data/' created successfully.");
            } else {
                System.err.println("Failed to create directory 'data/'. Check permissions.");
            }
        }
    }

    private void loadAllData() {
        loadUsersFromFile();
        loadRoomsFromFile();
        loadReservationsFromFile();

        if (users.isEmpty()) {
           System.out.println("No user data found, initializing dummy data...");
            initializeDummyData();
            saveAllDataToFiles();
        } else {
            System.out.println("Users data loaded successfully (" + users.size() + " users)."); 
        }
    }

    private String readFileAsString(String fileName) {
        try {
            if (!Files.exists(Paths.get(fileName))) {
                System.out.println("File " + fileName + " does not exist, will create new one.");
                return "[]";
            };
            return new String(Files.readAllBytes(Paths.get(fileName)));
        } catch (IOException e) {
            System.err.println("Error reading file " + fileName + ": " + e.getMessage());
            return "[]";
        }
    }

    private void writeStringToFile (String fileName, String content) {
        try {
            Files.write(Paths.get(fileName), content.getBytes());
            System.out.println("Data saved to: " + fileName);
        } catch (IOException e) {
            System.err.println("Error writing to file " + fileName + e.getMessage());
        }
    }

    private void loadUsersFromFile() {
        try {
            String jsonContent = readFileAsString(userFile);
            List<UserDataForSerialization> userDataList = gson.fromJson(
                jsonContent, 
                new TypeToken<List<UserDataForSerialization>>(){}.getType()
            );

            users.clear();
            if (userDataList != null) {
                for (UserDataForSerialization userData : userDataList) {
                    if (userData.username != null && userData.password != null && userData.role != null) {
                        if ("admin".equalsIgnoreCase(userData.role)) {
                            users.add(new Admin(userData.username, userData.password));
                        } else if ("guest".equalsIgnoreCase(userData.role)) {
                            users.add(new Guest(userData.username, userData.password));
                        } else {
                            System.err.println("Unknown user role: " + userData.role);
                        }
                    }
                }
            }
        } catch (JsonSyntaxException e) {
            System.err.println("Error parsing users JSON: " + e.getMessage());
            users.clear();
        } catch (Exception e) {
            System.err.println("Error loading users: " + e.getMessage());
            users.clear();
        }
    }

    private void saveUsersToFile() {
        try {
            List<UserDataForSerialization> userDataList = new ArrayList<>();
            for (User user : users) {
                userDataList.add(new UserDataForSerialization(user));
            }
            String jsonContent = gson.toJson(userDataList);
            writeStringToFile(userFile, jsonContent);
        } catch (Exception e) {
            System.err.println("Error saving users: " + e.getMessage());
        } 
    }

    private static class UserDataForSerialization {
        String username;
        String password;
        String role;

        UserDataForSerialization(User u) {
            this.username = u.getUsername();
            this.password = u.getPassword();
            this.role = u.getRole();
        }
    }

    private void loadRoomsFromFile() {
        try {
            String jsonContent = readFileAsString(roomFile);
            List<RoomDataForSerialization> roomDataList = gson.fromJson(
                jsonContent,
                new TypeToken<List<RoomDataForSerialization>>(){}.getType()
            );

            roomList.clear();
            if (roomDataList != null) {
                for (RoomDataForSerialization roomData : roomDataList) {
                    Kamar room;
                    if ("deluxe".equalsIgnoreCase(roomData.tipe)) {
                        room = new KamarDeluxe(roomData.nomor);
                    } else if ("standard".equalsIgnoreCase(roomData.tipe)) {
                        room = new KamarStandard(roomData.nomor);
                    } else {
                        System.err.println("Unknown room type: " + roomData.tipe);
                        continue;
                    }

                    room.setHarga(roomData.harga);
                    room.setTersedia(roomData.tersedia);
                    roomList.add(room);
                }
            }
        } catch (JsonSyntaxException e) {
            System.err.println("Error parsing rooms JSON: " + e.getMessage());
            roomList.clear();
        } catch (Exception e) {
            System.err.println("Error loading rooms: " + e.getMessage());
            roomList.clear();
        }
    }

    private void saveRoomsToFile() {
        try {
            List<RoomDataForSerialization> roomDataList = new ArrayList<>();
            for (Kamar room : roomList) {
                roomDataList.add(new RoomDataForSerialization(room));
            }
            String jsonContent = gson.toJson(roomDataList);
            writeStringToFile(roomFile, jsonContent);
        } catch (Exception e) {
            System.err.println("Error saving rooms: " + e.getMessage());
        }
    }
    
    private static class RoomDataForSerialization {
        String nomor, tipe;
        double harga;
        boolean tersedia;

        RoomDataForSerialization(Kamar k) {
            this.nomor = k.getNomor();
            this.harga = k.getHarga();
            this.tersedia = k.isTersedia();
            this.tipe = k.getTipe();
        }
    }

    private void loadReservationsFromFile() {
        try {
            String jsonContent = readFileAsString(reservationFile);
            List<ReservationDataForSerialization> reservationDataList = gson.fromJson(
                jsonContent,
                new TypeToken<List<ReservationDataForSerialization>>(){}.getType()
            );

            reservationList.clear();
            if (reservationDataList != null) {
                for (ReservationDataForSerialization resData : reservationDataList) {
                    Guest guest = findGuestByUsername(resData.tamuUsername);
                    Kamar room = findRoomByNumber(resData.kamarNomor);

                    if (guest == null) {
                        System.err.println("Guest not found: " + resData.tamuUsername);
                        continue;
                    }
                    if (room == null) {
                        System.err.println("Room not found: " + resData.kamarNomor);
                        continue;
                    }

                    LocalDate checkIn = LocalDate.parse(resData.checkIn, DATE_FORMATTER);
                    LocalDate checkOut = LocalDate.parse(resData.checkOut, DATE_FORMATTER);

                    Reservasi reservation = new Reservasi(guest, room, checkIn, checkOut);
                    reservation.setIdReservasi(resData.idReservasi);
                    reservationList.add(reservation);
                }
            }
        } catch (JsonSyntaxException e) {
            System.err.println("Error parsing reservations JSON: " + e.getMessage());
            reservationList.clear();
        } catch (DateTimeParseException e) {
            System.err.println("Error parsing date in reservations: " + e.getMessage());
            reservationList.clear();
        } catch (Exception e) {
            System.err.println("Error loading reservations: " + e.getMessage());
            reservationList.clear();
        }
    }

    private void saveReservationsToFile() {
        try {
            List<ReservationDataForSerialization> reservationDataList = new ArrayList<>();
            for (Reservasi r : reservationList) {
                if (r.getTamu() != null && r.getKamar() != null && 
                    r.getCheckIn() != null && r.getCheckOut() != null) {
                    reservationDataList.add(new ReservationDataForSerialization(r));
                }
            }
            String jsonContent = gson.toJson(reservationDataList);
            writeStringToFile(reservationFile, jsonContent);
        } catch (Exception e) {
            System.err.println("Error saving reservations: " + e.getMessage());
        }
    }

    private static class ReservationDataForSerialization {
        String idReservasi;
        String tamuUsername;
        String kamarNomor;
        String checkIn;
        String checkOut;

        ReservationDataForSerialization(Reservasi res) {
            this.idReservasi = res.getIdReservasi();
            this.tamuUsername = res.getTamu().getUsername();
            this.kamarNomor = res.getKamar().getNomor();
            this.checkIn = res.getCheckIn().format(DATE_FORMATTER);
            this.checkOut = res.getCheckOut().format(DATE_FORMATTER);
        }
    }

    // --- Helper methods ---
    private Guest findGuestByUsername (String username) {
        for (User user : users) {
            if (user instanceof Guest && user.getUsername().equals(username)) {
                return (Guest) user;
            }
        }
        return null;
    }

        private Kamar findRoomByNumber(String roomNumber) {
        for (Kamar room : roomList) {
            if (room.getNomor().equals(roomNumber)) {
                return room;
            }
        }
        return null;
    }

    private void initializeDummyData() {
        System.out.println("Initializing dummy data...");
        
        if (users.isEmpty()) {
            users.add(new Admin("admin", "admin123"));
            users.add(new Guest("tamu", "tamu123"));
            System.out.println("Dummy users added: admin, tamu.");
        }

        if (roomList.isEmpty()) {
            for (int i = 1; i <= 10; i++) {
                roomList.add(new KamarStandard("S" + String.format("%02d", i)));
            }
            for (int i = 11; i <= 20; i++) {
                roomList.add(new KamarDeluxe("D" + String.format("%02d", i)));
            }
            System.out.println("Dummy rooms added: 10 Standard, 10 Deluxe.");
        }
    }
    // --- Public Methods ---
    public void saveAllDataToFiles() {
        System.out.println("Saving all data to files...");
        saveUsersToFile();
        saveRoomsToFile();
        saveReservationsToFile();
        System.out.println("All data saved to files.");
    }

    public User authenticateUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public List<Kamar> getAllRooms() {
        return new ArrayList<>(roomList);
    }

    public List<Kamar> getAvailableRooms() {
        return roomList.stream()
                .filter(Kamar::isTersedia)
                .collect(java.util.stream.Collectors.toList());
    }

    public boolean isRoomAvailable(String roomNumber) {
        Kamar room = findRoomByNumber(roomNumber);
        return room != null && room.isTersedia();
    }

    public boolean updateRoomAvailability(String roomNumber, boolean isAvailable) {
        Kamar room = findRoomByNumber(roomNumber);
        if (room != null) {
            room.setTersedia(isAvailable);
            saveRoomsToFile();
            return true;
        }
        return false;
    }

    public List<Reservasi> getAllReservations() {
        return new ArrayList<>(reservationList);
    }

    public boolean addReservation(Reservasi reservation) {
        if (reservation.getIdReservasi() == null || reservation.getIdReservasi().trim().isEmpty()) {
            reservation.setIdReservasi("RES" + System.currentTimeMillis());
        }
        reservationList.add(reservation);
        saveReservationsToFile();
        return true;
    }

    public boolean addUser(User user) {
        for (User existingUser : users) {
            if (existingUser.getUsername().equalsIgnoreCase(user.getUsername())) {
                System.out.println("Add user failed: Username '" + user.getUsername() + "' already exists.");
                return false;
            }
        }
        users.add(user);
        saveUsersToFile();
        System.out.println("User '" + user.getUsername() + "' added successfully as " + user.getRole() + ".");
        return true;
    }

}