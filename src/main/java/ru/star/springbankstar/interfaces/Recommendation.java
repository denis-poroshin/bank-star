package ru.star.springbankstar.interfaces;

import ru.star.springbankstar.ProductDto.RecommendationDto;

import java.util.UUID;

public interface Recommendation {

    RecommendationDto getRecommendation(UUID idUser);
    int get(UUID id);
}
