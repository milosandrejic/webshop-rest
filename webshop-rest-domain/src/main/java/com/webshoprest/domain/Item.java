package com.webshoprest.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webshoprest.domain.enums.UnitOfMeasure;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "item")
@DynamicUpdate
public class Item {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "item_id")
	private Long itemId;
	
	@NotEmpty
	@Column(name = "name")
	private String itemName;

	@Positive
	@Column(name = "price")
	private Double price;

	@NotNull
	@Column(name = "unit_of_measure")
	@Enumerated(EnumType.STRING)
	private UnitOfMeasure unitOfMeasure;

	@Column(name = "qty")
	private Long qty;

	@Column(name = "discount")
	private Double discount;

	@NotEmpty
	@Column(name = "description", columnDefinition = "text")
	private String description;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Column(name = "image_url")
	private String imageUrl;

	@JsonIgnore
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "item_level_id")
	private ItemLevel itemLevel;

	@JsonIgnore
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "item_category_id")
	private ItemCategory itemCategory;

	@JsonIgnore
	@OneToMany(mappedBy = "item", fetch = FetchType.LAZY, orphanRemoval = true)
	private List<ShoppingCartItem> shoppingCartItems;

}
