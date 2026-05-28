package ra.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ra.demo.controller.BookController;
import ra.demo.exception.BookNotFound;
import ra.demo.model.entity.Book;
import ra.demo.service.BookService;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    private Book book1;
    private Book book2;

    @BeforeEach
    void setUp() {
        book1 = Book.builder()
                .id(1L)
                .title("Clean Code")
                .author("Robert C. Martin")
                .category("IT")
                .quantity(10)
                .build();

        book2 = Book.builder()
                .id(2L)
                .title("Effective Java")
                .author("Joshua Bloch")
                .category("IT")
                .quantity(5)
                .build();
    }

    @Test
    void getBooks_shouldReturnListAnd200() throws Exception {
        when(bookService.getBooks()).thenReturn(Arrays.asList(book1, book2));

        mockMvc.perform(get("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].title").value("Clean Code"))
                .andExpect(jsonPath("$.data[1].title").value("Effective Java"));
    }

    @Test
    void getBookById_found_shouldReturnBookAnd200() throws Exception {
        when(bookService.getBookById(1L)).thenReturn(book1);

        mockMvc.perform(get("/api/v1/books/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("Clean Code"));
    }

    @Test
    void getBookById_notFound_shouldReturn404() throws Exception {
        when(bookService.getBookById(99L)).thenThrow(new BookNotFound("Không tồn tại sách có mã 99"));

        mockMvc.perform(get("/api/v1/books/99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
