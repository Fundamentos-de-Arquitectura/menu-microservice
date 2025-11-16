package upc.edu.pe.menuservicefoodflow.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import upc.edu.pe.menuservicefoodflow.domain.model.Dish;

import java.util.List;
import java.util.Optional;

public interface DishRepository extends JpaRepository<Dish, Long> {
    List<Dish> findByUserId(Long userId);
    Optional<Dish> findByIdAndUserId(Long id, Long userId);
}
