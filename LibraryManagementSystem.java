import java.time.LocalDate;
import java.util.*;

class Book{
    private int bookId;
    private String bookTitle;
    private String authorName;
    private boolean isAvailable;

    public Book(int bookId, String bookTitle, String authorName){
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.authorName = authorName;
        this.isAvailable = true;
    }

    public int getBookID() {
        return bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getAuthorName() {
        return authorName;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailability(boolean status) {
        this.isAvailable = status;
    }

}

class User{
    private int registrationNumber;
    private String name;

    public User(int registrationNumber, String name){
        this.registrationNumber = registrationNumber;
        this.name = name;
    }

    public int getRegistrationNumber(){
        return registrationNumber;
    }

    public String getName(){
        return name;
    }
}

class IssuedRecords{
    private Book book;
    private User user;
    private LocalDate issuedDate;
    private LocalDate returnDate;
    private boolean isReturned;

    public IssuedRecords(Book book, User user){
        this.book = book;
        this.user = user;
        this.issuedDate = LocalDate.now();
        this.isReturned = false;
    }

    public Book getBook(){
        return book;
    }

    public User getUser(){
        return user;
    }

    public boolean isReturned(){
        return isReturned;
    }

    public void markReturned(){
        this.returnDate = LocalDate.now();
        this.isReturned = true;
    }

    public LocalDate getIssuedDate(){
        return issuedDate;
    }
}

class Library{
    private Map<Integer, Book> books;
    private Map<Integer, User> users;
    private List<IssuedRecords> records = new ArrayList<>();

    public Library(){
        books = new HashMap<>();
        users = new HashMap<>();
    }

    public void addBook(Book book){
        books.put(book.getBookID(), book);
    }

    public void registerUser(User user){
        users.put(user.getRegistrationNumber(), user);
    }

    public void issueBook(int bookId, int registrationNumber){
        Book book = books.get(bookId);
        User user = users.get(registrationNumber);

        if(book == null || user == null) {
            System.out.println("Error 404");
            return;
        }

        if(!book.isAvailable()){
            System.out.println("Book is not available");
            return;
        }

        book.setAvailability(false);
        IssuedRecords record = new IssuedRecords(book, user);
        records.add(record);
        System.out.println("Book Issued Successfully");
        return;
    }

    public void returnBook(int bookId, int registrationNumber){
        for(IssuedRecords record : records){
            if(record.getBook().getBookID() == bookId &&
                record.getUser().getRegistrationNumber() == registrationNumber &&
                !record.isReturned()){

                record.markReturned();
                record.getBook().setAvailability(true);
                System.out.println("Book Returned Successfully");
                return;
            }
        }
        System.out.println("Error 404");
    }

    public void showAvailableBooks(){
        for(Book book : books.values()){
            if(book.isAvailable()) {
                System.out.println(
                        "BookID: " + book.getBookID() +
                                ", Title: " + book.getBookTitle() +
                                ", Author: " + book.getAuthorName()
                );
            }
        }
    }

    public void showUsersBooks(int registrationNumber){
        for(IssuedRecords record : records){
            if(record.getUser().getRegistrationNumber() == registrationNumber && !record.isReturned()){
                System.out.println(
                        "Book: " + record.getBook().getBookTitle() +
                                ", User: " + record.getUser().getName() +
                                ", Issued Date: " + record.getIssuedDate() +
                                ", Returned: " + record.isReturned()
                );
            }
        }
    }
}

public class LibraryManagementSystem {
    public static void main(String[] args) {
        Library library = new Library();

        library.addBook(new Book(1, "Java Basics", "James Gosling"));
        library.addBook(new Book(2, "DSA", "CLRS"));

        library.registerUser(new User(101, "Shanmukh"));
        library.registerUser(new User(102, "Raj"));

        library.issueBook(1, 101);
        library.issueBook(2, 101);

        System.out.println("\nAvailable Books:");
        library.showAvailableBooks();

        System.out.println("\nUser 101 Books:");
        library.showUsersBooks(101);

        library.returnBook(1, 101);

        System.out.println("\nAfter Return:");
        library.showAvailableBooks();
    }
}
