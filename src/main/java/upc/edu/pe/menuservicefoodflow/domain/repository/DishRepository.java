package upc.edu.pe.menuservicefoodflow.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import upc.edu.pe.menuservicefoodflow.domain.model.Dish;

public interface DishRepository extends JpaRepository<Dish, Long> {
}
