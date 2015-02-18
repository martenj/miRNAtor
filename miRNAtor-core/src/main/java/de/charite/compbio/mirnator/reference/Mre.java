/**
 * 
 */
package de.charite.compbio.mirnator.reference;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import de.charite.compbio.mirnator.constants.MREtype;

/**
 * Simple bean like MRE representation.
 *
 * @author Marten Jäger <marten.jaeger@charite.de>
 *
 */
public final class Mre {
	/** the {@link Mirna} the {@link Mre} is associated with. */
	public final Mirna mirna;
	/** the {@link SequenceModel} the MRE is associated with. */
	public final SequenceModel sequenceModel;
	/** the '0'-based start on the {@link SequenceModel} */
	public final int sequence_start;
	/** the '0'-based end on the {@link SequenceModel} */
	public final int sequence_end;
	/** the '0'-based start on the {@link Mirna} */
	public final int mirna_start;
	/** the '1'-based end on the {@link Mirna} */
	public final int mirna_end;
	/** the {@link MREtype} of the MRE */
	public final MREtype type;
	/** has 'A'/'U' binding improvement site at position 9 */
	public boolean hasPos9UA;
	/** has compensatory site */
	public boolean hasCompensatorySite;
	/**
	 * the conservation of the MRE in respect to the genomic position - only possible if the sequence is associated with
	 * a database transcript.
	 */
	public Double conservation;
	/** the free energy for the miRNA<->sequence interaction */
	public Double free_energy;

	public Mre(Mirna mirna, SequenceModel sequenceModel, int sequence_start, int sequence_end, int mirna_start,
			int mirna_end, MREtype type) {
		this(mirna, sequenceModel, sequence_start, sequence_end, mirna_start, mirna_end, type, null, null);
	}

	public Mre(Mirna mirna, SequenceModel sequenceModel, int sequence_start, int sequence_end, int mirna_start,
			int mirna_end, MREtype type, Double conservation, Double free_energy) {
		this.mirna = mirna;
		this.sequenceModel = sequenceModel;
		this.sequence_start = sequence_start;
		this.sequence_end = sequence_end;
		this.mirna_start = mirna_start;
		this.mirna_end = mirna_end;
		this.type = type;
		this.conservation = conservation;
		this.free_energy = free_energy;
	}

	// /**
	// * Returns the associated {@link Mirna}.
	// *
	// * @return the mirna
	// */
	// public Mirna getMirna() {
	// return mirna;
	// }
	//
	// /**
	// * Sets the {@link Mirna} for this {@link Mre}.
	// *
	// * @param mirna
	// * the mirna to set
	// */
	// public void setMirna(Mirna mirna) {
	// this.mirna = mirna;
	// }
	//
	// /**
	// * Returns the associated Sequence.
	// *
	// * @return the sequence
	// */
	// public Sequence getSequence() {
	// return sequence;
	// }
	//
	// /**
	// * Sets the {@link Sequence} for this {@link Mre}.
	// *
	// * @param sequence
	// * the sequence to set
	// */
	// public void setSequence(Sequence sequence) {
	// this.sequence = sequence;
	// }
	//
	// /**
	// * Returns the start position ('0'-based,inclusive) in the {@link Sequence}.
	// *
	// * @return the sequence_start
	// */
	// public int getSequence_start() {
	// return sequence_start;
	// }
	//
	// /**
	// * Sets the start position ('0'-based,inclusive) in the {@link Sequence}.
	// *
	// * @param sequence_start
	// * the sequence_start to set
	// */
	// public void setSequence_start(int sequence_start) {
	// this.sequence_start = sequence_start;
	// }
	//
	// /**
	// * Returns the end position ('0'-based, exclusive) in the {@link Sequence}.
	// *
	// * @return the sequence_end
	// */
	// public int getSequence_end() {
	// return sequence_end;
	// }
	//
	// /**
	// * Sets the end position ('0'-based, exclusive) in the {@link Sequence}.
	// *
	// * @param sequence_end
	// * the sequence_end to set
	// */
	// public void setSequence_end(int sequence_end) {
	// this.sequence_end = sequence_end;
	// }
	//
	// /**
	// * Returns the start position ('0'-based,inclusive) in the {@link Mirna}.
	// *
	// * @return the mirna_start
	// */
	// public int getMirna_start() {
	// return mirna_start;
	// }
	//
	// /**
	// * Sets the start position ('0'-based,inclusive) in the {@link Mirna}.
	// *
	// * @param mirna_start
	// * the mirna_start to set
	// */
	// public void setMirna_start(int mirna_start) {
	// this.mirna_start = mirna_start;
	// }
	//
	// /**
	// * Returns the end position ('0'-based,exclusive) in the {@link Mirna}.
	// *
	// * @return the mirna_end
	// */
	// public int getMirna_end() {
	// return mirna_end;
	// }
	//
	// /**
	// * Sets the end position ('0'-based,exclusive) in the {@link Mirna}.
	// *
	// * @param mirna_end
	// * the mirna_end to set
	// */
	// public void setMirna_end(int mirna_end) {
	// this.mirna_end = mirna_end;
	// }
	//
	// /**
	// * Returns the {@link MREtype} according to Bartel.
	// *
	// * @return the type
	// */
	// public MREtype getType() {
	// return type;
	// }
	//
	// /**
	// * Sets the {@link MREtype} according to Bartel.
	// *
	// * @param type
	// * the type to set
	// */
	// public void setType(MREtype type) {
	// this.type = type;
	// }
	//
	// /**
	// * Returns the conservation score for this {@link Mirna} <-> {@link Sequence} interaction. This value will only be
	// * present if the chromosomal {@link Sequence} information is known.
	// *
	// * @return the conservation
	// */
	// public double getConservation() {
	// return conservation;
	// }
	//
	// /**
	// * Sets the conservation score for this {@link Mirna} <-> {@link Sequence} interaction. This should only be used
	// if
	// * the chromosomal {@link Sequence} information is known.
	// *
	// * @param conservation
	// * the conservation to set
	// */
	// public void setConservation(double conservation) {
	// this.conservation = conservation;
	// }
	//
	// /**
	// * Returns the free energy for this {@link Mirna} <-> {@link Sequence} interaction.
	// *
	// * @return the free_energy
	// */
	// public double getFree_energy() {
	// return free_energy;
	// }
	//
	// /**
	// * Sets the free energy for this {@link Mirna} <-> {@link Sequence} interaction.
	// *
	// * @param free_energy
	// * the free_energy to set
	// */
	// public void setFree_energy(double free_energy) {
	// this.free_energy = free_energy;
	// }

	/**
	 * returns a Flat File styled w/o added line break:<br>
	 * <code> John Doe, 33, false</code>
	 * 
	 * @return flat string representation of the MRE
	 */
	public String toStringFlatFileSimple() {
		return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append(mirna.accession)
				.append(",", sequenceModel.accession).append(",", sequence_start).append(",", sequence_end)
				.append(",", type).append(",", mirna_start).append(",", mirna_end).toString();
	}

	/**
	 * returns a Flat File styled w/o added line break:<br>
	 * <code> John Doe, 33, false</code>
	 * 
	 * @param id
	 *            an ID of this MRE
	 * 
	 * @return flat string representation of the MRE
	 */
	public String toStringFlatFile(int id) {
		return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append(id).append(",", mirna.accession)
				.append(",", sequenceModel.accession).append(",", sequence_start).append(",", sequence_end)
				.append(",", type).append(",", mirna_start).append(",", mirna_end).append(",", free_energy)
				.append(",", conservation).append(",", hasPos9UA).append(",", hasCompensatorySite).toString();
	}
}
