package ru.star.springbankstar.repositorys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.star.springbankstar.ProductDto.ProductDto;
import ru.star.springbankstar.configurations.ProductRowMapper;
import ru.star.springbankstar.createSql.CreateSql;
import ru.star.springbankstar.entity.Rules;
import ru.star.springbankstar.entity.TotalRule;
import ru.star.springbankstar.model.OfferDescriptionText;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Repository
public class DynamicRecommendationRepository {
    private final JdbcTemplate jdbcTemplate;
    private final OfferDescriptionText offerDescriptionText = new OfferDescriptionText();
    private final RecommendationRulesRepository rulesRepository;
    private final TotalRuleRepository totalRuleRepository;
    private final CreateSql createSql;
    private Logger logger = LoggerFactory.getLogger(DynamicRecommendationRepository.class);


    public DynamicRecommendationRepository(JdbcTemplate jdbcTemplate, RecommendationRulesRepository rulesRepository, TotalRuleRepository totalRuleRepository, CreateSql createSql) {
        this.jdbcTemplate = jdbcTemplate;
        this.rulesRepository = rulesRepository;
        this.totalRuleRepository = totalRuleRepository;
        this.createSql = createSql;
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
        String qetInvest500Dynamic = createSql.createQetInvest500Dynamic(user);
        return jdbcTemplate.query(qetInvest500Dynamic, new ProductRowMapper());
    }

    private Collection<ProductDto> getTopSavingDynamic(UUID user) {
        String topSavingDynamic = createSql.createTopSavingDynamic(user);
        return jdbcTemplate.query(topSavingDynamic, new ProductRowMapper());
    }



    private Collection<ProductDto> getSimpleLoanDynamic(UUID user){
        String simpleLoanDynamic = createSql.createSimpleLoanDynamic(user);
        return jdbcTemplate.query(simpleLoanDynamic, new ProductRowMapper());

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
