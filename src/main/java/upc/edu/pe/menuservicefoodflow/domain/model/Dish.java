package upc.edu.pe.menuservicefoodflow.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dishes")
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @ElementCollection
    @CollectionTable(name = "dish_ingredients", joinColumns = @JoinColumn(name = "dish_id"))
    private List<Ingredient> ingredients = new ArrayList<>();

    @NotNull
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private Long userId; // ID del usuario que creó el plato

    protected Dish() {}

    public Dish(String name, List<Ingredient> ingredients, BigDecimal price, String description, Long userId) {
        this.name = name;
        this.ingredients = ingredients != null ? ingredients : new ArrayList<>();
        this.price = price;
        this.description = description;
        this.userId = userId;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public List<Ingredient> getIngredients() { return ingredients; }
    public BigDecimal getPrice() { return price; }
    public String getDescription() { return description; }
    public Long getUserId() { return userId; }

    public void updateDescription(String newDescription) {
        this.description = newDescription;
    }

    public void updatePrice(BigDecimal newPrice) {
        this.price = newPrice;
    }
}
