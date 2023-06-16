package com.lcwd.electronic.store.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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

}
