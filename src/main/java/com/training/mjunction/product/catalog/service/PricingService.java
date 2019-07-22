package com.training.mjunction.product.catalog.service;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.training.mjunction.product.catalog.data.documents.Price;
import com.training.mjunction.product.catalog.data.repository.ReactivePriceRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PricingService {

	@Autowired
	private ReactivePriceRepository pricingRepository;

	public Flux<Price> findAll(final String productIds) {

		Flux<Price> products = Flux.empty();

		if (isBlank(productIds)) {

			// fetch all products from repository
			products = pricingRepository.findAll();

		}

		if (isNotBlank(productIds)) {

			// fetch all products from repository
			products = pricingRepository.findByProductIdIn(of(productIds.split("\\s*,\\s*")).collect(toList()));

		}

		return products;

	}

	public Mono<Price> findByPriceId(final String priceId) {
		return pricingRepository.findById(priceId);
	}

	public Mono<Price> save(final Mono<Price> monoProduct) {
		return monoProduct.flatMap(price -> pricingRepository.save(price));
	}

	public Mono<Price> deleteById(final String priceId) {

		// get product from repository
		final Mono<Price> productMono = findByPriceId(priceId);

		pricingRepository.deleteById(priceId);

		return productMono;

	}

}