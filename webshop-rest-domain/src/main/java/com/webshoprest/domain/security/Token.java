package com.webshoprest.domain.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.webshoprest.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@Entity
@DynamicUpdate
public class Token {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "token_id")
	private Long tokenId;
	
	@Column(name = "token")
	private String token;
	
	@Getter
	@Column(name = "created")
	private final long created = new Date().getTime();
	
	@Column(name = "expiration")
	private long expiration;

	@JsonIgnore
	@OneToOne(mappedBy = "token", cascade = CascadeType.PERSIST)
	private User user;

}
