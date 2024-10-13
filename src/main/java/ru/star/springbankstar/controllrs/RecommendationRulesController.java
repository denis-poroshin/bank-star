package ru.star.springbankstar.controllrs;

import org.springframework.web.bind.annotation.*;
import ru.star.springbankstar.interfaces.RecommendationRulesService;
import ru.star.springbankstar.entity.Rules;

@RestController
@RequestMapping("/rules")
public class RecommendationRulesController {

    private final RecommendationRulesService rulesService;


    public RecommendationRulesController(RecommendationRulesService rulesService) {
        this.rulesService = rulesService;
    }
    @PostMapping
    public Rules createRules(@RequestBody Rules rules) {
        return rulesService.createRules(rules);

    }

    @DeleteMapping("/{id}")
    public  Rules removeRules(@PathVariable Long id) {
        return rulesService.removeRules(id);
    }
    @GetMapping("/{id}")
    public Rules getRules(@PathVariable Long id) {
        return rulesService.getRules(id);
    }
}
