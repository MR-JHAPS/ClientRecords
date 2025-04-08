package com.jhaps.clientrecords.entity.system;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.jhaps.clientrecords.entity.BaseEntity;
import com.jhaps.clientrecords.entity.client.ClientLog;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;


@Entity
@Table(name="users")
public class User extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name="email", nullable = false, unique = true)
	private String email;
	
	@Column(name="password") //nullable for oauth2 
	private String password;
	
	@Column(name = "wrong_password_attempts", nullable = false )
	private int attempts;
	
	@Column(name="account_locked", nullable= false)
	private boolean isAccountLocked;	
	
	@Column(name = "lock_time")
	private LocalDateTime lockTime;		//time when the account was locked.

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore // This is the forward reference that will be serialized
	private List<ClientLog> clientLogs = new ArrayList<>();
	
	@OneToOne
	@JoinColumn(name = "profile_image_id")
	private Image profileImage; // this field is for the profile image
	
//	@OneToMany(mappedBy = "user")
//	private List<Client> clientList;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "user_role",
			joinColumns = @JoinColumn(name="user_id"),	//joins this entity with FK.
			inverseJoinColumns = @JoinColumn(name="role_id") //joins the relationedEntity(Role) FK.	
	)
	private Set<Role> roles;

	
	
	
	
	public User(int id, String email, String password, int attempts, Set<Role> roles, boolean isAccountLocked, 
			LocalDateTime lockTime) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.attempts = attempts;
		this.roles = roles;
		this.isAccountLocked = isAccountLocked;
		this.lockTime = lockTime;

	}
	
	public User() {
		super();
		this.id = 0;
		this.email = "";
		this.password = "";
		this.attempts = 0;
		this.roles = new HashSet<>();
		this.isAccountLocked = false;
		this.lockTime = null;

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


	public Image getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(Image profileImage) {
		this.profileImage = profileImage;
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


	public int getAttempts() {
		return attempts;
	}


	public void setAttempts(int attempts) {
		this.attempts = attempts;
	}


	public Set<Role> getRoles() {
		return roles;
	}


	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}


	public boolean isAccountLocked() {
		return isAccountLocked;
	}


	public void setAccountLocked(boolean isAccountLocked) {
		this.isAccountLocked = isAccountLocked;
	}


	public LocalDateTime getLockTime() {
		return lockTime;
	}


	public void setLockTime(LocalDateTime lockTime) {
		this.lockTime = lockTime;
	}

	
	
	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", password=" + password + ", attempts=" + attempts
				+ ", isAccountLocked=" + isAccountLocked + ", lockTime=" + lockTime + ", profileImage=" + profileImage
				+ ", roles=" + roles + "]";
	}	
	
	
	
	
}//ends class
