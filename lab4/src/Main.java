public class Main {
    public static void main(String[] args) {
        Library library = new Library();


        Book book1 = new Book("Живые", "Варвара Еналь", 2017);
        Book book2 = new Book("Цветы для Элджернона", "Дэниел Киз", 1966);
        Book book3 = new Book("Скорбь Сатаны", "Мария Корелли", 1895);
        Book book4 = new Book("Норвежский лес", "Харуки Мураками", 1987);
        Book book5 = new Book("Глаза дракона", "Стивен Кинг", 1987);

        library.AddBook(book1);
        library.AddBook(book2);
        library.AddBook(book3);
        library.AddBook(book4);
        library.AddBook(book5);

        System.out.println("все книги в библиотеке:");
        library.printAllBooks();
        System.out.println();

        System.out.println("уникальные авторы:");
        library.printUniqueAuthors();
        System.out.println();

        System.out.println("статистика по авторам:");
        library.printAuthorStatistics();
        System.out.println();

        System.out.println("книги Варвары Еналь:");
        library.findBooksByAuthor("Варвара Еналь").forEach(System.out::println);
        System.out.println();

        System.out.println("книги, изданные в 1987 году:");
        library.findBooksByYear(1987).forEach(System.out::println);
        System.out.println();

        System.out.println("удаление книги");
        library.RemoveBook(book5);

        System.out.println("все книги после удаления:");
        library.printAllBooks();
        System.out.println();

        System.out.println("статистика по авторам:");
        library.printAuthorStatistics();
    }
}