package com.sps.hobbymatcher.repository;

import java.util.*;
import com.sps.hobbymatcher.domain.Post;

import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends DatastoreRepository<Post, Long> {
}