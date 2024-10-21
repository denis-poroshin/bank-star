package ru.star.springbankstar.controllrs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;
import ru.star.springbankstar.ProductDto.RecommendationDto;
import ru.star.springbankstar.interfaces.Recommendation;

import java.util.UUID;
/**
 * Контроллре служит для получения рекомендаций для пользователя
 */


@RestController
@RequestMapping("/recommendation")
public class RecommendationController {

    private final Recommendation recommendationRuleSet;

    public RecommendationController(Recommendation recommendationRuleSet) {
        this.recommendationRuleSet = recommendationRuleSet;
    }

    /**
     * Возваращает рекомендации для повльзователя
     *
     * @param id нужен для поиска по id пользоватлея
     * Если для пользователя не будет рекомендаций, тогда ему придет пустой список
     */
    @Operation(summary = "Получение рекомендаций для пользователя по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Получение рекомендаций", content = { @Content(mediaType = "application/json",
            schema = @Schema(implementation = RecommendationDto.class))}),
            @ApiResponse(responseCode = "400", description = "Ошибка при вводе данных", content = @Content),
            @ApiResponse(responseCode = "404", description = "Такого id нет")
    })
    @GetMapping("/{id}")
    public RecommendationDto getRecommendation(@PathVariable UUID id) {
        return recommendationRuleSet.getRecommendation(id);

    }

    /**
     * Метод служит для тестовой проверки JDBC
     * @param id нужен для поиска по id пользоватлея
     * Выдает четрые цифры, что служит правильному подключению к JDBC драйверу
     */
    @GetMapping("/test/{id}")
    public int test(@PathVariable UUID id) {
        return recommendationRuleSet.get(id);
    }

}
