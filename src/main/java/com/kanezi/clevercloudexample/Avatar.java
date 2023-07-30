package com.kanezi.clevercloudexample;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@Table(name = "avatars")
@AllArgsConstructor
@NoArgsConstructor
public class Avatar implements Serializable {
    @Id
    String name;
    @Column(name = "image_url", length = 4000)
    String imageUrl;
}
