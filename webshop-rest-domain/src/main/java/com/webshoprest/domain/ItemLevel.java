package com.webshoprest.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.webshoprest.domain.enums.ItemLevels;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "item_level")
public class ItemLevel{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "item_level_id")
	private Long itemLevelId;
	
	@Column(name = "level")
	@Enumerated(value = EnumType.STRING)
	private ItemLevels level;

	@JsonIgnore
	@OneToMany(mappedBy = "itemLevel")
	private List<Item> items;
	

}
