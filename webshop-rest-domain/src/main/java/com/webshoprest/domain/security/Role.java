
package com.webshoprest.domain.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.webshoprest.domain.User;
import com.webshoprest.domain.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "role")
public class Role {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_id")
	private Long roleId;
	
	@Column(name = "role")
	@Enumerated(EnumType.STRING)
	private Roles role;

	@JsonIgnore
	@OneToMany(mappedBy = "role")
	private List<User> users;
	
}
