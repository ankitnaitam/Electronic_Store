package com.lcwd.electronic.store.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    @Id
    private String cartId;
    private Date createdAt;

    @OneToOne
    private User user;

    //while fetching cart items if we get duplicates item, to solve these issue we can use Set interface(will get efficiency but get problem while removing order) or we can remove FetchType.EAGER
    //we r most likely getting duplicates bz when using FetchType.EAGER, Hibernate uses an outer join to fetch the data in join table
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CartItem> items = new ArrayList<>();
    //orphanRemoval will clear cart from db also
    //its usage is to delete orphaned entities from the database.
    // An entity that is no longer attached to its parent is the definition of being an orphan.
    //issue :HibernateException - A collection with cascade="all-delete-orphan"
    //What is happening is that Hibernate requires complete ownership of the children collection in the parent object. If you set it to a new object, Hibernate is unable to track changes to that collection and thus has no idea how to apply the cascading persistence to your objects.
    //To avoid this problem, any time we want to add or delete something to the list, we have to modify the contents of the collection instead of assigning a new one
    //Updated setter of children
//    public void setItems(List<CartItem> items) { //if we use these to solve orphanRemoval issue we will get UnsupportedOperationException breakPoint while testing addItemToCart() method
//        this.items.addAll(items);
//        for (CartItem item : items)
//            item.setCart(this);
//   }
}
