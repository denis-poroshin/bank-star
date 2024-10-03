package ru.star.springbankstar.repositorys;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.star.springbankstar.DTO.Product;
import ru.star.springbankstar.model.OfferDescriptionText;
import ru.star.springbankstar.configurations.ProductRowMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;


@Repository
public class RecommendationsRepository {
    private final JdbcTemplate jdbcTemplate;
    private final OfferDescriptionText offerDescriptionText = new OfferDescriptionText();
//
//    private static final String TEXT_INVEST_500 = "Откройте свой путь к успеху с индивидуальным инвестиционным " +
//            "счетом (ИИС) от нашего банка! Воспользуйтесь налоговыми льготами и начните инвестировать " +
//            "с умом. Пополните счет до конца года и получите выгоду в виде вычета на взнос в следующем " +
//            "налоговом периоде. Не упустите возможность разнообразить свой портфель, снизить риски и " +
//            "следить за актуальными рыночными тенденциями. Откройте ИИС сегодня и станьте ближе к финансовой независимости!";
//
//    private static final String TEXT_TOP_SAVING = "Откройте свою собственную «Копилку» с нашим банком! «Копилка» — это уникальный " +
//            "банковский инструмент, который поможет вам легко и удобно накапливать деньги на важные цели. Больше никаких " +
//            "забытых чеков и потерянных квитанций — всё под контролем!" +
//            "Преимущества «Копилки»:" +
//            "Накопление средств на конкретные цели. Установите лимит и срок накопления, и банк будет автоматически " +
//            "переводить определенную сумму на ваш счет." +
//            "Прозрачность и контроль. Отслеживайте свои доходы и расходы, контролируйте процесс накопления и корректируйте" +
//            " стратегию при необходимости." +
//            "Безопасность и надежность. Ваши средства находятся под защитой банка, а доступ к ним возможен только через " +
//            "мобильное приложение или интернет-банкинг.\n" +
//            "Начните использовать «Копилку» уже сегодня и станьте ближе к своим финансовым целям!";
//
//
//
//    private static final String TEXT_SIMPLE_LOAN = "Откройте мир выгодных кредитов с нами!" +
//            "Ищете способ быстро и без лишних хлопот получить нужную сумму? Тогда наш выгодный " +
//            "кредит — именно то, что вам нужно! Мы предлагаем низкие процентные ставки, гибкие условия и индивидуальный " +
//            "подход к каждому клиенту." +
//            "Почему выбирают нас:" +
//            "Быстрое рассмотрение заявки. Мы ценим ваше время, поэтому процесс рассмотрения заявки занимает всего несколько часов." +
//            "Удобное оформление. Подать заявку на кредит можно онлайн на нашем сайте или в мобильном приложении." +
//            "Широкий выбор кредитных продуктов. Мы предлагаем кредиты на различные цели: покупку недвижимости, автомобиля, " +
//            "образование, лечение и многое другое." +
//            "Не упустите возможность воспользоваться выгодными условиями кредитования от нашей компании!";


    public RecommendationsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public int getRandomTransactionAmount(UUID user){
        Integer result = jdbcTemplate.queryForObject(
                "SELECT amount FROM transactions t WHERE t.user_id = ? LIMIT 1",
                Integer.class, user);
        return result != null ? result : 0;
    }

    public Collection<Product> getTransactionAmount(UUID user){
        Collection<Product> products = getInvest500(user);
        if (!products.isEmpty()){
            return products;
        }
        products = getTopSaving(user);
        return !products.isEmpty() ? products : getSimpleLoan(user);

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

}
