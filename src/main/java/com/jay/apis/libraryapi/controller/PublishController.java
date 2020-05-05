package com.jay.apis.libraryapi.controller;

import com.jay.apis.libraryapi.model.Publisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/publisher")
public class PublishController {

    @GetMapping(path = "/{publisherId}")
    public Publisher getPublisher(@PathVariable String publisherId) {
        return new Publisher(publisherId, "Book Publisher", "test@publisher.com", "123-456-789");
    }
}
