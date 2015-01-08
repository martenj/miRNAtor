/**
 * 
 */
package mirnator.parser.mirna.targets;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import mirnator.constants.MREtypeConstant;
import mirnator.sql2java.KnowngeneBean;
import mirnator.sql2java.KnowngeneManager;
import mirnator.sql2java.MirnaBean;
import mirnator.sql2java.MirnaManager;
import mirnator.sql2java.MreBean;
import mirnator.sql2java.MreManager;
import mirnator.sql2java.exception.DAOException;

/**
 * @author mjaeger
 *
 */
public class MREdatabaseParser extends MiRNAParser {

	// private static final Logger logger = Logger.getLogger(MREdatabaseParser.class.getSimpleName());
	private static final Logger logger = Logger.getLogger(MREdatabaseParser.class.getSimpleName());

	// protected int location;
	// private int n_mre;
	private int max_mre_id;
	private int n_fetch = 1700000;
	private short[][] cdsMREs;
	private short[][] utr5MREs;

	private Vector<Gene> mRNAs;

	private MreManager mreMan = MreManager.getInstance();
	private MirnaManager mirnaMan = MirnaManager.getInstance();
	private KnowngeneManager kgMan = KnowngeneManager.getInstance();
	private MreBean mreTemplate;
	// private MirnaBean mirnaTemplate;
	// private KnowngeneBean kgTemplate;

	// testing
	private int mb = 1024 * 1024;
	private Runtime runtime = Runtime.getRuntime();

	// using prepared statements
	private static Connection connection = null;
	private static Statement stmt;

	private String query;
	private String user = "mjaeger";
	private String password = "mjaeger";

	private PreparedStatement query_MREs;

	// private static int POS_MIRNA_REF = 1;
	// private static int POS_KG_REF = 2;
	// private static int POS_MRE_TYPE_REF = 3;
	// private static int POS_KG_START = 4;
	// private static int POS_CONSERVATION = 5;
	// private static int POS_FREEENERGY = 6;

	private static int POS_MIRNA_REF = 1;
	private static int POS_KG_REF = 2;
	private static int POS_MRE_TYPE_REF = 3;
	private static int POS_STRAND = 4;
	private static int POS_CHR_START = 5;
	private static int POS_CHR_END = 6;
	private static int POS_CONSERVATION = 7;
	private static int POS_FREEENERGY = 8;
	private static int POS_CODON1 = 9;
	private static int POS_CODON2 = 10;
	private static int POS_CODON3 = 11;
	private static int POS_CODON1_AA = 12;
	private static int POS_CODON2_AA = 13;
	private static int POS_CODON3_AA = 14;

	// MRE selection

	private boolean useFiveUTR;
	private boolean useCDS;
	private boolean useThreeUTR;
	private boolean useCodon;

	private double maxFreeEnergy;
	private double minConservation;
	private double minCodonFreq;
	private double maxCodonFreq;

	private int minMREconstraint = 0;
	private int mreGroup = 0;

	/**
	 * default constructor considering all MREs (CDS and 3'UTR)
	 * 
	 * @param path
	 *            path to the MiRnator database
	 */
	public MREdatabaseParser(String path) {
		this(path, true, true, true);
	}

	public MREdatabaseParser(String path, boolean useFiveUTR, boolean useCDS, boolean useThreeUTR) {
		this(path, useFiveUTR, useCDS, useThreeUTR, Integer.MIN_VALUE, Integer.MAX_VALUE);
		// super(path);
		// this.useFiveUTR = useFiveUTR;
		// this.useCDS = useCDS;
		// this.useThreeUTR = useThreeUTR;
		// this.mRNAs = new Vector<Gene>();
		// init();
	}

	public MREdatabaseParser(String path, boolean useFiveUTR, boolean useCDS, boolean useThreeUTR, int mreConstrait) {
		this(path, useFiveUTR, useCDS, useThreeUTR, Integer.MIN_VALUE, Integer.MAX_VALUE, mreConstrait);
	}

	public MREdatabaseParser(String path, boolean useFiveUTR, boolean useCDS, boolean useThreeUTR,
			double minConservation, double maxFreeEnergy) {
		this(path, useFiveUTR, useCDS, useThreeUTR, minConservation, maxFreeEnergy, -1);
		// super(path);
		// this.useFiveUTR = useFiveUTR;
		// this.useCDS = useCDS;
		// this.useThreeUTR = useThreeUTR;
		// this.minConservation = minConservation;
		// this.maxFreeEnergy = maxFreeEnergy;
		// this.mRNAs = new Vector<Gene>();
		// init();
	}

