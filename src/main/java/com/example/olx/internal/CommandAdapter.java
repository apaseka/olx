package com.example.olx.internal;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandAdapter implements Function<String, CommandEnum> {

    private static final Map<String, CommandEnum> commands;

    static {
        commands = Arrays.stream(CommandEnum.values()).collect(Collectors.toMap(CommandEnum::getName, command -> command));
    }

    @Override
    public CommandEnum apply(String s) {
        final Stream<Map.Entry<String, CommandEnum>> entryStream = commands.entrySet().stream().filter(c -> s.toLowerCase().contains(c.getKey()));
        return entryStream.findFirst().orElseGet(() -> {
            System.out.println("wrong input");
            return new AbstractMap.SimpleEntry<>("error", CommandEnum.ERROR);
        }).getValue();
    }
}
