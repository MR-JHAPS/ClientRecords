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
 *	Multiple users can have sameImage name.
 *			 Example:  user1 and user2 both may have image called "default.png".
 *	But single user cannot have 2 images with same name. 
 *			Example: user1 cannot have 2 images  with the name "default.png".
 */
@Table(name = "images",
	uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "image_name"})
)
@Data 
@ToString(exclude = "user")

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image {

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "image_name")
	private String imageName;
	
	@Column(name = "stored_file_name")
	private String storedFileName;
	
	@Column(name = "content_type")
	private String contentType;
	
	@Column(name = "url")
	private String url;
	
	@Column(name = "uploadTime")
	private LocalDateTime uploadTime;
	
	/* This is the id used to delete the images From the cloudinary. */	
	@Column(name="public_id")
	private String publicId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

}
