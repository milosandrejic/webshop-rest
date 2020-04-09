package com.webshoprest.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "_order")
public class Order {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id")
	private Long orderId;
	
	@Column(name = "total_amount")
	private Double totalAmount;
	
	@Column(name = "order_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date orderDate;

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "user_id")
	private User user;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "order_number_generator_id")
	private OrderNumberGenerator orderNumber;

	@JsonIgnore
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderItemOrder> orderItemOrders;

	public Order(Long orderId) {
		this.orderId = orderId;
	}
}
