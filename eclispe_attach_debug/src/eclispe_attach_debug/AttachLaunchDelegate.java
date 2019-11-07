package eclispe_attach_debug;

import org.eclipse.cdt.dsf.debug.service.IDsfDebugServicesFactory;
import org.eclipse.cdt.dsf.gdb.launching.GdbLaunchDelegate;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunchConfiguration;

import debug.ServiceFactoriesManager;

public class AttachLaunchDelegate extends GdbLaunchDelegate {

	private static ServiceFactoriesManager fDebugServiceFactoriesMgr = new ServiceFactoriesManager();
	 
	@Override
	protected IProject[] getBuildOrder(ILaunchConfiguration configuration,
			String mode) throws CoreException {
		return null;
	}

	@Override
	protected IProject[] getProjectsForProblemSearch(
			ILaunchConfiguration configuration, String mode)
			throws CoreException {
		return null;
	}

	@Override
	public boolean buildForLaunch(ILaunchConfiguration configuration,
			String mode, IProgressMonitor monitor) throws CoreException {
		return false;
	}

	@Override
	public boolean finalLaunchCheck(ILaunchConfiguration configuration,
			String mode, IProgressMonitor monitor) throws CoreException {
		return true;
	}

	@Override
	protected IDsfDebugServicesFactory newServiceFactory(
			ILaunchConfiguration config, String version) {
		// Check if this test has registered a services factory for this launch
		String servicesFactoryId = null;
		try {
			servicesFactoryId = config.getAttribute(
					ServiceFactoriesManager.DEBUG_SERVICES_FACTORY_KEY, "");
		} catch (CoreException e) {
		}

		if (servicesFactoryId != null && servicesFactoryId.length() > 0) {
			// A services factory has been registered, so lets resolve it and use it
			return fDebugServiceFactoriesMgr.removeTestServicesFactory(servicesFactoryId);
		}

		// Use the original services factory
		return super.newServiceFactory(config, version);
	}
}
