package me.jamestmartin.wasteland.permissions.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;

import me.jamestmartin.wasteland.permissions.WastelandPermissions;
import me.jamestmartin.wasteland.permissions.api.Group;
import me.jamestmartin.wasteland.permissions.api.Permissions;
import me.jamestmartin.wasteland.permissions.api.PlayerGroup;

public class ConfigParser {
    public static Permissions parse(ConfigurationSection c) {
        Map<String, Group> groups = parseGroups(c.getConfigurationSection("groups"));
        Map<UUID, PlayerGroup> players = parsePlayers(groups, c.getConfigurationSection("players"));
        
        return new Permissions(groups, players);
    }
    
    private static Map<String, Group> parseGroups(ConfigurationSection c) {
        if (c == null) {
            return Map.of();
        }
        
        Map<String, GroupConfig> groupConfigs = new HashMap<>();
        for (String group : c.getKeys(false)) {
            groupConfigs.put(group, parseGroup(group, c.getConfigurationSection(group)));
        }
        
        MutableGraph<GroupConfig> gb = GraphBuilder.directed().allowsSelfLoops(false).build();
        for (GroupConfig group : groupConfigs.values()) {
            gb.addNode(group);
        }
        
        for (GroupConfig group : groupConfigs.values()) {
            for (String parent : group.getParents()) {
                gb.putEdge(group, groupConfigs.get(parent));
            }
        }
        
        Set<String> orderedGroupsSet = new HashSet<>();
        List<GroupConfig> orderedGroups = new ArrayList<>();
        Set<GroupConfig> nodes = gb.nodes();
        while (!nodes.isEmpty()) {
            int previousSize = nodes.size();
            // You can't remove from a set that you're iterating over.
            Set<GroupConfig> nextNodes = new HashSet<>(nodes);
            
            nextNode: for (GroupConfig node : nodes) {
                for (String parent : node.getParents()) {
                    if (!orderedGroupsSet.contains(parent)) {
                        continue nextNode;
                    }
                }
                
                orderedGroupsSet.add(node.getId());
                orderedGroups.add(node);
                nextNodes.remove(node);
            }
            
            if (previousSize == nextNodes.size()) {
                throw new RuntimeException("Cyclic group dependencies");
            }
            
            nodes = nextNodes;
        }
        
        Map<String, Group> groups = new HashMap<>();
        for (GroupConfig group : orderedGroups) {
            List<Group> parents = group.getParents().stream().map(groups::get).collect(Collectors.toUnmodifiableList());
            groups.put(group.getId(), new Group(group.getId(), parents, group.getPermissions()));
        }
        
        return groups;
    }
    
    private static GroupConfig parseGroup(String id, ConfigurationSection c) {
        return new GroupConfig(id, parsePseudoGroup(c.getStringList("inherits"), c.getConfigurationSection("permissions")));
    }
    
    private static Map<UUID, PlayerGroup> parsePlayers(Map<String, Group> groups, ConfigurationSection c) {
        Map<UUID, PlayerGroup> players = new HashMap<>();
        if (c == null) {
            return players;
        }
        
        for (String uuidStr : c.getKeys(false)) {
            UUID uuid = UUID.fromString(uuidStr);
            players.put(uuid, parsePlayer(groups, uuidStr, c.getConfigurationSection(uuidStr)));
        }
        return players;
    }
    
    private static PlayerGroup parsePlayer(Map<String, Group> groups, String uuid, ConfigurationSection c) {
        OfflinePlayer player = WastelandPermissions.getInstance().getServer().getOfflinePlayer(UUID.fromString(uuid));
        PseudoGroupConfig config = parsePseudoGroup(c.getStringList("groups"), c.getConfigurationSection("permissions"));
        List<Group> playerGroups = config.getParents().stream().map(groups::get).collect(Collectors.toUnmodifiableList());
        return new PlayerGroup(player, playerGroups, config.getPermissions());
    }
    
    private static PseudoGroupConfig parsePseudoGroup(List<String> inherits, ConfigurationSection permissions) {
        if (inherits == null) {
            inherits = List.of();
        }
        return new PseudoGroupConfig(inherits, parsePermissions(permissions));
    }
    
    private static Map<String, Boolean> parsePermissions(ConfigurationSection c) {
        Map<String, Boolean> permissions = new HashMap<>();
        if (c == null) {
            return permissions;
        }
        
        for (String node : c.getKeys(false)) {
            if (c.isBoolean(node)) {
                permissions.put(node,  c.getBoolean(node));
            } else {
                for (Entry<String, Boolean> entry : parsePermissions(c.getConfigurationSection(node)).entrySet()) {
                    permissions.put(node + "." + entry.getKey(), entry.getValue());
                }
            }
        }
        return permissions;
    }
}
