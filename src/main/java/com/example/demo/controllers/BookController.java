package com.example.demo.controllers;


import com.example.demo.dao.BookDAO;
import com.example.demo.dao.PersonDAO;
import com.example.demo.models.Book;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookDAO bookDAO;

    @Autowired
    private PersonDAO personDAO;

    // страница с книгами ( /books )
    @GetMapping()
    public String showAll(Model model){
        model.addAttribute("books",bookDAO.showAll());
        return "book/allBooks";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id,Model model){
        Book book = bookDAO.show(id);
        model.addAttribute("book",book);
        if(book.getPersonId() != null){
            model.addAttribute("owner",personDAO.show(book.getPersonId()));
        }else {
            model.addAttribute("people",personDAO.showAll());
        }
        return "book/show";
    }

    @PostMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id, @RequestParam("personId") int personId){
        bookDAO.assignBook(id,personId);
        return "redirect:/books/" + id;
    }
    @PostMapping("/{id}/release")
    public String release(@PathVariable("id") int id){
        bookDAO.releaseBook(id);
        return "redirect:/books/" + id;
    }


    // страница добавления книги ( /books/new )
    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book){
        return "book/new";
    }
    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult){
        if (bindingResult.hasErrors()) return "book/new";
        bookDAO.save(book);
        return "redirect:/books";
    }

    // страница изменения книги ( /books/{id}/edit )
    @GetMapping("{id}/edit")
    public String edit(Model model,@PathVariable("id") int id){
        model.addAttribute("book",bookDAO.show(id));
        return "book/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                         @ModelAttribute("id") int id){
        if (bindingResult.hasErrors()) return "book/edit";
        bookDAO.update(id,book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        bookDAO.delete(id);
        return "redirect:/books";
    }


}
