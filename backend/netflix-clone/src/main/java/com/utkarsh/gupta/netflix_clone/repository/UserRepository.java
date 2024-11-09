package com.utkarsh.gupta.netflix_clone.repository;

import com.utkarsh.gupta.netflix_clone.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    Optional<User> findById(String id);
    Optional<User> findByEmail(String email);

}
