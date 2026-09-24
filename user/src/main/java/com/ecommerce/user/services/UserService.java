package com.ecommerce.user.services;


import com.ecommerce.user.dto.AdressDto;
import com.ecommerce.user.dto.UserRequest;
import com.ecommerce.user.dto.UserResponse;
import com.ecommerce.user.entity.Adress;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // Get all users
    public List<UserResponse> fetchAllUser() {
        return userRepository.findAll().stream()
                .map(this::mapTOUserResponse)
                .collect(Collectors.toList());
    }

    // Save user
    public void addUser(UserRequest userRequest) {
        User user = new User();
        updateUserFromRequest(user,userRequest);
        userRepository.save(user);
    }


    // Get user by ID
    public Optional<UserResponse> fetchUser(long id) {
        return userRepository.findById(id).map(this::mapTOUserResponse);
    }

    // Update user
    public boolean updatedUser(long id, UserRequest userRequestupdate ) {

        return userRepository.findById(id)
                .map(user -> {
                   updateUserFromRequest(user,userRequestupdate);

                    userRepository.save(user);

                    return true;
                })
                .orElse(false);
    }
    private void updateUserFromRequest(User user, UserRequest userRequest) {
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
    if(userRequest.getAdress()!=null){
        Adress adress = new Adress();
        adress.setStreet(userRequest.getAdress().getStreet());
        adress.setCity(userRequest.getAdress().getCity());
        adress.setState(userRequest.getAdress().getState());
        adress.setCountry(userRequest.getAdress().getCountry());
        adress.setZipcode(userRequest.getAdress().getZipcode());
        user.setAdress(adress);
    }


    }
    private UserResponse  mapTOUserResponse(User user){
        UserResponse userResponse=new UserResponse();
        userResponse.setId(String.valueOf(user.getId()));
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhone(user.getPhone());
        userResponse.setRole(user.getRole());
        if(user.getAdress()!=null){
            AdressDto adressDto = new AdressDto();
            adressDto.setStreet(user.getAdress().getStreet());
            adressDto.setCity(user.getAdress().getCity());
            adressDto.setState(user.getAdress().getState());
            adressDto.setCountry(user.getAdress().getCountry());
            adressDto.setZipcode(user.getAdress().getZipcode());
            userResponse.setAdress(adressDto);
        }
        return userResponse;

    }
}