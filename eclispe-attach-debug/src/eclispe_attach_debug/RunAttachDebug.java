package eclispe_attach_debug;

import org.eclipse.cdt.debug.core.ICDTLaunchConfigurationConstants;
import org.eclipse.cdt.dsf.service.DsfSession;
import org.eclipse.cdt.dsf.service.DsfSession.SessionEndedListener;
import org.eclipse.cdt.dsf.service.DsfSession.SessionStartedListener;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;

public class RunAttachDebug {
	
	private static SessionStartedListener sessionStartedListener = null;
	private static SessionEndedListener sessionEndedListener = null;

	public static void launchDebugger(String projectPath, String programPath, int processId) {
		Assert.isTrue(isSetAny(projectPath));
		Assert.isTrue(processId >= 0);
		
		IProject project = getProjectFromPath(new Path(projectPath));
		
        if (project != null && project.isOpen()) {
            try {
                ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
                ILaunchConfigurationType type = manager.getLaunchConfigurationType("eclispe-attach-debug.AttacthDebugLaunchConfigurationType");
                ILaunchConfigurationWorkingCopy wc = type.newInstance(null, "SPECS");
                wc.setAttribute(ICDTLaunchConfigurationConstants.ATTR_PROGRAM_NAME, programPath);
                wc.setAttribute(ICDTLaunchConfigurationConstants.ATTR_DEBUGGER_START_MODE,
                        ICDTLaunchConfigurationConstants.DEBUGGER_MODE_ATTACH);
                wc.setAttribute(ICDTLaunchConfigurationConstants.ATTR_PROJECT_NAME, project.getName());
                wc.setAttribute(ICDTLaunchConfigurationConstants.ATTR_PLATFORM, "*");
                wc.setAttribute(ICDTLaunchConfigurationConstants.ATTR_ATTACH_PROCESS_ID, processId);

                ILaunchConfiguration config = wc.doSave();
                
                sessionStartedListener = new SessionStartedListener() {
                    @Override
                    public void sessionStarted(DsfSession session) {
                        System.out.println("Dsf Sesstion has been started.");
                        DsfSession.removeSessionStartedListener(sessionStartedListener);
                    }
                };
                
                sessionEndedListener = new SessionEndedListener() {
					@Override
					public void sessionEnded(DsfSession session) {
						System.out.println("Dsf Sesstion has been ended.");
						DsfSession.removeSessionEndedListener(sessionEndedListener);
					}
                };
                
                DsfSession.addSessionStartedListener(sessionStartedListener);
                DsfSession.addSessionEndedListener(sessionEndedListener);
                config.launch(ILaunchManager.DEBUG_MODE, new NullProgressMonitor());
                showDebugPerspective();
            } catch (CoreException e) {
                e.printStackTrace();
            }
        }
	}
	
	private static IProject getProjectFromPath(IPath location) {
		if (location == null) {
			return null;
		}

		IWorkspaceRoot root = getWorkspaceRoot();
		if (root == null) {
			return null;
		}

		// Get a list of projects managed in the workspace
		IProject[] projects = root.getProjects();
		if (projects == null) {
			return null;
		}

		for (int i = 0; i < projects.length; i++) {
			IProject project = projects[i];
			if (project != null) {
				if (isLocationOfProject(project, location)) {
					return project;
				}
			}
		}

		return null;
	}
	
	public static IWorkspaceRoot getWorkspaceRoot() {

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		if (workspace != null) {
			IWorkspaceRoot root = workspace.getRoot();
			return root;
		}

		return null;

	}
	
	private static boolean isLocationOfProject(IProject project,
			IPath location) {
		if (project == null || location == null) {
			return false;
		}

		IPath projectLocation = project.getLocation();
		if (projectLocation == null) {
			return false;
		}

		// Get Device of the project, and then location
		String deviceOfProject = projectLocation.getDevice();
		String deviceOfLocation = location.getDevice();

		if (isSetAny(deviceOfProject) && isSetAny(deviceOfLocation)) {
			// If device is set in rootPath but device is not set in dirPath,
			// also set device in dirPath
			location = location.setDevice(deviceOfProject);
		}

		return projectLocation.isPrefixOf(location);
	}
	
    private static boolean isSetAny(String str) {
        return (str != null && !str.isEmpty());
    }
    
    private static void showDebugPerspective() throws WorkbenchException {
        getDisplay().asyncExec(new Runnable() {
            public void run() {
                IWorkbench workbench = PlatformUI.getWorkbench();
                if (workbench != null) {
                    IPerspectiveRegistry preg = workbench.getPerspectiveRegistry();
                    IPerspectiveDescriptor pd = preg.findPerspectiveWithId("org.eclipse.debug.ui.DebugPerspective");
                    if (pd != null) {

                        IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
                        try {
                            workbench.showPerspective(pd.getId(), window);
                        } catch (WorkbenchException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }
    
    private static Display getDisplay() {
        IWorkbench wb = PlatformUI.getWorkbench();
        if (wb != null) {
            return wb.getDisplay();
        } else {
            return null;
        }
    }
}
