import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

class Main {
    public static void main(String[] args) {
        CinemaSystem system = new CinemaSystem();
        system.run();
    }
}

class CinemaSystem {
    private List<Cinema> cinemas;
    private List<Movie> movies;
    private List<User> users;
    private User currentUser;
    private Scanner scanner;
    private final String DATA_FILE = "cinema_data.ser";

    public CinemaSystem() {
        this.cinemas = new ArrayList<>();
        this.movies = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        initializeUsers();
        loadData();
    }

    private void initializeUsers() {
        this.users = new ArrayList<>();
        users.add(new User("admin", "adminchik", UserRole.ADMIN));
        users.add(new User("user1", "userok1", UserRole.USER));
        users.add(new User("user2", "userok2", UserRole.USER));
    }

    public void run() {
        while (true) {
            if (currentUser == null) {
                showLoginMenu();
            } else {
                showMainMenu();
            }
        }
    }

    private void showLoginMenu() {
        System.out.println("\n =____билетная система кинотеатров____=");
        System.out.println("1. вход в систему");
        System.out.println("2. выход из системы");

        int choice = getIntInput("выберите действие: ", 1, 2);

        switch (choice) {
            case 1:
                login();
                break;
            case 2:
                saveData();
                System.out.println("пока, мой дорогой друг!");
                System.exit(0);
        }
    }

    private void showMainMenu() {
        System.out.println("\n=____главное меню____=");
        System.out.printf("вы вошли как: %s (%s)\n", currentUser.getLogin(), currentUser.getRole());

        if (currentUser.getRole() == UserRole.ADMIN) {
            showAdminMenu();
        } else {
            showUserMenu();
        }
    }

    private void showAdminMenu() {
        System.out.println("1. управление кинотеатрами");
        System.out.println("2. управление фильмами");
        System.out.println("3. управление сеансами");
        System.out.println("4. выйти из системы");
        System.out.println("5. выход из программы");

        int choice = getIntInput("выберите действие: ", 1, 5);

        switch (choice) {
            case 1:
                manageCinemas();
                break;
            case 2:
                manageMovies();
                break;
            case 3:
                manageSessions();
                break;
            case 4:
                logout();
                break;
            case 5:
                saveData();
                System.out.println("пока, мой дорогой друг!");
                System.exit(0);
        }
    }

    private void showUserMenu() {
        System.out.println("1. поиск сеансов");
        System.out.println("2. купить билет");
        System.out.println("3. выйти из системы");
        System.out.println("4. выход из программы");

        int choice = getIntInput("выберите действие: ", 1, 4);

        switch (choice) {
            case 1:
                findSessions();
                break;
            case 2:
                buyTicket();
                break;
            case 3:
                logout();
                break;
            case 4:
                saveData();
                System.out.println("пока, мой дорогой друг!");
                System.exit(0);
        }
    }

    private void manageCinemas() {
        while (true) {
            System.out.println("\n=____управление кинотеатрами____=");
            System.out.println("1. добавить кинотеатр");
            System.out.println("2. добавить зал");
            System.out.println("3. просмотреть все кинотеатры");
            System.out.println("4. назад");

            int choice = getIntInput("выберите действие: ", 1, 4);

            switch (choice) {
                case 1:
                    addCinema();
                    break;
                case 2:
                    addHall();
                    break;
                case 3:
                    viewAllCinemas();
                    break;
                case 4:
                    return;
            }
        }
    }

    private void manageMovies() {
        while (true) {
            System.out.println("\n=____управление фильмами____=");
            System.out.println("1. добавить фильм");
            System.out.println("2. просмотреть все фильмы");
            System.out.println("3. назад");

            int choice = getIntInput("выберите действие: ", 1, 3);

            switch (choice) {
                case 1:
                    addMovie();
                    break;
                case 2:
                    viewAllMovies();
                    break;
                case 3:
                    return;
            }
        }
    }

