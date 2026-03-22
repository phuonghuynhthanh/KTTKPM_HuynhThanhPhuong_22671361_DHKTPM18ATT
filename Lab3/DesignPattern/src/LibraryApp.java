import java.util.*;

public class LibraryApp {
    public static void main(String[] args) {

        Library library = Library.getInstance();

        BookFactory paperFactory = new PaperBookFactory();
        BookFactory ebookFactory = new EBookFactory();

        Book b1 = paperFactory.createBook("Clean Code", "Robert C. Martin");
        Book b2 = ebookFactory.createBook("Design Patterns", "GoF");

        library.addBook(b1);
        library.addBook(b2);

        library.addObserver(new LibraryMember("Alice"));
        library.addObserver(new LibraryStaff("Bob"));

        library.setSearchStrategy(new SearchByName());
        List<Book> result = library.searchBooks("Clean");

        for (Book b : result) {
            System.out.println(b.getBookInfo());
        }

        BorrowRecord record = new BaseBorrowRecord(b1);
        record = new ExtendedTimeDecorator(record);
        record = new BrailleVersionDecorator(record);

        System.out.println(record.getDetails());
        System.out.println("Duration: " + record.getDurationDays());

        library.notifyObservers("New books available!");
    }
}

//////////////////////////////////////////////////////////
// ================= FACTORY ============================
//////////////////////////////////////////////////////////

abstract class BookFactory {
    public abstract Book createBook(String title, String author);
}

class PaperBookFactory extends BookFactory {
    public Book createBook(String title, String author) {
        return new PaperBook(title, author);
    }
}

class EBookFactory extends BookFactory {
    public Book createBook(String title, String author) {
        return new EBook(title, author);
    }
}

//////////////////////////////////////////////////////////
// ================= BOOK ===============================
//////////////////////////////////////////////////////////

interface Book {
    String getTitle();
    String getBookInfo();
}

class PaperBook implements Book {
    private String title;
    private String author;

    public PaperBook(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public String getBookInfo() {
        return "PaperBook: " + title + " by " + author;
    }
}

class EBook implements Book {
    private String title;
    private String author;

    public EBook(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public String getBookInfo() {
        return "EBook: " + title + " by " + author;
    }
}

//////////////////////////////////////////////////////////
// ================= STRATEGY ===========================
//////////////////////////////////////////////////////////

interface SearchStrategy {
    List<Book> search(List<Book> books, String keyword);
}

class SearchByName implements SearchStrategy {
    public List<Book> search(List<Book> books, String keyword) {
        List<Book> result = new ArrayList<>();
        for (Book b : books) {
            if (b.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(b);
            }
        }
        return result;
    }
}

class SearchByAuthor implements SearchStrategy {
    public List<Book> search(List<Book> books, String keyword) {
        List<Book> result = new ArrayList<>();
        for (Book b : books) {
            if (b.getBookInfo().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(b);
            }
        }
        return result;
    }
}

class SearchByCategory implements SearchStrategy {
    public List<Book> search(List<Book> books, String keyword) {
        return new ArrayList<>();
    }
}

//////////////////////////////////////////////////////////
// ================= OBSERVER ===========================
//////////////////////////////////////////////////////////

interface ObserverLib {
    void update(String message);
}

class LibraryMember implements ObserverLib {
    private String name;

    public LibraryMember(String name) {
        this.name = name;
    }

    public void update(String message) {
        System.out.println("Member " + name + " notified: " + message);
    }
}

class LibraryStaff implements ObserverLib {
    private String name;

    public LibraryStaff(String name) {
        this.name = name;
    }

    public void update(String message) {
        System.out.println("Staff " + name + " notified: " + message);
    }
}

//////////////////////////////////////////////////////////
// ================= SINGLETON ==========================
//////////////////////////////////////////////////////////

class Library {
    private static Library instance;
    private List<Book> books = new ArrayList<>();
    private List<ObserverLib> observers = new ArrayList<>();
    private SearchStrategy searchStrategy;

    private Library() {}

    public static Library getInstance() {
        if (instance == null) {
            instance = new Library();
        }
        return instance;
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void setSearchStrategy(SearchStrategy strategy) {
        this.searchStrategy = strategy;
    }

    public List<Book> searchBooks(String keyword) {
        if (searchStrategy == null) return new ArrayList<>();
        return searchStrategy.search(books, keyword);
    }

    public void addObserver(ObserverLib o) {
        observers.add(o);
    }

    public void notifyObservers(String message) {
        for (ObserverLib o : observers) {
            o.update(message);
        }
    }
}

//////////////////////////////////////////////////////////
// ================= DECORATOR ==========================
//////////////////////////////////////////////////////////

interface BorrowRecord {
    String getDetails();
    int getDurationDays();
}

class BaseBorrowRecord implements BorrowRecord {
    private Book book;

    public BaseBorrowRecord(Book book) {
        this.book = book;
    }

    public String getDetails() {
        return "Borrowing: " + book.getTitle();
    }

    public int getDurationDays() {
        return 7;
    }
}

abstract class BorrowDecorator implements BorrowRecord {
    protected BorrowRecord record;

    public BorrowDecorator(BorrowRecord record) {
        this.record = record;
    }
}

class ExtendedTimeDecorator extends BorrowDecorator {
    public ExtendedTimeDecorator(BorrowRecord record) {
        super(record);
    }

    public String getDetails() {
        return record.getDetails() + " + Extended Time";
    }

    public int getDurationDays() {
        return record.getDurationDays() + 7;
    }
}

class BrailleVersionDecorator extends BorrowDecorator {
    public BrailleVersionDecorator(BorrowRecord record) {
        super(record);
    }

    public String getDetails() {
        return record.getDetails() + " + Braille Version";
    }

    public int getDurationDays() {
        return record.getDurationDays();
    }
}