import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TestRunDetails {

	// xml Schema field names
	public static String TESTS = "tests";
	public static String PLATFORMS = "platforms";
	public static String PLATFORMS_aix_db2 = "aix-db2";
	public static String PLATFORMS_aix_ora = "aix-ora";
	public static String PLATFORMS_rhel_ora = "rhel-ora";
	public static String PLATFORMS_rhel_db2 = "rhel-db2";
	public static String PLATFORMS_sparc_ora = "sparc-ora";
	public static String PLATFORMS_solx86_ora = "solx86-ora";
	public static String STREAMS = "streams";
	public static String STREAMS_MAN_AIX_DB2 = "streams_man_aix_db2";
	public static String STREAMS_MAN_AIX_ORA = "streams_man_aix_ora";
	public static String STREAMS_MAN_RHEL_DB2 = "streams_man_rhel_db2";
	public static String STREAMS_MAN_RHEL_ORA = "streams_man_rhel_ora";
	public static String STREAMS_MAN_SPARC_ORA = "streams_man_sparc_ora";
	public static String STREAMS_MAN_SOLX86 = "streams_man_solx86_ora";
	public static String STREAMS_ALL = "streams_all";
	public static String RUN = "run";
		
	// Private class methods
	private Map<String, String> platforms;
	private Map<String, Map<String, String>> runs = new HashMap<>();

	
	public TestRunDetails() {
		platforms = new HashMap<>();
	}

	public String getPlatform(String platform) {
		return platforms.get(platform);
	}

	public void setPlatform(String platform, String name) {
		this.platforms.put(platform, name);
	}

	/**
	 * @return the runs
	 */
	public Set<String> getRunFieldNames(String runName) {
		if(!runs.containsKey(runName))
		{
			return null;
		}
		Set<String> results = runs.get(runName).keySet();
		return results;
	}	
	
	/**
	 * @return the runs
	 */
	public String getRunValue(String runName, String field) {
		if(!runs.containsKey(runName))
		{
			return "";
		}
		Map<String, String> tempRun = runs.get(runName);
		return tempRun.get(field);
	}

	/**
	 * @param runs the runs to set
	 */
	public void setRuns(String runName, String field, String value) {
		
		if(runs.containsKey(runName)){
			Map<String, String> tempRun = runs.get(runName);
			tempRun.put(field, value);
			runs.remove(runName);
			runs.put(runName, tempRun);
		}
		else
		{
			Map<String, String> tempRun = new HashMap<>();
			tempRun.put(field, value);
			runs.put(runName, tempRun);
		}
	}
}
