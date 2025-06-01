package com.bookstore.webservice.controller;

import com.bookstore.dto.BookDto;
import com.bookstore.model.BookType;
import com.bookstore.webservice.service.WebApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class WebPageController {
    private final WebApiService webApiService;
    @GetMapping("/books")
    public String listBooks(Model model) {
        model.addAttribute("books", webApiService.getAllBooks());
        model.addAttribute("bookTypes", BookType.values());

        return "books";
    }

    @GetMapping("/books/add")
    public String showAddForm(Model model) {
        model.addAttribute("book", new BookDto());
        model.addAttribute("bookTypes", BookType.values());

        return "books/add";
    }

    @PostMapping("/books/add")
    public String addBook(@ModelAttribute("book") BookDto bookDto, RedirectAttributes redirectAttributes) {
        BookDto created = webApiService.createBook(bookDto);
        if (created != null) {
            redirectAttributes.addFlashAttribute("success", "Book added successfully!");
            return "redirect:/books";
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to add book.");
            return "redirect:/books/add";
        }
    }

    @GetMapping("/books/edit/{isbn}")
    public String showEditForm(@PathVariable String isbn, Model model, RedirectAttributes redirectAttributes) {
        BookDto book = webApiService.getBookByIsbn(isbn);
        if (book == null) {
            redirectAttributes.addFlashAttribute("error", "Book not found.");
            return "redirect:/books";
        }
        model.addAttribute("book", book);
        model.addAttribute("bookTypes", BookType.values());
        return "books/edit";
    }

    @PostMapping("/books/update/{isbn}")
    public String updateBook(
            @PathVariable String isbn,
            @ModelAttribute("book") BookDto bookDto,
            RedirectAttributes redirectAttributes) {

        BookDto updated = webApiService.updateBook(isbn, bookDto);

        if (updated != null) {
            redirectAttributes.addFlashAttribute("success", "Book updated successfully!");
            return "redirect:/books";
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to update book.");
            return "redirect:/books/edit/" + isbn;
        }
    }


    @GetMapping("/books/delete/{isbn}")
    public String deleteBook(@PathVariable String isbn, RedirectAttributes redirectAttributes) {
        boolean deleted = webApiService.deleteBook(isbn);
        if (deleted) {
            redirectAttributes.addFlashAttribute("success", "Book deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to delete book.");
        }
        return "redirect:/books";
    }
}
