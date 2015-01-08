package mirnator.constants;

public enum MREsource {
	BartelMREpredictor, FreeAdhesionEnergy, ChiMREpredictor;

	/**
	 * Returns a short indication which tool was used to identify this MRE
	 * 
	 * @param m
	 *            - the MREsource
	 * @return
	 */
	public String getSource(MREsource m) {
		String source;
		switch (m) {
		case BartelMREpredictor:
			source = "Bartel+ MREseeker";
			break;
		case ChiMREpredictor:
			source = "Chi bulged MREs";
			break;
		case FreeAdhesionEnergy:
			source = "Free adhesion energy MREseeker";
			break;

		default:
			source = "unknown source";
			break;
		}
		return source;
	}
}