    private void manageSessions() {
        while (true) {
            System.out.println("\n=____управление сеансами____=");
            System.out.println("1. добавить сеанс");
            System.out.println("2. просмотреть все сеансы");
            System.out.println("3. назад");

            int choice = getIntInput("выберите действие: ", 1, 3);

            switch (choice) {
                case 1:
                    addSession();
                    break;
                case 2:
                    viewAllSessions();
                    break;
                case 3:
                    return;
            }
        }
    }

    private void addCinema() {
        System.out.println("\n=____добавление кинотеатра____=");
        System.out.print("название кинотеатра: ");
        String name = scanner.nextLine();
        System.out.print("адрес: ");
        String address = scanner.nextLine();

        Cinema cinema = new Cinema(name, address);
        cinemas.add(cinema);
        System.out.println("кинотеатр успешно добавлен!");
        saveData();
    }

    private void addHall() {
        if (cinemas.isEmpty()) {
            System.out.println("сначала добавьте кинотеатр!");
            return;
        }

        System.out.println("\n=____Добавление зала____=");
        System.out.println("выберите кинотеатр:");
        listCinemas();

        int cinemaIndex = getIntInput("номер кинотеатра: ", 1, cinemas.size()) - 1;
        Cinema cinema = cinemas.get(cinemaIndex);

        System.out.print("название зала: ");
        String hallName = scanner.nextLine();
        int rows = getIntInput("количество рядов: ", 1, 50);
        int seatsPerRow = getIntInput("количество мест в ряду: ", 1, 50);

        Hall hall = new Hall(hallName, rows, seatsPerRow);
        cinema.addHall(hall);
        System.out.println("зал успешно добавлен!");
        saveData();
    }

    private void addMovie() {
        System.out.println("\n=____добавление фильма____=");
        System.out.print("название фильма: ");
        String title = scanner.nextLine();
        System.out.print("длительность (например, 2ч 30мин): ");
        String duration = scanner.nextLine();

        Movie movie = new Movie(title, duration);
        movies.add(movie);
        System.out.println("фильм успешно добавлен!");
        saveData();
    }

