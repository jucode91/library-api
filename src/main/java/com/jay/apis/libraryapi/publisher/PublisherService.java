package com.jay.apis.libraryapi.publisher;

import com.jay.apis.libraryapi.exception.LibraryResourceALreadyExistException;
import lombok.AllArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PublisherService {

    private final PublisherRepository publisherRepository;

    public Publisher addPublisher(Publisher publisherToBeAdded) throws LibraryResourceALreadyExistException {

        PublisherEntity publisherEntity = new PublisherEntity(
                publisherToBeAdded.getName(),
                publisherToBeAdded.getEmailId(),
                publisherToBeAdded.getPhoneNumber());


        PublisherEntity addedPublisher = null;

        try {
            addedPublisher = publisherRepository.save(publisherEntity);
        } catch (DataIntegrityViolationException exception) {
            throw new LibraryResourceALreadyExistException("Publisher already exist!!");
        }
        publisherToBeAdded.setPublisherId(addedPublisher.getPublisherId());

        return publisherToBeAdded;
    }
}
