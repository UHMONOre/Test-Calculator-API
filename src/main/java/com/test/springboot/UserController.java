package com.test.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/all")
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @PostMapping("/create")
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @GetMapping("/allsaved")
    public List<Calculations> allSavedCalculations(@RequestHeader("User-ID")  Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid Id"));

        List<Calculations> list = new ArrayList<>();
        list = user.getSavedCalc();

        list.sort(Comparator.comparing(Calculations::getId).reversed());

        return list;
    }

    @DeleteMapping("/delete/{userid}")
    public void deleteUser(@RequestHeader("User-Id")  Integer userId, @PathVariable("userid") Integer deletedUserId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid Id"));

        if (user.getId().equals(deletedUserId)) {
            userRepository.delete(user);
        }else if(user.getId() == 1 || user.getAdminFlag()){
            User deletedUser = userRepository.findById(deletedUserId).orElseThrow(() -> new IllegalArgumentException("Invalid Id"));

            userRepository.delete(deletedUser);
        }else {
            throw new IllegalArgumentException("invalid authorization");
        }
    }

    @PostMapping("/admin/{userid}")
    public ResponseEntity<String> adminRequest(@RequestHeader("User-Id") Integer userId, @PathVariable("userid") Integer futureAdminId){
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid Id"));

        User futureAdmin = userRepository.findById(futureAdminId).orElseThrow(() -> new IllegalArgumentException("Invalid Id"));

        if (user.getId() == 1 || user.getAdminFlag()){
            futureAdmin.setAdminFlag(true);
            return ResponseEntity.ok("{\"userId\": " + futureAdmin + " is successfully become admin.}");
        }else {
            return ResponseEntity.status(401).body("Invalid authorization");
        }
    }

    @PostMapping("/removeadmin/{userid}")
    public void removeAdmin(@RequestHeader("User-Id") Integer userId, @PathVariable("userid") Integer adminId){
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid Id"));

        User admin = userRepository.findById(adminId).orElseThrow(() -> new IllegalArgumentException("Invalid Id"));

        if (admin.getId() == 1){
            throw new IllegalArgumentException("invalid request");
        }else if (user.getId() == 1 || user.getAdminFlag()){
            admin.setAdminFlag(false);
        }else {
            throw new IllegalArgumentException("invalid authorization");
        }
    }
}
