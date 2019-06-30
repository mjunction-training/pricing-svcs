package com.training.mjunction.product.catalog.handler;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.training.mjunction.product.catalog.data.documents.Price;
import com.training.mjunction.product.catalog.data.repository.ReactivePriceRepository;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Log4j2
public class ProductHandler {

	@Autowired
	private ReactivePriceRepository pricingRepository;

	public Mono<ServerResponse> getAll(final ServerRequest request) {

		log.info("Get prices");

		// parse query parameter product name
		final String productId = request.queryParam("productId").orElseGet(() -> null);

		Flux<Price> products = Flux.empty();

		if (isBlank(productId)) {

			// fetch all products from repository
			products = pricingRepository.findAll();

		}

		if (isNotBlank(productId)) {

			// fetch all products from repository
			products = pricingRepository.findByProductId(productId);

		}

		// build response
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(products, Price.class);

	}

	public Mono<ServerResponse> getPrice(final ServerRequest request) {

		log.info("Get price");

		// parse path-variable
		final String productId = request.pathVariable("priceId");

		// build notFound response
		final Mono<ServerResponse> notFound = ServerResponse.notFound().build();

		// get product from repository
		final Mono<Price> productMono = pricingRepository.findById(productId);

		// build response
		return productMono.flatMap(product -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromObject(product))).switchIfEmpty(notFound);
	}

	public Mono<ServerResponse> postPrice(final ServerRequest request) {

		log.info("Saving price");

		final Mono<Price> monoProduct = request.bodyToMono(Price.class);

		return ServerResponse.status(201).build(monoProduct.flatMap(p -> pricingRepository.save(p)).then());

	}

	public Mono<ServerResponse> putPrice(final ServerRequest request) {

		log.info("Updating price");

		// parse id from path-variable
		final String productId = request.pathVariable("priceId");

		// get product data from request object
		final Mono<Price> monoProduct = request.bodyToMono(Price.class);

		monoProduct.doOnNext(b -> b.id(productId)).then();

		// get product from repository
		final Mono<Price> responseMono = monoProduct.doOnNext(pricingRepository::save);

		// build response
		return responseMono.flatMap(product -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromObject(product)));

	}

	public Mono<ServerResponse> deletePrice(final ServerRequest request) {

		log.info("Deleting price");

		// parse id from path-variable
		final String productId = request.pathVariable("priceId");

		pricingRepository.deleteById(productId);

		// get product from repository
		final Mono<String> responseMono = Mono.just("Delete Succesfully!");

		// build response
		return responseMono.flatMap(strMono -> ServerResponse.accepted().contentType(MediaType.TEXT_PLAIN)
				.body(BodyInserters.fromObject(strMono)));

	}

}
