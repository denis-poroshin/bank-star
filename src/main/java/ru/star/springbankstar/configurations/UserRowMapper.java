package ru.star.springbankstar.configurations;

import org.springframework.jdbc.core.RowMapper;
import ru.star.springbankstar.ProductDto.ProductDto;
import ru.star.springbankstar.UserDto.UserDto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserRowMapper implements RowMapper<UserDto> {
    @Override
    public UserDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserDto user = new UserDto();
        user.setId(rs.getObject("id", UUID.class));
        user.setUserName(rs.getString("username"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        return user;
    }
}
