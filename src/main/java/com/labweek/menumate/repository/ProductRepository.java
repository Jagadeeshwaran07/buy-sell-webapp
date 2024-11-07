package com.labweek.menumate.repository;

import com.labweek.menumate.entity.NewProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<NewProductEntity, Long> {
    // Custom query methods can be added if needed
    List<NewProductEntity> findByCategory(String category);

}