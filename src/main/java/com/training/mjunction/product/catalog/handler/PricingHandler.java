package com.training.mjunction.product.catalog.handler;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.ServerResponse.accepted;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.training.mjunction.product.catalog.data.documents.Price;
import com.training.mjunction.product.catalog.service.PricingService;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@Component
@Log4j2
public class PricingHandler {

	@Autowired
	private PricingService pricingService;

	public Mono<ServerResponse> getAll(final ServerRequest request) {

		log.info("Get prices");

		// parse query parameter product name
		final String productIds = request.queryParam("productIds").orElseGet(() -> null);

		// build response
		return ok().contentType(APPLICATION_JSON).body(pricingService.findAll(productIds), Price.class);

	}

	public Mono<ServerResponse> getPrice(final ServerRequest request) {

		log.info("Get price");

		// parse path-variable
		final String priceId = request.pathVariable("priceId");

		// build notFound response
		final Mono<ServerResponse> notFound = ServerResponse.notFound().build();

		// get product from repository
		final Mono<Price> priceMono = pricingService.findByPriceId(priceId);

		// build response
		return priceMono.flatMap(product -> ok().contentType(APPLICATION_JSON).body(fromObject(product)))
				.switchIfEmpty(notFound);
	}

	public Mono<ServerResponse> postPrice(final ServerRequest request) {

		log.info("Saving price");

		final Mono<Price> monoPrice = request.bodyToMono(Price.class);

		return status(201).contentType(APPLICATION_JSON).body(pricingService.save(monoPrice), Price.class);

	}

	public Mono<ServerResponse> putPrice(final ServerRequest request) {

		log.info("Updating price");

		// parse id from path-variable
		final String priceId = request.pathVariable("priceId");

		// get product data from request object
		final Mono<Price> monoPrice = request.bodyToMono(Price.class);

		monoPrice.doOnNext(b -> b.id(priceId)).then();

		// get product from repository
		final Mono<Price> responseMono = pricingService.save(monoPrice);

		// build response
		return responseMono.flatMap(price -> ok().contentType(APPLICATION_JSON).body(fromObject(price)));

	}

	public Mono<ServerResponse> deletePrice(final ServerRequest request) {

		log.info("Deleting price");

		// parse id from path-variable
		final String priceId = request.pathVariable("priceId");

		final Mono<Price> priceMono = pricingService.deleteById(priceId);

		// build response
		return priceMono.flatMap(price -> accepted().contentType(APPLICATION_JSON).body(fromObject(price)));

	}

}
