package com.lab5.controllers;

import com.lab5.Database;
import com.lab5.models.Book;
import com.lab5.models.Review;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MainController {
    @FXML
    private TableView<Book> booksTable;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, Integer> yearColumn;

    @FXML
    private ListView<Review> reviewsList;
    @FXML
    private TextArea reviewText;
    @FXML
    private Slider ratingSlider;
    @FXML
    private Label ratingValue;
    @FXML
    private Button addReviewBtn;
    @FXML
    private Button addBookBtn;

    private ObservableList<Book> books = FXCollections.observableArrayList();
    private ObservableList<Review> reviews = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Инициализация таблицы книг
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));

        booksTable.setItems(books);
        reviewsList.setItems(reviews);

        // Загрузка данных из БД
        loadBooks();

        // Обработчик выбора книги
        booksTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showBookReviews(newValue));

        // Обработчик изменения рейтинга
        ratingSlider.valueProperty().addListener(
                (observable, oldValue, newValue) -> ratingValue.setText(String.valueOf(newValue.intValue())));

        // Инициализация кнопок
        addReviewBtn.setOnAction(event -> addReview());
        addBookBtn.setOnAction(event -> showAddBookDialog());
    }

    private void loadBooks() {
        books.clear();
        books.addAll(Database.getAllBooks());
    }

    private void showBookReviews(Book book) {
        if (book != null) {
            reviews.clear();
            reviews.addAll(Database.getReviewsForBook(book.getId()));
        }
    }

    private void addReview() {
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showAlert("Ошибка", "Выберите книгу для добавления отзыва");
            return;
        }

        String reviewText = this.reviewText.getText();
        if (reviewText.isEmpty()) {
            showAlert("Ошибка", "Введите текст отзыва");
            return;
        }

        int rating = (int) ratingSlider.getValue();
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        Review review = new Review();
        review.setBookId(selectedBook.getId());
        review.setRating(rating);
        review.setReview(reviewText);
        review.setDate(date);

        Database.addReview(review);
        reviews.add(review);

        this.reviewText.clear();
        ratingSlider.setValue(3);
    }

    private void showAddBookDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/lab5/views/add_book.fxml"));
            Parent root = loader.load();

            AddBookController controller = loader.getController();
            controller.setMainController(this);

            Stage stage = new Stage();
            stage.setTitle("Добавить новую книгу");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addBookToTable(Book book) {
        books.add(book);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}