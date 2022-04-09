/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.configuration;

import devs.mrp.turkeydesktop.common.FeedbackListener;
import devs.mrp.turkeydesktop.common.FileHandler;
import devs.mrp.turkeydesktop.common.RemovableLabel;
import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.database.config.ConfigElement;
import devs.mrp.turkeydesktop.database.config.FConfigElementService;
import devs.mrp.turkeydesktop.database.config.IConfigElementService;
import devs.mrp.turkeydesktop.database.imports.ImportService;
import devs.mrp.turkeydesktop.database.imports.ImportServiceFactory;
import devs.mrp.turkeydesktop.i18n.LocaleMessages;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;
import java.io.File;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author miguel
 */
public class ConfigurationHandler extends PanelHandler<ConfigurationPanelEnum, AWTEvent, FeedbackerPanelWithFetcher<ConfigurationPanelEnum, AWTEvent>> {

    private IConfigElementService configService = FConfigElementService.getService();
    private ImportService importService = ImportServiceFactory.getService();
    private LocaleMessages localeMessages = LocaleMessages.getInstance();
    private Logger logger = Logger.getLogger(ConfigurationHandler.class.getName());

    public ConfigurationHandler(JFrame frame, PanelHandler<?, ?, ?> caller) {
        super(frame, caller);
    }

    @Override
    protected FeedbackerPanelWithFetcher<ConfigurationPanelEnum, AWTEvent> initPanel() {
        this.setPanel(FConfigurationPanel.getPanel());
        return this.getPanel();
    }

