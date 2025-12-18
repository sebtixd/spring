// File: src/main/java/com/library/loanservice/client/UserClient.java
package com.library.loan_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service", url = "http://localhost:8081")  // ← ADD url HERE
public interface UserClient {

    @GetMapping("/api/users/profile")
    ResponseEntity<UserDto> getCurrentUser(
            @RequestHeader("Authorization") String authorizationHeader);

    // MUST BE PUBLIC
    public record UserDto(Long id, String email, String firstName, String lastName) {}
}