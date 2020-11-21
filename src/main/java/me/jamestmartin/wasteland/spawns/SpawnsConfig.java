package me.jamestmartin.wasteland.spawns;

import java.util.Map;

public class SpawnsConfig {
    private final Map<MonsterType, MonsterSpawnConfig> monsterConfigs;
    
    public SpawnsConfig(Map<MonsterType, MonsterSpawnConfig> monsterConfigs) {
        this.monsterConfigs = monsterConfigs;
    }
    
    public Map<MonsterType, MonsterSpawnConfig> getMonsterConfigs() {
        return monsterConfigs;
    }

    public MonsterSpawnConfig getMonster(MonsterType type) {
        return monsterConfigs.get(type);
    }
}
