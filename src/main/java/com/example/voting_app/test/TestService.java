package com.example.voting_app.test;

import org.springframework.stereotype.Service;

@Service
public class TestService {
   public TestResponse add(TestRequest request) {
       TestResponse response = new TestResponse();
       response.setResult(request.getA() + request.getB());
       return response;
   }    
}
