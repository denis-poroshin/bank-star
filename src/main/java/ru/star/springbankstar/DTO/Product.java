package ru.star.springbankstar.DTO;

import java.util.Objects;
import java.util.UUID;

public class Product {
    private UUID id;
    private String name;
    private String text;

    public Product(UUID id, String name, String text) {
        this.id = id;
        this.name = name;
        this.text = text;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", text='" + text + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) && Objects.equals(name, product.name) && Objects.equals(text, product.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, text);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Product() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
