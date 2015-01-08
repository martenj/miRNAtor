/**
 * 
 */
package mirnator.constants;

import java.util.HashMap;

/**
 * @author mjaeger
 *
 */
public interface TargetScanConstants {

	final String targetscanPath = "/media/data/data/TargetScan/";

	final String predictedTargets = "Predicted_Targets_Info.txt";
	final String conservedFamily = "Conserved_Family_Info.txt";
	final String nonconservedFamily = "Nonconserved_Family_Info.txt";

	final HashMap<String, String> releaseTable = new HashMap<String, String>() {
		{
			put("mmu52", "Mouse5.2/");
			put("hsa52", "Human5.2/");
			put("mmu60", "Mouse6.0/");
			put("hsa60", "Human6.0/");
		}

	};

}
