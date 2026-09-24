package com.ecommerce.user.controller;


import com.ecommerce.user.dto.UserRequest;
import com.ecommerce.user.dto.UserResponse;
import com.ecommerce.user.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
   private  final UserService userService;
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAlluser(){

        return ResponseEntity.ok(userService.fetchAllUser());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getuser(@PathVariable long id){
        return userService.fetchUser(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public  ResponseEntity<String> createuser(@RequestBody UserRequest userRequest){
        userService.addUser(userRequest);
        return ResponseEntity.ok("User added Succesfully");
    }
    @PutMapping("{id}")
    public  ResponseEntity<String> updateuser(@PathVariable long id,@RequestBody UserRequest userRequestupdate){
        boolean updated=userService.updatedUser(id,userRequestupdate);
        if(updated)
            return ResponseEntity.ok("User Updated Succesfully");
        return  ResponseEntity.notFound().build();
    }
}
