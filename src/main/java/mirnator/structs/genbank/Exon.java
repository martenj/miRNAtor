package mirnator.structs.genbank;

public class Exon {
	private int start;
	private int end;

	public Exon(int start, int end) {
		this.start = start;
		this.end = end;
	}

	public int length() {
		return end - start + 1;
	}

	public int get_start() {
		return start;
	}

	public int get_end() {
		return end;
	}
}
