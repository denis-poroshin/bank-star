package ru.star.springbankstar.services;

import org.springframework.stereotype.Service;
import ru.star.springbankstar.interfaces.RecommendationRuleSet;
import ru.star.springbankstar.poductDto.Product;
import ru.star.springbankstar.poductDto.Recommendation;
import ru.star.springbankstar.repositorys.RecommendationsRepository;

import java.util.Collection;
import java.util.UUID;
@Service
public class RecommendationService implements RecommendationRuleSet {
    /*
    Реализовать логирование
     */

    private final RecommendationsRepository recommendationsRepository;
    private final Recommendation recommendation;

    public RecommendationService(RecommendationsRepository recommendationsRepository, Recommendation recommendation) {
        this.recommendationsRepository = recommendationsRepository;
        this.recommendation = recommendation;
    }


    @Override
    public Recommendation getRecommendation(UUID idUser) {
        Collection<Product> productCollection = recommendationsRepository.getTransactionAmount(idUser);
        recommendation.setId(idUser);
        recommendation.setRecommendations(productCollection);
        return recommendation;
    }
}
