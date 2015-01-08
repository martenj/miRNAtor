/**
 * 
 */
package mirnator.constants;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 * @author mjaeger TODO completiere das hier
 */
public interface CodonConstants {

	final HashMap<String, ArrayList<String>> aa2codons = new HashMap<String, ArrayList<String>>() {
		{
			put("Ala", new ArrayList<String>() {
				{
					add("GCU");
					add("GCC");
					add("GCA");
					add("GCG");
				}
			});
			put("Leu", new ArrayList<String>() {
				{
					add("UUA");
					add("UUG");
					add("CUU");
					add("CUC");
					add("CUA");
					add("CUG");
				}
			});
			put("Arg", new ArrayList<String>() {
				{
					add("CGU");
					add("CGC");
					add("CGA");
					add("CGG");
					add("AGA");
					add("AGG");
				}
			});
			put("Lys", new ArrayList<String>() {
				{
					add("AAA");
					add("AAG");
				}
			});
			put("Asn", new ArrayList<String>() {
				{
					add("AAU");
					add("AAC");
				}
			});
			put("Met", new ArrayList<String>() {
				{
					add("AUG");
				}
			});
			put("Asp", new ArrayList<String>() {
				{
					add("GAU");
					add("GAC");
				}
			});
			put("Phe", new ArrayList<String>() {
				{
					add("UUU");
					add("UUC");
				}
			});
			put("Cys", new ArrayList<String>() {
				{
					add("UGU");
					add("UGC");
				}
			});
			put("Pro", new ArrayList<String>() {
				{
					add("CCU");
					add("CCC");
					add("CCA");
					add("CCG");
				}
			});
			put("Gln", new ArrayList<String>() {
				{
					add("CAA");
					add("CAG");
				}
			});
			put("Ser", new ArrayList<String>() {
				{
					add("UCU");
					add("UCC");
					add("UCA");
					add("UCG");
					add("AGU");
					add("AGC");
				}
			});
			put("Glu", new ArrayList<String>() {
				{
					add("GAA");
					add("GAG");
				}
			});
			put("Thr", new ArrayList<String>() {
				{
					add("ACU");
					add("ACC");
					add("ACA");
					add("ACG");
				}
			});
			put("Gly", new ArrayList<String>() {
				{
					add("GGU");
					add("GGC");
					add("GGA");
					add("GGG");
				}
			});
			put("Trp", new ArrayList<String>() {
				{
					add("UGG");
				}
			});
			put("His", new ArrayList<String>() {
				{
					add("CAU");
					add("CAC");
				}
			});
			put("Tyr", new ArrayList<String>() {
				{
					add("UAU");
					add("UAC");
				}
			});
			put("Ile", new ArrayList<String>() {
				{
					add("AUU");
					add("AUC");
					add("AUA");
				}
			});
			put("Val", new ArrayList<String>() {
				{
					add("GUU");
					add("GUC");
					add("GUA");
					add("GUG");
				}
			});
			put("Sec", new ArrayList<String>() {
				{
					add("UGA");
				}
			});
			put("Pyl", new ArrayList<String>() {
				{
					add("UAG");
				}
			});
			put("Tyl", new ArrayList<String>() {
				{
					add("UAG");
				}
			});
			put("START", new ArrayList<String>() {
				{
					add("AUG");
				}
			});
			put("STOP", new ArrayList<String>() {
				{
					add("UAA");
					add("UGA");
					add("UAG");
				}
			});
		}
	};

	final HashMap<String, ArrayList<String>> codon2aa = new HashMap<String, ArrayList<String>>() {
		{
			put("GCU", new ArrayList<String>() {
				{
					add("Ala");
				}
			});
			put("GCC", new ArrayList<String>() {
				{
					add("Ala");
				}
			});
			put("GCA", new ArrayList<String>() {
				{
					add("Ala");
				}
			});
			put("GCG", new ArrayList<String>() {
				{
					add("Ala");
				}
			});
		}
	};

}
