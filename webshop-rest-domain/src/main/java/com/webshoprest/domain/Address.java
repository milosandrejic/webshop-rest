package com.webshoprest.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "address")
@DynamicUpdate
public class Address {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "address_id", nullable = false)
	private Long addressId;
	
	@NotEmpty
	@Column(name = "street", nullable = false)
	private String street;
	
	@Min(value = 1)
	@Column(name = "street_number")
	private Long streetNumber;

	@Valid
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
	@JoinColumn(name = "city_id")
	private City city;

	@JsonIgnore
	@OneToMany(mappedBy = "address")
	private List<User> users;


}
