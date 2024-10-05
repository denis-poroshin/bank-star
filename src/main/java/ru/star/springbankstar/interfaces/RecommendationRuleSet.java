package ru.star.springbankstar.interfaces;

import ru.star.springbankstar.poductDto.Recommendation;

import java.util.UUID;

public interface RecommendationRuleSet {

    Recommendation getRecommendation(UUID idUser);
}
