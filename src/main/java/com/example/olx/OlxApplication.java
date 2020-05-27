package com.example.olx;

import com.example.olx.configuration.Config;
import com.example.olx.internal.CommandAdapter;
import com.example.olx.internal.CommandEnum;
import com.example.olx.service.ParserService;
import com.example.olx.service.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

@SpringBootApplication
public class OlxApplication implements CommandLineRunner {

    @Autowired
    ParserService parserService;
    @Autowired
    CommandAdapter commandAdapter;

    public static void main(String[] args) throws IOException {
        SpringApplication.run(OlxApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        final Scanner scanner = new Scanner(System.in);
        while (true) {
            final String string = scanner.nextLine();
            final CommandEnum command = commandAdapter.apply(string);

            switch (command) {
                case HTTP:
                    RateService.downloadRates();
                    System.out.println("Scanning started");
                    parserService.parse(string);
                    endLine(string);
                    break;
                case CLEAN:
                    Config.urlToParse.clear();
                    System.out.println("url list cleared");
                    break;
                case PARSE_ALL:
                    Config.urlToParse.forEach(href -> {
                        System.out.println("Scanning started");
                        parserService.parse(href);
                        endLine(string);
                    });
                    break;
                case BIKES:
                    String[] bikes = {"Comanche ", "Cross ", "Cannondale ", "Centurion ", "Felt ", "Focus ", "Cyclone ", "DRAG ", "Bergamont ", "BH ", "Apollo ", "Author ", "Bottecchia ", "Bulls ", "Bianchi ", "BMW ", "Le-Grand ", "LEON ", "Kross ", "Lapierre ", "Marin ", "Medano ", "Liv ", "Lombardo ", "GHOST ", "Giant ", "Formula ", "Fuji ", "HEAD ", "Kellys ", "Graziella ", "Haibike ", "Stolen ", "Trek ", "Scott ", "Spelli ", "VNC ", "Volkswagen ", "Trinx ", "United-Cruiser ", "Momentum ", "Orbea ", "Mercedes-Benz ", "Merida ", "Sava ", "Schwinn ", "Polygon ", "Pride "};
                    Arrays.stream(bikes).forEach(e -> parserService.parse("https://www.olx.ua/hobbi-otdyh-i-sport/sport-otdyh/velo/velosipedy/kiev/q-" + e.trim().toLowerCase() + "/?search%5Bfilter_float_price%3Ato%5D=10000&search%5Bfilter_float_price%3Afrom%5D=4500&search%5Bprivate_business%5D=private"));
                    break;
                case ERROR:
                    System.out.println("error case");
            }
        }
    }

    private void endLine(String string) {
        while (ParserService.detailsFutures.stream().anyMatch(f -> !f.isDone())) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Scanning finished: " + string);
    }
}