    @Override
    protected void initListeners(FeedbackerPanelWithFetcher<ConfigurationPanelEnum, AWTEvent> pan) {
        pan.addFeedbackListener((tipo, feedback) -> {
            switch (tipo) {
                case BACK:
                    exit();
                    break;
                case PROPORTION:
                    handleNewProportion();
                    break;
                case LOCKDOWN:
                    try {
                        handleLockdownStatusChange();
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "error handling response", e);
                        exit();
                    }
                    break;
                case LOCKDOWN_FROM:
                    try{
                        handleLockdownFromChange();
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "error handling response", e);
                        exit();
                    }
                    break;
                case LOCKDOWN_TO:
                    try {
                        handleLockdownToChange();
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "error handling response", e);
                        exit();
                    }
                    break;
                case LOCKDOWN_NOTIFY:
                    try {
                        handleLockDownNotificationChange();
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "error handling response", e);
                        exit();
                    }
                    break;
                case LOCKDOWN_NOTIFY_MIN:
                    try {
                        handleLockDownMinutesNotificationChange();
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "error handling response", e);
                        exit();
                    }
                    break;
                case NOTIFY_MIN_LEFT:
                    try {
                        handleMinLeftNotificationChange();
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "error handling response", e);
                        exit();
                    }
                    break;
                case NOTIFY_MIN_LEFT_QTY:
                    try {
                        handleMinLeftQtyNotificationChange();
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "error handling response", e);
                        exit();
                    }
                    break;
                case EXPORT_BUTTON:
                    try {
                        handleExportButton();
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "error handling response", e);
                        exit();
                    }
                    break;
                case EXPORT_TOGGLE:
                    try {
                        handleExportToggle();
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "error handling response", e);
                        exit();
                    }
                    break;
                case IMPORT_BUTTON:
                    try {
                        handleImportButton();
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "error handling response", e);
                        exit();
                    }
                    break;
                case IDLE_SPINNER:
                    try {
                        handleIdleChange();
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "error handling response", e);
                        exit();
                    }
                default:
                    break;
            }
        });
    }

    @Override
    protected void doExtraBeforeShow() {
        try {
            setupProportion();
            setupLockDown();
            setupLockDownNotification();
            setupMinLeftNotification();
            setupExport();
            refreshImportPanel();
            setupIdle();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "error showing panel", e);
            exit();
        }
    }

    private void setupProportion() {
        int proportion = Integer.valueOf(configService.configElement(ConfigurationEnum.PROPORTION).getValue());
        JSlider slider = (JSlider) this.getPanel().getProperty(ConfigurationPanelEnum.PROPORTION);
        slider.setValue(proportion);
    }

    private void setupLockDown() throws Exception {
        boolean locked = Boolean.valueOf(configService.configElement(ConfigurationEnum.LOCKDOWN).getValue());
        JToggleButton lockedButton = (JToggleButton) getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN, JToggleButton.class).orElseThrow(() -> new Exception("wrong object"));
        lockedButton.setSelected(locked);

        long from = Long.valueOf(configService.configElement(ConfigurationEnum.LOCKDOWN_FROM).getValue());
        JSpinner fromHour = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_FROM_HOUR, JSpinner.class).orElseThrow(() -> new Exception("wrong object"));
        JSpinner fromMin = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_FROM_MIN, JSpinner.class).orElseThrow(() -> new Exception("wrong object"));
        fromHour.setValue(TimeConverter.getHours(from));
        fromMin.setValue(TimeConverter.getMinutes(from));

        long to = Long.valueOf(configService.configElement(ConfigurationEnum.LOCKDOWN_TO).getValue());
        JSpinner toHour = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_TO_HOUR, JSpinner.class).orElseThrow(() -> new Exception("wrong object"));
        JSpinner toMin = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_TO_MIN, JSpinner.class).orElseThrow(() -> new Exception("wrong object"));
        toHour.setValue(TimeConverter.getHours(to));
        toMin.setValue(TimeConverter.getMinutes(to));
    }

    private void setupLockDownNotification() throws Exception {
        boolean notify = Boolean.valueOf(configService.configElement(ConfigurationEnum.LOCK_NOTIFY).getValue());
        JToggleButton notifyButton = (JToggleButton) getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_NOTIFY, JToggleButton.class).orElseThrow(() -> new Exception("wrong object"));
        notifyButton.setSelected(notify);

        long notifyMinutes = Long.valueOf(configService.configElement(ConfigurationEnum.LOCK_NOTIFY_MINUTES).getValue());
        if (notifyMinutes < 1 * 60 * 1000 || notifyMinutes > 60 * 60 * 1000) {
            notifyMinutes = 10 * 60 * 1000;
        }
        JSpinner minSpin = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_NOTIFY_MIN, JSpinner.class).orElseThrow(() -> new Exception("wrong object"));
        minSpin.setValue(TimeConverter.getMinutes(notifyMinutes));
    }

    private void setupMinLeftNotification() throws Exception {
        boolean notify = Boolean.valueOf(configService.configElement(ConfigurationEnum.MIN_LEFT_BUTTON).getValue());
        JToggleButton notifyButton = (JToggleButton) getObjectFromPanel(ConfigurationPanelEnum.NOTIFY_MIN_LEFT, JToggleButton.class).orElseThrow(() -> new Exception("wrong object"));
        notifyButton.setSelected(notify);

        long notifyMinutes = Long.valueOf(configService.configElement(ConfigurationEnum.MIN_LEFT_QTY).getValue());
        if (notifyMinutes < 1 * 60 * 1000 || notifyMinutes > 60 * 60 * 1000) {
            notifyMinutes = 10 * 60 * 1000;
        }
        JSpinner minSpin = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.NOTIFY_MIN_LEFT_QTY, JSpinner.class).orElseThrow(() -> new Exception("wrong object"));
        minSpin.setValue(TimeConverter.getMinutes(notifyMinutes));
    }
    
    private void setupExport() throws Exception {
        boolean export = Boolean.valueOf(configService.configElement(ConfigurationEnum.EXPORT_TOGGLE).getValue());
        JToggleButton exportToggle = (JToggleButton) getObjectFromPanel(ConfigurationPanelEnum.EXPORT_TOGGLE, JToggleButton.class).orElseThrow(() -> new Exception("wrong object"));
        exportToggle.setSelected(export);
        
        String pathName = configService.configElement(ConfigurationEnum.EXPORT_PATH).getValue();
        if (Objects.isNull(pathName) || "".equals(pathName)) {
            pathName = localeMessages.getString("noFile");
        }
        JButton button = (JButton) getObjectFromPanel(ConfigurationPanelEnum.EXPORT_BUTTON, JButton.class).orElseThrow(() -> new Exception("wrong object"));
        int size = 25;
        button.setText(pathName.length() > size ? pathName.substring(pathName.length()-size) : pathName);
    }
    
    private void setupIdle() throws Exception {
        long idleMinutes = Long.valueOf(configService.configElement(ConfigurationEnum.IDLE).getValue());
        if (idleMinutes < 1*60*1000 || idleMinutes > 15*60*1000) {
            idleMinutes = 1*60*1000;
        }
        JSpinner spinner = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.IDLE_SPINNER, JSpinner.class).orElseThrow(() -> new Exception("wrong object"));
        spinner.setValue(TimeConverter.getMinutes(idleMinutes));
    }
    
    // HANDLE EVENTS IN THE UI

    private void handleNewProportion() {
        JSlider slider = (JSlider) this.getPanel().getProperty(ConfigurationPanelEnum.PROPORTION);
        int proportion = slider.getValue();
        ConfigElement el = new ConfigElement();
        el.setKey(ConfigurationEnum.PROPORTION);
        el.setValue(String.valueOf(proportion));
        configService.add(el);
    }

    private void handleLockdownStatusChange() throws Exception {
        JToggleButton lockDownButton = (JToggleButton) getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN, JToggleButton.class).orElseThrow(() -> new Exception("wrong object"));
        boolean checked = lockDownButton.isSelected();
        ConfigElement el = new ConfigElement();
        el.setKey(ConfigurationEnum.LOCKDOWN);
        el.setValue(String.valueOf(checked));
        configService.add(el);
    }

    private void handleLockdownFromChange() throws Exception {
        JSpinner lockDownHourSpinner = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_FROM_HOUR, JSpinner.class).orElseThrow(() -> new Exception("wrong object"));
        JSpinner lockDownMinSpinner = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_FROM_MIN, JSpinner.class).orElseThrow(() -> new Exception("wrong object"));
        try {
            Long time = TimeConverter.minutesToMilis((Long) lockDownMinSpinner.getValue());
            time += TimeConverter.hoursToMilis((Long) lockDownHourSpinner.getValue());
            ConfigElement el = new ConfigElement();
            el.setKey(ConfigurationEnum.LOCKDOWN_FROM);
            el.setValue(String.valueOf(time));
            configService.add(el);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting values from spinners to db", e);
        }
    }

    private void handleLockdownToChange() throws Exception {
        JSpinner lockDownHourSpinner = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_TO_HOUR, JSpinner.class).orElseThrow(() -> new Exception("wrong object"));
        JSpinner lockDownMinSpinner = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_TO_MIN, JSpinner.class).orElseThrow(() -> new Exception("wrong object"));
        try {
            Long time = TimeConverter.minutesToMilis((Long) lockDownMinSpinner.getValue());
            time += TimeConverter.hoursToMilis((Long) lockDownHourSpinner.getValue());
            ConfigElement el = new ConfigElement();
            el.setKey(ConfigurationEnum.LOCKDOWN_TO);
            el.setValue(String.valueOf(time));
            configService.add(el);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting values from spinners to db", e);
        }
    }

    private void handleLockDownNotificationChange() throws Exception {
        JToggleButton lockDownNotification = (JToggleButton) getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_NOTIFY, JToggleButton.class).orElseThrow(() -> new Exception("wrong object"));
        boolean checked = lockDownNotification.isSelected();
        ConfigElement el = new ConfigElement();
        el.setKey(ConfigurationEnum.LOCK_NOTIFY);
        el.setValue(String.valueOf(checked));
        configService.add(el);
    }

    private void handleLockDownMinutesNotificationChange() throws Exception {
        JSpinner lockDownMinSpinner = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_NOTIFY_MIN, JSpinner.class).orElseThrow(() -> new Exception("wrong object"));
        try {
            Long time = TimeConverter.minutesToMilis((Long) lockDownMinSpinner.getValue());
            ConfigElement el = new ConfigElement();
            el.setKey(ConfigurationEnum.LOCK_NOTIFY_MINUTES);
            el.setValue(String.valueOf(time));
            configService.add(el);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting values from spinners to db", e);
        }
    }

    private void handleMinLeftNotificationChange() throws Exception {
        JToggleButton minLeftNotification = (JToggleButton) getObjectFromPanel(ConfigurationPanelEnum.NOTIFY_MIN_LEFT, JToggleButton.class).orElseThrow(() -> new Exception("wrong object"));
        boolean checked = minLeftNotification.isSelected();
        ConfigElement el = new ConfigElement();
        el.setKey(ConfigurationEnum.MIN_LEFT_BUTTON);
        el.setValue(String.valueOf(checked));
        configService.add(el);
    }

    private void handleMinLeftQtyNotificationChange() throws Exception {
        JSpinner minLeftQty = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.NOTIFY_MIN_LEFT_QTY, JSpinner.class).orElseThrow(() -> new Exception("wrong object"));
        try {
            Long time = TimeConverter.minutesToMilis((Long) minLeftQty.getValue());
            ConfigElement el = new ConfigElement();
            el.setKey(ConfigurationEnum.MIN_LEFT_QTY);
            el.setValue(String.valueOf(time));
            configService.add(el);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting values from spinners to db", e);
        }
    }

    private void handleExportToggle() throws Exception {
        JToggleButton exportToggle = (JToggleButton) getObjectFromPanel(ConfigurationPanelEnum.EXPORT_TOGGLE, JToggleButton.class).orElseThrow(() -> new Exception("wrong object"));
        boolean checked = exportToggle.isSelected();
        ConfigElement el = new ConfigElement();
        el.setKey(ConfigurationEnum.EXPORT_TOGGLE);
        el.setValue(String.valueOf(checked));
        configService.add(el);
    }

    private void handleExportButton() throws Exception {
        JButton exportButton = (JButton) getObjectFromPanel(ConfigurationPanelEnum.EXPORT_BUTTON, JButton.class).orElseThrow(() -> new Exception("wrong object"));
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Plain text files .txt only", "txt");
        chooser.setFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(false);
        int returnVal = chooser.showSaveDialog(chooser);
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File file = FileHandler.createFileIfNotExists(chooser.getSelectedFile(), ".txt");
        // save file path to db
        if (file.getPath().length() > 150) {
            exportButton.setText(localeMessages.getString("errorPath150"));
            return;
        }
        ConfigElement el = new ConfigElement();
        el.setKey(ConfigurationEnum.EXPORT_PATH);
        el.setValue(file.getPath());
        configService.add(el);
        // change button's name
        int size = 25;
        String path = file.getPath();
        exportButton.setText(path.length() > size ? path.substring(path.length()-size) : path);
    }
    
    private void handleImportButton() throws Exception {
        JPanel importPanel = (JPanel) getObjectFromPanel(ConfigurationPanelEnum.IMPORT_PANEL, JPanel.class).orElseThrow(() -> new Exception("wrong object"));
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Plain text files .txt only", "txt");
        chooser.setFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(false);
        int returnVal = chooser.showOpenDialog(chooser);
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File file = chooser.getSelectedFile();
        if (file.getPath().length() > 500) {
            JLabel message = new JLabel();
            message.setText(localeMessages.getString("errorPath500"));
            importPanel.add(message);
            importPanel.revalidate();
            importPanel.repaint();
            return;
        }
        importService.add(file.getPath());
        refreshImportPanel();
    }
    
    private void refreshImportPanel() throws Exception {
        JPanel importPanel = (JPanel) getObjectFromPanel(ConfigurationPanelEnum.IMPORT_PANEL, JPanel.class).orElseThrow(() -> new Exception("wrong object"));
        importPanel.removeAll();
        importService.findAll().stream()
                .map(path -> {
                    RemovableLabel<String> label = new RemovableLabel<>(path) {
                        @Override
                        protected String getNameFromElement(String element) {
                            return element;
                        }
                        @Override
                        protected void initializeOtherElements() {
                            // ¯\_ (ツ)_/¯
                        }
                        @Override
                        protected void addOtherItems(JPanel panel) {
                            // ¯\_ (ツ)_/¯
                        }
                    };
                    label.addFeedbackListener((String tipo, RemovableLabel.Action feedback) -> {
                        if (feedback.equals(RemovableLabel.Action.DELETE)) {
                            importService.deleteById(tipo);
                            try {
                                refreshImportPanel();
                            } catch (Exception e) {
                                logger.log(Level.SEVERE, "could not refresh import panel after path deletion", e);
                            }
                        }
                    });
                    return label;
                })
                .forEach(label -> importPanel.add(label));
        importPanel.revalidate();
        importPanel.repaint();
    }
    
    private void handleIdleChange() throws Exception {
        JSpinner spinner = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.IDLE_SPINNER, JSpinner.class).orElseThrow(() -> new Exception("wrong object"));
        try {
            Long time = TimeConverter.minutesToMilis((Long) spinner.getValue());
            ConfigElement el = new ConfigElement();
            el.setKey(ConfigurationEnum.IDLE);
            el.setValue(String.valueOf(time));
            configService.add(el);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting values from spinners to db", e);
        }
    }

    @Override
    protected void doBeforeExit() {
        // blank
    }

}
