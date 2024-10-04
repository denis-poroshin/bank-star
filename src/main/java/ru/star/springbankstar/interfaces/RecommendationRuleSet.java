package ru.star.springbankstar.interfaces;

import ru.star.springbankstar.ProductDto.Recommendation;

import java.util.UUID;

public interface RecommendationRuleSet {

    Recommendation getRecommendation(UUID idUser);
    int get(UUID id);
}
