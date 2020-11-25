package me.jamestmartin.wasteland.modtools.infraction;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class Duration implements Comparable<Duration> {
    public static final Duration ZERO = new Duration(0);
    public static final Duration INFINITY = new Duration();
    
    /** Durations of "none" are infinite. */
    private final Optional<Long> seconds;
    
    private Duration() {
        this.seconds = Optional.empty();
    }
    
    private Duration(Optional<Long> seconds) {
        this.seconds = seconds;
    }
    
    public Duration(long seconds) {
        this.seconds = Optional.of(seconds);
    }
    
    public Optional<Long> getSeconds() {
        return seconds;
    }
    
    public Duration max(Duration other) {
        return new Duration(this.seconds.flatMap(x -> other.seconds.map(y -> Math.max(x, y))));
    }
    
    public static Optional<Duration> parse(String str) {
        if (str.equals("0")) {
            return Optional.of(ZERO);
        }
        if (str.equals("forever")) {
            return Optional.of(INFINITY);
        }
        return DurationParts.parse(str).map(DurationParts::toDuration);
    }
    
    private Optional<DurationParts> toParts() {
        if (seconds.isEmpty()) {
            return Optional.empty();
        }
        long secs = seconds.get();

        // Find the greatest prefix that gives an exact value.
        List<DurationSuffix> suffixes = List.of(DurationSuffix.values());
        Collections.reverse(suffixes);
        DurationSuffix s = null;
        for (DurationSuffix suffix : suffixes) {
            if (secs % suffix.getSeconds() == 0) {
                s = suffix;
            }
        }
        
        return Optional.of(new DurationParts(secs / s.getSeconds(), s));
    }
    
    @Override
    public String toString() {
        return toParts().map(DurationParts::toString).orElse("forever");
    }
    
    public String toStringLong() {
        return toParts().map(DurationParts::toStringLong).orElse("forever");
    }
    
    private static class DurationParts {
        public final long value;
        public final DurationSuffix suffix;
        
        public DurationParts(long value, DurationSuffix suffix) {
            this.value = value;
            this.suffix = suffix;
        }
        
        public Duration toDuration() {
            return new Duration(value * suffix.getSeconds());
        }
        
        @Override
        public String toString() {
            return value + suffix.toString();
        }
        
        public String toStringLong() {
            return value + " " + suffix.toStingLong();
        }
        
        public static Optional<DurationParts> parse(String str) {
            if (str.length() < 2) {
                return Optional.empty();
            }
            
            Optional<DurationSuffix> suffix = DurationSuffix.fromChar(str.charAt(str.length() - 1));
            if (suffix.isEmpty()) {
                return Optional.empty();
            }
            
            String valueStr = str.substring(0, str.length() - 1);
            if (!valueStr.matches("\\d+")) {
                return Optional.empty();
            }
            
            return Optional.of(new DurationParts(Long.parseLong(valueStr), suffix.get()));
        }
    }
    
    private static enum DurationSuffix {
        SECONDS('s', "seconds", 1),
        MINUTES('m', "minutes", 60),
        HOURS('h', "hours", 3600),
        DAYS('d', "days", 86400),
        WEEKS('w', "weeks", 604800),
        MONTHS('M', "months", 2628000),
        YEARS('y', "years", 31540000);
        
        private final char character;
        private final String longName;
        private final long seconds;
        
        private DurationSuffix(char character, String longName, long seconds) {
            this.character = character;
            this.longName = longName;
            this.seconds = seconds;
        }
        
        public long getSeconds() {
            return seconds;
        }
        
        public char toChar() {
            return character;
        }
        
        @Override
        public String toString() {
            return String.valueOf(toChar());
        }
        
        public String toStingLong() {
            return longName;
        }
        
        public static Optional<DurationSuffix> fromChar(char c) {
            for (DurationSuffix suffix : DurationSuffix.values()) {
                if (suffix.character == c) {
                    return Optional.of(suffix);
                }
            }
            return Optional.empty();
        }
    }

    @Override
    public int compareTo(Duration other) {
        if (getSeconds().isEmpty() && other.getSeconds().isEmpty()) {
            return 0;
        }
        if (getSeconds().isEmpty()) {
            return 1;
        }
        if (other.getSeconds().isEmpty()) {
            return -1;
        }
        
        return Long.compare(getSeconds().get(), other.getSeconds().get());
    }
    
    public static Duration fromDates(Date begin, Date end) {
        return new Duration(end.getTime() - begin.getTime());
    }
    
    public static Duration fromDates(Date begin, Optional<Date> end) {
        if (end.isEmpty()) {
            return INFINITY;
        }
        return fromDates(begin, end.get());
    }
}
