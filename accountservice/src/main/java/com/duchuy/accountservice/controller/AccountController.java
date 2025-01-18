package com.duchuy.accountservice.controller;


import com.duchuy.accountservice.model.AccountDTO;
import com.duchuy.accountservice.service.AccountService;
import com.duchuy.accountservice.service.UserService;
import io.swagger.v3.oas.annotations.headers.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {
    @Autowired
    AccountService accountService;

    @Autowired
    UserService userService;

    @GetMapping
    public Flux<AccountDTO> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/{id}")
    public Mono<AccountDTO> getAccountById(@PathVariable String id, @RequestHeader(name = "token", defaultValue = "") String token) {
        return userService.validateToken(token).then(accountService.getAccountById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<AccountDTO> createAccount(@RequestBody AccountDTO request){
        return accountService.createAccount(request);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> updateAccount(@PathVariable String id, @RequestBody AccountDTO accountDTO) {
        return accountService.updateAccount(id, accountDTO)
                .map(updatedAccount -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("status", 200);
                    response.put("message", "Cập nhật thành công");
                    response.put("data", AccountDTO.entityToModel(updatedAccount)); // Trả về dữ liệu đã cập nhật
                    return ResponseEntity.ok(response);
                })
                .defaultIfEmpty(ResponseEntity.notFound().build()); // Nếu không tìm thấy tài khoản, trả về 404
    }


    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> deleteAccount(@PathVariable String id) {
        return accountService.deleteAccount(id)
                .then(Mono.just(ResponseEntity.ok().body(createResponse(200, "Xóa thành công"))))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).body(createResponse(404, "Tài khoản không tồn tại")));
    }

    private Map<String, Object> createResponse(int status, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status);
        response.put("message", message);
        return response;
    }
}
