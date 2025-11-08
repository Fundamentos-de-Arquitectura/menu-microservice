package upc.edu.pe.menuservicefoodflow.domain.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dishes")
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ElementCollection
    @CollectionTable(name = "dish_ingredients", joinColumns = @JoinColumn(name = "dish_id"))
    @Column(name = "ingredient", nullable = false)
    private List<String> ingredients = new ArrayList<>();

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(length = 500)
    private String description;

    protected Dish() {}

    public Dish(String name, List<String> ingredients, BigDecimal price, String description) {
        this.name = name;
        this.ingredients = ingredients;
        this.price = price;
        this.description = description;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public List<String> getIngredients() { return ingredients; }
    public BigDecimal getPrice() { return price; }
    public String getDescription() { return description; }

    public void updateDescription(String newDescription) {
        this.description = newDescription;
    }
}
