/**
 * 
 */
package forge_sandbox.com.someguyssoftware.gottschcore.generator;

/**
 * @author Mark Gottschling on Jul 27, 2019
 *
 */
public class GeneratorResult implements IGeneratorResult {
	private boolean success;

	public GeneratorResult() {}
	
	public GeneratorResult(boolean success) {
		setSuccess(success);
	}
		
	@Override
	public IGeneratorResult success() {
		this.success = true;
		return this;
	}

	@Override
	public IGeneratorResult fail() {
		this.success = false;
		return this;
	}
	
	@Override
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
}
