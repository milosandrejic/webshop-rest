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
@Table(name = "country")
public class Country{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "country_id")
	private Long countryId;
	
	@NotEmpty
	@Column(name = "country_name", unique = true)
	private String countryName;

	@JsonIgnore
	@OneToMany(mappedBy = "country", cascade = CascadeType.ALL)
	private List<City> cities;

}
