package ru.star.springbankstar.configurations;

import org.springframework.jdbc.core.RowMapper;
import ru.star.springbankstar.ProductDto.ProductDto;
import ru.star.springbankstar.UserDto.UserDto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ProductRowMapper implements RowMapper<ProductDto> {
    @Override
    public ProductDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        ProductDto product = new ProductDto();
        product.setId(rs.getObject("id", UUID.class));
        product.setName(rs.getString("name"));
        product.setText(rs.getString("SENTENCE_TEXT"));
        return product;
    }
}
