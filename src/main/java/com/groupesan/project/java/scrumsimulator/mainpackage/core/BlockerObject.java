package com.groupesan.project.java.scrumsimulator.mainpackage.core;

public class BlockerObject {
    
        private BlockerType type;
        private int duration;
    
        public BlockerObject(BlockerType type) {
            this.type = type;
        }
    
        public BlockerType getType() {
            return type;
        }
    
        public int getDuration() {
            return duration;
        }
    
        public String toString() {
            return "[Blocker] " + type.toString();
        }
}
