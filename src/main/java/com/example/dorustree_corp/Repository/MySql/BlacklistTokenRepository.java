package com.example.dorustree_corp.Repository.MySql;

import com.example.dorustree_corp.Model.MySql.BlacklistToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlacklistTokenRepository extends JpaRepository<BlacklistToken, Long> {
    boolean existsByToken(String token);
}
