/**
 * 
 */
package forge_sandbox.com.someguyssoftware.gottschcore.meta;

import forge_sandbox.com.someguyssoftware.gottschcore.enums.IRarity;
import forge_sandbox.com.someguyssoftware.gottschcore.positional.ICoords;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mark Gottschling on Jul 28, 2019
 *
 */
public class Meta implements IMeta {
	private String name;
	private String description;
	private String author;
	private String parent;
	private ICoords offset;
	private List<IMetaArchetype> archetypes;
	private IMetaType type;
	private List<IMetaTheme> themes;
	private List<String> biomeWhiteList;
	private List<String> biomeBlackList;
	private List<IRarity> rarities;
	private Double order;
	
	public Meta() {}

	/* (non-Javadoc)
	 * @see com.someguyssoftware.gottschcore.meta.IMeta#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see com.someguyssoftware.gottschcore.meta.IMeta#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see com.someguyssoftware.gottschcore.meta.IMeta#getDescription()
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/* (non-Javadoc)
	 * @see com.someguyssoftware.gottschcore.meta.IMeta#setDescription(java.lang.String)
	 */
	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	/* (non-Javadoc)
	 * @see com.someguyssoftware.gottschcore.meta.IMeta#getAuthor()
	 */
	@Override
	public String getAuthor() {
		return author;
	}

	/* (non-Javadoc)
	 * @see com.someguyssoftware.gottschcore.meta.IMeta#setAuthor(java.lang.String)
	 */
	@Override
	public void setAuthor(String author) {
		this.author = author;
	}

	/* (non-Javadoc)
	 * @see com.someguyssoftware.gottschcore.meta.IMeta#getParent()
	 */
	@Override
	public String getParent() {
		return parent;
	}

	/* (non-Javadoc)
	 * @see com.someguyssoftware.gottschcore.meta.IMeta#setParent(java.lang.String)
	 */
	@Override
	public void setParent(String parent) {
		this.parent = parent;
	}

	/* (non-Javadoc)
	 * @see com.someguyssoftware.gottschcore.meta.IMeta#getArchetypes()
	 */
	@Override
	public List<IMetaArchetype> getArchetypes() {
		if (this.archetypes == null) {
			this.archetypes = new ArrayList<>();
		}
		return archetypes;
	}

	/* (non-Javadoc)
	 * @see com.someguyssoftware.gottschcore.meta.IMeta#setArchetypes(java.util.List)
	 */
	@Override
	public void setArchetypes(List<IMetaArchetype> archetypes) {
		this.archetypes = archetypes;
	}

	/* (non-Javadoc)
	 * @see com.someguyssoftware.gottschcore.meta.IMeta#getType()
	 */
	@Override
	public IMetaType getType() {
		return type;
	}

	/* (non-Javadoc)
	 * @see com.someguyssoftware.gottschcore.meta.IMeta#setType(com.someguyssoftware.gottschcore.meta.IMetaType)
	 */
	@Override
	public void setType(IMetaType type) {
		this.type = type;
	}

	/* (non-Javadoc)
	 * @see com.someguyssoftware.gottschcore.meta.IMeta#getThemes()
	 */
	@Override
	public List<IMetaTheme> getThemes() {
		if (this.themes == null) {
			this.themes = new ArrayList<>();
		}
		return themes;
	}

	/* (non-Javadoc)
	 * @see com.someguyssoftware.gottschcore.meta.IMeta#setThemes(java.util.List)
	 */
	@Override
	public void setThemes(List<IMetaTheme> themes) {
		this.themes = themes;
	}

	/* (non-Javadoc)
	 * @see com.someguyssoftware.gottschcore.meta.IMeta#getBiomeWhiteList()
	 */
	@Override
	public List<String> getBiomeWhiteList() {
		if (this.biomeWhiteList == null) {
			this.biomeWhiteList = new ArrayList<>();
		}
		return biomeWhiteList;
	}

	/* (non-Javadoc)
	 * @see com.someguyssoftware.gottschcore.meta.IMeta#setBiomeWhiteList(java.util.List)
	 */
	@Override
	public void setBiomeWhiteList(List<String> biomeWhiteList) {
		this.biomeWhiteList = biomeWhiteList;
	}

	/* (non-Javadoc)
	 * @see com.someguyssoftware.gottschcore.meta.IMeta#getBiomeBlackList()
	 */
	@Override
	public List<String> getBiomeBlackList() {
		if (this.biomeBlackList == null) {
			this.biomeBlackList = new ArrayList<>();
		}
		return biomeBlackList;
	}

	/* (non-Javadoc)
	 * @see com.someguyssoftware.gottschcore.meta.IMeta#setBiomeBlackList(java.util.List)
	 */
	@Override
	public void setBiomeBlackList(List<String> biomeBlackList) {
		this.biomeBlackList = biomeBlackList;
	}

	/* (non-Javadoc)
	 * @see com.someguyssoftware.gottschcore.meta.IMeta#getRarities()
	 */
	@Override
	public List<IRarity> getRarities() {
		if (this.rarities == null) {
			this.rarities = new ArrayList<>();
		}
		return rarities;
	}

	/* (non-Javadoc)
	 * @see com.someguyssoftware.gottschcore.meta.IMeta#setRarities(java.util.List)
	 */
	@Override
	public void setRarities(List<IRarity> rarities) {
		this.rarities = rarities;
	}

	/* (non-Javadoc)
	 * @see com.someguyssoftware.gottschcore.meta.IMeta#getOrder()
	 */
	@Override
	public Double getOrder() {
		return order;
	}

	/* (non-Javadoc)
	 * @see com.someguyssoftware.gottschcore.meta.IMeta#setOrder(java.lang.Double)
	 */
	@Override
	public void setOrder(Double order) {
		this.order = order;
	}

	public ICoords getOffset() {
		return offset;
	}

	public void setOffset(ICoords offset) {
		this.offset = offset;
	}
	
}
