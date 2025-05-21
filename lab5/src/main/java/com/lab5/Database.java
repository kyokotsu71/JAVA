package com.lab5;

import com.lab5.models.Book;
import com.lab5.models.Review;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final String URL = "jdbc:sqlite:reader_diary.db";

    public static void initialize() {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            // Создаем таблицу книг
            String sql = "CREATE TABLE IF NOT EXISTS books (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "title TEXT NOT NULL," +
                    "author TEXT," +
                    "genre TEXT," +
                    "year INTEGER," +
                    "pages INTEGER)";
            stmt.execute(sql);

            // Создаем таблицу отзывов
            sql = "CREATE TABLE IF NOT EXISTS reviews (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "book_id INTEGER NOT NULL," +
                    "rating INTEGER," +
                    "review TEXT," +
                    "date TEXT," +
                    "FOREIGN KEY (book_id) REFERENCES books(id))";
            stmt.execute(sql);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void addBook(Book book) {
        String sql = "INSERT INTO books(title, author, genre, year, pages) VALUES(?,?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getGenre());
            pstmt.setInt(4, book.getYear());
            pstmt.setInt(5, book.getPages());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void addReview(Review review) {
        String sql = "INSERT INTO reviews(book_id, rating, review, date) VALUES(?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, review.getBookId());
            pstmt.setInt(2, review.getRating());
            pstmt.setString(3, review.getReview());
            pstmt.setString(4, review.getDate());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT id, title, author, genre, year, pages FROM books";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Book book = new Book();
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setGenre(rs.getString("genre"));
                book.setYear(rs.getInt("year"));
                book.setPages(rs.getInt("pages"));
                books.add(book);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return books;
    }

    public static List<Review> getReviewsForBook(int bookId) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT id, book_id, rating, review, date FROM reviews WHERE book_id = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Review review = new Review();
                review.setId(rs.getInt("id"));
                review.setBookId(rs.getInt("book_id"));
                review.setRating(rs.getInt("rating"));
                review.setReview(rs.getString("review"));
                review.setDate(rs.getString("date"));
                reviews.add(review);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return reviews;
    }
}