package com.training.mjunction.product.catalog.router;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.training.mjunction.product.catalog.handler.PricingHandler;

@Configuration
public class PricingRouter {

	@Bean
	public RouterFunction<ServerResponse> pricingRouterFunction(final PricingHandler priceHandler) {

		return route(GET("/api/v1/pricing").and(accept(APPLICATION_JSON)), priceHandler::getAll)

				.andRoute(GET("/api/v1/pricing/{priceId}").and(accept(APPLICATION_JSON)), priceHandler::getPrice)

				.andRoute(POST("/api/v1/pricing").and(accept(APPLICATION_JSON)), priceHandler::postPrice)

				.andRoute(PUT("/api/v1/pricing/{priceId}").and(accept(APPLICATION_JSON)), priceHandler::putPrice)

				.andRoute(DELETE("/api/v1/pricing/{priceId}").and(accept(APPLICATION_JSON)), priceHandler::deletePrice);

	}

}
