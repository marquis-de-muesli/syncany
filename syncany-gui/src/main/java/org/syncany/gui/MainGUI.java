package org.syncany.gui;

import java.util.UUID;
import java.util.logging.Logger;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.syncany.gui.messaging.InterfaceUpdate;
import org.syncany.gui.tray.TrayIcon;
import org.syncany.gui.tray.TrayIconFactory;

import com.google.common.eventbus.Subscribe;

public class MainGUI {
	private static final Logger log = Logger.getLogger(MainGUI.class.getSimpleName());
	private static String clientIdentification = UUID.randomUUID().toString();

	private Display display = Display.getDefault();
	private Shell shell;
	
	private TrayIcon tray;
	
	public void dispose(){
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				display.dispose();
			}
		});
	}
	
	public void open() {
		shell = new Shell();	
		tray = new TrayIconFactory().createTrayIcon(shell);

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	@Subscribe
	public void updateInterface(InterfaceUpdate update) {
		if (tray != null){
			log.info("Update Interface Event : " + update.getAction());
			switch (update.getAction()){
				case START_SYSTEM_TRAY_SYNC:
					tray.makeSystemTrayStartSync();
					break;
				case STOP_SYSTEM_TRAY_SYNC:
					tray.makeSystemTrayStopSync();
					break;
				case UPDATE_WATCHED_FOLDERS:
					tray.updateFolders(update.getData());
					break;
			}
		}
	}

	public static String getClientIdentification() {
		return clientIdentification;
	}
}