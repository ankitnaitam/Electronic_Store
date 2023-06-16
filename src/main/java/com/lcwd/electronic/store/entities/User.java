package com.lcwd.electronic.store.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "user_details")
public class User extends CustomFields {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_email", unique = true)
    private String email;

    private String password;

    private String gender;

    @Column(length = 1000)
    private String about;

    @Column(name = "user_image")
    private String imageName;

}
