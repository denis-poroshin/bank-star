package ru.star.springbankstar.ProductDto;

import org.apache.tomcat.util.digester.Rule;

import java.util.*;

public class Recommendation {
    private UUID id;

    private Collection<Product> recommendations;

    private Collection<Rule> rules;

    public Recommendation(UUID id, Collection<Product> recommendations, Collection<Rule> rules) {
        this.id = id;
        this.recommendations = recommendations;
        this.rules = rules;
    }

    public Recommendation() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Collection<Product> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(Collection<Product> recommendations) {
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
        Recommendation that = (Recommendation) o;
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
