package com.example.olx.controller;

import com.example.olx.configuration.Config;
import com.example.olx.internal.CompletableFutures;
import com.example.olx.model.AdvertisementDashboard;
import com.example.olx.model.AdvertisementDetail;
import com.example.olx.model.ParsingRequest;
import com.example.olx.service.ParserService;
import com.example.olx.service.RateService;
import com.example.olx.service.SchedulingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class ParsingRequestController {

    @Autowired
    ParserService parserService;
    @Autowired
    SchedulingService schedulingService;
    @Autowired
    CompletableFutures completableFutures;

    @GetMapping("/parse")
    public String delete(@RequestParam(value = "parsingRequest") String parsingRequest) throws JsonProcessingException {
        final ParsingRequest request = new ObjectMapper().readValue(parsingRequest, ParsingRequest.class);

        RateService.downloadRates();
        Config.urlToParse.add(request.getUrl());

        CompletableFuture.runAsync(() -> completableFutures.retry(request.getNumberOfLoops(), request.getDelayMinutes(), TimeUnit.MINUTES, () -> CompletableFuture.runAsync(() -> parserService.parse(request))));

        return "Parsing started";
    }

    @GetMapping("/clear")
    public String clear() {

        Config.urlToParse.clear();

        return "List cleared";
    }

    @GetMapping("/get_last_added")
    public List<AdvertisementDetail> getLast() throws InterruptedException {

        while (ParserService.lastParsingDate.compareTo(LocalDateTime.MIN) == 0)
            Thread.sleep(1000);

        return ParserService.identity.values().stream().filter(v -> (v.getParsingDate().compareTo(ParserService.lastParsingDate.minusMinutes(100))) > 0).collect(Collectors.toList());
    }


    @GetMapping("/getAdv/{extId}")
    public AdvertisementDetail getAdv(@PathVariable String extId) throws InterruptedException {

        while (ParserService.lastParsingDate.compareTo(LocalDateTime.MIN) == 0)
            Thread.sleep(1000);

        return ParserService.identity.get(extId);
    }

    @PostMapping("update_adv")
    public AdvertisementDetail update(@RequestBody AdvertisementDetail advertisementDetail) throws IOException {
        AdvertisementDashboard advertisementDashboard = new AdvertisementDashboard();
        advertisementDashboard.setUrl(advertisementDetail.getUrl());
        advertisementDashboard.setSearchUrl(advertisementDetail.getSearchingUrl());
        return parserService.parseFurther(advertisementDashboard, null, advertisementDetail.getMainImage());
    }
}
