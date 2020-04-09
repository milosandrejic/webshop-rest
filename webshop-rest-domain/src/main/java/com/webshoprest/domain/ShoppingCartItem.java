package com.webshoprest.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "shopping_cart_item")
public class ShoppingCartItem {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "shopping_cart_item_id")
	private Long shoppingCartItemId;

	@Column(name = "orderedQty")
	Long orderedQty;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Column(name = "total_item_price")
	Double totalItemPrice;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "item_id")
	private Item item;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "shopping_cart_id", insertable = false, updatable = false)
	private ShoppingCart shoppingCart;
	
}
