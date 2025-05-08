import java.util.*;
import java.util.stream.Collectors;
import java.util.Objects;
public class Library {
    private List<Book> books = new ArrayList<>();
    private Set<String> authors = new HashSet<>();
    private Map<String, Integer> statistics = new HashMap<>();

    public void AddBook(Book book){
        books.add(book);
        authors.add(book.GetAuthor());
        statistics.merge(book.GetAuthor(), 1, Integer::sum);
    }
    public void RemoveBook(Book book){
        if (books.remove(book)) {

            boolean authorHasMoreBooks = books.stream()
                    .anyMatch(b -> b.GetAuthor().equals(book.GetAuthor()));

            if (!authorHasMoreBooks) {
                authors.remove(book.GetAuthor());
            }

            statistics.computeIfPresent(book.GetAuthor(), (key, count) -> count > 1 ? count - 1 : null);
        }
    }

    public List<Book> findBooksByAuthor(String author) {
        return books.stream()
                .filter(book -> book.GetAuthor().equals(author))
                .collect(Collectors.toList());
    }

    public List<Book> findBooksByYear(int year) {
        return books.stream()
                .filter(book -> book.getYear() == year)
                .collect(Collectors.toList());
    }

    public void printAllBooks() {
        books.forEach(System.out::println);
    }

    public void printUniqueAuthors() {
        authors.forEach(System.out::println);
    }

    public void printAuthorStatistics() {
        statistics.forEach((author, count) ->
                System.out.println(author + ": " + count + " книг(и)"));
    }
}