    private void addSession() {
        if (cinemas.isEmpty()) {
            System.out.println("сначала добавьте кинотеатр!");
            return;
        }

        if (movies.isEmpty()) {
            System.out.println("сначала добавьте фильм!");
            return;
        }

        System.out.println("\n=____добавление сеанса____=");
        System.out.println("выберите кинотеатр:");
        listCinemas();

        int cinemaIndex = getIntInput("номер кинотеатра: ", 1, cinemas.size()) - 1;
        Cinema cinema = cinemas.get(cinemaIndex);

        if (cinema.getHalls().isEmpty()) {
            System.out.println("в этом кинотеатре нет залов!");
            return;
        }

        System.out.println("выберите зал:");
        cinema.listHalls();

        int hallIndex = getIntInput("номер зала: ", 1, cinema.getHalls().size()) - 1;
        Hall hall = cinema.getHalls().get(hallIndex);

        System.out.println("выберите фильм:");
        listMovies();

        int movieIndex = getIntInput("номер фильма: ", 1, movies.size()) - 1;
        Movie movie = movies.get(movieIndex);

        LocalDateTime sessionTime = getDateTimeInput("дата и время сеанса (ГГГГ-ММ-ДД ЧЧ:ММ): ");

        for (Session existingSession : hall.getSessions()) {
            Duration existingDuration = parseDuration(existingSession.getMovie().getDuration());
            LocalDateTime existingEnd = existingSession.getTime().plus(existingDuration);

            Duration newDuration = parseDuration(movie.getDuration());
            LocalDateTime newEnd = sessionTime.plus(newDuration);

            if ((sessionTime.isAfter(existingSession.getTime()) && sessionTime.isBefore(existingEnd)) ||
                    (newEnd.isAfter(existingSession.getTime()) && newEnd.isBefore(existingEnd)) ||
                    (sessionTime.isBefore(existingSession.getTime()) && newEnd.isAfter(existingEnd))) {
                System.out.println("ошибка: сеанс пересекается с существующим сеансом!");
                System.out.printf("пересекается с: %s в %s\n",
                        existingSession.getMovie().getTitle(),
                        existingSession.getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                return;
            }
        }

        Session session = new Session(movie, sessionTime, hall.getRows(), hall.getSeatsPerRow());
        hall.addSession(session);
        System.out.println("сеанс успешно добавлен!");
        saveData();
    }

    private void findSessions() {
        if (movies.isEmpty()) {
            System.out.println("нет доступных фильмов!");
            return;
        }

        System.out.println("\n=____поиск сеансов____=");
        System.out.println("выберите фильм:");
        listMovies();

        int movieIndex = getIntInput("номер фильма: ", 1, movies.size()) - 1;
        Movie selectedMovie = movies.get(movieIndex);

        LocalDateTime now = LocalDateTime.now();
        List<SessionInfo> upcomingSessions = new ArrayList<>();

        for (Cinema cinema : cinemas) {
            for (Hall hall : cinema.getHalls()) {
                for (Session session : hall.getSessions()) {
                    if (session.getMovie().equals(selectedMovie) &&
                            session.getTime().isAfter(now) &&
                            session.hasAvailableSeats()) {

                        upcomingSessions.add(new SessionInfo(cinema, hall, session));
                    }
                }
            }
        }

        if (upcomingSessions.isEmpty()) {
            System.out.println("нет доступных сеансов для выбранного фильма.");
            return;
        }

        upcomingSessions.sort(Comparator.comparing(si -> si.session.getTime()));

        System.out.println("\nдоступные сеансы:");
        for (int i = 0; i < upcomingSessions.size(); i++) {
            SessionInfo si = upcomingSessions.get(i);
            System.out.printf("%d. Кинотеатр: %s, Зал: %s, Время: %s, Свободных мест: %d\n",
                    i + 1,
                    si.cinema.getName(),
                    si.hall.getName(),
                    si.session.getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                    si.session.getAvailableSeatsCount());
        }

        int sessionChoice = getIntInput("выберите сеанс для просмотра мест (0 - назад): ", 0, upcomingSessions.size());
        if (sessionChoice == 0) return;

        SessionInfo selectedSession = upcomingSessions.get(sessionChoice - 1);
        showSeatMap(selectedSession.hall, selectedSession.session);

        if (currentUser != null) {
            String answer = getStringInput("хотите купить билет? (да/нет): ").toLowerCase();
            if (answer.equals("да")) {
                buyTicket(selectedSession.cinema, selectedSession.hall, selectedSession.session);
            }
        }
    }

    private void buyTicket() {
        findSessions();
    }

    private void buyTicket(Cinema cinema, Hall hall, Session session) {
        System.out.println("\n=____покупка билета____=");
        showSeatMap(hall, session);

        int row = getIntInput("выберите ряд: ", 1, hall.getRows()) - 1;
        int seat = getIntInput("выберите место: ", 1, hall.getSeatsPerRow()) - 1;

        if (!session.isSeatAvailable(row, seat)) {
            System.out.println("это место уже занято!");
            return;
        }

        session.bookSeat(row, seat);
        System.out.println("\nбилет успешно куплен!");
        System.out.printf("Фильм: %s\n", session.getMovie().getTitle());
        System.out.printf("Кинотеатр: %s, Зал: %s\n", cinema.getName(), hall.getName());
        System.out.printf("Ряд: %d, Место: %d\n", row + 1, seat + 1);
        System.out.printf("Время сеанса: %s\n",
                session.getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        saveData();
    }

    private void showSeatMap(Hall hall, Session session) {
        System.out.println("\nплан зала (X - занято, O - свободно):");
        System.out.print("     ");
        for (int i = 0; i < hall.getSeatsPerRow(); i++) {
            System.out.printf("%2d ", i + 1);
        }
        System.out.println();

        for (int row = 0; row < hall.getRows(); row++) {
            System.out.printf("Ряд %2d: ", row + 1);
            for (int seat = 0; seat < hall.getSeatsPerRow(); seat++) {
                System.out.print(session.isSeatAvailable(row, seat) ? " O " : " X ");
            }
            System.out.println();
        }
    }

    private void viewAllCinemas() {
        if (cinemas.isEmpty()) {
            System.out.println("нет доступных кинотеатров.");
            return;
        }

        System.out.println("\n=____список кинотеатров____=");
        for (Cinema cinema : cinemas) {
            System.out.printf("\nКинотеатр: %s\n", cinema.getName());
            System.out.printf("Адрес: %s\n", cinema.getAddress());
            System.out.println("Залы:");
            for (Hall hall : cinema.getHalls()) {
                System.out.printf("  - %s: %d рядов x %d мест\n",
                        hall.getName(), hall.getRows(), hall.getSeatsPerRow());
                System.out.println("    Сеансы:");
                for (Session session : hall.getSessions()) {
                    System.out.printf("    * %s в %s (%s)\n",
                            session.getMovie().getTitle(),
                            session.getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                            session.getMovie().getDuration());
                }
            }
        }
    }

    private void viewAllMovies() {
        if (movies.isEmpty()) {
            System.out.println("нет доступных фильмов.");
            return;
        }

        System.out.println("\n=____список фильмов____=");
        for (int i = 0; i < movies.size(); i++) {
            System.out.printf("%d. %s (%s)\n", i + 1, movies.get(i).getTitle(), movies.get(i).getDuration());
        }
    }

    private void viewAllSessions() {
        if (cinemas.isEmpty()) {
            System.out.println("нет доступных кинотеатров.");
            return;
        }

        System.out.println("\n=____все сеансы____=");
        for (Cinema cinema : cinemas) {
            System.out.printf("\nКинотеатр: %s\n", cinema.getName());
            for (Hall hall : cinema.getHalls()) {
                System.out.printf("  Зал: %s\n", hall.getName());
                for (Session session : hall.getSessions()) {
                    System.out.printf("    * %s в %s (%s), Свободных мест: %d\n",
                            session.getMovie().getTitle(),
                            session.getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                            session.getMovie().getDuration(),
                            session.getAvailableSeatsCount());
                }
            }
        }
    }

    private void login() {
        System.out.println("\n=____вход в систему____=");
        System.out.print("Логин: ");
        String login = scanner.nextLine();
        System.out.print("Пароль: ");
        String password = scanner.nextLine();

        for (User user : users) {
            if (user.getLogin().equals(login) && user.getPassword().equals(password)) {
                currentUser = user;
                System.out.printf("добро пожаловать, %s!\n", login);
                return;
            }
        }

        System.out.println("неверный логин или пароль");
    }

    private void logout() {
        currentUser = null;
        System.out.println("вы вышли из системы");
    }

    private void listCinemas() {
        for (int i = 0; i < cinemas.size(); i++) {
            System.out.printf("%d. %s - %s\n", i + 1, cinemas.get(i).getName(), cinemas.get(i).getAddress());
        }
    }

    private void listMovies() {
        for (int i = 0; i < movies.size(); i++) {
            System.out.printf("%d. %s (%s)\n", i + 1, movies.get(i).getTitle(), movies.get(i).getDuration());
        }
    }

    private Duration parseDuration(String durationStr) {
        int hours = 0;
        int minutes = 0;

        if (durationStr.contains("ч")) {
            String[] parts = durationStr.split("ч");
            hours = Integer.parseInt(parts[0].trim());
            if (parts.length > 1 && parts[1].contains("мин")) {
                String minutesPart = parts[1].split("мин")[0].trim();
                minutes = Integer.parseInt(minutesPart);
            }
        } else if (durationStr.contains("мин")) {
            minutes = Integer.parseInt(durationStr.split("мин")[0].trim());
        }

        return Duration.ofHours(hours).plusMinutes(minutes);
    }

    private int getIntInput(String prompt, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine());
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("введите число от %d до %d\n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("неверный ввод. пожалуйста, введите число.");
            }
        }
    }

    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private LocalDateTime getDateTimeInput(String prompt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine();
                return LocalDateTime.parse(input, formatter);
            } catch (Exception e) {
                System.out.println("неверный формат даты. используйте ГГГГ-ММ-ДД ЧЧ:ММ");
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            cinemas = (List<Cinema>) ois.readObject();
            movies = (List<Movie>) ois.readObject();
            System.out.println("данные успешно загружены.");
        } catch (FileNotFoundException e) {
            System.out.println("файл данных не найден. будет создан новый.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("ошибка загрузки данных: " + e.getMessage());
        }
    }

    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(cinemas);
            oos.writeObject(movies);
            System.out.println("данные успешно сохранены.");
        } catch (IOException e) {
            System.out.println("ошибка сохранения данных: " + e.getMessage());
        }
    }

    private static class SessionInfo {
        Cinema cinema;
        Hall hall;
        Session session;

        SessionInfo(Cinema cinema, Hall hall, Session session) {
            this.cinema = cinema;
            this.hall = hall;
            this.session = session;
        }
    }
}

