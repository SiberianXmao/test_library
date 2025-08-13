package com.example.demo.dao;

import com.example.demo.models.Book;
import com.example.demo.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PersonDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> showAll(){
        return jdbcTemplate.query("SELECT * FROM forst.person",new BeanPropertyRowMapper<>(Person.class));

    }

    public Person show(int id){
        return jdbcTemplate.query("SELECT * FROM forst.person WHERE id=?",new Object[]{id},
                new BeanPropertyRowMapper<>(Person.class)).stream()
                .findAny().orElse(null);
    }

    public Person showWithBooks(int id){
        // ищем существует ли наш пользователь и если да то возвращаем его
        Person person = jdbcTemplate.query("SELECT * FROM forst.person WHERE id=?",
                        new Object[]{id},new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny().orElse(null);
        // если он есть то ищем все книги которые ему принадлежат и записываем их в лист
        if (person != null){
            List<Book> books = jdbcTemplate.query(
                    "SELECT * FROM forst.book WHERE person_id=?",new Object[]{id},
                    new BeanPropertyRowMapper<>(Book.class)
            );
            person.setBooks(books);
        }
        return person;
    }

    public void save(Person person){
        jdbcTemplate.update("INSERT INTO forst.person(name, year_of_birth) VALUES (?, ?)",
                person.getName(),person.getYearOfBirth());
    }

    public void update(int id,Person updatedPerson){
        jdbcTemplate.update("UPDATE forst.person SET name=?, year_of_birth=? WHERE id =?",
                updatedPerson.getName(),updatedPerson.getYearOfBirth(),id);
    }

    public void delete(int id){
        jdbcTemplate.update("DELETE FROM forst.person WHERE id=?",id);
    }



}
