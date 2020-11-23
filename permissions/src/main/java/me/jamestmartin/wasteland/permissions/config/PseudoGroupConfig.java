package me.jamestmartin.wasteland.permissions.config;

import java.util.List;
import java.util.Map;

class PseudoGroupConfig {
    private final List<String> parents;
    private final Map<String, Boolean> permissions;
    
    public PseudoGroupConfig(List<String> parents, Map<String, Boolean> permissions) {
        this.parents = parents;
        this.permissions = permissions;
    }
    
    public List<String> getParents() {
        return parents;
    }
    
    public Map<String, Boolean> getPermissions() {
        return permissions;
    }
}
