package me.jamestmartin.wasteland.permissions.config;

import java.util.List;
import java.util.Map;

class GroupConfig extends PseudoGroupConfig {
    private final String id;
    
    public GroupConfig(String id, List<String> parents, Map<String, Boolean> permissions) {
        super(parents, permissions);
        this.id = id;
    }
    
    public GroupConfig(String id, PseudoGroupConfig group) {
        this(id, group.getParents(), group.getPermissions());
    }
    
    public String getId() {
        return id;
    }
}
