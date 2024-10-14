package ru.star.springbankstar.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Objects;
@Entity
@Table(name = "rules")
public class Rules {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "query")
    private String query;

    //    @ElementCollection
//    @CollectionTable(name = "arguments", joinColumns = @JoinColumn(name = "rule_id"))
//    private ArrayList<String> arguments = new ArrayList<>();
    @Column(name = "arguments")
    private String arguments;


    @Column(name = "negate")
    private boolean negate;
//    @ManyToOne
//    @JoinColumn(name = "product_dynamic_rule_id")
//    private ProductDynamicRule productDynamicRule;

    public Rules(Long id, String query, String arguments, boolean negate) {
        this.id = id;
        this.query = query;
        this.arguments = arguments;
        this.negate = negate;

    }


    public Rules() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getArguments() {
        return arguments;
    }

    public void setArguments(String arguments) {
        this.arguments = arguments;
    }

    public boolean isNegate() {
        return negate;
    }

    public void setNegate(boolean negate) {
        this.negate = negate;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rules rules = (Rules) o;
        return negate == rules.negate && Objects.equals(id, rules.id) && Objects.equals(query, rules.query) && Objects.equals(arguments, rules.arguments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, query, arguments, negate);
    }

    @Override
    public String toString() {
        return "Rules{" +
                "id=" + id +
                ", query='" + query + '\'' +
                ", arguments='" + arguments + '\'' +
                ", negate=" + negate +
                '}';
    }
}


