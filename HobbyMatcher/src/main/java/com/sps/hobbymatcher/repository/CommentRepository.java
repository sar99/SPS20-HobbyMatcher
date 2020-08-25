package com.sps.hobbymatcher.repository;

import java.util.*;
import com.sps.hobbymatcher.domain.Comment;

import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends DatastoreRepository<Comment, Long> {
} 