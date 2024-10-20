package ru.star.springbankstar.repositorys;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.star.springbankstar.UserDto.UserDto;
import ru.star.springbankstar.configurations.ProductRowMapper;
import ru.star.springbankstar.configurations.UserRowMapper;

import java.util.Collection;
import java.util.List;

@Repository
public class TelegramBotRepository {
    private final JdbcTemplate jdbcTemplate;

    public TelegramBotRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Collection<UserDto> getUser(String username) {
        String sql = "SELECT u.id, u.username, u.frist_name, u.last_name FROM users u WHERE u.username = ?";
        return jdbcTemplate.query(sql, new UserRowMapper(), username);
    }
}
