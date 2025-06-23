package testController;

import com.bookStore.controller.BookController;
import com.bookStore.entity.Book;
import com.bookStore.entity.MyBookList;
import com.bookStore.service.BookService;
import com.bookStore.service.MyBookListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class BookControllerTest {

    private BookService bookService;
    private MyBookListService myBookListService;
    private BookController bookController;

    @BeforeEach
    void setUp() {
        bookService = mock(BookService.class);
        myBookListService = mock(MyBookListService.class);
        bookController = new BookController();
        bookController.service = bookService;
        bookController.myBookService = myBookListService;
    }

    @Test
    void homeReturnsHomeView() {
        assertEquals("home", bookController.home());
    }

    @Test
    void bookRegisterReturnsBookRegisterView() {
        assertEquals("bookRegister", bookController.bookRegister());
    }

    @Test
    void getAllBookReturnsBookListView() {
        List<Book> books = Arrays.asList(new Book(), new Book());
        when(bookService.getAllBook()).thenReturn(books);
        ModelAndView modelAndView = bookController.getAllBook();
        assertEquals("bookList", modelAndView.getViewName());
        assertEquals(books, modelAndView.getModel().get("book"));
    }

    @Test
    void addBookRedirectsToAvailableBooks() {
        Book book = new Book();
        String view = bookController.addBook(book);
        verify(bookService).save(book);
        assertEquals("redirect:/available_books", view);
    }

    @Test
    void getMyBooksReturnsMyBooksView() {
        List<MyBookList> myBooks = Arrays.asList(new MyBookList(), new MyBookList());
        when(myBookListService.getAllMyBooks()).thenReturn(myBooks);
        Model model = mock(Model.class);
        String view = bookController.getMyBooks(model);
        verify(model).addAttribute("book", myBooks);
        assertEquals("myBooks", view);
    }

    @Test
    void getMyListRedirectsToMyBooks() {
        Book book = new Book(1, "Title", "Author", "10");
        when(bookService.getBookById(1)).thenReturn(book);
        String view = bookController.getMyList(1);
        verify(myBookListService).saveMyBooks(any(MyBookList.class));
        assertEquals("redirect:/my_books", view);
    }

    @Test
    void editBookReturnsBookEditView() {
        Book book = new Book(1, "Title", "Author", "10");
        when(bookService.getBookById(1)).thenReturn(book);
        Model model = mock(Model.class);
        String view = bookController.editBook(1, model);
        verify(model).addAttribute("book", book);
        assertEquals("bookEdit", view);
    }

    @Test
    void deleteBookRedirectsToAvailableBooks() {
        String view = bookController.deleteBook(1);
        verify(bookService).deleteById(1);
        assertEquals("redirect:/available_books", view);
    }
}

