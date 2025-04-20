import java.util.*;

// =============================
// Exception Classes
// =============================
class BookNotAvailableException extends Exception {
    public BookNotAvailableException(String message) {
        super(message);
    }
}

class BookNotFoundException extends Exception {
    public BookNotFoundException(String message) {
        super(message);
    }
}

// =============================
// Iterator Interface
// =============================
interface Iterator<Book> {
    public Book getNext();
    public boolean hasNext();
}

// =============================
// GenreIterator Interface
// =============================
class GenreIterator implements Iterator<Book> { 
    private List<Book> books;
    private int currentPosition;
      
    // constructor 
    GenreIterator(List<Book> books) { 
        this.books = books;
    } 
      
    // Checks if the next element exists 
    public boolean hasNext(){ 
        return currentPosition < books.size() ;
    } 
      
    // moves the iterator to next element 
    public Book getNext() { 
        while (!books.get(currentPosition).isAvailable()) {
            currentPosition++;
        }
        Book result = books.get(currentPosition);
        currentPosition++;
        return result;
    }
} 

// =============================
// Book Class
// =============================
class Book {
    private String title;
    private String author;
    private String genre;
    private boolean isAvailable;

    public Book(String title, String author, String genre) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.isAvailable = true;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void checkout() throws BookNotAvailableException {
        // TODO: Throw exception if book is not available
        if (!this.isAvailable) {
            throw new BookNotAvailableException("Book is unavaialble.");
        }
        this.isAvailable = false;
    }

    public void returnBook() {
        // TODO: Mark book as available
        this.isAvailable = true;
    }

    public String getGenre() {
        return genre;
    }

    public String getTitle() {
        return title;
    }

    public String toString() {
        return title + " by " + author + " (" + genre + ")";
    }
}

// =============================
// LibraryCollection Class
// =============================
class LibraryCollection {
    private Map<String, List<Book>> genreMap;

    public LibraryCollection() {
        genreMap = new HashMap<>();
    }

    public void addBook(Book book) {
        // TODO: Add books to genreMap
        if (this.genreMap.get(book.getGenre()) != null) {
            this.genreMap.get(book.getGenre()).add(book);
        } else {
            List<Book> genreBooks = new ArrayList<Book>();
            genreBooks.add(book);
            this.genreMap.put(book.getGenre(), genreBooks);
        }
    }

    public Iterator<Book> getGenreIterator(String genre) {
        // TODO: Return custom iterator for available books in that genre
        List<Book> books = genreMap.get(genre);
        return new GenreIterator(books);
    }

    // TODO: Add method to search
    public Book searchCollection(String genre, String title) {
        try {
            List<Book> genreBooks = genreMap.get(genre);
            for (Book book : genreBooks) {
                if (book.getTitle().equals(title)) {
                    return book;
                }
            }
            // check if book is not present
            throw new BookNotFoundException("Book is not found");
        } catch (BookNotFoundException e) {
            System.out.println("Book is not found.");
        }
        return null;
    }

    // return books
    public void returnBook(String genre, String title) throws BookNotFoundException {
        List<Book> genreBooks = genreMap.get(genre);
        boolean isPresent = false;
        for (Book book : genreBooks) {
            if (book.getTitle().equals(title)) {
                book.returnBook();
                isPresent = true;
            }
        }
        
        // check if book not found
        if (!isPresent) {
            throw new BookNotFoundException("Book is not found.");
        }
    }

    // checkout books
    public void checkoutBook(String genre, String title) {
        List<Book> genreBooks = genreMap.get(genre);
        for (Book book : genreBooks) {
            if (book.getTitle().equals(title)) {
                try {
                    book.checkout();
                } catch (BookNotAvailableException e) {
                    System.out.println("Book is not available");
                }
            }
        }
    }
}

// =============================
// LibraryUser Class
// =============================
class LibraryUser {
    private String username;
    private LibraryCollection library;

    // constructor
    LibraryUser(String username, LibraryCollection library) {
        this.username = username;
        this.library = library;
    }

    // checkout book
    public void checkoutBook(String genre, String title) {
        library.checkoutBook(genre, title);
    }

    // return book
    public void returnBook(String genre, String title) {
        try {
            library.returnBook(genre, title);
        } catch (BookNotFoundException e) { // catch error
            System.out.println("Book is not found");
        }
    }
}

// =============================
// Main Program
// =============================
public class LibraryTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LibraryCollection library = new LibraryCollection();

        // TODO: Add sample books to library
        Book testBook1 = new Book("La La Land", "Oscars", "movies");
        Book testBook2 = new Book("Moonlight", "Unknown", "movies");
        Book testBook3 = new Book("Murder on the Orient Express", "Agatha Christie", "mystery");
        library.addBook(testBook1);
        library.addBook(testBook2);
        library.addBook(testBook3);

        // TODO: Prompt user for genre, 
        System.out.print("Enter genre: ");
        String genre = scanner.nextLine();

        // list available books using iterator
        Iterator<Book> bookIterator = library.getGenreIterator(genre);
        while (bookIterator.hasNext()) {
            Book current = bookIterator.getNext();
            System.out.println(current);
        }

        // create libraryUser
        LibraryUser user1 = new LibraryUser("Johnny", library);

        // checkout book
        System.out.print("Checkout Book:\nEnter title: ");
        String title = scanner.nextLine();
        System.out.print("Enter genre: ");
        genre = scanner.nextLine();
        user1.checkoutBook(genre, title);
        // check checkout
        if (library.searchCollection(genre, title) != null) {
            System.out.println(library.searchCollection(genre, title).toString() + " availability: " + library.searchCollection(genre, title).isAvailable());
        }
        // return book
        System.out.print("Return Book:\nEnter title: ");
        title = scanner.nextLine();
        System.out.print("Enter genre: ");
        genre = scanner.nextLine();
        user1.returnBook(genre, title);
        // check return
        if (library.searchCollection(genre, title) != null) {
            System.out.println(library.searchCollection(genre, title).toString() + " availability: " + library.searchCollection(genre, title).isAvailable());
        }
        
        // TODO: Allow checkout and return, handling exceptions

        scanner.close();
    }
}