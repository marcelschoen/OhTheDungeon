/**
 * 
 */
package forge_sandbox.com.someguyssoftware.dungeons2.printer;

import forge_sandbox.com.someguyssoftware.gottschcore.Quantity;

/**
 * @author Mark Gottschling on Aug 27, 2017
 *
 */
public interface IPrettyPrinter {

    default public String quantityToString(Quantity q) {
        return q.getMin() + " <--> " + q.getMax();
    }
}
