package com.training.mjunction.product.catalog.data.documents;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@Document(collection = "price")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "productId", "currency", "amount" })
public class Price {

	@Id
	@JsonProperty("id")
	private String id = UUID.randomUUID().toString();

	@Field("productId")
	@JsonProperty("productId")
	@Indexed(unique = false, direction = IndexDirection.ASCENDING, name = "product-id")
	private String productId;

	@Field("currency")
	@JsonProperty("currency")
	private String currency;

	@Field("amount")
	@JsonProperty("amount")
	private Double amount;

}
