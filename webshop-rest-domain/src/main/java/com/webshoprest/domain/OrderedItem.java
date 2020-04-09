package com.webshoprest.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "order_item")
public class OrderedItem {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_item_id")
	private Long orderedItemId;

	@NotEmpty
	@Column(name = "order_item_name")
	private String orderedItemName;

	@Column(name = "order_item_price")
	private Double orderedItemPrice;

	@Column(name = "orderedQty")
	private Long orderedQty;

	@Column(name = "total_item_price")
	private Double totalItemPrice;

	@Transient
	private Integer includedInOrderCount;

	@JsonIgnore
	@OneToMany(mappedBy = "orderItem")
	private List<OrderItemOrder> orderItemOrders;

	public OrderedItem(String orderItemName, Double orderItemPrice, Long orderedQty, Double totalItemPrice,
			List<OrderItemOrder> orderItemOrders) {
		this.orderedItemName = orderItemName;
		this.orderedItemPrice = orderItemPrice;
		this.orderedQty = orderedQty;
		this.totalItemPrice = totalItemPrice;
		this.orderItemOrders = orderItemOrders;
	}
	
}