enum UserRole {
    ADMIN, USER
}

class User implements Serializable {
    private String login;
    private String password;
    private UserRole role;

    public User(String login, String password, UserRole role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getRole() {
        return role;
    }
}

class Cinema implements Serializable {
    private String name;
    private String address;
    private List<Hall> halls;

    public Cinema(String name, String address) {
        this.name = name;
        this.address = address;
        this.halls = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public List<Hall> getHalls() {
        return halls;
    }

    public void addHall(Hall hall) {
        halls.add(hall);
    }

    public void listHalls() {
        for (int i = 0; i < halls.size(); i++) {
            System.out.printf("%d. %s (%d рядов x %d мест)\n",
                    i + 1, halls.get(i).getName(),
                    halls.get(i).getRows(), halls.get(i).getSeatsPerRow());
        }
    }
}

class Hall implements Serializable {
    private String name;
    private int rows;
    private int seatsPerRow;
    private List<Session> sessions;

    public Hall(String name, int rows, int seatsPerRow) {
        this.name = name;
        this.rows = rows;
        this.seatsPerRow = seatsPerRow;
        this.sessions = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getRows() {
        return rows;
    }

    public int getSeatsPerRow() {
        return seatsPerRow;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public void addSession(Session session) {
        sessions.add(session);
    }
}

class Movie implements Serializable {
    private String title;
    private String duration;

    public Movie(String title, String duration) {
        this.title = title;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public String getDuration() {
        return duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return Objects.equals(title, movie.title) &&
                Objects.equals(duration, movie.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, duration);
    }
}

class Session implements Serializable {
    private Movie movie;
    private LocalDateTime time;
    private boolean[][] seats;

    public Session(Movie movie, LocalDateTime time, int rows, int seatsPerRow) {
        this.movie = movie;
        this.time = time;
        this.seats = new boolean[rows][seatsPerRow];
    }

    public Movie getMovie() {
        return movie;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public boolean isSeatAvailable(int row, int seat) {
        return !seats[row][seat];
    }

    public void bookSeat(int row, int seat) {
        seats[row][seat] = true;
    }

    public int getAvailableSeatsCount() {
        int count = 0;
        for (boolean[] row : seats) {
            for (boolean seat : row) {
                if (!seat) count++;
            }
        }
        return count;
    }

    public boolean hasAvailableSeats() {
        return getAvailableSeatsCount() > 0;
    }
}