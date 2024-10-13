package ru.star.springbankstar.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.star.springbankstar.entity.Rules;


public interface RecommendationRulesRepository extends JpaRepository<Rules, Long> {

    Rules findByQuery(String query);




}
