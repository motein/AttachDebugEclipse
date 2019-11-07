package eclispe_attach_debug;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class InputSettingsDialog extends Dialog {
	
	private static final String PROCESS_ID_STR = "Process ID:";
	private static final String PROJECT_PATH_STR = "Project Path:";
	private static final String PROGRAM_PATH_STR = "Program Path:";
	
	protected Object result;
	protected Shell shell;
	
	Text idText;
	Text projectPathText;
	Text programPathText;
	
	private int processId;
	private String projectPath;
	private String programPath;

	/**
	 * Create the dialog.
	 * @param parent
	 */
	public InputSettingsDialog(Shell parent) {
		super(parent);
	}
	
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite base = (Composite) super.createDialogArea(parent);
        base.setLayout(new FormLayout());
        createContent(base);

        return base;
    }
    
    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Debug Configuration");
    }
    
    @Override
    protected void okPressed() {
        processId = Integer.parseInt(idText.getText());
        projectPath = projectPathText.getText();
        programPath = programPathText.getText();
        
        RunAttachDebug.launchDebugger(projectPath, programPath, processId);
        super.okPressed();
    }

	/**
	 * Create contents of the dialog.
	 */
    private void createContent(Composite parent) {
		Group setupGroup = new Group(parent, SWT.NONE);
		setupGroup.setLayout(new GridLayout(2, false));
		FormData setupfd = new FormData();
		setupfd.bottom = new FormAttachment(0, 150);
		setupfd.top = new FormAttachment(0, 10);
		setupfd.left = new FormAttachment(0, 10);
		setupfd.right =  new FormAttachment(100, -10);
		setupGroup.setLayoutData(setupfd);
		
		Label idLabel = new Label(setupGroup, SWT.NONE);
		idLabel.setText(PROCESS_ID_STR);
		idLabel.setToolTipText("positive integer");
		idLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		
		idText = new Text(setupGroup, SWT.BORDER);
		idText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		Label projectPathLabel = new Label(setupGroup, SWT.NONE);
		projectPathLabel.setText(PROJECT_PATH_STR);
		projectPathLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		
		projectPathText = new Text(setupGroup, SWT.BORDER);
		projectPathText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		Label programPathLabel = new Label(setupGroup, SWT.NONE);
		programPathLabel.setText(PROGRAM_PATH_STR);
		programPathLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		
		programPathText = new Text(setupGroup, SWT.BORDER);
		programPathText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
	}
}
