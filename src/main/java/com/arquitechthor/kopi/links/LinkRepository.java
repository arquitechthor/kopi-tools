package com.arquitechthor.kopi.links;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LinkRepository extends JpaRepository<Link, Long> {
    List<Link> findByCategory(String category);

    List<Link> findAllByOrderByCategoryAsc();
}
