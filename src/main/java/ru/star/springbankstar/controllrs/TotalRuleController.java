package ru.star.springbankstar.controllrs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.star.springbankstar.entity.Rules;
import ru.star.springbankstar.entity.TotalRule;
import ru.star.springbankstar.services.TotalRuleService;

import java.util.Collection;
/**
 * Контроллре служит получения id диномического правила и счетчик сколько раз его использовали
 */
@RestController
@RequestMapping("/rule_total")
public class TotalRuleController {

    private final TotalRuleService totalRuleService;


    public TotalRuleController(TotalRuleService totalRuleService) {
        this.totalRuleService = totalRuleService;
    }
    /**
     * Метод getTotalRules служит для получения правил с счетчиком его использования в запросах
     */
    @Operation(summary = "Получение рекомендаций для пользователя по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Получить все правила с счетчиком", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TotalRule.class))}),
            @ApiResponse(responseCode = "400", description = "Ошибка при вводе данных", content = @Content),
    })

    @GetMapping
    public Collection<TotalRule> getTotalRules() {
        return totalRuleService.getTotalRules();
    }
}
