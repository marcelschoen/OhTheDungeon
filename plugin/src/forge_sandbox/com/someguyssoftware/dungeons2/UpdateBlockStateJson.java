/**
 * 
 */
package forge_sandbox.com.someguyssoftware.dungeons2;

import java.util.LinkedHashMap;

/**
 * @author Mark Gottschling on Feb 6, 2017
 *
 */
public class UpdateBlockStateJson {

    public class Wrapper {
        LinkedHashMap<String, BlockStates> variants;

        /**
         * @return the variants
         */
        public LinkedHashMap<String, BlockStates> getVariants() {
            return variants;
        }

        /**
         * @param variants the variants to set
         */
        public void setVariants(LinkedHashMap<String, BlockStates> variants) {
            this.variants = variants;
        }
        
    }
    public static class BlockStates {
        String model;
        Integer x;
        Integer y;
        Boolean uvlock;

        public BlockStates() {}
        
        public BlockStates(String model) {
            this.model = model;
        }
        
        /**
         * @return the model
         */
        public String getModel() {
            return model;
        }

        /**
         * @param model the model to set
         */
        public void setModel(String model) {
            this.model = model;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "BlockStates [model=" + model + "]";
        }

        /**
         * @return the x
         */
        public Integer getX() {
            return x;
        }

        /**
         * @param x the x to set
         */
        public void setX(Integer x) {
            this.x = x;
        }

        /**
         * @return the y
         */
        public Integer getY() {
            return y;
        }

        /**
         * @param y the y to set
         */
        public void setY(Integer y) {
            this.y = y;
        }

        /**
         * @return the uvlock
         */
        public Boolean isUvlock() {
            return uvlock;
        }

        public Boolean getUvlock() {
            return uvlock;
        }
        
        /**
         * @param uvlock the uvlock to set
         */
        public void setUvlock(Boolean uvlock) {
            this.uvlock = uvlock;
        }
        
    }

}
