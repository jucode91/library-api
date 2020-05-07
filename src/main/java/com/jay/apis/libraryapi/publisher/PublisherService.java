package com.jay.apis.libraryapi.publisher;

import com.jay.apis.libraryapi.exception.LibraryResourceALreadyExistException;
import com.jay.apis.libraryapi.exception.LibraryResourceNotFoundException;
import com.jay.apis.libraryapi.util.LibraryApiUtils;
import lombok.AllArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PublisherService {

    private final PublisherRepository publisherRepository;

    public void addPublisher(Publisher publisherToBeAdded) throws LibraryResourceALreadyExistException {

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
    }

    public Publisher getPublisher(Integer publisherId) throws LibraryResourceNotFoundException {

        Optional<PublisherEntity> publisherEntity = publisherRepository.findById(publisherId);
        Publisher publisher = null;

        if (publisherEntity.isPresent()) {
            publisher = createPublisherFromEntity(publisherEntity.get());
        } else {
            throw new LibraryResourceNotFoundException("Publisher Id :" + publisherId + " Not Found");
        }
        return publisher;
    }

    private Publisher createPublisherFromEntity(PublisherEntity publisherEntity) {
        return new Publisher(
                publisherEntity.getPublisherId(),
                publisherEntity.getName(),
                publisherEntity.getEmailId(),
                publisherEntity.getPhoneNumber()
        );
    }


    public void updatePublisher(Publisher publisherToBeUpdated) throws LibraryResourceNotFoundException {
        Optional<PublisherEntity> publisherEntity = publisherRepository.findById(publisherToBeUpdated.getPublisherId());

        if (publisherEntity.isPresent()) {
            PublisherEntity publisherEntityToBeUpdated = publisherEntity.get();

            if(LibraryApiUtils.doesStingValueExist(publisherToBeUpdated.getEmailId())){
                publisherEntityToBeUpdated.setEmailId(publisherToBeUpdated.getEmailId());
            }

            if(LibraryApiUtils.doesStingValueExist(publisherToBeUpdated.getPhoneNumber())){
                publisherEntityToBeUpdated.setPhoneNumber(publisherToBeUpdated.getPhoneNumber());
            }

            publisherRepository.save(publisherEntityToBeUpdated);

        } else {
            throw new LibraryResourceNotFoundException("Publisher Id :" + publisherToBeUpdated.getPublisherId() + " Not Found");
        }
    }



    private PublisherEntity createPublisherEntityFromPublisher(Publisher publisher) {
        return new PublisherEntity(
                publisher.getName(),
                publisher.getEmailId(),
                publisher.getPhoneNumber()
        );
    }
}
