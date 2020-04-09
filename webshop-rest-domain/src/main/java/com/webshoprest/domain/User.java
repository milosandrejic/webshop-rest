package com.webshoprest.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webshoprest.domain.security.Role;
import com.webshoprest.domain.security.Token;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@Setter
@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
@Table(name = "user")
public class User extends NamedEntity {

    @Past
    @Column(name = "dob")
    private LocalDate dob;

    @NotEmpty
    @Email
    @Column(name = "email")
    private String email;

    @NotEmpty
    @Column(name = "username")
    private String username;

    @NotEmpty
    @Column(name = "password")
    private String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Transient
    private String confirmPassword;

    @NotEmpty
    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "total_spent")
    private Double totalSpent;

    @Valid
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "token_id")
    private Token token;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "role_id")
    private Role role;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "shopping_cart_id")
    private ShoppingCart shoppingCart;
}
