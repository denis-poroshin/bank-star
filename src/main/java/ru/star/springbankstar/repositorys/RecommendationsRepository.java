package ru.star.springbankstar.repositorys;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.star.springbankstar.DTO.Product;
import ru.star.springbankstar.DTO.Recommendation;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;


@Repository
public class RecommendationsRepository {
    private final JdbcTemplate jdbcTemplate;
    private static final String TEXT_ONE = "Откройте свой путь к успеху с индивидуальным инвестиционным " +
            "счетом (ИИС) от нашего банка! Воспользуйтесь налоговыми льготами и начните инвестировать " +
            "с умом. Пополните счет до конца года и получите выгоду в виде вычета на взнос в следующем " +
            "налоговом периоде. Не упустите возможность разнообразить свой портфель, снизить риски и " +
            "следить за актуальными рыночными тенденциями. Откройте ИИС сегодня и станьте ближе к финансовой независимости!";

    private static final String TEXT_TWO = "Откройте свою собственную «Копилку» с нашим банком! «Копилка» — это уникальный " +
            "банковский инструмент, который поможет вам легко и удобно накапливать деньги на важные цели. Больше никаких " +
            "забытых чеков и потерянных квитанций — всё под контролем!" +
            "Преимущества «Копилки»:" +
            "Накопление средств на конкретные цели. Установите лимит и срок накопления, и банк будет автоматически " +
            "переводить определенную сумму на ваш счет." +
            "Прозрачность и контроль. Отслеживайте свои доходы и расходы, контролируйте процесс накопления и корректируйте" +
            " стратегию при необходимости." +
            "Безопасность и надежность. Ваши средства находятся под защитой банка, а доступ к ним возможен только через " +
            "мобильное приложение или интернет-банкинг.\n" +
            "Начните использовать «Копилку» уже сегодня и станьте ближе к своим финансовым целям!";



    private static final String TEXT_THREE = "Откройте мир выгодных кредитов с нами!" +
            "Ищете способ быстро и без лишних хлопот получить нужную сумму? Тогда наш выгодный " +
            "кредит — именно то, что вам нужно! Мы предлагаем низкие процентные ставки, гибкие условия и индивидуальный " +
            "подход к каждому клиенту." +
            "Почему выбирают нас:" +
            "Быстрое рассмотрение заявки. Мы ценим ваше время, поэтому процесс рассмотрения заявки занимает всего несколько часов." +
            "Удобное оформление. Подать заявку на кредит можно онлайн на нашем сайте или в мобильном приложении." +
            "Широкий выбор кредитных продуктов. Мы предлагаем кредиты на различные цели: покупку недвижимости, автомобиля, " +
            "образование, лечение и многое другое." +
            "Не упустите возможность воспользоваться выгодными условиями кредитования от нашей компании!";


    public RecommendationsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public int getRandomTransactionAmount(UUID user){
        Integer result = jdbcTemplate.queryForObject(
                "SELECT amount FROM transactions t WHERE t.user_id = ? LIMIT 1",
                Integer.class, user);
        return result != null ? result : 0;
    }


    public Optional<Product> getTransactionAmount(UUID user){
        Optional<Product> invest500 = getInvest500(user);
        Optional<Product> topSaving = getTopSaving(user);
        Optional<Product> simpleLoan = getSimpleLoan(user);

        if (getInvest500(user).isPresent()){
            return invest500;
        }else if(topSaving.isPresent()){
            return topSaving;

        }
        return simpleLoan;


    }

    private Optional<Product> getInvest500(UUID user){
        Product result = jdbcTemplate.queryForObject(
                "SELECT p.id ,p.name " +
                        "FROM transactions t, products p " +
                        "WHERE t.user_id = user " +
                        "AND t.type = 'DEPOSIT' " +
                        "AND p.type != 'INVEST' " +
                        "AND p.type = 'SAVING' " +
                        "GROUP BY p.name " +
                        "HAVING SUM(t.amount) > 1000;", Product.class, user
        );
        Objects.requireNonNull(result).setText(TEXT_ONE);
        return Optional.of(result);
    }
    private Optional<Product> getTopSaving(UUID user){
        return null;

    }
    private Optional<Product> getSimpleLoan(UUID user){
        return null;

    }
}
