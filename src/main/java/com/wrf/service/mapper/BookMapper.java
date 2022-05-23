package com.wrf.service.mapper;

import com.wrf.entity.Book;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @program: bookStore
 * @description: 图书模块
 * @author: Rifu Wu
 * @create: 2022-05-22 10:54
 **/
public interface BookMapper {


    /**添加一本书
     * @param book
     */
    @Insert("INSERT INTO t_book(name, author, price, sales, stock, img_path) VALUES(#{book.name}, #{book.author}, #{book.price}, #{book.sales}, #{book.stock}, #{book.imgPath})")
    void addBook(@Param("book") Book book);

    /**根据id删除一本书
     * @param id
     */
    @Select("DELETE FROM t_book WHERE id = #{id}")
    void deleteBookById(@Param("id") Integer id);

    /**更新书籍信息
     * @param book
     */
    @Update("UPDATE t_book SET name = #{book.name}, author = #{book.author}, price = #{book.price}, sales = #{book.sales}, stock = #{book.stock}, img_path = #{book.imgPath} WHERE id = #{book.id}")
    void updateBook(@Param("book") Book book);

    /**根据id查询书籍
     * @param id
     * @return
     */
    @Select("SELECT id, name, author, price, sales, stock, img_path FROM t_book WHERE id= #{id}")
    Book queryBookById(@Param("id") Integer id);

    /**
     * @return 所有书籍的列表
     */
    @Select("SELECT id, name, author, price, sales, stock, img_path imgPath FROM t_book")
    List<Book> queryBooks();

    /**
     * @return 书籍条目的数量
     */
    @Select("SELECT COUNT(*) FROM t_book")
    Integer queryForPageTotalCount();

    /**
     * @param min
     * @param max
     * @return 单页中条目的数量
     */
    @Select("SELECT COUNT(*) FROM t_book WHERE price BETWEEN #{min} AND #{max}")
    Integer queryForPageTotalCountScreen(@Param("min") int min, @Param("max") int max);

    /**
     * @param begin
     * @param pageSize
     * @return 查询一页中的元素
     */
    @Select("SELECT id, name, author, price, sales, stock, img_path imgPath FROM t_book LIMIT #{begin}, #{pageSize}")
    List<Book> queryForPageElement(@Param("begin") int begin, @Param("pageSize") int pageSize);

    /**
     * @param begin
     * @param pageSize
     * @param min
     * @param max
     * @return 查询筛选之后的分页元素
     */
    @Select("SELECT id, name, author, price, sales, stock, img_path imgPath FROM t_book WHERE price BETWEEN #{min} AND #{max} ORDER BY price DESC LIMIT #{begin}, #{pageSize}")
    List<Book> queryForPageElementScreen(@Param("begin") int begin, @Param("pageSize") int pageSize, @Param("min") int min, @Param("max") int max);


}