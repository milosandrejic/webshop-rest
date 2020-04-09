package com.webshoprest.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "order_item_order")
public class OrderItemOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_item_order_id")
	private Long orderItemOrderId;

	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name ="order_item_id")
	private OrderedItem orderItem;

	public OrderItemOrder(Order order, OrderedItem orderItem) {
		this.order = order;
		this.orderItem = orderItem;
	}

	public OrderItemOrder(OrderedItem orderItem) {
		this.orderItem = orderItem;
	}
}
