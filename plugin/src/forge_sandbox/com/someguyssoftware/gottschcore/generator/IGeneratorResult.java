/**
 * 
 */
package forge_sandbox.com.someguyssoftware.gottschcore.generator;

/**
 * @author Mark Gottschling on Jul 27, 2019
 *
 */
public interface IGeneratorResult {
	public boolean isSuccess();
	public IGeneratorResult success();
	public IGeneratorResult fail();
}
