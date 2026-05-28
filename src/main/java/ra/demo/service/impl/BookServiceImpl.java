package ra.demo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ra.demo.exception.BookNotFound;
import ra.demo.model.dto.request.BookDTO;
import ra.demo.model.entity.Book;
import ra.demo.repository.BookResporitory;
import ra.demo.service.BookService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookResporitory bookRepository;

    @Override
    public List<Book> getBooks() {
        log.debug("Bắt đầu hàm getBooks");
        List<Book> books = bookRepository.findAll();
        log.info("Lấy thanh cong {} books.", books.size());
        return books;
    }

    @Override
    public Book getBookById(Long id) {
        log.debug("Bắt đầu lấy book có ID: {}", id);
        return bookRepository.findById(id).orElseThrow(() -> {
            String errorMsg = "Không tồn tại sách có mã " + id;
            log.error(errorMsg);
            return new BookNotFound(errorMsg);
        });
    }

    @Override
    public Book insertBook(BookDTO bookDTO) {
        log.debug("Bắt đầu thêm mới sách với dữ liệu: {}", bookDTO);

        Book book = Book.builder()
                .title(bookDTO.getTitle())
                .author(bookDTO.getAuthor())
                .category(bookDTO.getCategory())
                .quantity(bookDTO.getQuantity())
                .build();

        Book savedBook = bookRepository.save(book);
        log.info("Them moi sach thanh cong có ID: {}", savedBook.getId());
        return savedBook;
    }

    @Override
    public Book updateBook(Long id, BookDTO bookDTO) {
        log.debug("Bắt đầu cập nhật toàn bộ sách ID: {} với dữ liệu: {}", id, bookDTO);

        bookRepository.findById(id).orElseThrow(() -> {
            String errorMsg = "Không tồn tại sách có mã " + id;
            log.error(errorMsg);
            return new BookNotFound(errorMsg);
        });

        Book book = Book.builder()
                .id(id)
                .title(bookDTO.getTitle())
                .author(bookDTO.getAuthor())
                .category(bookDTO.getCategory())
                .quantity(bookDTO.getQuantity())
                .build();

        Book updatedBook = bookRepository.save(book);
        log.info("Cập nhật thành công sách có ID: {}", id);
        return updatedBook;
    }

    @Override
    public boolean deleteBook(Long id) {
        log.debug("Bắt đầu xóa sách có ID: {}", id);

        bookRepository.findById(id).orElseThrow(() -> {
            String errorMsg = "Không tồn tại sách có mã " + id;
            log.error(errorMsg);
            return new BookNotFound(errorMsg);
        });

        bookRepository.deleteById(id);
        log.info("xóa thành công sách có ID: {}", id);
        return true;
    }

    @Override
    public Book updatePartialBook(Long id, BookDTO bookDTO) {
        log.debug("Bắt đầu cập nhật một phần sách ID: {} với dữ liệu: {}", id, bookDTO);

        Book book = bookRepository.findById(id).orElseThrow(() -> {
            String errorMsg = "Không tồn tại sách có mã " + id;
            log.error(errorMsg);
            return new BookNotFound(errorMsg);
        });

        if (bookDTO.getTitle() != null && !bookDTO.getTitle().isBlank()) {
            book.setTitle(bookDTO.getTitle());
        }

        if (bookDTO.getAuthor() != null && !bookDTO.getAuthor().isBlank()) {
            book.setAuthor(bookDTO.getAuthor());
        }

        if (bookDTO.getCategory() != null && !bookDTO.getCategory().isBlank()) {
            book.setCategory(bookDTO.getCategory());
        }

        if (bookDTO.getQuantity() != null && bookDTO.getQuantity() >= 0) {
            book.setQuantity(bookDTO.getQuantity());
        }

        Book updatedBook = bookRepository.save(book);
        log.info("Cập nhật thành công ID: {}", id);
        return updatedBook;
    }
}