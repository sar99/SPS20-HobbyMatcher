package com.sps.hobbymatcher.repository;

import java.util.*;
import com.sps.hobbymatcher.domain.Hobby;

import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.*;

@Repository
public interface HobbyRepository extends DatastoreRepository<Hobby, Long> {
    public Set<Hobby> findAll();
    public List<Hobby> findByName(String name);
}