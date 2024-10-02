package ru.star.springbankstar.services;

import org.springframework.stereotype.Service;
import ru.star.springbankstar.DTO.Product;
import ru.star.springbankstar.DTO.Recommendation;
import ru.star.springbankstar.interfaces.RecommendationRuleSet;
import ru.star.springbankstar.repositorys.RecommendationsRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class RecommendationService implements RecommendationRuleSet {
    private final RecommendationsRepository recommendationsRepository;

    public RecommendationService(RecommendationsRepository recommendationsRepository) {
        this.recommendationsRepository = recommendationsRepository;
    }


    @Override
    public Recommendation getRecommendation(UUID idUser) {
        Optional<Product> transactionAmount = recommendationsRepository.getTransactionAmount(idUser);
        if (transactionAmount.isPresent()) {
            Recommendation recommendation = new Recommendation();
            recommendation.setId(idUser);
            recommendation.setName("dawd");
            recommendation.setRecommendations(transactionAmount.stream().toList());
            return recommendation;

        }

//        Recommendation recommendation = new Recommendation();
//        recommendation.setId(transactionAmount.get().getId());
//        recommendation.setName(transactionAmount.get().getName());

//        return recommendation;

        return null;
    }

    @Override
    public int get(UUID id) {
        return recommendationsRepository.getRandomTransactionAmount(id);
    }

}
