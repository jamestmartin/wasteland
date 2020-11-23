package me.jamestmartin.wasteland.permissions.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** An actual named group, or just an unnamed set of permissions associated with a player. */
public class PseudoGroup {
    private final List<Group> parents;
    private final Map<String, Boolean> permissions;
    private final Map<String, Boolean> allPermissions;
    
    public PseudoGroup(List<Group> parents, Map<String, Boolean> permissions) {
        this.parents = parents;
        this.permissions = permissions;
        
        this.allPermissions = new HashMap<>();
        parents.stream().map(PseudoGroup::getPermissions).forEach(allPermissions::putAll);
        allPermissions.putAll(permissions);
    }
    
    public List<Group> getParents() {
        return parents;
    }
    
    /** Get only the permissions defined directly on this group. */
    public Map<String, Boolean> getDirectPermissions() {
        return permissions;
    }
    
    /** Get all of the permissions provided by this group. */
    public Map<String, Boolean> getPermissions() {
        return allPermissions;
    }
}
