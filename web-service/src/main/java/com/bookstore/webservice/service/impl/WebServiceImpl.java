package com.bookstore.webservice.service.impl;

import com.bookstore.dto.BookDto;
import com.bookstore.webservice.service.WebApiService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
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
public class WebServiceImpl implements WebApiService {

    private static final String BASE_URI = "http://localhost:8081/api/v1/books";

    private Client client;

    public WebServiceImpl(Client client) {
        this.client = client;
    }

    @Override
    public List<BookDto> getAllBooks() {
        WebTarget target = client.target(BASE_URI);

        try {
            BookDto[] books = target
                    .request(MediaType.APPLICATION_JSON)
                    .get(BookDto[].class);

            return Arrays.asList(books);
        } catch (Exception e) {
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
        WebTarget target = client.target(BASE_URI + "/update/" + isbn);

        Response response = target
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(book, MediaType.APPLICATION_JSON));

        System.out.println("Response status: " + response.getStatus());
        String responseBody = response.readEntity(String.class);
        System.out.println("Response body: " + responseBody);

// Try to deserialize manually if status == 200
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                return mapper.readValue(responseBody, BookDto.class);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            System.err.println("Failed to update book. Status: " + response.getStatus());
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
        Response response = target
                .request(MediaType.APPLICATION_JSON)
                .get();

        String responseBody = response.readEntity(String.class); // Read once
        System.out.println("Response body: " + responseBody);

        if (response.getStatus() == 200) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule()); // If using LocalDate
                mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                BookDto dto = mapper.readValue(responseBody, BookDto.class); // Parse manually
                return dto;
            } catch (Exception e) {
                System.err.println("Failed to parse BookDto: " + e.getMessage());
                return null;
            }
        } else {
            System.err.println("Failed to fetch book. Status: " + response.getStatus());
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
