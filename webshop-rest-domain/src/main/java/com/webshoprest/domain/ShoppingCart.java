package com.webshoprest.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "shopping_cart")
public class ShoppingCart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "shopping_cart_id")
	private Long shoppingCartId;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Column(name = "total_amount")
	private Double totalAmount;

	@JsonIgnore
	@OneToOne(mappedBy = "shoppingCart")
	private User user;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "shopping_cart_id")
	private List<ShoppingCartItem> shoppingCartItems = new ArrayList<>();
	
							
	
}
