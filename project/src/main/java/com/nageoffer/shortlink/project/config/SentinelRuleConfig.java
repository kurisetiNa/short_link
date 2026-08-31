package com.nageoffer.shortlink.project.config;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 初始化 Sentinel 限流规则
 */
@Component
public class SentinelRuleConfig implements InitializingBean {

    @Override
    public void afterPropertiesSet() {
        List<FlowRule> rules = new ArrayList<>();
        FlowRule createShortLinkRule = new FlowRule();
        createShortLinkRule.setResource("create_short-link");
        createShortLinkRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        createShortLinkRule.setCount(1);
        rules.add(createShortLinkRule);
        FlowRuleManager.loadRules(rules);
    }
}
