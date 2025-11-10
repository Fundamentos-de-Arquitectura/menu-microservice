package upc.edu.pe.menuservicefoodflow.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Entidad embebida que representa un ingrediente con su nombre y cantidad
 */
@Embeddable
public class Ingredient {

    @NotBlank
    @Column(name = "ingredient_name", nullable = false)
    private String name;

    @NotNull
    @Positive
    @Column(name = "ingredient_quantity", nullable = false)
    private Double quantity;

    @Column(name = "ingredient_unit", length = 50)
    private String unit; // opcional: "gramos", "ml", "unidades", etc.

    protected Ingredient() {} // Constructor requerido por JPA

    public Ingredient(String name, Double quantity) {
        this.name = name;
        this.quantity = quantity;
        this.unit = "unidades"; // valor por defecto
    }

    public Ingredient(String name, Double quantity, String unit) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit != null ? unit : "unidades";
    }

    public String getName() {
        return name;
    }

    public Double getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}

