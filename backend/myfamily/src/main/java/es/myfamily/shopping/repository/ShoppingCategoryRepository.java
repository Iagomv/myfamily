package es.myfamily.shopping.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.myfamily.shopping.model.ShoppingCategory;

@Repository
public interface ShoppingCategoryRepository extends JpaRepository<ShoppingCategory, Long> {

  Optional<ShoppingCategory> findByName(String name);
}
