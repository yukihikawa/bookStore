package com.wrf.service.mapper;

import com.wrf.entity.Book;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @program: bookStore
 * @description: 图书模块
 * @author: Rifu Wu
 * @create: 2022-05-22 14:46
 **/
public interface BookMapper {
    /**
     * 使用 useGeneratedKeys 回填主键 book.id（对外 SPA API 需要）
     */
    @Options(useGeneratedKeys = true, keyProperty = "book.id", keyColumn = "id")
    @Insert("INSERT INTO t_book(name, author, price, sales, stock, img_path) VALUES(#{book.name}, #{book.author}, #{book.price}, #{book.sales}, #{book.stock}, #{book.imgPath})")
    void addBook(@Param("book") Book book);

    @Delete("DELETE FROM t_book WHERE id = #{id}")
    void deleteBookById(@Param("id") Integer id);

    @Update("UPDATE t_book SET name = #{book.name}, author = #{book.author}, price = #{book.price}, sales = #{book.sales}, stock = #{book.stock}, img_path = #{book.imgPath} WHERE id = #{book.id}")
    void updateBook(@Param("book") Book book);

    @Select("SELECT id, name, author, price, sales, stock, img_path FROM t_book WHERE id= #{id}")
    Book queryBookById(@Param("id") Integer id);

    @Select("SELECT id, name, author, price, sales, stock, img_path imgPath FROM t_book")
    List<Book> queryBooks();

    @Select("SELECT COUNT(*) FROM t_book")
    Integer queryForPageTotalCount();

    @Select("SELECT COUNT(*) FROM t_book WHERE price BETWEEN #{min} AND #{max}")
    Integer queryForPageTotalCountScreen(@Param("min") int min, @Param("max") int max);

    @Select("SELECT id, name, author, price, sales, stock, img_path imgPath FROM t_book LIMIT #{begin}, #{pageSize}")
    List<Book> queryForPageElement(@Param("begin") int begin, @Param("pageSize") int pageSize);

    @Select("SELECT id, name, author, price, sales, stock, img_path imgPath FROM t_book WHERE price BETWEEN #{min} AND #{max} ORDER BY price DESC LIMIT #{begin}, #{pageSize}")
    List<Book> queryForPageElementScreen(@Param("begin") int begin, @Param("pageSize") int pageSize,
                                         @Param("min") int min, @Param("max") int max);

    /**
     * 条件扣库存（防超卖的关键）：仅当当前库存足够时才扣减，
     * 由数据库行锁保证并发安全；返回 0 表示库存不足，调用方据此回滚。
     */
    @Update("UPDATE t_book SET stock = stock - #{count} WHERE id = #{id} AND stock >= #{count}")
    int decreaseStock(@Param("id") Integer id, @Param("count") int count);

    /** 销量 +count（无条件） */
    @Update("UPDATE t_book SET sales = sales + #{count} WHERE id = #{id}")
    int increaseSales(@Param("id") Integer id, @Param("count") int count);
}
