package com.duchuy.accountservice.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@Table
public class User {
    @Id
    private String id;
    private String username;
    private String password;
}
