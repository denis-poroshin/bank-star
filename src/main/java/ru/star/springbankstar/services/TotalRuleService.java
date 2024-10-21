package ru.star.springbankstar.services;

import org.springframework.stereotype.Service;
import ru.star.springbankstar.entity.TotalRule;
import ru.star.springbankstar.repositorys.TotalRuleRepository;

import java.util.Collection;
import java.util.Collections;

@Service
public class TotalRuleService {

    private final TotalRuleRepository totalRuleRepository;

    public TotalRuleService(TotalRuleRepository totalRuleRepository) {
        this.totalRuleRepository = totalRuleRepository;
    }
    public Collection<TotalRule> getTotalRules() {
        return Collections.unmodifiableCollection(totalRuleRepository.findAll());
    }
}
