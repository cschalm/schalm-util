package org.schalm.util.helper.app;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.io.File;
import javax.swing.JFrame;
import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Helper to manage the size of an application {@link Frame}.
 *
 * @author <a href="mailto:cschalm@users.sourceforge.net">Carsten Schalm</a>
 * @version $Id: SizeHelper.java 158 2014-03-01 22:22:07Z cschalm $
 */
public class SizeHelper {
    private static final Log log = LogFactory.getLog(SizeHelper.class);
    private static final String INI_FILE = "ini.xml";
    private final Frame customer;
    private File iniFile = null;
    private String configDir = null;

    public SizeHelper(Frame customer) {
        this.customer = customer;
    }

    public void setConfigDir(String configDir) {
        this.configDir = configDir;
    }

    public void restoreSize() {
        if (this.configDir != null) {
            File dir = new File(this.configDir);
            dir.mkdirs();
            this.iniFile = new File(this.configDir + SizeHelper.INI_FILE);
            if (iniFile.canRead()) {
                Init init = JAXB.unmarshal(iniFile, Init.class);
                if (log.isDebugEnabled()) {
                    log.debug("Read: " + init);
                }
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                if (init.getPosX() > screenSize.width) {
                    init.setPosX(0);
                }
                if (init.getPosX() < 0) {
                    init.setPosX(0);
                }
                if (init.getPosY() > screenSize.height) {
                    init.setPosY(0);
                }
                if (init.getPosY() < 0) {
                    init.setPosY(0);
                }
                if (init.getWidth() > screenSize.width) {
                    init.setWidth(screenSize.width);
                }
                if (init.getWidth() <= 0) {
                    init.setWidth(screenSize.width);
                }
                if (init.getHeight() > screenSize.height) {
                    init.setHeight(screenSize.height);
                }
                if (init.getHeight() <= 0) {
                    init.setHeight(screenSize.height);
                }
                this.customer.setLocation(init.getPosX(), init.getPosY());
                this.customer.setSize(init.getWidth(), init.getHeight());
                if (init.isFullScreen()) {
                    this.customer.setExtendedState(JFrame.MAXIMIZED_BOTH);
                }
            } else {
                // set size to maximum = screen-resolution
                this.customer.setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        }
    }

    public void saveSize() {
        Init init = new Init();
        init.setFullScreen(JFrame.MAXIMIZED_BOTH == customer.getExtendedState());
        init.setHeight(customer.getSize().height);
        init.setWidth(customer.getSize().width);
        init.setPosX(customer.getLocation().x);
        init.setPosY(customer.getLocation().y);
        if (log.isDebugEnabled()) {
            log.debug("Write: " + init);
        }

        JAXB.marshal(init, iniFile);
    }

    /**
     * Class holding values for initializing size of applications.
     *
     * @author <a href="mailto:cschalm@users.sourceforge.net">Carsten Schalm</a>
     * @version $Id: SizeHelper.java 158 2014-03-01 22:22:07Z cschalm $
     */
    @XmlRootElement(namespace = "http://www.schalm.org")
    public static final class Init {
        private int posX;
        private int posY;
        private int width;
        private int height;
        private boolean fullScreen;

        public Init() {
            super();
        }

        public boolean isFullScreen() {
            return fullScreen;
        }

        public void setFullScreen(boolean fullScreen) {
            this.fullScreen = fullScreen;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getPosX() {
            return posX;
        }

        public void setPosX(int posX) {
            this.posX = posX;
        }

        public int getPosY() {
            return posY;
        }

        public void setPosY(int posY) {
            this.posY = posY;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        @Override
        public String toString() {
            return "Init{" + "posX=" + posX + ", posY=" + posY + ", width=" + width + ", height=" + height + ", fullScreen="
                    + fullScreen + '}';
        }

    }

}
