package ru.star.springbankstar.services;

import org.springframework.stereotype.Service;
import ru.star.springbankstar.interfaces.RecommendationRuleSet;
import ru.star.springbankstar.poductDto.Recommendation;
import ru.star.springbankstar.repositorys.RecommendationsRepository;

import java.util.UUID;
@Service
public class RecommendationService implements RecommendationRuleSet {
    /*
    Реализовать логирование
     */

    private final RecommendationsRepository recommendationsRepository;

    public RecommendationService(RecommendationsRepository recommendationsRepository) {
        this.recommendationsRepository = recommendationsRepository;
    }


    @Override
    public Recommendation getRecommendation(UUID idUser) {
        return null;
    }
}