	public MREdatabaseParser(String path, boolean useFiveUTR, boolean useCDS, boolean useThreeUTR,
			double minConservation, double maxFreeEnergy, int mreConstraint) {
		this(path, useFiveUTR, useCDS, useThreeUTR, minConservation, maxFreeEnergy, mreConstraint, 0);
	}

	/**
	 * Initiates a {@link MREdatabaseParser} by connecting to the database at <code>path</code> and selecting the MREs
	 * matching the defined parameters:
	 * 
	 * @param path
	 *            - path to the data (JDBC database adress)
	 * @param useFiveUTR
	 *            - use 5'UTR MREs
	 * @param useCDS
	 *            - use CDS MREs
	 * @param useThreeUTR
	 *            - use 3'UTR MREs
	 * @param minConservation
	 *            - minimum conversation for the MREs
	 * @param maxFreeEnergy
	 *            - maximum free energy for the MREs
	 * @param mreConstraint
	 *            - MRE constraints (number MREs, distance)
	 * @param mreGroup
	 *            - MREtype constraint (match 8,7,6)
	 */
	public MREdatabaseParser(String path, boolean useFiveUTR, boolean useCDS, boolean useThreeUTR,
			double minConservation, double maxFreeEnergy, int mreConstraint, int mreGroup) {
		super(path);
		this.useFiveUTR = useFiveUTR;
		this.useCDS = useCDS;
		this.useThreeUTR = useThreeUTR;
		this.minConservation = minConservation;
		this.maxFreeEnergy = maxFreeEnergy;
		this.mRNAs = new Vector<Gene>();
		this.minMREconstraint = mreConstraint;
		this.mreGroup = mreGroup;
		printSettings();
		init();
	}

	public MREdatabaseParser(String path, int mreConstraint, int mreGroup, double minCodonFreq, double maxCodonFreq) {
		super(path);
		this.useCodon = true;
		this.useFiveUTR = true;
		this.useCDS = true;
		this.useThreeUTR = true;
		this.mRNAs = new Vector<Gene>();
		this.minMREconstraint = mreConstraint;
		this.mreGroup = mreGroup;
		printSettings();
		init();
	}

	private void printSettings() {
		System.out.println("The settings are: \n" + "\t5\'UTR:\t" + this.useFiveUTR + "\n" + "\tCDS:\t" + this.useCDS
				+ "\n" + "\t3\'UTR:\t" + this.useThreeUTR + "\n" + "\tmreGroup: " + this.mreGroup + "\n"
				+ "\tno. MREs: " + this.minMREconstraint + "\n" + "\tmin_conservation: " + this.minConservation + "\n"
				+ "\tmax_energy: " + this.maxFreeEnergy);

	}

	public String toOutputString() {
		return (this.useFiveUTR + "_" + this.useCDS + "_" + this.useThreeUTR + "_" + this.mreGroup + "_"
				+ this.minMREconstraint + "_"
				+ (this.minConservation == Integer.MIN_VALUE ? "min" : this.minConservation) + "_"
				+ (this.maxFreeEnergy == Integer.MAX_VALUE ? "max" : this.maxFreeEnergy) + ".txt");
	}

	// /**
	// *
	// * @param path
	// * path to the MiRnator database
	// * @param location
	// * which MREs should be considered:<br>
	// * 3 - 5'utr<br>
	// * 4 - cds<br>
	// * 5 - 3'utr<br>
	// * other - all
	// */
	// public MREdatabaseParser(String path, int location) {
	// super(path);
	// // this.location = location;
	// this.mRNAs = new Vector<Gene>();
	// init();
	// // TODO add selection to rest of code !!!
	// }

