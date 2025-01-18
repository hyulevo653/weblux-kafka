package com.duchuy.accountservice.service;

import com.duchuy.accountservice.data.Account;
import com.duchuy.accountservice.model.AccountDTO;
import com.duchuy.accountservice.repository.AccountRepository;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class AccountService {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public Mono<AccountDTO> createNewAccount(AccountDTO accountDTO){
        return Mono.just(accountDTO)
                .map(AccountDTO::dtoToEntity)
                .flatMap(account -> accountRepository.save(account))
                .map(AccountDTO::entityToModel)
                .doOnError(throwable -> log.error(throwable.getMessage()));
    }
    public Flux<AccountDTO> getAllAccounts() {
        return accountRepository.findAll()
                .map(AccountDTO::entityToModel);
    }

    public Mono<AccountDTO> getAccountById(String accountId) {
        //B1: check redis
        AccountDTO accountDTO = (AccountDTO) redisTemplate.opsForValue().get(accountId);
        if (accountDTO == null) {
          return accountRepository.findById(accountId)
                .map(AccountDTO::entityToModel)
              .flatMap(account -> {
                redisTemplate.opsForValue().set(accountId, account, Duration.of(10, ChronoUnit.SECONDS));
                return Mono.just(account);
              });
        }
        return Mono.just(accountDTO);
    }

    public Mono<AccountDTO> createAccount(AccountDTO accountDTO){
        Account account = AccountDTO.dtoToEntity(accountDTO);
        return accountRepository.save(account)
                .map(AccountDTO::entityToModel);
    }

    public Mono<Account> updateAccount(String id, AccountDTO accountDTO) {
        return accountRepository.findById(id)
                .flatMap(existingAccount -> {
                    existingAccount.setEmail(accountDTO.getEmail());
                    existingAccount.setCurrency(accountDTO.getCurrency());
                    existingAccount.setBalance(accountDTO.getBalance());
                    existingAccount.setReserved(accountDTO.getReserved());

                    return accountRepository.save(existingAccount);
                });
    }

    public Mono<Void> deleteAccount(String id) {
        return accountRepository.findById(id)
                .flatMap(account -> accountRepository.delete(account)) // Xóa tài khoản
                .then(); // Trả về Mono<Void> khi xóa thành công
    }

}
