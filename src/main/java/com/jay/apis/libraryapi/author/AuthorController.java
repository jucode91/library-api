package com.jay.apis.libraryapi.author;

import com.jay.apis.libraryapi.exception.LibraryResourceAlreadyExistException;
import com.jay.apis.libraryapi.exception.LibraryResourceBadRequestException;
import com.jay.apis.libraryapi.exception.LibraryResourceNotFoundException;
import com.jay.apis.libraryapi.util.LibraryApiUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping(path = "/v1/authors")
public class AuthorController {

    private static Logger logger = LoggerFactory.getLogger(AuthorController.class);

    private AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping(path = "/{authorId}")
    public ResponseEntity<?> getAuthor(@PathVariable Integer authorId,
                                          @RequestHeader(value = "Trace-Id", defaultValue = "") String traceId)
            throws LibraryResourceNotFoundException {

        if(!LibraryApiUtils.doesStringValueExist(traceId)) {
            traceId = UUID.randomUUID().toString();
        }

        return new ResponseEntity<>(authorService.getAuthor(authorId, traceId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addAuthor(@Valid @RequestBody Author author,
                                          @RequestHeader(value = "Trace-Id", defaultValue = "") String traceId)
            throws LibraryResourceAlreadyExistException {

        logger.debug("Request to add Author: {}", author);
        if(!LibraryApiUtils.doesStringValueExist(traceId)) {
            traceId = UUID.randomUUID().toString();
        }
        logger.debug("Added TraceId: {}", traceId);
        authorService.addAuthor(author, traceId);

        logger.debug("Returning response for TraceId: {}", traceId);
        return new ResponseEntity<>(author, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{authorId}")
    public ResponseEntity<?> updateAuthor(@PathVariable Integer authorId,
                                             @Valid @RequestBody Author author,
                                             @RequestHeader(value = "Trace-Id", defaultValue = "") String traceId)
            throws LibraryResourceNotFoundException {

        if(!LibraryApiUtils.doesStringValueExist(traceId)) {
            traceId = UUID.randomUUID().toString();
        }

        author.setAuthorId(authorId);
        authorService.updateAuthor(author, traceId);

        return new ResponseEntity<>(author, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{authorId}")
    public ResponseEntity<?> deleteAuthor(@PathVariable Integer authorId,
                                             @RequestHeader(value = "Trace-Id", defaultValue = "") String traceId)
            throws LibraryResourceNotFoundException {

        if(!LibraryApiUtils.doesStringValueExist(traceId)) {
            traceId = UUID.randomUUID().toString();
        }

        authorService.deleteAuthor(authorId, traceId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping(path = "/search")
    public ResponseEntity<?> searchAuthor(@RequestParam String firstName, @RequestParam String lastName,
                                             @RequestHeader(value = "Trace-Id", defaultValue = "") String traceId)
            throws LibraryResourceBadRequestException {

        if(!LibraryApiUtils.doesStringValueExist(traceId)) {
            traceId = UUID.randomUUID().toString();
        }

        if(!LibraryApiUtils.doesStringValueExist(firstName) && !LibraryApiUtils.doesStringValueExist(lastName)) {
            logger.error("TraceId: {}, Please enter at least one search criteria to search Authors!!", traceId);
            throw new LibraryResourceBadRequestException(traceId, "Please enter a name to search Author.");
        }

        return new ResponseEntity<>(authorService.searchAuthor(firstName, lastName, traceId), HttpStatus.OK);
    }
}
