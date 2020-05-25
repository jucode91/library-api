package com.jay.apis.libraryapi.user;

import com.jay.apis.libraryapi.exception.LibraryResourceAlreadyExistException;
import com.jay.apis.libraryapi.exception.LibraryResourceNotFoundException;
import com.jay.apis.libraryapi.util.LibraryApiUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addUser(User userToBeAdded, String traceId)
            throws LibraryResourceAlreadyExistException {

        logger.debug("TraceId: {}, Request to add User: {}", traceId, userToBeAdded);
        UserEntity userEntity = new UserEntity(
                userToBeAdded.getUsername(),
                "SecurityConstants.NEW_USER_DEFAULT_PASSWORD",
                userToBeAdded.getFirstName(),
                userToBeAdded.getLastName(),
                userToBeAdded.getDateOfBirth(),
                userToBeAdded.getGender(),
                userToBeAdded.getPhoneNumber(),
                userToBeAdded.getEmailId(),
                "USER");

        userToBeAdded.setPassword("SecurityConstants.NEW_USER_DEFAULT_PASSWORD");
        UserEntity addedUser = null;

        try {
            addedUser = userRepository.save(userEntity);
        } catch (DataIntegrityViolationException e) {
            logger.error("TraceId: {}, User already exists!!", traceId, e);
            throw new LibraryResourceAlreadyExistException(traceId, "User already exists!!");
        }

        userToBeAdded.setUserId(addedUser.getUserId());
        logger.info("TraceId: {}, User added: {}", traceId, userToBeAdded);
    }

    public User getUser(Integer userId, String traceId) throws LibraryResourceNotFoundException {

        Optional<UserEntity> userEntity = userRepository.findById(userId);
        User user = null;

        if(userEntity.isPresent()) {
            UserEntity pe = userEntity.get();
            user = createUserFromEntity(pe);
        } else {
            throw new LibraryResourceNotFoundException(traceId, "User Id: " + userId + " Not Found");
        }

        return user;
    }

    public void updateUser(User userToBeUpdated, String traceId) throws LibraryResourceNotFoundException {

        Optional<UserEntity> userEntity = userRepository.findById(userToBeUpdated.getUserId());

        if(userEntity.isPresent()) {
            UserEntity ue = userEntity.get();
            if(LibraryApiUtils.doesStringValueExist(userToBeUpdated.getEmailId())) {
                ue.setEmailId(userToBeUpdated.getEmailId());
            }
            if(LibraryApiUtils.doesStringValueExist(userToBeUpdated.getPhoneNumber())) {
                ue.setPhoneNumber(userToBeUpdated.getPhoneNumber());
            }
            if(LibraryApiUtils.doesStringValueExist(userToBeUpdated.getPassword())) {
                ue.setPassword(userToBeUpdated.getPassword());
            }
            userRepository.save(ue);
            userToBeUpdated = createUserFromEntity(ue);
        } else {
            throw new LibraryResourceNotFoundException(traceId, "User Id: " + userToBeUpdated.getUserId() + " Not Found");
        }

    }


    public void deleteUser(Integer userId, String traceId) throws LibraryResourceNotFoundException {

        try {
            userRepository.deleteById(userId);
        } catch(EmptyResultDataAccessException e) {
            logger.error("TraceId: {}, User Id: {} Not Found", traceId, userId, e);
            throw new LibraryResourceNotFoundException(traceId, "User Id: " + userId + " Not Found");
        }
    }

    public List<User> searchUser(String firstName, String lastName, String traceId) {

        List<UserEntity> userEntities = null;
        if(LibraryApiUtils.doesStringValueExist(firstName) && LibraryApiUtils.doesStringValueExist(lastName)) {
            userEntities = userRepository.findByFirstNameAndLastNameContaining(firstName, lastName);
        } else if(LibraryApiUtils.doesStringValueExist(firstName) && !LibraryApiUtils.doesStringValueExist(lastName)) {
            userEntities = userRepository.findByFirstNameContaining(firstName);
        } else if(!LibraryApiUtils.doesStringValueExist(firstName) && LibraryApiUtils.doesStringValueExist(lastName)) {
            userEntities = userRepository.findByLastNameContaining(lastName);
        }
        if(userEntities != null && userEntities.size() > 0) {
            return createUsersForSearchResponse(userEntities);
        } else {
            return Collections.emptyList();
        }
    }

    private User createUserFromEntity(UserEntity ue) {
        return new User(ue.getUserId(), ue.getUsername(), ue.getFirstName(), ue.getLastName(),
                ue.getDateOfBirth(), ue.getGender(), ue.getPhoneNumber(), ue.getEmailId(), Role.valueOf(ue.getRole()));
    }

    private List<User> createUsersForSearchResponse(List<UserEntity> userEntities) {
        return userEntities.stream()
                .map(ue -> new User(ue.getUsername(), ue.getFirstName(), ue.getLastName()))
                .collect(Collectors.toList());
    }
}
