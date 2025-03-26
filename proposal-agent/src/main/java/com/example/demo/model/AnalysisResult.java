package com.example.demo.model;

import java.math.BigDecimal;
import java.util.Map;

public record AnalysisResult(Map<String, Double> coverageDistribution, Map<String, Double> riskAssessment) {
}
