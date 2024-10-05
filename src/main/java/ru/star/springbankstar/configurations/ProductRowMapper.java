package ru.star.springbankstar.configurations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import ru.star.springbankstar.poductDto.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ProductRowMapper implements RowMapper<Product> {
    private Logger logger = LoggerFactory.getLogger(ProductRowMapper.class);
    @Override
    public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
        Product product = new Product();
        product.setId(rs.getObject("id", UUID.class));
        product.setName(rs.getString("name"));
        product.setText(rs.getString("SENTENCE_TEXT"));
        logger.info("Created product {}", product);
        return product;

    }
}
