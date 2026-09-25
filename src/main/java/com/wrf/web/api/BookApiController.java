package com.wrf.web.api;

import com.wrf.entity.Book;
import com.wrf.entity.Page;
import com.wrf.service.BookService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

/**
 * @program: bookStore
 * @description: SPA 书籍浏览接口
 *   GET /api/books?min=&max=&page=&pageSize=
 *   GET /api/books/{id}
 * @author: Rifu Wu
 **/
@RestController
@RequestMapping("/api/books")
@Slf4j
@AllArgsConstructor
public class BookApiController {

    private final BookService bookService;

    @GetMapping
    public java.util.Map<String, Object> list(
            @RequestParam(value = "min", required = false) BigDecimal min,
            @RequestParam(value = "max", required = false) BigDecimal max,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "12") int pageSize) {

        boolean minV = min != null && min.signum() != 0;
        boolean maxV = max != null;

        // 复用服务层逻辑：无筛选走全量分页
        Page<Book> data;
        if (minV || maxV) {
            int minc = minV ? min.intValue() : 0;
            int maxc = maxV ? max.intValue() : Integer.MAX_VALUE;
            data = bookService.pageByPrice(page, pageSize, minc, maxc);
        } else {
            data = bookService.page(page, pageSize);
        }

        java.util.Map<String, Object> result = new LinkedHashMap<>();
        result.put("items", data.getItems());
        result.put("pageNo", data.getPageNo());
        result.put("pageSize", data.getPageSize());
        result.put("pageTotal", data.getPageTotal());
        result.put("pageTotalCount", data.getPageTotalCount());
        return result;
    }

    @GetMapping("/{id}")
    public Book get(@PathVariable("id") Integer id) {
        Book book = bookService.getById(String.valueOf(id));
        if (book == null) {
            throw new ApiException(ApiCodes.NOT_FOUND, "书籍不存在");
        }
        return book;
    }
}
