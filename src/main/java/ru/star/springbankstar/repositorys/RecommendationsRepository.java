package ru.star.springbankstar.repositorys;

import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.star.springbankstar.configurations.ProductRowMapper;
import ru.star.springbankstar.model.OfferDescriptionText;
import ru.star.springbankstar.poductDto.Product;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
@Repository
public class RecommendationsRepository {

    private final JdbcTemplate jdbcTemplate;
    private final OfferDescriptionText offerDescriptionText = new OfferDescriptionText();
    //* почему new? зачем создавать новый объект, если они все у нас есть/будут в классе OfferDescriptionText? /

    public RecommendationsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public Collection<Product> getTransactionAmount(UUID user){
        /*
        прописать логику для выбора SQL запроса
         */
        return null;
    }
    private Collection<Product> getInvest500(UUID user){
        String sql = "SELECT p.id, p.name, ? AS SENTENCE_TEXT FROM transactions t " +
                "JOIN products p ON t.type = 'DEPOSIT' AND p.type IN ('SAVING') " +
                "WHERE t.user_id = ? GROUP BY p.name HAVING SUM(t.amount) > ?";
        return jdbcTemplate.query(sql, new ProductRowMapper(), offerDescriptionText.getTEXT_INVEST_500(), user, 1000);
    }

    private Collection<Product> getEasyCredit(UUID user) {
        String sql =
                "SELECT transactions.product_id, transactions.user_id, transactions.type, transactions.amount, products.type " +
                        "FROM transactions " +
                        "INNER JOIN products ON transactions.product_id = products.id " +
                        "WHERE products.\"TYPE\" != 'CREDIT' " +
                        "AND (SELECT SUM(AMOUNT) " +
                        "FROM TRANSACTIONS t " +
                        "WHERE t.\"TYPE\" = 'DEPOSIT') > (SELECT SUM(AMOUNT) " +
                        "FROM TRANSACTIONS t " +
                        "WHERE t.\"TYPE\" = 'WITHDRAW') " +
                        "AND (SELECT SUM(AMOUNT) " +
                        "FROM TRANSACTIONS t " +
                        "WHERE t.\"TYPE\" = 'WITHDRAW') " +
                        "AND PRODUCTS.\"TYPE\" = 'DEBIT') > 100000) " +
                        "AND transactions.user_id = ?";
        return jdbcTemplate.query(sql, new ProductRowMapper(), offerDescriptionText.getTEXT_EASY_CREDIT(), user);
    }
    /*
    Реализовать еще 2 SQL запроса

    принемать должны UUID user
    отдавать id продукта, название продукта, предложение
    пример в методе getInvest500
     */


}
