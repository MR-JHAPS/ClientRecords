package com.jhaps.clientrecords.entity.system;

import java.time.LocalDateTime;

import org.hibernate.annotations.processing.Exclude;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
/*
 *	Multiple users can have sameFile name.
 *			 Example:  user1 and user2 both may have file called "default.png".
 *	But single user cannot have 2 files with same name. 
 *			Example: user1 cannot have 2 files  with the name "default.png".
 */
@Table(name = "files",
	uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "file_name"})
)
@Data 
@ToString(exclude = "user")

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFile {

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "file_name")
	private String fileName;
	
	@Column(name = "stored_file_name")
	private String storedFileName;
	
	@Column(name = "content_type")
	private String contentType;

	
	@Column(name = "uploadTime")
	private LocalDateTime uploadTime;
	
	/* This is the id used to delete the images From the cloudinary. */	
	@Column(name="public_id")
	private String publicId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

}
