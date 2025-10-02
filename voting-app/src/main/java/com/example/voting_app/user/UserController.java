package com.example.voting_app.user;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final AppUserRepository userRepository;

    public UserController(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Tạo user đơn giản (JSON body), chỉ cần fullName + email; các field khác optional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public AppUser create(@RequestBody AppUser req) {
        // Đơn giản: lưu trực tiếp đối tượng nhận được
        return userRepository.save(req);
    }

    // Danh sách users
    @GetMapping
    public List<AppUser> list() {
        return userRepository.findAll();
    }
}
