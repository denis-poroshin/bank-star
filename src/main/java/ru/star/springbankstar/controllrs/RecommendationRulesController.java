package ru.star.springbankstar.controllrs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;
import ru.star.springbankstar.ProductDto.RecommendationDto;
import ru.star.springbankstar.interfaces.RecommendationRulesService;
import ru.star.springbankstar.entity.Rules;
/**
 * Контроллре служит получения, создание и удаления правил
 */

@RestController
@RequestMapping("/rules")
public class RecommendationRulesController {

    private final RecommendationRulesService rulesService;


    public RecommendationRulesController(RecommendationRulesService rulesService) {
        this.rulesService = rulesService;
    }

    /**
     * Метод createRules служит для создания правила
     * @param rules это объект в JSON  формате, который приходит в метод
     * Если правило создано, тогда этот метод вернет его
     */
    @Operation(summary = "Получение рекомендаций для пользователя по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Создание правила", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Rules.class))}),
            @ApiResponse(responseCode = "400", description = "Ошибка при вводе данных", content = @Content),
            @ApiResponse(responseCode = "404", description = "Такое правило уже есть")
    })
    @PostMapping
    public Rules createRules(@RequestBody Rules rules) {
        return rulesService.createRules(rules);

    }
    /**
     * Метод removeRules служит для удаления правила
     * @param id это id правила, которого мы хотим удалить
     */
    @Operation(summary = "Получение рекомендаций для пользователя по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Правило удалено", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Rules.class))}),
            @ApiResponse(responseCode = "400", description = "Ошибка при вводе данных", content = @Content),
            @ApiResponse(responseCode = "404", description = "Такаого правила нет")
    })
    @DeleteMapping("/{id}")
    public  Rules removeRules(@PathVariable Long id) {
        return rulesService.removeRules(id);
    }
    /**
     * Метод getRules служит поиска правила
     * @param id это id правила, который мы хотим найти
     */
    @Operation(summary = "Получение рекомендаций для пользователя по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Правило найдено", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Rules.class))}),
            @ApiResponse(responseCode = "400", description = "Ошибка при вводе данных", content = @Content),
            @ApiResponse(responseCode = "404", description = "Такаого правила нет")
    })
    @GetMapping("/{id}")
    public Rules getRules(@PathVariable Long id) {
        return rulesService.getRules(id);
    }
}
