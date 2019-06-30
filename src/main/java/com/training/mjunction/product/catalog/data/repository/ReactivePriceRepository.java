
package com.training.mjunction.product.catalog.data.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.training.mjunction.product.catalog.data.documents.Price;

import reactor.core.publisher.Flux;

@Repository
public interface ReactivePriceRepository extends ReactiveMongoRepository<Price, String> {

	Flux<Price> findByProductId(String productId);

}
