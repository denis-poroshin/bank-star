package ru.star.springbankstar.ProductDto;

import org.apache.tomcat.util.digester.Rule;

import java.util.*;

public class RecommendationDto {
    private UUID id;

    private Collection<ProductDto> recommendations;

    private Collection<Rule> rules;

    public RecommendationDto(UUID id, Collection<ProductDto> recommendations, Collection<Rule> rules) {
        this.id = id;
        this.recommendations = recommendations;
        this.rules = rules;
    }

    public RecommendationDto() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Collection<ProductDto> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(Collection<ProductDto> recommendations) {
        this.recommendations = recommendations;
    }

    public Collection<Rule> getRules() {
        return rules;
    }

    public void setRules(Collection<Rule> rules) {
        this.rules = rules;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecommendationDto that = (RecommendationDto) o;
        return Objects.equals(id, that.id) && Objects.equals(recommendations, that.recommendations) && Objects.equals(rules, that.rules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, recommendations, rules);
    }

    @Override
    public String toString() {
        return "Recommendation{" +
                "id=" + id +
                ", recommendations=" + recommendations +
                ", rules=" + rules +
                '}';
    }
}
