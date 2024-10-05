package ru.star.springbankstar.services;

import org.springframework.stereotype.Service;
import ru.star.springbankstar.ProductDto.Product;
import ru.star.springbankstar.ProductDto.Recommendation;
import ru.star.springbankstar.interfaces.RecommendationRuleSet;
import ru.star.springbankstar.repositorys.RecommendationsRepository;

import java.util.Collection;
import java.util.UUID;


@Service
public class RecommendationService implements RecommendationRuleSet {
    private final RecommendationsRepository recommendationsRepository;

    public RecommendationService(RecommendationsRepository recommendationsRepository) {
        this.recommendationsRepository = recommendationsRepository;
    }

    /**
     * Возваращает рекомендации для повльзователя
     *
     * @param idUser нужен для поиска по id пользоватлея
     * Если для пользователя не будет рекомендаций, тогда ему придет пустой список
     */
    @Override
    public Recommendation getRecommendation(UUID idUser) {
        Collection<Product> info = recommendationsRepository.getTransactionAmount(idUser);
        Recommendation recommendation = new Recommendation();
        recommendation.setId(idUser);
        recommendation.setRecommendations(info);
        return recommendation;
    }

    /*
    Метод get необходим для проверки подключения к базе данных
     */

    @Override
    public int get(UUID id) {
        return recommendationsRepository.getRandomTransactionAmount(id);
    }


}
