package mirnator.structs.mirna;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class Mre {
	private String microrna;
	private String mrna;
	private int mir_start;
	private int mir_end;
	private int mrna_start;
	private int mrna_end;
	private int mre_type;
	private int source;
	private double conservation_score;
	private double free_energy_score;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((microrna == null) ? 0 : microrna.hashCode());
		result = prime * result + mir_end;
		result = prime * result + mir_start;
		result = prime * result + ((mrna == null) ? 0 : mrna.hashCode());
		result = prime * result + mrna_end;
		result = prime * result + mrna_start;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Mre other = (Mre) obj;
		if (microrna == null) {
			if (other.microrna != null)
				return false;
		} else if (!microrna.equals(other.microrna))
			return false;
		if (mir_end != other.mir_end)
			return false;
		if (mir_start != other.mir_start)
			return false;
		if (mrna == null) {
			if (other.mrna != null)
				return false;
		} else if (!mrna.equals(other.mrna))
			return false;
		if (mrna_end != other.mrna_end)
			return false;
		if (mrna_start != other.mrna_start)
			return false;
		return true;
	}

	/**
	 * @return the microrna
	 */
	public String getMicrorna() {
		return microrna;
	}

	/**
	 * @param microrna
	 *            the microrna to set
	 */
	public void setMicrorna(String microrna) {
		this.microrna = microrna;
	}

	/**
	 * @return the mrna
	 */
	public String getMrna() {
		return mrna;
	}

	/**
	 * @param mrna
	 *            the mrna to set
	 */
	public void setMrna(String mrna) {
		this.mrna = mrna;
	}

	/**
	 * @return the mir_start
	 */
	public int getMir_start() {
		return mir_start;
	}

	/**
	 * @param mir_start
	 *            the mir_start to set
	 */
	public void setMir_start(int mir_start) {
		this.mir_start = mir_start;
	}

	/**
	 * @return the mir_end
	 */
	public int getMir_end() {
		return mir_end;
	}

	/**
	 * @param mir_end
	 *            the mir_end to set
	 */
	public void setMir_end(int mir_end) {
		this.mir_end = mir_end;
	}

	/**
	 * @return the mrna_start
	 */
	public int getMrna_start() {
		return mrna_start;
	}

	/**
	 * @param mrna_start
	 *            the mrna_start to set
	 */
	public void setMrna_start(int mrna_start) {
		this.mrna_start = mrna_start;
	}

	/**
	 * @return the mrna_end
	 */
	public int getMrna_end() {
		return mrna_end;
	}

	/**
	 * @param mrna_end
	 *            the mrna_end to set
	 */
	public void setMrna_end(int mrna_end) {
		this.mrna_end = mrna_end;
	}

	/**
	 * @return the mre_type
	 */
	public int getMre_type() {
		return mre_type;
	}

	/**
	 * @param mre_type
	 *            the mre_type to set
	 */
	public void setMre_type(int mre_type) {
		this.mre_type = mre_type;
	}

	/**
	 * @return the source
	 */
	public int getSource() {
		return source;
	}

	/**
	 * @param source
	 *            the source to set
	 */
	public void setSource(int source) {
		this.source = source;
	}

	/**
	 * @return the conservation_score
	 */
	public double getConservation_score() {
		return conservation_score;
	}

	/**
	 * @param conservation_score
	 *            the conservation_score to set
	 */
	public void setConservation_score(double conservation_score) {
		this.conservation_score = conservation_score;
	}

	/**
	 * @return the free_energy_score
	 */
	public double getFree_energy_score() {
		return free_energy_score;
	}

	/**
	 * @param free_energy_score
	 *            the free_energy_score to set
	 */
	public void setFree_energy_score(double free_energy_score) {
		this.free_energy_score = free_energy_score;
	}

	public String toStringFlatFile(int id) {
		return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append(id).append(",", getMicrorna())
				.append(",", getMrna()).append(",", getMrna_start()).append(",", getMrna_end())
				.append(",", getMre_type()).append(",", getSource()).append(",", getMir_start())
				.append(",", getMir_end()).append(",", getConservation_score()).append(",", getFree_energy_score())
				.toString();
	}

}
