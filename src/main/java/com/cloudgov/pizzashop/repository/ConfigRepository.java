package com.cloudgov.pizzashop.repository;

import com.cloudgov.pizzashop.model.Config;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * Repository interface for managing Config documents in the MongoDB database.
 * This interface extends ReactiveMongoRepository to provide basic CRUD operations
 * and also defines a custom method for finding a Config by its key.
 */
@Repository
public interface ConfigRepository extends ReactiveMongoRepository<Config, String> {

    /**
     * Finds a Config document by its unique key.
     *
     * @param key the unique key of the Config document to find
     * @return a Mono emitting the Config document if found, or an empty Mono if not found
     */
    Mono<Config> findByKey(String key);
}