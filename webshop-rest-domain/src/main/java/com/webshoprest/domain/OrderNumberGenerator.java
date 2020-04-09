package com.webshoprest.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "order_number_generator")
public final class OrderNumberGenerator {
	
	@Id
	@SequenceGenerator(allocationSize = 1, initialValue = 1000000000, name = "orderNumberSeq")
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "orderNumberSeq")
	private Long orderNumber;

}
