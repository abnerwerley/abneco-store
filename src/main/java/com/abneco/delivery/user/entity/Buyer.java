package com.abneco.delivery.user.entity;

import com.abneco.delivery.purchase.entity.Purchase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Buyer extends NaturalPerson {

    @OneToMany(mappedBy = "buyer")
    private List<Purchase> purchases = new ArrayList<>();
}
