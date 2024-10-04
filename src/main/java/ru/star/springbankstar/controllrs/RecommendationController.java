package ru.star.springbankstar.controllrs;

import org.springframework.web.bind.annotation.*;
import ru.star.springbankstar.ProductDto.Recommendation;
import ru.star.springbankstar.interfaces.RecommendationRuleSet;

import java.util.UUID;

@RestController
@RequestMapping("/recommendation")
public class RecommendationController {

    private final RecommendationRuleSet recommendationRuleSet;

    public RecommendationController(RecommendationRuleSet recommendationRuleSet) {
        this.recommendationRuleSet = recommendationRuleSet;
    }

    @GetMapping("/{id}")
    public Recommendation getRecommendation(@PathVariable UUID id) {
        return recommendationRuleSet.getRecommendation(id);

    }
    @GetMapping("/test/{id}")
    public int test(@PathVariable UUID id) {
        return recommendationRuleSet.get(id);
    }

}
