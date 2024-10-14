package ru.star.springbankstar.interfaces;

import ru.star.springbankstar.entity.Rules;

public interface RecommendationRulesService {

    Rules createRules(Rules rules);
    Rules removeRules(Long id);
    Rules getRules(Long id);
}
