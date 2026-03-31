package ru.yandex.practicum.model;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.*;

import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "shopping_cart")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "username", nullable = false, length = 64)
    private String username;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private boolean active = true;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<ShoppingCartItem> shoppingCartItems = new LinkedHashSet<>();

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public void addItem(ShoppingCartItem item) {
        item.setCart(this);
        shoppingCartItems.add(item);
    }

    public boolean removeItemByProductId(UUID id) {
        for (ShoppingCartItem item : shoppingCartItems) {
            if (item.getProductId()
                .equals(id)) {
                return shoppingCartItems.remove(item);
            }
        }
        return false;
    }

    public void updateItemQuantity(UUID productId, long quantity) {
        shoppingCartItems.stream()
            .filter(
                item -> item.getProductId()
                    .equals(productId))
            .findFirst()
            .ifPresent(item -> item.setQuantity(quantity));
    }
}
