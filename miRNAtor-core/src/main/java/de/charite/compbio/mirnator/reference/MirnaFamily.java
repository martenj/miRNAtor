package de.charite.compbio.mirnator.reference;

import java.util.ArrayList;

/**
 * Simple mirna Family representation.
 * 
 *
 * @author Marten Jäger <marten.jaeger@charite.de>
 *
 */
public class MirnaFamily {
	private String name;
	private String accession;
	private ArrayList<Mirna> members;

	/**
	 * Constructs an "empty" miRNA Family with no name or accession, but inits the member ArrayList.
	 */
	public MirnaFamily() {
		this.members = new ArrayList<Mirna>();
	}

	/**
	 * Constructs a new miRNA Family with the given name, accession and inits the member ArrayList.
	 * 
	 * @param name
	 *            - the name for the miRNA Family (e.g. mir-17)
	 * @param accession
	 *            - the accession ID for this family (e.g. MIPF0000001)
	 */
	public MirnaFamily(String name, String accession) {
		this.name = name;
		this.accession = accession;
		this.members = new ArrayList<Mirna>();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the accession
	 */
	public String getAccession() {
		return accession;
	}

	/**
	 * @param accession
	 *            the accession to set
	 */
	public void setAccession(String accession) {
		this.accession = accession;
	}

	/**
	 * Returns true if this list contains the specified element. More formally, returns true if and only if this list
	 * contains at least one element e such that <code>(o==null ? e==null : o.equals(e))</code>.
	 * 
	 * @param m
	 *            - element whose presents in the member list is to be tested
	 * @return true if the member list contains the specified object
	 */
	public boolean containsMember(String m) {
		return this.members.contains(m) ? true : false;
	}

	/**
	 * Appends the specified member to the end of this list.
	 * 
	 * @param m
	 *            - member to be append to the list (e.g. MI0000071).
	 * @return <code>true</code> (as specified by Collection.add(E))
	 */
	public boolean addMember(Mirna m) {
		return this.members.add(m);
	}

	/**
	 * Removes the first occurrence of the specified member from this list, if it is present. If the list does not
	 * contain the element, it is unchanged. More formally, removes the element with the lowest index i such that
	 * <code>(o==null ? get(i)==null : o.equals(get(i)))</code> (if such an element exists). Returns true if this list
	 * contained the specified element (or equivalently, if this list changed as a result of the call).
	 * 
	 * @param m
	 *            - member to be removed from the list, if present
	 * @return <code>true</code> if this list contained the specified element
	 */
	public boolean removeMember(String m) {
		return this.members.remove(m);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MirnaFamily [accession=" + accession + ", name=" + name + "]";
	}

}
