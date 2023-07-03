package com.lcwd.electronic.store.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "categories")
public class Category extends CustomFields {

    @Id
    @Column(name = "category_id")
    private String categoryId;

    private String title;

    private String description;

    private String coverImage;

    @OneToMany(mappedBy ="category" ,cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Product> products=new ArrayList<>();

}
