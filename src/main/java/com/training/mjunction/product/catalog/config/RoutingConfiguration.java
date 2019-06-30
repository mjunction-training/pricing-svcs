package com.training.mjunction.product.catalog.config;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.training.mjunction.product.catalog.handler.ProductHandler;

@Configuration
public class RoutingConfiguration {

	@Bean
	public RouterFunction<ServerResponse> monoRouterFunction(final ProductHandler priceHandler) {
		return route(GET("/api/v1/pricing").and(accept(MediaType.APPLICATION_JSON)), priceHandler::getAll)
				.andRoute(GET("/api/v1/pricing/{priceId}").and(accept(MediaType.APPLICATION_JSON)),
						priceHandler::getPrice)
				.andRoute(POST("/api/v1/pricing").and(accept(MediaType.APPLICATION_JSON)), priceHandler::postPrice)
				.andRoute(PUT("/api/v1/pricing/{priceId}").and(accept(MediaType.APPLICATION_JSON)),
						priceHandler::putPrice)
				.andRoute(DELETE("/api/v1/pricing/{priceId}").and(accept(MediaType.APPLICATION_JSON)),
						priceHandler::deletePrice);
	}

}
