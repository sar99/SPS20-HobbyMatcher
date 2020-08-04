package com.sps.hobbymatcher.repository;

import java.util.*;
import com.sps.hobbymatcher.domain.User;

import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends DatastoreRepository<User, Long> {

    public Optional<User> findByUsername(String username);

}