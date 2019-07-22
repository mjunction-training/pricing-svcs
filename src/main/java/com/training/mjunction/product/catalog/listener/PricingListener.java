package com.training.mjunction.product.catalog.listener;

import static reactor.core.publisher.Mono.fromSupplier;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.training.mjunction.product.catalog.data.documents.Price;
import com.training.mjunction.product.catalog.service.PricingService;

@Component
public class PricingListener {

	@Autowired
	private PricingService pricingService;

	@RabbitListener(queues = "${spring.rabbitmq.queue.name}")
	public void save(@Payload final Price price, @Header("action") final String action) {
		if ("create".equals(action)) {
			pricingService.save(fromSupplier(() -> price));
		} else if ("update".equals(action)) {
			pricingService.save(fromSupplier(() -> price));
		} else if ("delete".equals(action)) {
			pricingService.deleteById(price.id());
		}
	}

}
