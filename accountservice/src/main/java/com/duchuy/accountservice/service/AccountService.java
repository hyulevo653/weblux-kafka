package com.duchuy.accountservice.service;

import com.duchuy.accountservice.data.Account;
import com.duchuy.accountservice.model.AccountDTO;
import com.duchuy.accountservice.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class AccountService {
    @Autowired
    AccountRepository accountRepository;
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
