package com.jhaps.clientrecords.entity.system;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.jhaps.clientrecords.entity.BaseEntity;
import com.jhaps.clientrecords.entity.client.Client;
import com.jhaps.clientrecords.entity.client.ClientLog;

import io.micrometer.common.lang.Nullable;
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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
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
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "profile_image_id", nullable= true)
	@Nullable
	private Image profileImage ; // this field is for the profile image
	
	@OneToMany(mappedBy = "user", 
				cascade = CascadeType.ALL,
				orphanRemoval = true,
				fetch = FetchType.LAZY )
	private List<Image> images;
	
	@OneToMany(mappedBy = "user")
	private List<Client> clientList;
	
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "user_role",
			joinColumns = @JoinColumn(name="user_id"),	//joins this entity with FK.
			inverseJoinColumns = @JoinColumn(name="role_id") //joins the relationedEntity(Role) FK.	
	)
	private Set<Role> roles;

	
	/* Helps to clear the roles.
	 * This breaks the relationship with the roles and allows for user Deletion.*/
	public void removeRoles() {
		this.roles.clear();
	}
	
	
	public Optional<Image> getProfileImage() {
		return Optional.ofNullable(this.profileImage);
	}
	
	
	//to cheking if the imageName is null .
	public String getProfileImageName() {
	    return getProfileImage()
	           .map(Image::getImageName)
	           .orElse(""); // or return Optional<String>
	}
	
	
}//ends class
