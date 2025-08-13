package com.example.demo.dao;


import com.example.demo.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> showAll(){
        return jdbcTemplate.query("SELECT * FROM forst.book",new BeanPropertyRowMapper<>(Book.class));

    }

    public Book show(int id){
        return jdbcTemplate.query("SELECT * FROM forst.book WHERE id=?",new Object[]{id},
                new BeanPropertyRowMapper<>(Book.class)
        ).stream().findAny().orElse(null);
    }

    public void save(Book book){
        jdbcTemplate.update("INSERT INTO forst.book(person_id,name,author,year) VALUES (?,?,?,?)",
                book.getPersonId(),book.getName(),book.getAuthor(),book.getYear());
    }

    public void update(int id, Book updatedBook){
        jdbcTemplate.update("UPDATE forst.book SET person_id=?,name=?,author=?,year=? WHERE id=?",
                updatedBook.getPersonId(),updatedBook.getName(),updatedBook.getAuthor(),updatedBook.getYear(),id);
    }
    public void delete(int id){
        jdbcTemplate.update("DELETE FROM forst.book WHERE id=?",id);
    }

    public void assignBook(int bookId, int personId){
        jdbcTemplate.update("UPDATE forst.book SET person_id=? WHERE id=?",personId,bookId);
    }
    public void releaseBook(int bookId){
        jdbcTemplate.update("UPDATE forst.book SET person_id=NULL WHERE id=?",bookId);
    }

}
