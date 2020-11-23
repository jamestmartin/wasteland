package me.jamestmartin.wasteland.permissions.api;

import java.util.List;
import java.util.Map;

public class Group extends PseudoGroup {
    private final String name;

    public Group(String name, List<Group> parents, Map<String, Boolean> permissions) {
        super(parents, permissions);
        this.name = name;
    }
    
    public Group(String name, PseudoGroup group) {
        this(name, group.getParents(), group.getDirectPermissions());
    }

    public String getName() {
        return name;
    }
}
