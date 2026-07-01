package ru.karpin.commondto.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;


@FeignClient(name = "attractions-service")
public interface AttractionClient {

    @PatchMapping("/attraction/{id}/rating")
    void updateAverageRating(@PathVariable("id") Long attractionId, @RequestParam BigDecimal averageRating);
}
