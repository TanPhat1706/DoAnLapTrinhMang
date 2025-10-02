package com.example.voting_app.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @Autowired
    private TestService testService;

    @GetMapping
    public String test() {
        return "Call API successfully!";
    }

    @PostMapping("/add")
    public ResponseEntity<TestResponse> add(@RequestBody TestRequest request) {
        return ResponseEntity.ok(testService.add(request));
    }
}
