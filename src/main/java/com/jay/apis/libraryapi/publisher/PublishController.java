package com.jay.apis.libraryapi.publisher;

import com.jay.apis.libraryapi.LibraryApiApplication;
import com.jay.apis.libraryapi.exception.LibraryResourceALreadyExistException;
import com.jay.apis.libraryapi.exception.LibraryResourceNotFoundException;
import com.jay.apis.libraryapi.util.LibraryApiUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/publisher")
@AllArgsConstructor
public class PublishController {

    private PublisherService publisherService;

    @GetMapping
    public ResponseEntity<?> getPublishers() {
        List<Publisher> publishers = null;
        publishers = publisherService.getPublishers();
        return new ResponseEntity(publishers, HttpStatus.OK);
    }

    @GetMapping(path = "/{publisherId}")
    public ResponseEntity<?> getPublisher(@PathVariable Integer publisherId) {
        Publisher publisher = null;
        try {
            publisher = publisherService.getPublisher(publisherId);
        } catch (LibraryResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(publisher, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addPublisher(@RequestBody Publisher publisher) {
        try {
            publisherService.addPublisher(publisher);
        } catch (LibraryResourceALreadyExistException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
        return new ResponseEntity(publisher, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{publisherId}")
    public ResponseEntity<?> updatePublisher(@PathVariable Integer publisherId,
                                             @RequestBody Publisher publisher) {
        try {
            publisher.setPublisherId(publisherId);
            publisherService.updatePublisher(publisher);
        } catch (LibraryResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(publisher, HttpStatus.OK);
    }

    @GetMapping(path = "/search")
    public ResponseEntity<?> searchPublisher(@RequestParam String name) {
        if (!LibraryApiUtils.doesStingValueExist(name)) {
            return new ResponseEntity("Please enter a name to search publisher", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(publisherService.searchPublisher(name), HttpStatus.OK);
    }

}
