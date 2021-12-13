/**
 * 
 */
package forge_sandbox.com.someguyssoftware.dungeons2.spawner;

import com.google.gson.annotations.Since;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mark Gottschling on Jan 15, 2017
 *
 */
public class SpawnSheet {
    @Since(1.0)
    private Map<String, SpawnGroup> groups;

    /**
     * 
     */
    public SpawnSheet() {}

    /**
     * @return the groups
     */
    public Map<String, SpawnGroup> getGroups() {
        if (groups == null) {
            this.groups = new HashMap<>();
        }
        return groups;
    }

    /**
     * @param groups the groups to set
     */
    public void setGroups(Map<String, SpawnGroup> groups) {
        this.groups = groups;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "SpawnSheet [groups=" + groups + "]";
    }
    
}