	/**
	 * Inits the database connection and sets the fetchsize.<br>
	 * Sets up the SQL queries.
	 */
	private void init() {
		try {
			Class.forName("org.postgresql.Driver");
			// System.out.println("DB-path: "+this.path);
			// System.out.println("user: "+user);
			// System.out.println("pwrd: "+password);
			connection = DriverManager.getConnection(this.path, user, password);
			connection.setAutoCommit(false);
			stmt = connection.createStatement(
			// ResultSet.TYPE_FORWARD_ONLY,
			// ResultSet.CONCUR_READ_ONLY
					);

			// turn on cursor. fetch 5000 lines at once
			stmt.setFetchSize(5000);

			// query =
			// " SELECT mirna_ref, knowngene_ref, mre_type_ref, knowngene_start, mre_conservation_score, mre_free_energy_score "
			// + " FROM mre ";
			// query =
			// " SELECT m.mirna_ref, g.ucsc_id, m.mre_type_ref, m.knowngene_start, m.mre_conservation_score, m.mre_free_energy_score "
			// +
			// " FROM mre m, knowngene g " +
			// " WHERE m.knowngene_ref = g.gene_id";
			if (this.useCodon)
				query = " SELECT m.mirna_ref, g.ucsc_id, m.mre_type_ref, m.strand, m.chr_start, m.chr_end, m.mre_conservation_score, m.mre_free_energy_score,"
						+ " m.codon1_frequency, m.codon2_frequency, m.codon3_frequency, m.codon1_frequency_aa, m.codon2_frequency_aa, m.codon3_frequency_aa "
						+ " FROM mre m, knowngene g " + " WHERE m.knowngene_ref = g.gene_id";
			else
				query = " SELECT m.mirna_ref, g.ucsc_id, m.mre_type_ref, m.strand, m.chr_start, m.chr_end, m.mre_conservation_score, m.mre_free_energy_score "
						+ " FROM mre m, knowngene g " + " WHERE m.knowngene_ref = g.gene_id";
			this.query_MREs = connection.prepareStatement(query);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mirnator.parser.mirna.targets.MiRNAParser#parse()
	 */
	@Override
	public void parse() {

		try {
			List<MreBean> mres;
			long start = System.currentTimeMillis();
			register_miRNAs_and_mRNAs();
			long stop = System.currentTimeMillis();

			logger.info("Time needed to initialize miRNAs/mRNAs: " + ((stop - start) / 1000) + "s");

			this.miRTargetMatrix = new boolean[n_mRNA][n_miRNA];
			this.cdsMREs = new short[n_mRNA][n_miRNA];
			this.utr3MREs = new short[n_mRNA][n_miRNA];
			this.utr5MREs = new short[n_mRNA][n_miRNA];
			this.mres = new MRElist[n_mRNA][n_miRNA];

			this.mreTemplate = mreMan.loadByWhereAsList("WHERE mre_id = (SELECT MAX(mre_id) FROM mre)").get(0);
			this.max_mre_id = this.mreTemplate.getMreId();

			MRE mre;
			int mreCounter = 0;
			int mreCounterTmp0 = 0;
			int mreCounterTmp1 = 0;
			int mreCounterTmp2 = 0;
			int mreCounterTmp3 = 0;
			int mreCounterTmp4 = 0;
			int mreCounterTmp5 = 0;
			int n, m; // mRNA bzw. miRNA index

			String miRNA_ref, knowngene_ref;
			int mre_type, mre_start, mre_end;
			double mre_conservation, mre_free_energy;
			boolean strand;
			int mreGroup;

			// System.out.println("max mre_id: "+this.max_mre_id);

			// logger.info("Used memory (before processing MREs): "+(runtime.totalMemory()-runtime.freeMemory())/mb+"MB");

			// PREPARED STATEMENTS

			ResultSet result_MREs = stmt.executeQuery(query);

			start = System.currentTimeMillis();
			// int counter = 0;
			while (result_MREs.next()) {
				// if(counter++ > 100000)
				// continue;

				miRNA_ref = result_MREs.getString(MREdatabaseParser.POS_MIRNA_REF);
				knowngene_ref = result_MREs.getString(MREdatabaseParser.POS_KG_REF);
				mre_type = result_MREs.getInt(MREdatabaseParser.POS_MRE_TYPE_REF);
				strand = result_MREs.getBoolean(MREdatabaseParser.POS_STRAND);
				mre_start = result_MREs.getInt(MREdatabaseParser.POS_CHR_START);
				mre_end = result_MREs.getInt(MREdatabaseParser.POS_CHR_END);
				mre_conservation = result_MREs.getDouble(MREdatabaseParser.POS_CONSERVATION);
				mre_free_energy = result_MREs.getDouble(MREdatabaseParser.POS_FREEENERGY);

				// filter MRE type
				if (MREtypeConstant.getMreGroup(MREtypeConstant.values()[mre_type]) < this.mreGroup)
					continue;

				n = mRNA2index.get(knowngene_ref);
				m = mirna2index.get(miRNA_ref);
				// filter non-coding Genes (by CDS start==end)
				if (this.mRNAs.get(n).getCdsstart() == this.mRNAs.get(n).getCdsend())
					continue;
				mreCounterTmp0++;

				// System.out.print("Start: cds "+this.mRNAs.get(n).getCdsstart());
				// System.out.print("  3utr "+this.mRNAs.get(n).getUtr3start());
				// System.out.println("  mre "+mre_start);
				// System.out.println(this.mRNAs.get(n).getCdsend()-this.mRNAs.get(n).getCdsstart());
				// check if this MRE should actually be used
				// if(mre_start < this.mRNAs.get(n).getCdsstart() && !this.useFiveUTR)
				// continue;
				// mreCounterTmp1++;
				// if(mre_start < this.mRNAs.get(n).getUtr3start() && mre_start >= this.mRNAs.get(n).getCdsstart() &&
				// !this.useCDS)
				// continue;
				// mreCounterTmp2++;
				// if(mre_start >= this.mRNAs.get(n).getUtr3start() && !this.useThreeUTR)
				// continue;
				// mreCounterTmp3++;
				if (strand) {
					if (mre_start < this.mRNAs.get(n).getCdsstart() && !this.useFiveUTR)
						continue;
					mreCounterTmp1++;
					if (mre_start < this.mRNAs.get(n).getCdsend() && mre_start >= this.mRNAs.get(n).getCdsstart()
							&& !this.useCDS)
						continue;
					mreCounterTmp2++;
					if (mre_start >= this.mRNAs.get(n).getCdsend() && !this.useThreeUTR)
						continue;
					mreCounterTmp3++;
				} else {
					// if(mre_start > this.mRNAs.get(n).getCdsend() && !this.useFiveUTR)
					// continue;
					// mreCounterTmp1++;
					// if(mre_start <= this.mRNAs.get(n).getCdsend() && mre_start > this.mRNAs.get(n).getCdsstart() &&
					// !this.useCDS)
					// continue;
					// mreCounterTmp2++;
					// if(mre_start <= this.mRNAs.get(n).getCdsstart() && !this.useThreeUTR)
					// continue;
					// mreCounterTmp3++;

					if (mre_end > this.mRNAs.get(n).getCdsend() && !this.useFiveUTR)
						continue;
					mreCounterTmp1++;
					if (mre_end > this.mRNAs.get(n).getCdsstart() && mre_end <= this.mRNAs.get(n).getCdsend()
							&& !this.useCDS)
						continue;
					mreCounterTmp2++;
					if (mre_end <= this.mRNAs.get(n).getCdsstart() && !this.useThreeUTR)
						continue;
					mreCounterTmp3++;
				}
				if (this.minConservation > mre_conservation)
					continue;
				mreCounterTmp4++;
				if (this.maxFreeEnergy < mre_free_energy)
					continue;
				mreCounterTmp5++;

				// überhaupt
				this.miRTargetMatrix[n][m] = true;
				if (strand) {
					// 5'utr
					if (mre_start < this.mRNAs.get(n).getCdsstart())
						this.utr5MREs[n][m]++;
					// cds
					else if (mre_start < this.mRNAs.get(n).getCdsend())
						this.cdsMREs[n][m]++;
					// 3'utr
					else
						this.utr3MREs[n][m]++;
				} else {
					// 3'utr
					if (mre_end <= this.mRNAs.get(n).getCdsstart())
						this.utr3MREs[n][m]++;
					// cds
					else if (mre_end <= this.mRNAs.get(n).getCdsend())
						this.cdsMREs[n][m]++;
					// 5'utr
					else
						this.utr5MREs[n][m]++;
					// // 3'utr
					// if(mre_start <= this.mRNAs.get(n).getCdsstart())
					// this.utr3MREs[n][m]++;
					// // cds
					// else if(mre_start <= this.mRNAs.get(n).getCdsend())
					// this.cdsMREs[n][m]++;
					// // 5'utr
					// else
					// this.utr5MREs[n][m]++;
				}

				// MREs
				if (this.mres[n][m] == null)
					this.mres[n][m] = new MRElist();

				mre = new MRE();
				mre.setType(mre_type);
				mre.setPosition(mre_start);
				this.mres[n][m].addMRE(mre);

				if (mreCounterTmp0 % this.n_fetch == 0) {
					stop = System.currentTimeMillis();
					System.out
							.println("Time used to fetch " + this.n_fetch + " mres: " + ((stop - start) / 1000) + "s");
					start = System.currentTimeMillis();
				}
			}

			// logger.info("Used memory (after processing MREs): "+(runtime.totalMemory()-runtime.freeMemory())/mb+"MB");
			logger.info("Found and initialized " + mreCounterTmp5 + "(" + mreCounterTmp0 + "|" + mreCounterTmp1 + "|"
					+ mreCounterTmp2 + "|" + mreCounterTmp3 + "|" + mreCounterTmp4 + ") MREs.");
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Used memory (after counting): " + (runtime.totalMemory() - runtime.freeMemory()) / mb + "MB");
	}

	/**
	 * This method counts the number of miRNAs and mRNAs and initializes the HashMaps that will contain the indices of
	 * the miRNA/mRNAs and their names as Strings.
	 * 
	 * @throws DAOException
	 *             TODO while registering the mRNAs -> change GeneSymbol to real Genesymbol not UCSCid (using xRefTable)
	 */
	private void register_miRNAs_and_mRNAs() throws DAOException {

		int currentMicroRNAIndex = 0;
		int currentTranscriptIndex = 0;
		// System.out.println("Used memory (before parsing): "+(runtime.totalMemory()-runtime.freeMemory())/mb+"MB");
		this.n_miRNA = mirnaMan.countAll();
		this.n_mRNA = kgMan.countAll();
		logger.info("Used memory (after counting): " + (runtime.totalMemory() - runtime.freeMemory()) / mb);

		// register mirna
		MirnaBean[] mirnas = mirnaMan.loadAll();

		for (MirnaBean mirnaBean : mirnas) {
			this.mirna2index.put(mirnaBean.getMirnaId().toString(), currentMicroRNAIndex);
			this.index2mirna.put(currentMicroRNAIndex, mirnaBean.getMirnaId().toString());
			currentMicroRNAIndex++;
		}
		logger.info("Used memory (after registering miRNAs): " + (runtime.totalMemory() - runtime.freeMemory()) / mb
				+ "MB");

		// register mRNAs
		List<KnowngeneBean> knowngenes = kgMan.loadAllAsList();

		for (KnowngeneBean knowngeneBean : knowngenes) {
			// this.mRNA2index.put(knowngeneBean.getGeneId().toString(), currentTranscriptIndex);
			// this.index2mRNA.put(currentTranscriptIndex, knowngeneBean.getGeneId().toString());
			// this.index2GeneSymbol.put(currentTranscriptIndex, knowngeneBean.getGeneId().toString());
			this.mRNA2index.put(knowngeneBean.getUcscId(), currentTranscriptIndex);
			this.index2mRNA.put(currentTranscriptIndex, knowngeneBean.getUcscId());
			this.index2GeneSymbol.put(currentTranscriptIndex, knowngeneBean.getUcscId());
			Gene gen = new Gene();
			// gen.setCdsstart(knowngeneBean.getCdsStartmRNA());
			// gen.setUtr3start(knowngeneBean.getCdsEndmRNA()+1);
			gen.setCdsstart(knowngeneBean.getCdsStart());
			gen.setCdsend(knowngeneBean.getCdsEnd());
			this.mRNAs.add(currentTranscriptIndex, gen);
			currentTranscriptIndex++;
		}
		logger.info("Used memory (after registering mRNAs): " + (runtime.totalMemory() - runtime.freeMemory()) / mb
				+ "MB");
		logger.info("Finished registering " + mirnas.length + " miRNAs and " + knowngenes.size() + " transcripts.");
	}

	/**
	 * Returns all the mRNAs (by GeneSymbol) that are targeted by a miRNA. <br>
	 * If the <code>minMREconstrain</code> is set it's also considered.
	 * 
	 * @param miRNA
	 *            - name of microRNA (mmu-miR-29a)
	 * @return List of target genes (empty when miRNA is unknown)
	 */
	public HashSet<String> getAllTarget_GeneSymbols(String miRNA) {
		return this.getAllTarget_GeneSymbols(miRNA, this.minMREconstraint);
	}

	/**
	 * Returns all the mRNAs (by GeneSymbol) that are targeted by a miRNA. This method returns the target genes for a
	 * given miRNA, satisfying the additional condition: depending on c It is known that multiple sites have additive or
	 * cooperative effects, especially if located within about 40 nucleotides (nt), but no closer than 8 nt to one
	 * another.<br>
	 * IF (c < 0) the exact number of |c| MREs<br>
	 * IF (0 < c < 9) - number of MREs is considered<br>
	 * IF (8 < c < n) - distance between MREs is considered<br>
	 * ELSE all targets<br>
	 * 
	 * @param miRNA
	 *            - name of microRNA (mmu-miR-29a)
	 * @param c
	 *            - number of MREs/minimal distance
	 * @return List of target genes, satisfying the given conditions (empty when miRNA is unknown)
	 */
	public HashSet<String> getAllTarget_GeneSymbols(String miRNA, int c) {
		HashSet<String> hs = new HashSet<String>();
		if (mirna2index == null)
			System.out.println("mirna2index is null");
		int n, m;
		String target;
		if (!mirna2index.containsKey(miRNA))
			return hs;
		m = mirna2index.get(miRNA); /* The crrect column for this miR */
		for (n = 0; n < n_mRNA; ++n) {
			if (miRTargetMatrix[n][m]) {
				target = index2GeneSymbol.get(n);
				if (c > 0 && c < 9) {
					if (this.mres[n][m].mres.size() >= c)
						hs.add(target.toLowerCase());
				} else if (c > 8) {
					// check for distance between MREs
					// MRElist mre = this.mres[n][m];
					if ((this.mres[n][m].mres.size() > 1) && checkMREDistance(c, this.mres[n][m])) {
						hs.add(target.toLowerCase());
					}
				} else {
					if (c == 0)
						hs.add(target.toLowerCase());
					else {
						if (this.mres[n][m].mres.size() == -c)
							hs.add(target.toLowerCase());
					}
				}
			}
		}
		return hs;
	}

	/**
	 * Returns all mRNAs (by GeneSymbol), that are not targeted by the given miRNA.
	 * 
	 * @param miRNA
	 *            name of themicroRNA (mmu-miR-29a)
	 * @param utr5
	 *            should the 5'UTR be considered
	 * @param cds
	 *            should the CDS be considered
	 * @param utr3
	 *            should the 3'UTR be considered
	 * @return List of target genes, satisfying the given conditions (empty when miRNA is unknown)
	 */
	public HashSet<String> getAllNonTargets(String miRNA, boolean utr5, boolean cds, boolean utr3) {
		HashSet<String> hs = new HashSet<String>();
		if (mirna2index == null)
			System.out.println("mirna2index is null");
		int n, m;
		String target;
		if (!mirna2index.containsKey(miRNA))
			return hs;
		m = mirna2index.get(miRNA); /* The crrect column for this miR */
		for (n = 0; n < n_mRNA; ++n) {
			if (utr5 && cds && utr3) {
				if (this.mres[n][m] == null) {
					target = index2GeneSymbol.get(n);
					hs.add(target.toLowerCase());
					continue;
				}

			}

			if (utr5 && utr5MREs[n][m] != 0)
				continue;
			if (cds && cdsMREs[n][m] != 0)
				continue;
			if (utr3 && utr3MREs[n][m] != 0)
				continue;

			target = index2GeneSymbol.get(n);
			hs.add(target.toLowerCase());
		}
		return hs;
	}

	// /**
	// * This method returns the target genes for a given miRNA, satisfying the additional condition: depending on c
	// * It is known that multiple sites have additive or cooperative effects, especially
	// * if located within about 40 nucleotides (nt), but no closer than 8 nt to one another.<br>
	// * IF (0 < c < 9) - number of MREs in 3'UTR is considered<br>
	// * IF (8 < c < n) - distance between MREs is considered<br>
	// * ELSE all targets<br>
	// *
	// * @param miRNA - name of microRNA (mmu-miR-29a)
	// * @param c - number of MREs/minimal distance
	// * @param only3utr - return genes with only mre hits in the 3'UTR
	// * @return List of target genes, satisfying the given conditions (empty when miRNA is unknown)
	// */
	// public HashSet<String> getAllTarget_3utr_GeneSymbols(String miRNA, int c, boolean only3utr) {
	// return
	// }
	public HashSet<String> getAllTarget_cds_GeneSymbols(String miRNA) {
		HashSet<String> hs = new HashSet<String>();
		if (mirna2index == null)
			System.out.println("mirna2index is null");
		int n, m;
		if (!mirna2index.containsKey(miRNA))
			return hs;
		m = mirna2index.get(miRNA); /* The correct column for this miR */
		for (n = 0; n < n_mRNA; ++n) {
			if (this.cdsMREs[n][m] > 0) {
				String target = index2GeneSymbol.get(n);
				hs.add(target.toLowerCase());
			}
		}
		return hs;
	}

	/**
	 * This method returns the target genes for a given miRNA, satisfying the additional condition: depending on c It is
	 * known that multiple sites have additive or cooperative effects, especially if located within about 40 nucleotides
	 * (nt), but no closer than 8 nt to one another. IF (0 < c < 9) - number of MREs in cds is considered IF (8 < c <
	 * 41) - distance between MREs is considered ELSE all targets
	 * 
	 * @param miRNA
	 *            - name of microRNA (mmu-miR-29a)
	 * @param c
	 *            - number of MREs/minimal distance
	 * @return List of target genes, full satisfying the given conditions
	 */
	public HashSet<String> getAllTarget_cds_GeneSymbols(String miRNA, int c) {
		return (this.getAllTarget_cds_GeneSymbols(miRNA, c, false));
	}

	/**
	 * This method returns the target genes for a given miRNA, satisfying the additional condition: depending on c It is
	 * known that multiple sites have additive or cooperative effects, especially if located within about 40 nucleotides
	 * (nt), but no closer than 8 nt to one another. IF (0 < c < 9) - number of MREs in cds is considered IF (8 < c <
	 * 41) - distance between MREs is considered ELSE all targets.<br>
	 * 
	 * @param miRNA
	 * @param c
	 * @param useCDSonly
	 * @return
	 */
	public HashSet<String> getAllTarget_cds_GeneSymbols(String miRNA, int c, boolean useCDSonly) {
		HashSet<String> hs = new HashSet<String>();
		if (mirna2index == null)
			System.out.println("mirna2index is null");
		int n, m;
		if (!mirna2index.containsKey(miRNA))
			return hs;
		m = mirna2index.get(miRNA); /* The correct column for this miR */

		String target;
		MRElist mre;
		Gene gene;
		for (n = 0; n < n_mRNA; ++n) {
			if (this.cdsMREs[n][m] > 0) {
				if (useCDSonly) {
					if (this.utr5MREs[n][m] > 0)
						continue;
					if (this.utr3MREs[n][m] > 0)
						continue;
				}
				target = index2GeneSymbol.get(n);
				gene = mRNAs.get(n);
				// TODO check for C
				if (c > 0 & c < 9) {
					if (this.cdsMREs[n][m] >= c)
						hs.add(target.toLowerCase());
				} else if (c > 0 & c < 41) {
					// check for distance between MREs
					mre = this.mres[n][m];
					if ((this.cdsMREs[n][m] > 1) && checkCdsMreDistance(c, gene, mre)) {
						hs.add(target.toLowerCase());
					}
				} else
					hs.add(target.toLowerCase());
			}
		}
		return hs;
	}

	/**
	 * Returns all the mRNAs (by GeneSymbol) that are targeted by a miRNA. This method returns the target genes for a
	 * given miRNA, satisfying the additional condition: depending on c It is known that multiple sites have additive or
	 * cooperative effects, especially if located within about 40 nucleotides (nt), but no closer than 8 nt to one
	 * another.<br>
	 * IF (0 < c < 9) - number of MREs in 3'UTR is considered<br>
	 * IF (8 < c < n) - distance between MREs is considered<br>
	 * ELSE all targets<br>
	 * 
	 * @param miRNA
	 *            - name of microRNA (mmu-miR-29a)
	 * @param c
	 *            - number of MREs/minimal distance
	 * @return List of target genes, satisfying the given conditions (empty when miRNA is unknown)
	 */
	public HashSet<String> getAllTarget_5utr_GeneSymbols(String miRNA, int c, boolean use5UTRonly) {
		HashSet<String> hs = new HashSet<String>();
		if (mirna2index == null)
			System.out.println("mirna2index is null");
		int n, m;
		if (!mirna2index.containsKey(miRNA))
			return hs;
		m = mirna2index.get(miRNA); /* The crrect column for this miR */

		MRElist mre;
		String target;

		for (n = 0; n < n_mRNA; ++n) {
			if (this.utr5MREs[n][m] > 0) {
				if (use5UTRonly) {
					if (this.utr3MREs[n][m] > 0)
						continue;
					if (this.cdsMREs[n][m] > 0)
						continue;
				}
				target = index2GeneSymbol.get(n);
				if (c > 0 && c < 9) {
					if (this.utr5MREs[n][m] >= c)
						hs.add(target.toLowerCase());
				} else if (c > 8) {
					// check for distance between MREs
					mre = this.mres[n][m];
					if ((this.utr5MREs[n][m] > 1) && check5utrMreDistance(c, mRNAs.get(n), mre)) {
						hs.add(target.toLowerCase());
					}
				} else
					hs.add(target.toLowerCase());
			}
		}
		return hs;
	}

	/**
	 * Returns all the mRNAs (by GeneSymbol) that are targeted by a miRNA. This method returns the target genes for a
	 * given miRNA, satisfying the additional condition: depending on c It is known that multiple sites have additive or
	 * cooperative effects, especially if located within about 40 nucleotides (nt), but no closer than 8 nt to one
	 * another.<br>
	 * IF (0 < c < 9) - number of MREs in 3'UTR is considered<br>
	 * IF (8 < c < n) - distance between MREs is considered<br>
	 * ELSE all targets<br>
	 * 
	 * @param miRNA
	 *            - name of microRNA (mmu-miR-29a)
	 * @param c
	 *            - number of MREs/minimal distance
	 * @return List of target genes, satisfying the given conditions (empty when miRNA is unknown)
	 */
	public HashSet<String> getAllTarget_3utr_GeneSymbols(String miRNA, int c, boolean use3UTRonly) {
		HashSet<String> hs = new HashSet<String>();
		if (mirna2index == null)
			System.out.println("mirna2index is null");
		int n, m;
		if (!mirna2index.containsKey(miRNA))
			return hs;
		m = mirna2index.get(miRNA); /* The crrect column for this miR */

		MRElist mre;
		String target;

		for (n = 0; n < n_mRNA; ++n) {
			if (this.utr3MREs[n][m] > 0) {
				if (use3UTRonly) {
					if (this.utr5MREs[n][m] > 0)
						continue;
					if (this.cdsMREs[n][m] > 0)
						continue;
				}
				target = index2GeneSymbol.get(n);
				if (c > 0 && c < 9) {
					if (this.utr3MREs[n][m] >= c)
						hs.add(target.toLowerCase());
				} else if (c > 8) {
					// check for distance between MREs
					mre = this.mres[n][m];
					if ((this.utr3MREs[n][m] > 1) && check3utrMreDistance(c, mRNAs.get(n), mre)) {
						hs.add(target.toLowerCase());
					}
				} else
					hs.add(target.toLowerCase());
			}
		}
		return hs;
	}

	/**
	 * Checks if there is another MRE within the max. distance on the same gene.
	 * 
	 * @param c
	 *            - max. distance
	 * @param gene
	 *            - the gene
	 * @param mre
	 *            - list with MREs located on the gene
	 * @return <code>true</code> if there is another MRE within the max. distance, otherwise <code>false</code>
	 */
	private boolean checkCdsMreDistance(int c, Gene gene, MRElist mre) {
		int start = gene.getCdsstart();
		int end = gene.getCdsend();
		int[] positions = mre.getPositions();
		for (int i = 0; i < positions.length; i++) {
			if (positions[i] > start && positions[i] < end) {
				for (int j = i + 1; j < positions.length; j++) {
					if (positions[j] > end)
						break;
					int dist = positions[j] - positions[i];
					if ((dist > 8) && (dist <= c))
						return true;
				}
			}
		}
		return false;
	}

	/**
	 * Checks if there is another MRE within the max. distance on the same gene.
	 * 
	 * @param c
	 *            - max. distance
	 * @param gene
	 *            - the gene
	 * @param mre
	 *            - list with MREs located on the gene
	 * @return <code>true</code> if there is another MRE within the max. distance, otherwise <code>false</code>
	 */
	private boolean check3utrMreDistance(int c, Gene gene, MRElist mre) {
		// int start = gene.getCdsstart();
		int end = gene.getCdsend();
		int[] positions = mre.getPositions();
		for (int i = 0; i < positions.length; i++) {
			if (positions[i] >= end) {
				for (int j = i + 1; j < positions.length; j++) {
					int dist = positions[j] - positions[i];
					if ((dist > 8) && (dist <= c))
						return true;
				}
			}
		}
		return false;
	}

	/**
	 * Checks if there is another MRE within the max. distance on the same gene.
	 * 
	 * @param c
	 *            - max. distance
	 * @param gene
	 *            - the gene
	 * @param mre
	 *            - list with MREs located on the gene
	 * @return <code>true</code> if there is another MRE within the max. distance, otherwise <code>false</code>
	 */
	private boolean check5utrMreDistance(int c, Gene gene, MRElist mre) {
		int end = gene.getCdsstart();
		int[] positions = mre.getPositions();
		for (int i = 0; i < positions.length; i++) {
			if (positions[i] < end) {
				for (int j = i + 1; j < positions.length; j++) {
					int dist = positions[j] - positions[i];
					if ((dist > 8) && (dist <= c))
						return true;
				}
			}
		}
		return false;
	}

}
