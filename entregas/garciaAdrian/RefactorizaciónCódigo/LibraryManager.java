import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Arrays;

public class LibraryManager {
    private List<Book> books;
    private List<Author> authors;
    private List<BookAuthor> relations;
    private Scanner scanner;

    public LibraryManager() {
        this.books = new ArrayList<>();
        this.authors = new ArrayList<>();
        this.relations = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    public void startLibraryManager() {
        while (true) {
            System.out.println("1. Listar Libros");
            System.out.println("2. Listar Autores");
            System.out.println("3. Agregar Libro");
            System.out.println("4. Agregar Autor");
            System.out.println("5. Agregar Relación");
            System.out.println("6. Buscar Libros por Palabras Clave");
            System.out.println("7. Salir");
            System.out.print("Ingrese su elección: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    if (books.isEmpty()) {
                        System.out.println("No hay libros para mostrar.");
                    } else {
                        listBooks();
                    }
                    break;
                case 2:
                    if (authors.isEmpty()) {
                        System.out.println("No hay autores para mostrar.");
                    } else {
                        listAuthors();
                    }
                    break;
                case 3:
                    addBook();
                    break;
                case 4:
                    addAuthor();
                    break;
                case 5:
                    addRelation();
                    break;
                case 6:
                    searchBooksByKeywords();
                    break;
                case 7:
                    System.out.println("Saliendo del Administrador de Biblioteca.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opción inválida. Por favor, intente de nuevo.");
            }
        }
    }

    private void listBooks() {
        System.out.println("Lista de Libros:");
        for (Book book : books) {
            System.out.println(book);
            List<Author> bookAuthors = getAuthorsByBookId(book.getId());
            if (!bookAuthors.isEmpty()) {
                System.out.println("Autores:");
                for (Author author : bookAuthors) {
                    System.out.println("- " + author.getName());
                }
            } else {
                System.out.println("No se encontraron autores para este libro.");
            }
            System.out.println();
        }
    }

    private void listAuthors() {
        System.out.println("Lista de Autores:");
        for (Author author : authors) {
            System.out.println(author);
        }
    }

    private void addBook() {
        System.out.print("Ingrese el título del libro: ");
        String title = scanner.nextLine();
        System.out.print("Ingrese el año de publicación: ");
        int publicationYear = scanner.nextInt();
        scanner.nextLine(); 

        boolean validTypeOfDocument = true;
        String typeDocument = "";
        do {
            System.out.println("Tipo de documento: (1-Libro, 2-Revista, 3-Artículo, 4-Paper) ");
            validTypeOfDocument = true;
            int numberDocument = scanner.nextInt();
            scanner.nextLine(); 

            switch (numberDocument) {
                case 1:
                    typeDocument = "Libro";
                    break;
                case 2:
                    typeDocument = "Revista";
                    break;
                case 3:
                    typeDocument = "Artículo";
                    break;
                case 4:
                    typeDocument = "Paper";
                    break;
                default:
                    System.out.println("Tipo de documento no válido, ingrese otro.");
                    validTypeOfDocument = false;
            }
        } while (!validTypeOfDocument);

        System.out.print("Ingrese las palabras clave separadas por coma: ");
        String inputKeywords = scanner.nextLine();
        String[] keywords = inputKeywords.split(",");

        Book newBook = new Book(books.size() + 1, title, publicationYear, typeDocument);
        newBook.setKeywords(keywords);
        books.add(newBook);
        System.out.println("Libro agregado correctamente.");
    }

    private void addAuthor() {
        System.out.print("Ingrese el nombre del autor: ");
        String name = scanner.nextLine();

        Author newAuthor = new Author(authors.size() + 1, name);
        authors.add(newAuthor);
        System.out.println("Autor agregado correctamente.");
    }

    private void addRelation() {
        System.out.println("Agregar relación entre Libro y Autor:");
        
        System.out.println("Libros disponibles:");
        for (Book book : books) {
            System.out.println(book.getId() + ". " + book.getTitle());
        }
        System.out.print("Seleccione el ID del libro: ");
        int bookId = scanner.nextInt();
        scanner.nextLine(); 

        Book book = findBookById(bookId);
        if (book == null) {
            System.out.println("Libro no encontrado.");
            return;
        }

        System.out.println("Autores disponibles:");
        for (Author author : authors) {
            System.out.println(author.getId() + ". " + author.getName());
        }
        System.out.print("Seleccione el ID del autor: ");
        int authorId = scanner.nextInt();
        scanner.nextLine();

        Author author = findAuthorById(authorId);
        if (author == null) {
            System.out.println("Autor no encontrado.");
            return;
        }

        for (BookAuthor relation : relations) {
            if (relation.getBookId() == bookId && relation.getAuthorId() == authorId) {
                System.out.println("La relación ya existe.");
                return;
            }
        }

        BookAuthor newRelation = new BookAuthor(bookId, authorId);
        relations.add(newRelation);
        System.out.println("Relación agregada correctamente: " + book.getTitle() + " - " + author.getName());
    }

    private void searchBooksByKeywords() {
        System.out.print("Ingrese las palabras clave para buscar libros: ");
        String inputKeywords = scanner.nextLine();
        String[] keywords = inputKeywords.split(",");

        List<Book> matchedBooks = new ArrayList<>();
        for (Book book : books) {
            for (String keyword : keywords) {
                if (Arrays.asList(book.getKeywords()).contains(keyword.trim())) {
                    matchedBooks.add(book);
                    break;
                }
            }
        }

        if (matchedBooks.isEmpty()) {
            System.out.println("No se encontraron libros con las palabras clave proporcionadas.");
        } else {
            System.out.println("Libros encontrados:");
            for (Book book : matchedBooks) {
                System.out.println(book);
            }
        }
    }

    private Book findBookById(int bookId) {
        for (Book book : books) {
            if (book.getId() == bookId) {
                return book;
            }
        }
        return null;
    }

    private Author findAuthorById(int authorId) {
        for (Author author : authors) {
            if (author.getId() == authorId) {
                return author;
            }
        }
        return null;
    }

    private List<Author> getAuthorsByBookId(int bookId) {
        List<Author> bookAuthors = new ArrayList<>();
        for (BookAuthor relation : relations) {
            if (relation.getBookId() == bookId) {
                Author author = findAuthorById(relation.getAuthorId());
                if (author != null) {
                    bookAuthors.add(author);
                }
            }
        }
        return bookAuthors;
    }

    public static void main(String[] args) {
        LibraryManager libraryManager = new LibraryManager();
        libraryManager.startLibraryManager();
    }
}
