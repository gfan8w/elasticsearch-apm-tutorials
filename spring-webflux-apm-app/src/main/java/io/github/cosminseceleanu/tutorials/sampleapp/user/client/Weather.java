package io.github.cosminseceleanu.tutorials.sampleapp.user.client;

import co.elastic.apm.api.CaptureTransaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
public class Weather {

    @CaptureTransaction(type = "Weather", value = "Together")
    public Mono<List<String>> getCityWeather(){
        return
                Flux.just("Hongkong","Shanghai","newyork").flatMap(city->{
                    var result = WebClient.create().get().uri("https://wttr.in/" + city + "?format=3").retrieve()
                            .bodyToMono(String.class).doOnNext(w->log.info("weather is: {}", w));
                    return result;
                }).collectList();

    }

    @CaptureTransaction(type = "Weather", value = "OneByOne")
    public Mono<List<String>> getCityWeatherOneByOne(){
        return
                Flux.just("london","dc","Beijing").concatMap(city->{
                    var result = WebClient.create().get().uri("https://wttr.in/" + city + "?format=3").retrieve()
                            .bodyToMono(String.class).doOnNext(w->log.info("weather is: {}", w));
                    return result;
                }).collectList();

    }
}
