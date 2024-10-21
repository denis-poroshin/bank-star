package ru.star.springbankstar.createSql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.star.springbankstar.configurations.ProductRowMapper;
import ru.star.springbankstar.entity.Rules;
import ru.star.springbankstar.entity.TotalRule;
import ru.star.springbankstar.model.OfferDescriptionText;
import ru.star.springbankstar.repositorys.DynamicRecommendationRepository;
import ru.star.springbankstar.repositorys.RecommendationRulesRepository;
import ru.star.springbankstar.repositorys.TotalRuleRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class CreateSql {
    private final RecommendationRulesRepository rulesRepository;
    private final TotalRuleRepository totalRuleRepository;
    private final OfferDescriptionText offerDescriptionText = new OfferDescriptionText();
    
    
    private Logger logger = LoggerFactory.getLogger(CreateSql.class);
    

    public CreateSql(RecommendationRulesRepository rulesRepository, TotalRuleRepository totalRuleRepository) {
        this.rulesRepository = rulesRepository;
        this.totalRuleRepository = totalRuleRepository;
    }

    public String createQetInvest500Dynamic(UUID user){
        Optional<Rules> userOf = rulesRepository.findByQuery("USER_OF");
        examinationTotalRule(userOf);

        Optional<Rules> transactionSumCompareDepositWithdraw = rulesRepository.findByQuery("TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW");
        examinationTotalRule(transactionSumCompareDepositWithdraw);

        String textForOffer = offerDescriptionText.getTEXT_INVEST_500();
        String[] argumentsTransactionSumCompareDepositWithdraw = transactionSumCompareDepositWithdraw.get().getArguments().split(", ");
        String arguments = userOf.get().getArguments();
        String comparisonOperation = argumentsTransactionSumCompareDepositWithdraw[0];
        String money = argumentsTransactionSumCompareDepositWithdraw[1];
        
        String sql = "SELECT p.id, p.name, %s AS SENTENCE_TEXT FROM transactions t " +
                "JOIN products p ON t.type = %s AND p.type IN ('SAVING') " +
                "WHERE t.user_id = " + user + " GROUP BY p.name HAVING SUM(t.amount) %s %s"
                .formatted(textForOffer,
                        arguments, 
                        user, 
                        comparisonOperation, 
                        money);
        return sql;
        
    }
    
    public String createTopSavingDynamic(UUID user){
        Optional<Rules> userOf = rulesRepository.findByQuery("USER_OF");
        examinationTotalRule(userOf);

        Optional<Rules> transactionSumCompare = rulesRepository.findByQuery("TRANSACTION_SUM_COMPARE");
        examinationTotalRule(transactionSumCompare);

        String[] argumentsTransactionSumCompare = transactionSumCompare.get().getArguments().split(", ");

        String transactionType = argumentsTransactionSumCompare[1];
        String textForOffer = offerDescriptionText.getTEXT_TOP_SAVING();
        String productType = userOf.get().getArguments();
        String comparisonOperation = argumentsTransactionSumCompare[2];
        String money = argumentsTransactionSumCompare[3];

        String sql = "WITH TransactionSums AS ( " +
                "SELECT " +
                "    SUM(CASE WHEN t.TYPE = %s THEN t.AMOUNT ELSE 0 END) AS total_deposit, " +
                "    SUM(CASE WHEN t.TYPE = 'WITHDRAW' THEN t.AMOUNT ELSE 0 END) AS total_withdraw " +
                "FROM TRANSACTIONS t " +
                "WHERE t.USER_ID = " + user + ") " +
                "SELECT p.ID, p.NAME, %s AS SENTENCE_TEXT " +
                "FROM PRODUCTS p " +
                ", TransactionSums ts " +
                "WHERE p.TYPE = %s " +
                "AND ((ts.total_deposit %s %s) OR (p.TYPE = 'SAVING' AND ts.total_deposit %s %s)) " +
                "AND (ts.total_deposit > ts.total_withdraw) " +
                "GROUP BY p.NAME;".formatted(transactionType,
                        textForOffer,
                        productType,
                        comparisonOperation,
                        money,
                        comparisonOperation,
                        money);
        return sql;
        
    }
    public String createSimpleLoanDynamic(UUID user){
        Optional<Rules> userOf = rulesRepository.findByQuery("USER_OF");
        examinationTotalRule(userOf);

        Optional<Rules> transactionSumCompareDepositWithdraw = rulesRepository.findByQuery("TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW");
        examinationTotalRule(transactionSumCompareDepositWithdraw);

        Optional<Rules> transactionSumCompare = rulesRepository.findByQuery("TRANSACTION_SUM_COMPARE");
        examinationTotalRule(transactionSumCompare);

        String[] argumentsTransactionSumCompareDepositWithdraw = transactionSumCompareDepositWithdraw.get().getArguments().split(", ");
        String[] argumentsTransactionSumCompare = transactionSumCompare.get().getArguments().split(", ");

        String transactionType = argumentsTransactionSumCompare[1];
        String textSimpleLoan = offerDescriptionText.getTEXT_SIMPLE_LOAN();
        String productType = userOf.get().getArguments();
        String productTypeSumTotal = argumentsTransactionSumCompareDepositWithdraw[0];
        String comparisonOperation = argumentsTransactionSumCompareDepositWithdraw[1];
        String productTypeSumCompare = argumentsTransactionSumCompare[0];
        String comparisonOperationSumCompare = argumentsTransactionSumCompare[2];
        String money = argumentsTransactionSumCompare[3];


        String sql = "WITH TransactionSums AS ( " +
                "SELECT " +
                "    SUM(CASE WHEN t.TYPE = 'DEPOSIT' THEN t.AMOUNT ELSE 0 END) AS total_deposit, " +
                "    SUM(CASE WHEN t.TYPE = 'WITHDRAW' THEN t.AMOUNT ELSE 0 END) AS total_withdraw " +
                "    SUM(CASE WHEN t.TYPE = %s THEN t.AMOUNT ELSE 0 END) AS total " +
                "FROM TRANSACTIONS t " +
                "WHERE t.USER_ID = " + user +") " +
                "SELECT p.ID, p.NAME, %s AS SENTENCE_TEXT " +
                "FROM PRODUCTS p " +
                ", TransactionSums ts " +
                "WHERE p.TYPE = %s " +
                "AND (p.TYPE = %s AND ts.total_deposit %s ts.total_withdraw) " +
                "AND (p.TYPE = %s AND ts.total %s %s) " +
                "GROUP BY p.NAME;"
                        .formatted(transactionType,
                                textSimpleLoan,
                                productType,
                                productTypeSumTotal,
                                comparisonOperation,
                                productTypeSumCompare,
                                comparisonOperationSumCompare,
                                money);
        return sql;

    }


    private void examinationTotalRule(Optional<Rules> rules){
        boolean result = totalRules(rules.get());
        if (result){
            logger.info("Правило было найдено и увеличин счетчик на +1");
        }else {
            totalRules(rules.get());
            logger.info("Счетчик правил был создан и увеличелся на +1");
        }


    }
    private boolean totalRules(Rules rules) {
        boolean flag = false;
        List<TotalRule> allTotalRule = totalRuleRepository.findAll();
        for (int i = 0; i < allTotalRule.size(); i++) {
            if (allTotalRule.get(i).getRuleId().getId().equals(rules.getId())){
                Long total = allTotalRule.get(i).getTotal();
                allTotalRule.get(i).setTotal(++total);
                return flag = true;
            }

        }
        return flag;
    }

    private void createTotalRule(Rules rules) {
        AtomicReference<TotalRule> totalRule = new AtomicReference<>(new TotalRule());
        totalRule.get().setRuleId(rules);
        totalRule.get().setTotal(1L);

    }
}
