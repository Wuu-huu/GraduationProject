package com.zzk.recommendmodule.config;

import org.springframework.stereotype.Component;

/**
 * Kafka 预留骨架。
 * 当前阶段仍采用单体同步写库，后续若启用 Kafka，可在这里切换为异步发送。
 */
@Component
public class RecommendAsyncStub {

    public void publishUserBehaviorAsync(String payload) {
        // Future upgrade reference:
        // if (recommendProperties.isKafkaEnabled()) {
        //     kafkaTemplate.send(KafkaTopicConstants.USER_BEHAVIOR_TOPIC, payload);
        // }
    }

    public void publishExposureAsync(String payload) {
        // Future upgrade reference:
        // if (recommendProperties.isKafkaEnabled()) {
        //     kafkaTemplate.send(KafkaTopicConstants.RECOMMEND_EXPOSURE_TOPIC, payload);
        // }
    }
}
