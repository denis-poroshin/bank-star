package ru.star.springbankstar.interfaces;

import ru.star.springbankstar.DTO.Product;
import ru.star.springbankstar.DTO.Recommendation;

import java.util.Collection;
import java.util.UUID;

public interface RecommendationRuleSet {

    Recommendation getRecommendation(UUID idUser);
    int get(UUID id);
}
