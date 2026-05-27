package com.wipro.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.wipro.account.entity.Account;

public interface AccountRepository extends JpaRepository<Account, String> {
}

