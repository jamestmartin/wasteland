package me.jamestmartin.wasteland.manual.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bukkit.configuration.ConfigurationSection;

public class ManualConfigParser {
    public static ManualConfig parse(ConfigurationSection c) {
        ManualSection rules = new ManualSection(
                "The Server Rules",
                parseManualSectionList(castToSectionList(c.getMapList("rules"))));
        ManualSection faq = new ManualSection(
                "Frequently Asked Questions",
                parseManualSectionList(castToSectionList(c.getMapList("faq"))));
        return new ManualConfig(rules, faq);
    }
    
    private static ManualSection parseManualSection(Map<?, ?> c) {
        String summary = (String) c.get("summary");
        Optional<String> details = Optional.ofNullable((String) c.get("details"));
        List<ManualSection> subsections = parseManualSectionList(castToSectionList(c.get("sections")));
        
        return new ManualSection(summary, details, subsections);
    }
    
    private static List<Map<?, ?>> castToSectionList(Object x) {
        if (x == null) {
            return null;
        }
        return ((List<?>) x).stream().map(xx -> (Map<?, ?>) xx).collect(Collectors.toUnmodifiableList());
    }
    
    public static List<ManualSection> parseManualSectionList(List<Map<?, ?>> list) {
        List<ManualSection> sections = new ArrayList<>();
        if (list == null) {
            return sections;
        }
        
        for (Map<?, ?> section : list) {
            sections.add(parseManualSection(section));
        }
        
        return sections;
    }
}
