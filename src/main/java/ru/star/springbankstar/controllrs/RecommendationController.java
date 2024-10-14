package ru.star.springbankstar.controllrs;

import org.springframework.web.bind.annotation.*;
import ru.star.springbankstar.ProductDto.RecommendationDto;
import ru.star.springbankstar.interfaces.Recommendation;

import java.util.UUID;

@RestController
@RequestMapping("/recommendation")
public class RecommendationController {

    private final Recommendation recommendationRuleSet;

    public RecommendationController(Recommendation recommendationRuleSet) {
        this.recommendationRuleSet = recommendationRuleSet;
    }

    @GetMapping("/{id}")
    public RecommendationDto getRecommendation(@PathVariable UUID id) {
        return recommendationRuleSet.getRecommendation(id);

    }
    @GetMapping("/test/{id}")
    public int test(@PathVariable UUID id) {
        return recommendationRuleSet.get(id);
    }

}
