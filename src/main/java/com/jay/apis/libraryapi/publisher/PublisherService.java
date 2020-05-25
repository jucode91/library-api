package com.jay.apis.libraryapi.publisher;

import com.jay.apis.libraryapi.exception.LibraryResourceAlreadyExistException;
import com.jay.apis.libraryapi.exception.LibraryResourceNotFoundException;
import com.jay.apis.libraryapi.util.LibraryApiUtils;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PublisherService {

    private PublisherRepository publisherRepository;

    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public void addPublisher(Publisher publisherToBeAdded, String traceId)
            throws LibraryResourceAlreadyExistException {

        PublisherEntity publisherEntity = new PublisherEntity(
                publisherToBeAdded.getName(),
                publisherToBeAdded.getEmailId(),
                publisherToBeAdded.getPhoneNumber()
        );

        PublisherEntity addedPublisher = null;

        try {
            addedPublisher = publisherRepository.save(publisherEntity);
        } catch (DataIntegrityViolationException e) {
            throw new LibraryResourceAlreadyExistException("TraceId: " + traceId + ", Publisher already exists!!");
        }

        publisherToBeAdded.setPublisherId(addedPublisher.getPublisherId());
    }

    public Publisher getPublisher(Integer publisherId, String traceId) throws LibraryResourceNotFoundException {

        Optional<PublisherEntity> publisherEntity = publisherRepository.findById(publisherId);
        Publisher publisher = null;

        if (publisherEntity.isPresent()) {

            PublisherEntity pe = publisherEntity.get();
            publisher = createPublisherFromEntity(pe);
        } else {
            throw new LibraryResourceNotFoundException("TraceId: " + traceId + ", Publisher Id: " + publisherId + " Not Found");
        }

        return publisher;
    }

    public void updatePublisher(Publisher publisherToBeUpdated, String traceId) throws LibraryResourceNotFoundException {

        Optional<PublisherEntity> publisherEntity = publisherRepository.findById(publisherToBeUpdated.getPublisherId());
        Publisher publisher = null;

        if (publisherEntity.isPresent()) {

            PublisherEntity pe = publisherEntity.get();
            if (LibraryApiUtils.doesStringValueExist(publisherToBeUpdated.getEmailId())) {
                pe.setEmailId(publisherToBeUpdated.getEmailId());
            }
            if (LibraryApiUtils.doesStringValueExist(publisherToBeUpdated.getPhoneNumber())) {
                pe.setPhoneNumber(publisherToBeUpdated.getPhoneNumber());
            }
            publisherRepository.save(pe);
            publisherToBeUpdated = createPublisherFromEntity(pe);
        } else {
            throw new LibraryResourceNotFoundException("TraceId: " + traceId + ", Publisher Id: " + publisherToBeUpdated.getPublisherId() + " Not Found");
        }

    }


    public void deletePublisher(Integer publisherId, String traceId) throws LibraryResourceNotFoundException {

        try {
            publisherRepository.deleteById(publisherId);
        } catch (EmptyResultDataAccessException e) {
            throw new LibraryResourceNotFoundException("TraceId: " + traceId + ", Publisher Id: " + publisherId + " Not Found");
        }
    }

    public List<Publisher> searchPublisher(String name, String traceId) {

        List<PublisherEntity> publisherEntities = null;
        if (LibraryApiUtils.doesStringValueExist(name)) {
            publisherEntities = publisherRepository.findByNameContaining(name);
        }
        if (publisherEntities != null && publisherEntities.size() > 0) {
            return createPublishersForSearchResponse(publisherEntities);
        } else {
            return Collections.emptyList();
        }
    }

    private List<Publisher> createPublishersForSearchResponse(List<PublisherEntity> publisherEntities) {

        return publisherEntities.stream()
                .map(pe -> createPublisherFromEntity(pe))
                .collect(Collectors.toList());
    }

    private Publisher createPublisherFromEntity(PublisherEntity pe) {
        return new Publisher(pe.getPublisherId(), pe.getName(), pe.getEmailId(), pe.getPhoneNumber());
    }
}
