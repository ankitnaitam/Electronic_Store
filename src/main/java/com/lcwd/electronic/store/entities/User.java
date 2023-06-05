package com.lcwd.electronic.store.entities;

import lombok.*;
import net.bytebuddy.utility.RandomString;
import org.hibernate.annotations.Generated;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.*;
import java.util.Random;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "user_details")
public class User extends CustomeFields{

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
