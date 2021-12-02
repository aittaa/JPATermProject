package project.service;

import project.domain.Address;
import project.domain.User;
import project.repository.UserRepository;

import java.util.List;

public class ReservationService {

    UserRepository userRepository = new UserRepository();

    public void createUser(String name, Long age, String city, String street, String zipCode) {
        User user = new User(name, age, new Address(city, street, zipCode));

        userRepository.save(user);
    }
    public User findOneWithID(Long userID){
        return userRepository.findOneWithID(userID);
    }


    public List<User> findAllUser(){
        return userRepository.findAll();
    }


}
