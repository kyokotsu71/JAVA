package com.lab5.controllers;

import com.lab5.Database;
import com.lab5.models.Book;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddBookController {
    @FXML
    private TextField titleField;
    @FXML
    private TextField authorField;
    @FXML
    private TextField genreField;
    @FXML
    private TextField yearField;
    @FXML
    private TextField pagesField;

    private MainController mainController;
    private Stage stage;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void handleAddBook() {
        String title = titleField.getText();
        String author = authorField.getText();
        String genre = genreField.getText();
        int year, pages;

        if (title.isEmpty()) {
            showError("Название книги не может быть пустым");
            return;
        }

        try {
            year = Integer.parseInt(yearField.getText());
        } catch (NumberFormatException e) {
            showError("Год издания должен быть числом");
            return;
        }

        try {
            pages = Integer.parseInt(pagesField.getText());
        } catch (NumberFormatException e) {
            showError("Количество страниц должно быть числом");
            return;
        }

        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setGenre(genre);
        book.setYear(year);
        book.setPages(pages);

        Database.addBook(book);
        mainController.addBookToTable(book);
        stage.close();
    }

    @FXML
    private void handleCancel() {
        stage.close();
    }

    private void showError(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}