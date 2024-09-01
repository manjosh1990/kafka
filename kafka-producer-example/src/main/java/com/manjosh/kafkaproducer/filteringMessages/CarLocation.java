package com.manjosh.kafkaproducer.filteringMessages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CarLocation {
    private String carId;
    private long timestamp;
    private int distance;
}
