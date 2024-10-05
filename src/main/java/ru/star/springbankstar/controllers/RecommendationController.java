package ru.star.springbankstar.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.star.springbankstar.interfaces.RecommendationRuleSet;
import ru.star.springbankstar.poductDto.Recommendation;

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
}
