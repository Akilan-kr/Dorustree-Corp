package com.example.dorustree_corp.Model.MySql;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor
@Data
public class BlacklistToken {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "token_seq")
    @SequenceGenerator(name = "token_seq", sequenceName = "token_sequence", initialValue = 10000, allocationSize = 1)
    private Long id;
    private String token;
    private Date expiryDate;

}
