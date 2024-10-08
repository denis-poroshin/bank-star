package ru.star.springbankstar.repositorys;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.star.springbankstar.configurations.ProductRowMapper;
import ru.star.springbankstar.model.OfferDescriptionText;
import ru.star.springbankstar.poductDto.Product;

import java.util.Collection;
import java.util.UUID;
@Repository
public class RecommendationsRepository {

    private final JdbcTemplate jdbcTemplate;
    private final OfferDescriptionText offerDescriptionText = new OfferDescriptionText();

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

    private Collection<Product> getTOP_SAVING(UUID user){
        String sql = "SELECT TRANSACTIONS.PRODUCT_ID, TRANSACTIONS.USER_ID, TRANSACTIONS.TYPE, TRANSACTIONS.AMOUNT, PRODUCTS.TYPE" +
                "FROM TRANSACTIONS" +
                "INNER JOIN PRODUCTS ON TRANSACTIONS.PRODUCT_ID = PRODUCTS.ID" +
                "WHERE PRODUCTS.TYPE = 'DEBIT'" +
                "AND (SELECT SUM(AMOUNT) " +
                "FROM TRANSACTIONS t " +
                "WHERE t.type = 'DEPOSIT') >= 50000" +
                "OR (SELECT SUM(AMOUNT) " +
                "FROM TRANSACTIONS t " +
                "WHERE PRODUCTS.TYPE = 'SAVING' >= 50000" +
                "AND (SELECT SUM(AMOUNT) " +
                "FROM TRANSACTIONS t " +
                "WHERE t.TYPE = 'DEPOSIT') > (SELECT SUM(AMOUNT) " +
                "FROM TRANSACTIONS t " +
                "where t.type = 'WITHDRAW'" +
                "AND TRANSACTIONS.USER_ID = ?";

        return jdbcTemplate.query(sql, new ProductRowMapper(), offerDescriptionText.getTOP_SAVING(), user, 1000);
    }

    //test

    /*
    Реализовать еще 2 SQL запроса

    принемать должны UUID user
    отдавать id продукта, название продукта, предложение
    пример в методе getInvest500
     */


}
