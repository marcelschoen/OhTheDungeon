/**
 * 
 */
package forge_sandbox.com.someguyssoftware.gottschcore.annotation;

import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

@Target(TYPE)
/**
 * @author Mark Gottschling on May 4, 2017
 *
 */
public @interface Credits {
	String[] values();
}
