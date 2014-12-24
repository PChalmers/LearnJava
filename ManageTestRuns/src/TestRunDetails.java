import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
	private Map<String, String> platforms = new HashMap<>();;
	private Map<String, Set<String>> streams = new HashMap<>();

	
	public TestRunDetails() {

	}

	public String getPlatform(String platform) {
		return platforms.get(platform);
	}
	
	public Set<String> getPlatformNames()
	{
		return this.platforms.keySet();
	}

	public void setPlatform(Map<String, String> platform) {
		this.platforms = platform;
	}

	public void setStreams(Map<String, String> nodeValues) {
		// TODO Auto-generated method stub
		Set<String> solutions = new HashSet<String>();
		Iterator<String> itr = nodeValues.keySet().iterator();
		while(itr.hasNext())
		{
			String streamName = itr.next();
			String streamValues = nodeValues.get(streamName);
			String values[] = streamValues.split(",");
			solutions = new HashSet<String>(Arrays.asList(values)); 
			streams.put(streamName, solutions);
		}
	}
}
