package com.webshoprest.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "city")
@DynamicUpdate
public class City {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "city_id")
	private Long cityId;
	
	@NotEmpty
	@Column(name = "city_name", unique = true, nullable = false)
	private String cityName;

	@Valid
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
	@JoinColumn(name = "country_id")
	private Country country;

	@JsonIgnore
	@OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
	private List<Address> addresses = new ArrayList<Address>();

}
