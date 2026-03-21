package com.zzk.recommendmodule.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.recommend")
public class RecommendProperties {

    private int defaultPageSize = 12;
    private int maxPageSize = 30;
    private int hotCandidateSize = 60;
    private int latestCandidateSize = 40;
    private int relatedCandidateSize = 40;
    private int itemCfTopN = 20;

    private double hotWeight = 1.0D;
    private double freshnessWeight = 0.3D;
    private double categoryWeight = 0.8D;
    private double tagWeight = 0.8D;
    private double itemCfWeight = 1.2D;

    private boolean trackingEnabled = true;
    private boolean asyncEnabled = false;
    private boolean kafkaEnabled = false;
}
