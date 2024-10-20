package ru.star.springbankstar.controllrs;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.star.springbankstar.entity.TotalRule;
import ru.star.springbankstar.services.TotalRuleService;

import java.util.Collection;

@RestController
@RequestMapping("/rule_total")
public class TotalRuleController {

    private final TotalRuleService totalRuleService;


    public TotalRuleController(TotalRuleService totalRuleService) {
        this.totalRuleService = totalRuleService;
    }

    @GetMapping
    public Collection<TotalRule> getTotalRules() {
        return totalRuleService.getTotalRules();
    }
}
