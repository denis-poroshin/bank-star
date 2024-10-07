package ru.star.springbankstar.poductDto;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

public class Recommendation {

    private UUID id;

    private Collection<Product> recommendations;

    public Recommendation(UUID id, Collection<Product> recommendations) {
        this.id = id;
        this.recommendations = recommendations;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recommendation that = (Recommendation) o;
        return Objects.equals(id, that.id) && Objects.equals(recommendations, that.recommendations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, recommendations);
    }

    @Override
    public String toString() {
        return "Recommendation{" +
                "id=" + id +
                ", recommendations=" + recommendations +
                '}';
    }
}
