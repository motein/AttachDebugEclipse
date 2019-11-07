package eclispe_attach_debug;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class ResourceUtil {

    public static Shell getShell() {
        IWorkbench wb = PlatformUI.getWorkbench();
        if (wb != null) {
            IWorkbenchWindow window = wb.getActiveWorkbenchWindow();
            if (window != null) {
                return window.getShell();
            }
        }

        return null;
    }
}
