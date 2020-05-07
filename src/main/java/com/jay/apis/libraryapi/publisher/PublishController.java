package com.jay.apis.libraryapi.publisher;

import com.jay.apis.libraryapi.exception.LibraryResourceALreadyExistException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/publisher")
@AllArgsConstructor
public class PublishController {

    private PublisherService publisherService;

    @GetMapping(path = "/{publisherId}")
    public Publisher getPublisher(@PathVariable Integer publisherId) {
        return new Publisher(publisherId, "Book Publisher", "test@publisher.com", "123-456-789");
    }

    @PostMapping
    public ResponseEntity<?> addPublisher(@RequestBody Publisher publisher) {
        try {
            publisher = publisherService.addPublisher(publisher);
        } catch (LibraryResourceALreadyExistException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
        return new ResponseEntity(publisher, HttpStatus.CREATED);
    }
}
