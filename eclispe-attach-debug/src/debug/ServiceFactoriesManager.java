package debug;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.cdt.dsf.gdb.service.GdbDebugServicesFactory;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import eclispe_attach_debug.Activator;

public class ServiceFactoriesManager {
	public final static String DEBUG_SERVICES_FACTORY_KEY = Activator.PLUGIN_ID
			+ ".DEBUG_SERVICES_FACTORY";

	private final Map<String, GdbDebugServicesFactory> fTestServiceFactoriesMap = new HashMap<>();

	public void addTestServicesFactory(String id,
			GdbDebugServicesFactory servicesFactory) throws CoreException {
		if (fTestServiceFactoriesMap.containsKey(id)) {
			throw new CoreException(
					new Status(IStatus.ERROR, Activator.getUniqueIdentifier(),
							"A factory with this id already exists " + id));
		}

		fTestServiceFactoriesMap.put(id, servicesFactory);
	}

	public GdbDebugServicesFactory removeTestServicesFactory(String id) {
		return fTestServiceFactoriesMap.remove(id);
	}
}
