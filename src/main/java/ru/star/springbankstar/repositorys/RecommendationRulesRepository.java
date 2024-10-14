package ru.star.springbankstar.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.star.springbankstar.entity.Rules;

import java.util.Optional;


public interface RecommendationRulesRepository extends JpaRepository<Rules, Long> {

    Optional<Rules> findByQuery(String query);




}
