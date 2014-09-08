package org.schalm.util.helper.swing;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * <code>WindowAdapter</code> to exit an application on window close.
 *
 * @author <a href="mailto:cschalm@users.sourceforge.net">Carsten Schalm</a>
 * @version $Id: WindowClosingAdapter.java 88 2010-04-22 12:18:26Z cschalm $
 */
public class WindowClosingAdapter extends WindowAdapter {
    private boolean exitSystem;

    /**
     * Erzeugt einen WindowClosingAdapter zum Schliessen
     * des Fensters. Ist exitSystem true, wird das komplette
     * Programm beendet.
     */
    public WindowClosingAdapter(boolean exitSystem) {
        this.exitSystem = exitSystem;
    }

    /**
     * Erzeugt einen WindowClosingAdapter zum Schliessen
     * des Fensters. Das Programm wird nicht beendet.
     */
    public WindowClosingAdapter() {
        this(false);
    }

    @Override
    public void windowClosing(WindowEvent event) {
        event.getWindow().setVisible(false);
        event.getWindow().dispose();
        if (exitSystem) {
            System.exit(0);
        }
    }

}
