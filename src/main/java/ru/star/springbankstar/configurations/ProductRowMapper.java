package ru.star.springbankstar.configurations;

import org.springframework.jdbc.core.RowMapper;
import ru.star.springbankstar.ProductDto.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ProductRowMapper implements RowMapper<Product> {
    @Override
    public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
        Product product = new Product();
        product.setId(rs.getObject("id", UUID.class));
        product.setName(rs.getString("name"));
        product.setText(rs.getString("SENTENCE_TEXT"));
        return product;
    }
}
