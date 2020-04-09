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
@Table(name = "item_category")
public class ItemCategory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "item_category_id")
	private Long itemCategoryId;

	@NotEmpty
	@Column(name = "item_category_name", unique = true)
	private String itemCategoryName;

	@NotEmpty
	@Column(name = "item_category_description")
	private String itemCategoryDescription;

	@JsonIgnore
	@OneToMany(mappedBy = "itemCategory", fetch = FetchType.LAZY)
	private List<Item> items;
	
}
