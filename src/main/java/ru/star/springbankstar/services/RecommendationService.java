package ru.star.springbankstar.services;

import org.springframework.stereotype.Service;
import ru.star.springbankstar.ProductDto.ProductDto;
import ru.star.springbankstar.ProductDto.RecommendationDto;
import ru.star.springbankstar.interfaces.Recommendation;
import ru.star.springbankstar.repositorys.DynamicRecommendationRepository;
import ru.star.springbankstar.repositorys.RecommendationsRepository;

import java.util.Collection;
import java.util.UUID;


@Service
public class RecommendationService implements Recommendation {
    private final RecommendationsRepository recommendationsRepository;
    private final DynamicRecommendationRepository dynamicRecommendationRepository;

    public RecommendationService(RecommendationsRepository recommendationsRepository, DynamicRecommendationRepository dynamicRecommendationRepository) {
        this.recommendationsRepository = recommendationsRepository;
        this.dynamicRecommendationRepository = dynamicRecommendationRepository;
    }

    /**
     * Возваращает рекомендации для повльзователя
     *
     * @param idUser нужен для поиска по id пользоватлея
     * Если для пользователя не будет рекомендаций, тогда ему придет пустой список
     */
    @Override
    public RecommendationDto getRecommendation(UUID idUser) {
        Collection<ProductDto> dynamicTransactionAmount = dynamicRecommendationRepository.getTransactionAmount(idUser);
        Collection<ProductDto> transactionAmount = recommendationsRepository.getTransactionAmount(idUser);
        transactionAmount.addAll(dynamicTransactionAmount);

        RecommendationDto recommendation = new RecommendationDto();
        recommendation.setId(idUser);
        recommendation.setRecommendations(transactionAmount);
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
