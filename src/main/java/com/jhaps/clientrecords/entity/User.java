package com.jhaps.clientrecords.entity;

import java.util.List;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Column(name="telephone")
//private String telephone;

//*******I will add a address, city and country here as well.*******



@NoArgsConstructor
@Entity
@Table(name="users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name="email", nullable = false, unique = true)
	private String email;
	
	@Column(name="password", nullable=false)
	private String password;
	
	@Column(name = "wrong_password_attempts", nullable = true )
	private Integer attempts;

	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "user_role",
			joinColumns = @JoinColumn(name="user_id"),	//joins this entity with FK.
			inverseJoinColumns = @JoinColumn(name="role_id") //joins the relationedEntity(Role) FK.	
	)
	private Set<Role> roles;
	
//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name="role_id", nullable = false)
//	private Role role;
	
	
	public User(int id, String email, String password, Integer attempts, Set<Role> roles) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.attempts = attempts;
		this.roles = roles;
	}
	
	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public Integer getAttempts() {
		return attempts;
	}


	public void setAttempts(Integer attempts) {
		this.attempts = attempts;
	}


	public Set<Role> getRoles() {
		return roles;
	}


	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}//ends class
