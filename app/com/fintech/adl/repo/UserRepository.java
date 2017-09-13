package com.fintech.adl.repo;

import java.util.List;

import com.google.inject.ImplementedBy;

@ImplementedBy(JPAUserRepository.class)
public interface UserRepository{
    List findByUsername(String username);
}
