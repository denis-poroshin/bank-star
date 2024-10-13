package ru.star.springbankstar.repositorys;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.star.springbankstar.ProductDto.Product;
import ru.star.springbankstar.entity.Rules;
import ru.star.springbankstar.model.OfferDescriptionText;
import ru.star.springbankstar.configurations.ProductRowMapper;

import java.util.Collection;
import java.util.UUID;


@Repository
public class RecommendationsRepository {
    private final JdbcTemplate jdbcTemplate;
    private final OfferDescriptionText offerDescriptionText = new OfferDescriptionText();
    private final RecommendationRulesRepository rulesRepository;


    public RecommendationsRepository(JdbcTemplate jdbcTemplate, RecommendationRulesRepository rulesRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.rulesRepository = rulesRepository;
    }

    public Collection<Product> getTransactionAmount(UUID user){
        Collection<Product> products = getInvest500(user);
        if (!products.isEmpty()){
            return products;
        }
        products = getTopSaving(user);
        return !products.isEmpty() ? products : getSimpleLoan(user);

    }
    public int getRandomTransactionAmount(UUID user){
        Integer result = jdbcTemplate.queryForObject(
                "SELECT amount FROM transactions t WHERE t.user_id = ? LIMIT 1",
                Integer.class, user);
        return result != null ? result : 0;
    }

    private Collection<Product> getInvest500(UUID user){
        String sql = "SELECT p.id, p.name, ? AS SENTENCE_TEXT FROM transactions t " +
                "JOIN products p ON t.type = 'DEPOSIT' AND p.type IN ('SAVING') " +
                "WHERE t.user_id = ? GROUP BY p.name HAVING SUM(t.amount) > ?";
        return jdbcTemplate.query(sql, new ProductRowMapper(), offerDescriptionText.getTEXT_INVEST_500(), user, 1000);


    }

    private Collection<Product> getTopSaving(UUID user){
        String sql = "WITH TransactionSums AS ( " +
                "SELECT " +
                "    SUM(CASE WHEN t.TYPE = 'DEPOSIT' THEN t.AMOUNT ELSE 0 END) AS total_deposit, " +
                "    SUM(CASE WHEN t.TYPE = 'WITHDRAW' THEN t.AMOUNT ELSE 0 END) AS total_withdraw " +
                "FROM TRANSACTIONS t " +
                "WHERE t.USER_ID = ? " +
                ") " +
                "SELECT p.ID, p.NAME, ? AS SENTENCE_TEXT " +
                "FROM PRODUCTS p " +
                ", TransactionSums ts " +
                "WHERE p.TYPE = 'DEBIT' " +
                "AND ((ts.total_deposit >= 50000) OR (p.TYPE = 'SAVING' AND ts.total_deposit >= 50000)) " +
                "AND (ts.total_deposit > ts.total_withdraw) " +
                "GROUP BY p.NAME;";

        return jdbcTemplate.query(sql, new ProductRowMapper(), user, offerDescriptionText.getTEXT_TOP_SAVING());
    }

    private Collection<Product> getSimpleLoan(UUID user){
        String sql = "WITH TransactionSums AS ( " +
                "SELECT " +
                "    SUM(CASE WHEN t.TYPE = 'DEPOSIT' THEN t.AMOUNT ELSE 0 END) AS total_deposit, " +
                "    SUM(CASE WHEN t.TYPE = 'WITHDRAW' THEN t.AMOUNT ELSE 0 END) AS total_withdraw " +
                "FROM TRANSACTIONS t " +
                "WHERE t.USER_ID = ? " +
                ") " +
                "SELECT p.ID, p.NAME, ? AS SENTENCE_TEXT " +
                "FROM PRODUCTS p " +
                ", TransactionSums ts " +
                "WHERE p.TYPE != 'CREDIT' " +
                "AND (p.TYPE = 'DEBIT' AND ts.total_deposit > ts.total_withdraw) " +
                "AND (p.TYPE = 'DEBIT' AND ts.total_withdraw > 100000) " +
                "GROUP BY p.NAME;";

        return jdbcTemplate.query(sql, new ProductRowMapper(), user, offerDescriptionText.getTEXT_SIMPLE_LOAN());
    }
    private Collection<Product> getSimpleLoanDynamic(UUID user){
        Rules userOf = rulesRepository.findByQuery("USER_OF");
        Rules transactionSumCompareDepositWithdraw = rulesRepository.findByQuery("TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW");
        Rules transactionSumCompare = rulesRepository.findByQuery("TRANSACTION_SUM_COMPARE");
        String[] argumentsTransactionSumCompareDepositWithdraw = transactionSumCompareDepositWithdraw.getArguments().split(", ");
        String[] argumentsTransactionSumCompare = transactionSumCompare.getArguments().split(", ");

        String sql = "WITH TransactionSums AS ( " +
                "SELECT " +
                "    SUM(CASE WHEN t.TYPE = 'DEPOSIT' THEN t.AMOUNT ELSE 0 END) AS total_deposit, " +
                "    SUM(CASE WHEN t.TYPE = 'WITHDRAW' THEN t.AMOUNT ELSE 0 END) AS total_withdraw " +
                "    SUM(CASE WHEN t.TYPE = ? THEN t.AMOUNT ELSE 0 END) AS total " +
                "FROM TRANSACTIONS t " +
                "WHERE t.USER_ID = ? " +
                ") " +
                "SELECT p.ID, p.NAME, ? AS SENTENCE_TEXT " +
                "FROM PRODUCTS p " +
                ", TransactionSums ts " +
                "WHERE p.TYPE = ? " +
                "AND (p.TYPE = ? AND ts.total_deposit ? ts.total_withdraw) " +
                "AND (p.TYPE = ? AND ts.total ? ?) " +
                "GROUP BY p.NAME;";
        return jdbcTemplate.query(sql,
                new ProductRowMapper(),
                argumentsTransactionSumCompare[1],
                user,
                offerDescriptionText.getTEXT_SIMPLE_LOAN(),
                userOf.getArguments(),
                argumentsTransactionSumCompareDepositWithdraw[0],
                argumentsTransactionSumCompareDepositWithdraw[1],
                argumentsTransactionSumCompare[0],
                argumentsTransactionSumCompare[2],
                argumentsTransactionSumCompare[3]
                );




    }

}
