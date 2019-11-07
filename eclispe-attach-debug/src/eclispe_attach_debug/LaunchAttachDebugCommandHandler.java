package eclispe_attach_debug;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class LaunchAttachDebugCommandHandler extends AbstractHandler {

	@Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
		System.out.println("Launch Attach Debug.");
		InputSettingsDialog dialog = new InputSettingsDialog(ResourceUtil.getShell());
		dialog.open();
		
		return null;
	}

}
