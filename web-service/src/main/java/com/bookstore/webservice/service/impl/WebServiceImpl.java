package com.bookstore.webservice.service.impl;

import com.bookstore.dto.BookDto;
import com.bookstore.webservice.dto.BookResponseDto;
import com.bookstore.webservice.service.WebApiService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class WebServiceImpl implements WebApiService {

    private static final String BASE_URI = "http://localhost:8081/api/v1/books";
    //private static final String BASE_URI = "http://management-service:8081/api/v1/books";
    @Autowired
    private Client client;

    @Override
    public List<BookDto> getAllBooks() {
        WebTarget target = client.target(BASE_URI);

        try {
            BookDto[] books = target
                    .request(MediaType.APPLICATION_JSON)
                    .get(BookDto[].class);

            return Arrays.asList(books);
        } catch (Exception e) {
            // Log the error for debugging
            e.printStackTrace();
            return Arrays.asList();
        }
    }

    @Override
    public BookDto createBook(BookDto book) {
        WebTarget target = client.target(BASE_URI);

        try {
            Response response = target
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(book, MediaType.APPLICATION_JSON));

            if (response.getStatus() == Response.Status.CREATED.getStatusCode() ||
                    response.getStatus() == Response.Status.OK.getStatusCode()) {
                return response.readEntity(BookDto.class);
            } else {
                System.err.println("Failed to create book. Status: " + response.getStatus());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    @Override
    public BookDto updateBook(String isbn, BookDto book) {
        WebTarget target = client.target(BASE_URI + "/" + isbn);

        try {
            Response response = target
                    .request(MediaType.APPLICATION_JSON)
                    .put(Entity.entity(book, MediaType.APPLICATION_JSON));

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                return response.readEntity(BookDto.class);
            } else {
                System.err.println("Failed to update book. Status: " + response.getStatus());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean deleteBook(String isbn) {
        WebTarget target = client.target(BASE_URI + "/" + isbn);

        try {
            Response response = target
                    .request()
                    .delete();

            return response.getStatus() == Response.Status.NO_CONTENT.getStatusCode();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public BookDto getBookByIsbn(String isbn) {
        WebTarget target = client.target(BASE_URI).path(isbn);

        try {
            Response response = target
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                return response.readEntity(BookDto.class);
            } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
                System.err.println("Book not found for ISBN: " + isbn);
                return null;
            } else {
                System.err.println("Failed to get book. Status: " + response.getStatus());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public BookDto findByIsbn(String isbn) {
        WebTarget target = client.target(BASE_URI).path(isbn);
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        if (response.getStatus() == 200) {
            return response.readEntity(BookDto.class);
        } else {
            System.err.println("Book not found in management API for ISBN: " + isbn + ", status: " + response.getStatus());
            return null;
        }
    }


}
