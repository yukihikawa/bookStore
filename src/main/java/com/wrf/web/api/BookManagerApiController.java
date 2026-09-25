package com.wrf.web.api;

import com.wrf.entity.Book;
import com.wrf.entity.Page;
import com.wrf.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @program: bookStore
 * @description: SPA 后台书籍管理接口（全部需要 admin 登录）
 *   GET    /api/admin/books?page=&pageSize=        分页列表
 *   POST   /api/admin/books                        新增
 *   PUT    /api/admin/books/{id}                   更新
 *   DELETE /api/admin/books/{id}                   删除
 * @author: Rifu Wu
 **/
@RestController
@RequestMapping("/api/admin/books")
@AllArgsConstructor
public class BookManagerApiController {

    private final BookService bookService;
    private final AuthSupport authSupport;

    @GetMapping
    public Map<String, Object> list(HttpServletRequest request,
                                    @RequestParam(value = "page", defaultValue = "1") int page,
                                    @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        requireAdmin(request);
        Page<Book> data = bookService.page(page, pageSize);
        return pageView(data, data.getItems());
    }

    @PostMapping
    public Book create(HttpServletRequest request, @RequestBody Book book) {
        requireAdmin(request);
        book.setId(null);
        if (book.getSales() == null) book.setSales(0);
        if (book.getStock() == null) book.setStock(0);
        if (book.getPrice() == null) book.setPrice(new java.math.BigDecimal(0));
        if (book.getImgPath() == null) book.setImgPath("static/img/default.jpg");
        bookService.addBook(book);
        return book;
    }

    @PutMapping("/{id}")
    public Book update(HttpServletRequest request,
                       @PathVariable("id") int id,
                       @RequestBody Book book) {
        requireAdmin(request);
        Book current = bookService.getById(String.valueOf(id));
        if (current == null) {
            throw new ApiException(ApiCodes.NOT_FOUND, "书籍不存在");
        }
        if (book.getPrice() == null) book.setPrice(current.getPrice());
        if (book.getSales() == null) book.setSales(current.getSales());
        if (book.getStock() == null) book.setStock(current.getStock());
        book.setId(id);
        bookService.updateBook(book);
        return bookService.getById(String.valueOf(id));
    }

    @DeleteMapping("/{id}")
    public Map<String, String> delete(HttpServletRequest request,
                                      @PathVariable("id") int id) {
        requireAdmin(request);
        bookService.deleteBook(String.valueOf(id));
        return Map.of("message", "已删除");
    }

    private void requireAdmin(HttpServletRequest request) {
        var user = authSupport.current(request);
        if (user == null) {
            throw new ApiException(ApiCodes.NOT_LOGIN, "请先登录");
        }
        if (!authSupport.isAdmin(user)) {
            throw new ApiException(ApiCodes.FORBIDDEN, "请使用管理员账号登录");
        }
    }

    private static Map<String, Object> pageView(Page<Book> data, List<Book> items) {
        java.util.Map<String, Object> view = new java.util.LinkedHashMap<>();
        view.put("items", items);
        view.put("pageNo", data.getPageNo());
        view.put("pageSize", data.getPageSize());
        view.put("pageTotal", data.getPageTotal());
        view.put("pageTotalCount", data.getPageTotalCount());
        return view;
    }
}
