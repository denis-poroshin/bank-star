package ru.star.springbankstar.repositorys;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.star.springbankstar.ProductDto.ProductDto;
import ru.star.springbankstar.configurations.ProductRowMapper;
import ru.star.springbankstar.entity.Rules;
import ru.star.springbankstar.model.OfferDescriptionText;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Repository
public class DynamicRecommendationRepository {
    private final JdbcTemplate jdbcTemplate;
    private final OfferDescriptionText offerDescriptionText = new OfferDescriptionText();
    private final RecommendationRulesRepository rulesRepository;

    public DynamicRecommendationRepository(JdbcTemplate jdbcTemplate, RecommendationRulesRepository rulesRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.rulesRepository = rulesRepository;
    }
    public Collection<ProductDto> getTransactionAmount(UUID user){
        Collection<ProductDto> products = getInvest500Dynamic(user);
        if (!products.isEmpty()){
            return products;
        }
        products = getTopSavingDynamic(user);
        return !products.isEmpty() ? products : getSimpleLoanDynamic(user);

    }
    private Collection<ProductDto> getInvest500Dynamic(UUID user){
        Optional<Rules> userOf = rulesRepository.findByQuery("USER_OF");
        Optional<Rules> transactionSumCompareDepositWithdraw = rulesRepository.findByQuery("TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW");
        String[] argumentsTransactionSumCompareDepositWithdraw = transactionSumCompareDepositWithdraw.get().getArguments().split(", ");

        String sql = "SELECT p.id, p.name, ? AS SENTENCE_TEXT FROM transactions t " +
                "JOIN products p ON t.type = ? AND p.type IN ('SAVING') " +
                "WHERE t.user_id = ? GROUP BY p.name HAVING SUM(t.amount) ? ?";
        return jdbcTemplate.query(sql,
                new ProductRowMapper(),
                offerDescriptionText.getTEXT_INVEST_500(),
                userOf.get().getArguments(),
                user,
                argumentsTransactionSumCompareDepositWithdraw[0],
                argumentsTransactionSumCompareDepositWithdraw[1]);


    }

    private Collection<ProductDto> getTopSavingDynamic(UUID user){
        Optional<Rules> userOf = rulesRepository.findByQuery("USER_OF");
        Optional<Rules> transactionSumCompare = rulesRepository.findByQuery("TRANSACTION_SUM_COMPARE");
        String[] argumentsTransactionSumCompare = transactionSumCompare.get().getArguments().split(", ");

        String sql = "WITH TransactionSums AS ( " +
                "SELECT " +
                "    SUM(CASE WHEN t.TYPE = ? THEN t.AMOUNT ELSE 0 END) AS total_deposit, " +
                "    SUM(CASE WHEN t.TYPE = 'WITHDRAW' THEN t.AMOUNT ELSE 0 END) AS total_withdraw " +
                "FROM TRANSACTIONS t " +
                "WHERE t.USER_ID = ? " +
                ") " +
                "SELECT p.ID, p.NAME, ? AS SENTENCE_TEXT " +
                "FROM PRODUCTS p " +
                ", TransactionSums ts " +
                "WHERE p.TYPE = ? " +
                "AND ((ts.total_deposit ? ?) OR (p.TYPE = 'SAVING' AND ts.total_deposit ? ?)) " +
                "AND (ts.total_deposit > ts.total_withdraw) " +
                "GROUP BY p.NAME;";

        return jdbcTemplate.query(sql,
                new ProductRowMapper(),
                argumentsTransactionSumCompare[1],
                user,
                offerDescriptionText.getTEXT_TOP_SAVING(),
                userOf.get().getArguments(),
                argumentsTransactionSumCompare[2],
                argumentsTransactionSumCompare[3],
                argumentsTransactionSumCompare[2],
                argumentsTransactionSumCompare[3]
                );
    }

    private Collection<ProductDto> getSimpleLoanDynamic(UUID user){
        Optional<Rules> userOf = rulesRepository.findByQuery("USER_OF");
        Optional<Rules> transactionSumCompareDepositWithdraw = rulesRepository.findByQuery("TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW");
        Optional<Rules> transactionSumCompare = rulesRepository.findByQuery("TRANSACTION_SUM_COMPARE");
        String[] argumentsTransactionSumCompareDepositWithdraw = transactionSumCompareDepositWithdraw.get().getArguments().split(", ");
        String[] argumentsTransactionSumCompare = transactionSumCompare.get().getArguments().split(", ");

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
                userOf.get().getArguments(),
                argumentsTransactionSumCompareDepositWithdraw[0],
                argumentsTransactionSumCompareDepositWithdraw[1],
                argumentsTransactionSumCompare[0],
                argumentsTransactionSumCompare[2],
                argumentsTransactionSumCompare[3]
        );




    }
}
