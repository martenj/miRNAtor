// ______________________________________________________
// Generated by sql2java - http://sql2java.sourceforge.net/
// jdbc driver used at code generation time: org.postgresql.Driver
//
// Please help us improve this tool by reporting:
// - problems and suggestions to
//   http://sourceforge.net/tracker/?group_id=54687
// - feedbacks and ideas on
//   http://sourceforge.net/forum/forum.php?forum_id=182208
// ______________________________________________________

package mirnator.sql2java;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import mirnator.sql2java.GeneratedBean;
import mirnator.sql2java.FamilyBean;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * MirnaBean is a mapping of mirna Table.
 * @author sql2java
*/
public class MirnaBean
    implements Serializable, GeneratedBean
{
	private static final long serialVersionUID = 3736261984140348523L;
	
    private Integer familyRef;

    private boolean familyRefIsModified = false;
    private boolean familyRefIsInitialized = false;

    private String mirnaSpecies;
    private boolean mirnaSpeciesIsModified = false;
    private boolean mirnaSpeciesIsInitialized = false;

    private String mirbaseAccession;
    private boolean mirbaseAccessionIsModified = false;
    private boolean mirbaseAccessionIsInitialized = false;

    private String mirnaSequence;
    private boolean mirnaSequenceIsModified = false;
    private boolean mirnaSequenceIsInitialized = false;

    private String mirnaName;
    private boolean mirnaNameIsModified = false;
    private boolean mirnaNameIsInitialized = false;

    private Integer mirnaId;
    private boolean mirnaIdIsModified = false;
    private boolean mirnaIdIsInitialized = false;

    private boolean _isNew = true;

    /**
     * Prefered methods to create a MirnaBean is via the createMirnaBean method in MirnaManager or
     * via the factory class MirnaFactory create method
     */
    protected MirnaBean()
    {
    }

    /**
     * Getter method for familyRef.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: mirna.family_ref</li>
     * <li> foreign key: family.family_id</li>
     * <li>column size: 10</li>
     * <li>jdbc type returned by the driver: Types.INTEGER</li>
     * </ul>
     *
     * @return the value of familyRef
     */
    public Integer getFamilyRef()
    {
        return familyRef;
    }

    /**
     * Setter method for familyRef.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to familyRef
     */
    public void setFamilyRef(Integer newVal)
    {
        if ((newVal != null && familyRef != null && (newVal.compareTo(familyRef) == 0)) ||
            (newVal == null && familyRef == null && familyRefIsInitialized)) {
            return;
        }
        familyRef = newVal;
        familyRefIsModified = true;
        familyRefIsInitialized = true;
    }

    /**
     * Setter method for familyRef.
     * <br>
     * Convenient for those who do not want to deal with Objects for primary types.
     *
     * @param newVal the new value to be assigned to familyRef
     */
    public void setFamilyRef(int newVal)
    {
        setFamilyRef(new Integer(newVal));
    }

    /**
     * Determines if the familyRef has been modified.
     *
     * @return true if the field has been modified, false if the field has not been modified
     */
    public boolean isFamilyRefModified()
    {
        return familyRefIsModified;
    }

    /**
     * Determines if the familyRef has been initialized.
     * <br>
     * It is useful to determine if a field is null on purpose or just because it has not been initialized.
     *
     * @return true if the field has been initialized, false otherwise
     */
    public boolean isFamilyRefInitialized()
    {
        return familyRefIsInitialized;
    }

    /**
     * Getter method for mirnaSpecies.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: mirna.mirna_species</li>
     * <li>column size: 2147483647</li>
     * <li>jdbc type returned by the driver: Types.VARCHAR</li>
     * </ul>
     *
     * @return the value of mirnaSpecies
     */
    public String getMirnaSpecies()
    {
        return mirnaSpecies;
    }

    /**
     * Setter method for mirnaSpecies.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to mirnaSpecies
     */
    public void setMirnaSpecies(String newVal)
    {
        if ((newVal != null && mirnaSpecies != null && (newVal.compareTo(mirnaSpecies) == 0)) ||
            (newVal == null && mirnaSpecies == null && mirnaSpeciesIsInitialized)) {
            return;
        }
        mirnaSpecies = newVal;
        mirnaSpeciesIsModified = true;
        mirnaSpeciesIsInitialized = true;
    }

    /**
     * Determines if the mirnaSpecies has been modified.
     *
     * @return true if the field has been modified, false if the field has not been modified
     */
    public boolean isMirnaSpeciesModified()
    {
        return mirnaSpeciesIsModified;
    }

    /**
     * Determines if the mirnaSpecies has been initialized.
     * <br>
     * It is useful to determine if a field is null on purpose or just because it has not been initialized.
     *
     * @return true if the field has been initialized, false otherwise
     */
    public boolean isMirnaSpeciesInitialized()
    {
        return mirnaSpeciesIsInitialized;
    }

    /**
     * Getter method for mirbaseAccession.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: mirna.mirbase_accession</li>
     * <li>column size: 2147483647</li>
     * <li>jdbc type returned by the driver: Types.VARCHAR</li>
     * </ul>
     *
     * @return the value of mirbaseAccession
     */
    public String getMirbaseAccession()
    {
        return mirbaseAccession;
    }

    /**
     * Setter method for mirbaseAccession.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to mirbaseAccession
     */
    public void setMirbaseAccession(String newVal)
    {
        if ((newVal != null && mirbaseAccession != null && (newVal.compareTo(mirbaseAccession) == 0)) ||
            (newVal == null && mirbaseAccession == null && mirbaseAccessionIsInitialized)) {
            return;
        }
        mirbaseAccession = newVal;
        mirbaseAccessionIsModified = true;
        mirbaseAccessionIsInitialized = true;
    }

    /**
     * Determines if the mirbaseAccession has been modified.
     *
     * @return true if the field has been modified, false if the field has not been modified
     */
    public boolean isMirbaseAccessionModified()
    {
        return mirbaseAccessionIsModified;
    }

    /**
     * Determines if the mirbaseAccession has been initialized.
     * <br>
     * It is useful to determine if a field is null on purpose or just because it has not been initialized.
     *
     * @return true if the field has been initialized, false otherwise
     */
    public boolean isMirbaseAccessionInitialized()
    {
        return mirbaseAccessionIsInitialized;
    }

    /**
     * Getter method for mirnaSequence.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: mirna.mirna_sequence</li>
     * <li>column size: 2147483647</li>
     * <li>jdbc type returned by the driver: Types.VARCHAR</li>
     * </ul>
     *
     * @return the value of mirnaSequence
     */
    public String getMirnaSequence()
    {
        return mirnaSequence;
    }

    /**
     * Setter method for mirnaSequence.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to mirnaSequence
     */
    public void setMirnaSequence(String newVal)
    {
        if ((newVal != null && mirnaSequence != null && (newVal.compareTo(mirnaSequence) == 0)) ||
            (newVal == null && mirnaSequence == null && mirnaSequenceIsInitialized)) {
            return;
        }
        mirnaSequence = newVal;
        mirnaSequenceIsModified = true;
        mirnaSequenceIsInitialized = true;
    }

    /**
     * Determines if the mirnaSequence has been modified.
     *
     * @return true if the field has been modified, false if the field has not been modified
     */
    public boolean isMirnaSequenceModified()
    {
        return mirnaSequenceIsModified;
    }

    /**
     * Determines if the mirnaSequence has been initialized.
     * <br>
     * It is useful to determine if a field is null on purpose or just because it has not been initialized.
     *
     * @return true if the field has been initialized, false otherwise
     */
    public boolean isMirnaSequenceInitialized()
    {
        return mirnaSequenceIsInitialized;
    }

    /**
     * Getter method for mirnaName.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: mirna.mirna_name</li>
     * <li>column size: 2147483647</li>
     * <li>jdbc type returned by the driver: Types.VARCHAR</li>
     * </ul>
     *
     * @return the value of mirnaName
     */
    public String getMirnaName()
    {
        return mirnaName;
    }

    /**
     * Setter method for mirnaName.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to mirnaName
     */
    public void setMirnaName(String newVal)
    {
        if ((newVal != null && mirnaName != null && (newVal.compareTo(mirnaName) == 0)) ||
            (newVal == null && mirnaName == null && mirnaNameIsInitialized)) {
            return;
        }
        mirnaName = newVal;
        mirnaNameIsModified = true;
        mirnaNameIsInitialized = true;
    }

    /**
     * Determines if the mirnaName has been modified.
     *
     * @return true if the field has been modified, false if the field has not been modified
     */
    public boolean isMirnaNameModified()
    {
        return mirnaNameIsModified;
    }

    /**
     * Determines if the mirnaName has been initialized.
     * <br>
     * It is useful to determine if a field is null on purpose or just because it has not been initialized.
     *
     * @return true if the field has been initialized, false otherwise
     */
    public boolean isMirnaNameInitialized()
    {
        return mirnaNameIsInitialized;
    }

    /**
     * Getter method for mirnaId.
     * <br>
     * PRIMARY KEY.<br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: mirna.mirna_id</li>
     * <li> imported key: mirnator_statistic.statistic_mirna</li>
     * <li> imported key: mre.mirna_ref</li>
     * <li>column size: 10</li>
     * <li>jdbc type returned by the driver: Types.INTEGER</li>
     * </ul>
     *
     * @return the value of mirnaId
     */
    public Integer getMirnaId()
    {
        return mirnaId;
    }

    /**
     * Setter method for mirnaId.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to mirnaId
     */
    public void setMirnaId(Integer newVal)
    {
        if ((newVal != null && mirnaId != null && (newVal.compareTo(mirnaId) == 0)) ||
            (newVal == null && mirnaId == null && mirnaIdIsInitialized)) {
            return;
        }
        mirnaId = newVal;
        mirnaIdIsModified = true;
        mirnaIdIsInitialized = true;
    }

    /**
     * Setter method for mirnaId.
     * <br>
     * Convenient for those who do not want to deal with Objects for primary types.
     *
     * @param newVal the new value to be assigned to mirnaId
     */
    public void setMirnaId(int newVal)
    {
        setMirnaId(new Integer(newVal));
    }

    /**
     * Determines if the mirnaId has been modified.
     *
     * @return true if the field has been modified, false if the field has not been modified
     */
    public boolean isMirnaIdModified()
    {
        return mirnaIdIsModified;
    }

    /**
     * Determines if the mirnaId has been initialized.
     * <br>
     * It is useful to determine if a field is null on purpose or just because it has not been initialized.
     *
     * @return true if the field has been initialized, false otherwise
     */
    public boolean isMirnaIdInitialized()
    {
        return mirnaIdIsInitialized;
    }

    /** The Family referenced by this bean. */
    private FamilyBean referencedFamily;
    /** Getter method for FamilyBean. */
    public FamilyBean getFamilyBean() {
        return this.referencedFamily;
    }
    /** Setter method for FamilyBean. */
    public void setFamilyBean(FamilyBean reference) {
        this.referencedFamily = reference;
    }
    
    /**
     * Determines if the current object is new.
     *
     * @return true if the current object is new, false if the object is not new
     */
    public boolean isNew()
    {
        return _isNew;
    }

    /**
     * Specifies to the object if it has been set as new.
     *
     * @param isNew the boolean value to be assigned to the isNew field
     */
    public void isNew(boolean isNew)
    {
        this._isNew = isNew;
    }

    /**
     * Determines if the object has been modified since the last time this method was called.
     * <br>
     * We can also determine if this object has ever been modified since its creation.
     *
     * @return true if the object has been modified, false if the object has not been modified
     */
    public boolean isModified()
    {
        return familyRefIsModified 		|| mirnaSpeciesIsModified  		|| mirbaseAccessionIsModified  		|| mirnaSequenceIsModified  		|| mirnaNameIsModified  		|| mirnaIdIsModified  ;
    }

    /**
     * Resets the object modification status to 'not modified'.
     */
    public void resetIsModified()
    {
        familyRefIsModified = false;
        mirnaSpeciesIsModified = false;
        mirbaseAccessionIsModified = false;
        mirnaSequenceIsModified = false;
        mirnaNameIsModified = false;
        mirnaIdIsModified = false;
    }

    /**
     * Copies the passed bean into the current bean.
     *
     * @param bean the bean to copy into the current bean
     */
    public void copy(MirnaBean bean)
    {
        setFamilyRef(bean.getFamilyRef());
        setMirnaSpecies(bean.getMirnaSpecies());
        setMirbaseAccession(bean.getMirbaseAccession());
        setMirnaSequence(bean.getMirnaSequence());
        setMirnaName(bean.getMirnaName());
        setMirnaId(bean.getMirnaId());
    }

    /**
     * return a dictionnary of the object
     */
    public Map<String,String> getDictionnary()
    {
        Map<String,String> dictionnary = new HashMap<String,String>();
		dictionnary.put("family_ref", getFamilyRef() == null ? "" : getFamilyRef().toString());
        dictionnary.put("mirna_species", getMirnaSpecies() == null ? "" : getMirnaSpecies().toString());
        dictionnary.put("mirbase_accession", getMirbaseAccession() == null ? "" : getMirbaseAccession().toString());
        dictionnary.put("mirna_sequence", getMirnaSequence() == null ? "" : getMirnaSequence().toString());
        dictionnary.put("mirna_name", getMirnaName() == null ? "" : getMirnaName().toString());
        dictionnary.put("mirna_id", getMirnaId() == null ? "" : getMirnaId().toString());
        return dictionnary;
    }

    /**
     * return a dictionnary of the pk columns
     */
    public Map<String,String> getPkDictionnary()
    {
        Map<String,String> dictionnary = new HashMap<String,String>();
        dictionnary.put("mirna_id", getMirnaId() == null ? "" : getMirnaId().toString());
        return dictionnary;
    }

    /**
     * return a the value string representation of the given field
     */
    public String getValue(String column)
    {
        if (null == column || "".equals(column)) {
            return "";
        } else if ("family_ref".equalsIgnoreCase(column) || "familyRef".equalsIgnoreCase(column)) {
            return getFamilyRef() == null ? "" : getFamilyRef().toString();
        } else if ("mirna_species".equalsIgnoreCase(column) || "mirnaSpecies".equalsIgnoreCase(column)) {
            return getMirnaSpecies() == null ? "" : getMirnaSpecies().toString();
        } else if ("mirbase_accession".equalsIgnoreCase(column) || "mirbaseAccession".equalsIgnoreCase(column)) {
            return getMirbaseAccession() == null ? "" : getMirbaseAccession().toString();
        } else if ("mirna_sequence".equalsIgnoreCase(column) || "mirnaSequence".equalsIgnoreCase(column)) {
            return getMirnaSequence() == null ? "" : getMirnaSequence().toString();
        } else if ("mirna_name".equalsIgnoreCase(column) || "mirnaName".equalsIgnoreCase(column)) {
            return getMirnaName() == null ? "" : getMirnaName().toString();
        } else if ("mirna_id".equalsIgnoreCase(column) || "mirnaId".equalsIgnoreCase(column)) {
            return getMirnaId() == null ? "" : getMirnaId().toString();
        }
        return "";
    }

    /**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object)
	{
		if (!(object instanceof MirnaBean)) {
			return false;
		}

		MirnaBean obj = (MirnaBean) object;
		return new EqualsBuilder()
            .append(getFamilyRef(), obj.getFamilyRef())
            .append(getMirnaSpecies(), obj.getMirnaSpecies())
            .append(getMirbaseAccession(), obj.getMirbaseAccession())
            .append(getMirnaSequence(), obj.getMirnaSequence())
            .append(getMirnaName(), obj.getMirnaName())
            .append(getMirnaId(), obj.getMirnaId())
            .isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		return new HashCodeBuilder(-82280557, -700257973)
            .append(getFamilyRef())
            .append(getMirnaSpecies())
            .append(getMirbaseAccession())
            .append(getMirnaSequence())
            .append(getMirnaName())
            .append(getMirnaId())
            .toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
	    return toString(ToStringStyle.MULTI_LINE_STYLE);
	}

	/**
	 * you can use the following styles:
	 * <li>ToStringStyle.DEFAULT_STYLE</li>
	 * <li>ToStringStyle.MULTI_LINE_STYLE</li>
     * <li>ToStringStyle.NO_FIELD_NAMES_STYLE</li>
     * <li>ToStringStyle.SHORT_PREFIX_STYLE</li>
     * <li>ToStringStyle.SIMPLE_STYLE</li>
	 */
	public String toString(ToStringStyle style) {
		return new ToStringBuilder(this, style)
            .append("family_ref", getFamilyRef())
            .append("mirna_species", getMirnaSpecies())
            .append("mirbase_accession", getMirbaseAccession())
            .append("mirna_sequence", getMirnaSequence())
            .append("mirna_name", getMirnaName())
            .append("mirna_id", getMirnaId())
            .toString();
	}


    public int compareTo(Object object)
    {
        MirnaBean obj = (MirnaBean) object;
        return new CompareToBuilder()
            .append(getFamilyRef(), obj.getFamilyRef())
            .append(getMirnaSpecies(), obj.getMirnaSpecies())
            .append(getMirbaseAccession(), obj.getMirbaseAccession())
            .append(getMirnaSequence(), obj.getMirnaSequence())
            .append(getMirnaName(), obj.getMirnaName())
            .append(getMirnaId(), obj.getMirnaId())
            .toComparison();
   }
    
    
    public String toStringFasta(){
	return new StringBuilder()
	.append(">")
	.append(getMirnaSpecies())
	.append("-")
	.append(getMirnaName())
	.append(" ")
	.append(getMirbaseAccession())
	.append("\n")
	.append(getMirnaSequence())
	.toString();
    }
}
