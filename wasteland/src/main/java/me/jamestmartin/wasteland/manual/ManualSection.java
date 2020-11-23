package me.jamestmartin.wasteland.manual;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ManualSection {
    private static final String MANUAL_SECTION_REGEX = "\\d+(\\.\\d+)*";
    
    private final String summary;
    private final Optional<String> details;
    private final List<ManualSection> sections;
    
    public ManualSection(String summary, Optional<String> details, List<ManualSection> sections) {
        this.summary = summary;
        this.details = details;
        this.sections = sections;
    }
    
    public ManualSection(String summary, Optional<String> details) {
        this(summary, details, List.of());
    }
    
    public ManualSection(String summary, List<ManualSection> subsections) {
        this(summary, Optional.empty(), subsections);
    }
    
    public ManualSection(String summary) {
        this(summary, Optional.empty());
    }
    
    public String getSummary() {
        return summary;
    }
    
    public Optional<String> getDetails() {
        return details;
    }
    
    public List<ManualSection> getSections() {
        return sections;
    }
    
    public Optional<ManualSection> getSection(int... path) {
        if (path.length == 0) {
            return Optional.of(this);
        }
        
        return getSection(getSections(), path);
    }
    
    private static final int[] parsePath(String path) {
        if (path.isEmpty()) {
            return new int[0];
        }
        String[] pathParts = path.split("\\.");
        return Stream.of(pathParts).mapToInt(Integer::parseInt).toArray();
    }
    
    public Optional<ManualSection> getSection(String path) {
        return getSection(parsePath(path));
    }
    
    public static Optional<ManualSection> getSection(List<ManualSection> sections, int... path) {
        if (path.length == 0) {
            throw new IllegalArgumentException("Empty manual section path");
        }
        
        if (sections.size() <= path[0] - 1) {
            return Optional.empty();
        }
        
        int[] rest = IntStream.of(path).skip(1).toArray();
        return sections.get(path[0] - 1).getSection(rest);
    }
    
    public static Optional<ManualSection> getSection(List<ManualSection> sections, String path) {
        if (path.isEmpty()) {
            throw new IllegalArgumentException("Empty manual section path");
        }
        
        return getSection(sections, parsePath(path));
    }
    
    public static final boolean isSectionPath(String path) {
        return path.matches(MANUAL_SECTION_REGEX);
    }
}
