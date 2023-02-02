/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.configuration;

import devs.mrp.turkeydesktop.common.ConfirmationWithDelay;
import devs.mrp.turkeydesktop.common.FileHandler;
import devs.mrp.turkeydesktop.common.RemovableLabel;
import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.common.impl.ConfirmationWithDelayFactory;
import devs.mrp.turkeydesktop.database.config.ConfigElement;
import devs.mrp.turkeydesktop.database.config.ConfigElementFactoryImpl;
import devs.mrp.turkeydesktop.database.imports.ImportService;
import devs.mrp.turkeydesktop.database.imports.ImportFactory;
import devs.mrp.turkeydesktop.i18n.LocaleMessages;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import java.awt.AWTEvent;
import java.io.File;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.filechooser.FileNameExtensionFilter;
import devs.mrp.turkeydesktop.database.config.ConfigElementService;

/**
 *
 * @author miguel
 */
public class ConfigurationHandler extends PanelHandler<ConfigurationPanelEnum, AWTEvent, FeedbackerPanelWithFetcher<ConfigurationPanelEnum, AWTEvent>> {

    private ConfigElementService configService = ConfigElementFactoryImpl.getService();
    private ImportService importService = ImportFactory.getService();
    private LocaleMessages localeMessages = LocaleMessages.getInstance();
    private Logger logger = Logger.getLogger(ConfigurationHandler.class.getName());
    private JFrame frame;
    private ConfirmationWithDelay popupMaker = new ConfirmationWithDelayFactory();
    public static final int SENSITIVE_WAITING_SECONDS = 30;
    
    // Flags to know when the UI has been loaded and we can start processing triggers
    private boolean proportionStarted = false;
    private boolean lockDownStarted = false;
    private boolean lockDownNotificationStarted = false;
    private boolean minLeftNotificationStarted = false;
    private boolean exportStarted = false;
    private boolean importStarted = false;
    private boolean idleStarted = false;
    private boolean notifySoundStarted = false;
    private boolean changeOfDayStarted = false;
    private boolean changeOfDayNotificationStarted = false;
    private boolean changeOfDayNotificationMinutesStarted = false;

    public ConfigurationHandler(JFrame frame, PanelHandler<?, ?, ?> caller) {
        super(frame, caller);
        this.frame = frame;
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
                    break;
                case NOTIFY_WITH_SOUND:
                    try {
                        handleNotifySound();
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "error handling response", e);
                        exit();
                    }
                    break;
                case CHANGE_OF_DAY:
                    try {
                        handleChangeOfDay();
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "error handling response", e);
                        exit();
                    }
                    break;
                case NOTIFY_CHANGE_OF_DAY:
                    try {
                        handleNotifyChangeOfDay();
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "error handling response", e);
                        exit();
                    }
                    break;
                case NOTIFY_CHANGE_OF_DAY_MINUTES:
                    try {
                        handleNotifyChangeOfDayMinutes();
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "error handling response", e);
                        exit();
                    }
                    break;
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
            setupNotifySound();
            setupChangeOfDay();
            setupChangeOfDayNotification();
            setupChangeOfDayNotificationMinutes();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "error showing panel", e);
            exit();
        }
    }

    private void setupProportion() {
        configService.configElement(ConfigurationEnum.PROPORTION).subscribe(proportionResult -> {
            int proportion = Integer.valueOf(proportionResult.getValue());
            JSpinner slider = (JSpinner) this.getPanel().getProperty(ConfigurationPanelEnum.PROPORTION);
            slider.setValue(proportion);
            proportionStarted = true;
        });
    }

    private void setupLockDown() throws Exception {
        configService.configElement(ConfigurationEnum.LOCKDOWN).subscribe(lockdownResult -> {
            boolean locked = Boolean.valueOf(lockdownResult.getValue());
            JToggleButton lockedButton = (JToggleButton) getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN, JToggleButton.class).get();
            lockedButton.setSelected(locked);
            
            configService.configElement(ConfigurationEnum.LOCKDOWN_FROM).subscribe(lockdownFromResult -> {
                long from = Long.valueOf(lockdownFromResult.getValue());
                JSpinner fromHour = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_FROM_HOUR, JSpinner.class).get();
                JSpinner fromMin = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_FROM_MIN, JSpinner.class).get();
                fromHour.setValue(TimeConverter.getHours(from));
                fromMin.setValue(TimeConverter.getMinutes(from));
                
                configService.configElement(ConfigurationEnum.LOCKDOWN_TO).subscribe(lockdownTo -> {
                    long to = Long.valueOf(lockdownTo.getValue());
                    JSpinner toHour = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_TO_HOUR, JSpinner.class).get();
                    JSpinner toMin = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_TO_MIN, JSpinner.class).get();
                    toHour.setValue(TimeConverter.getHours(to));
                    toMin.setValue(TimeConverter.getMinutes(to));
                    
                    lockDownStarted = true;
                });
            });
        });
    }

    private void setupLockDownNotification() throws Exception {
        configService.configElement(ConfigurationEnum.LOCK_NOTIFY).subscribe(lockNotifyResult -> {
            boolean notify = Boolean.valueOf(lockNotifyResult.getValue());
            JToggleButton notifyButton = (JToggleButton) getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_NOTIFY, JToggleButton.class).get();
            notifyButton.setSelected(notify);
            
            configService.configElement(ConfigurationEnum.LOCK_NOTIFY_MINUTES).subscribe(lockNotifyMinutes -> {
                long notifyMinutes = Long.valueOf(lockNotifyMinutes.getValue());
                if (notifyMinutes < 1 * 60 * 1000 || notifyMinutes > 60 * 60 * 1000) {
                    notifyMinutes = 10 * 60 * 1000;
                }
                JSpinner minSpin = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_NOTIFY_MIN, JSpinner.class).get();
                minSpin.setValue(TimeConverter.getMinutes(notifyMinutes));
                
                lockDownNotificationStarted = true;
            });
        });
    }

    private void setupMinLeftNotification() throws Exception {
        configService.configElement(ConfigurationEnum.MIN_LEFT_BUTTON).subscribe(minLeftButtonResult -> {
            boolean notify = Boolean.valueOf(minLeftButtonResult.getValue());
            JToggleButton notifyButton = (JToggleButton) getObjectFromPanel(ConfigurationPanelEnum.NOTIFY_MIN_LEFT, JToggleButton.class).get();
            notifyButton.setSelected(notify);
            
            configService.configElement(ConfigurationEnum.MIN_LEFT_QTY).subscribe(minLeftQtyResult -> {
                long notifyMinutes = Long.valueOf(minLeftQtyResult.getValue());
                if (notifyMinutes < 1 * 60 * 1000 || notifyMinutes > 60 * 60 * 1000) {
                    notifyMinutes = 10 * 60 * 1000;
                }
                JSpinner minSpin = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.NOTIFY_MIN_LEFT_QTY, JSpinner.class).get();
                minSpin.setValue(TimeConverter.getMinutes(notifyMinutes));
                
                minLeftNotificationStarted = true;
            });
        });
    }
    
    private void setupExport() throws Exception {
        configService.configElement(ConfigurationEnum.EXPORT_TOGGLE).subscribe(exportToggleResult -> {
            boolean export = Boolean.valueOf(exportToggleResult.getValue());
            JToggleButton exportToggle = (JToggleButton) getObjectFromPanel(ConfigurationPanelEnum.EXPORT_TOGGLE, JToggleButton.class).get();
            exportToggle.setSelected(export);
            
            configService.configElement(ConfigurationEnum.EXPORT_PATH).subscribe(exportPathResult -> {
                String pathName = exportPathResult.getValue();
                if (Objects.isNull(pathName) || "".equals(pathName)) {
                    pathName = localeMessages.getString("noFile");
                }
                JButton button = (JButton) getObjectFromPanel(ConfigurationPanelEnum.EXPORT_BUTTON, JButton.class).get();
                int size = 25;
                button.setText(pathName.length() > size ? pathName.substring(pathName.length()-size) : pathName);
                
                exportStarted = true;
            });
        });
    }
    
    private void setupIdle() throws Exception {
        configService.configElement(ConfigurationEnum.IDLE).subscribe(idleResult -> {
            long idleMinutes = Long.valueOf(idleResult.getValue());
            if (idleMinutes < 1*60*1000 || idleMinutes > 15*60*1000) {
                idleMinutes = 1*60*1000;
            }
            JSpinner spinner = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.IDLE_SPINNER, JSpinner.class).get();
            spinner.setValue(TimeConverter.getMinutes(idleMinutes));
            
            idleStarted = true;
        });
    }
    
    private void setupNotifySound() throws Exception {
        configService.configElement(ConfigurationEnum.SPEAK).subscribe(speakResult -> {
            boolean checked = Boolean.valueOf(speakResult.getValue());
            JCheckBox check = (JCheckBox) getObjectFromPanel(ConfigurationPanelEnum.NOTIFY_WITH_SOUND, JCheckBox.class).get();
            check.setSelected(checked);
            
            notifySoundStarted = true;
        });
    }
    
    private void setupChangeOfDay() throws Exception {
        configService.configElement(ConfigurationEnum.CHANGE_OF_DAY).subscribe(changeOfDayResult -> {
            int hours = Integer.valueOf(changeOfDayResult.getValue());
            JSpinner spinner = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.CHANGE_OF_DAY, JSpinner.class).get();
            spinner.setValue(hours);
            
            changeOfDayStarted = true;
        });
    }
    
    private void setupChangeOfDayNotification() throws Exception {
        configService.configElement(ConfigurationEnum.NOTIFY_CHANGE_OF_DAY).subscribe(notifyChangeOfDayResult -> {
            boolean notify = Boolean.valueOf(notifyChangeOfDayResult.getValue());
            JToggleButton notifyButton = (JToggleButton) getObjectFromPanel(ConfigurationPanelEnum.NOTIFY_CHANGE_OF_DAY, JToggleButton.class).get();
            notifyButton.setSelected(notify);
            
            changeOfDayNotificationStarted = true;
        });
    }
    
    private void setupChangeOfDayNotificationMinutes() throws Exception {
        configService.configElement(ConfigurationEnum.NOTIFY_CHANGE_OF_DAY_MINUTES).subscribe(notifyChangeOfDayMinutesResult -> {
            int notifyMinutes = Integer.valueOf(notifyChangeOfDayMinutesResult.getValue());
            JSpinner minSpin = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.NOTIFY_CHANGE_OF_DAY_MINUTES, JSpinner.class).get();
            minSpin.setValue(notifyMinutes);
            
            changeOfDayNotificationMinutesStarted = true;
        });
    }
    
    // HANDLE EVENTS IN THE UI

    private void handleNewProportion() {
        if (!proportionStarted) {
            return;
        }
        JSpinner slider = (JSpinner) this.getPanel().getProperty(ConfigurationPanelEnum.PROPORTION);
        int proportion = (Integer)slider.getValue();
        configService.configElement(ConfigurationEnum.PROPORTION).subscribe(proportionResult -> {
            int savedProportion = Integer.valueOf(proportionResult.getValue());
            // if we are decreasing and target value is lower than 4...
            if (proportion < 4 && proportion < savedProportion) {
                slider.setEnabled(false);
                popupMaker.show(frame,
                        localeMessages.getString("areYouSureYouShouldDoThis"),
                        localeMessages.getString("cancel"),
                        localeMessages.getString("confirm"),
                        () -> {
                    // runnable for positive button
                    ConfigElement el = new ConfigElement();
                    el.setKey(ConfigurationEnum.PROPORTION);
                    el.setValue(String.valueOf(proportion));
                    configService.add(el).subscribe();
                    slider.setEnabled(true);
                    slider.setValue(proportion);
                },
                        () -> {
                    // runnable for negative button
                    slider.setEnabled(true);
                    slider.setValue(savedProportion);
                }, SENSITIVE_WAITING_SECONDS);
            } else {
                ConfigElement el = new ConfigElement();
                el.setKey(ConfigurationEnum.PROPORTION);
                el.setValue(String.valueOf(proportion));
                configService.add(el).subscribe();
            }
        });
    }

    private void handleLockdownStatusChange() throws Exception {
        if (!lockDownStarted) {
            return;
        }
        JToggleButton lockDownButton = (JToggleButton) getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN, JToggleButton.class).orElseThrow(() -> new Exception("wrong object"));
        boolean checked = lockDownButton.isSelected();
        if (!checked) {
            lockDownButton.setEnabled(false);
            popupMaker.show(frame, localeMessages.getString("areYouSureYouShouldDoThis"),
                    localeMessages.getString("cancel"),
                    localeMessages.getString("confirm"),
                    () -> {
                        // runnable positive
                        ConfigElement el = new ConfigElement();
                        el.setKey(ConfigurationEnum.LOCKDOWN);
                        el.setValue(String.valueOf(false));
                        configService.add(el).subscribe();
                        lockDownButton.setEnabled(true);
                    }, () -> {
                        // runnable negative
                        lockDownButton.setSelected(true);
                        lockDownButton.setEnabled(true);
                    }, SENSITIVE_WAITING_SECONDS);
        } else {
            ConfigElement el = new ConfigElement();
            el.setKey(ConfigurationEnum.LOCKDOWN);
            el.setValue(String.valueOf(checked));
            configService.add(el).subscribe();
        }
    }

    private void handleLockdownFromChange() throws Exception {
        if (!lockDownStarted) {
            return;
        }
        JSpinner lockDownHourSpinner = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_FROM_HOUR, JSpinner.class).orElseThrow(() -> new Exception("wrong object"));
        JSpinner lockDownMinSpinner = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_FROM_MIN, JSpinner.class).orElseThrow(() -> new Exception("wrong object"));
        JSpinner toHourSpinner = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_TO_HOUR, JSpinner.class).orElseThrow(() -> new Exception("wrong object"));
        JSpinner toMinSpinner = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_TO_MIN, JSpinner.class).orElseThrow(() -> new Exception("wrong object"));
        configService.configElement(ConfigurationEnum.LOCKDOWN_FROM).subscribe(lockdownFromResult -> {
            long savedTime = Long.valueOf(lockdownFromResult.getValue());
            try {
                Long time = TimeConverter.minutesToMilis((Long) lockDownMinSpinner.getValue());
                time += TimeConverter.hoursToMilis((Long) lockDownHourSpinner.getValue());
                final long targetTimeForDb = time; // to use inside the lambda has to be final
                Long toTime = TimeConverter.minutesToMilis((Long) toMinSpinner.getValue());
                toTime += TimeConverter.hoursToMilis((Long) toHourSpinner.getValue());

                long savedToTime = toTime;
                if (toTime < time) {
                    // for example start time 23:00 and end time at 5:00 then make 5:00 -> 29:00
                    toTime = toTime + TimeConverter.hoursToMilis(24);
                }
                if (savedToTime < savedTime) {
                    // for example start time 23:00 and end time at 5:00 then make 5:00 -> 29:00
                    savedToTime = savedToTime + TimeConverter.hoursToMilis(24);
                }

                long diffNow = toTime - time;
                long savedDiff = savedToTime - savedTime;

                if (diffNow < savedDiff) { // if the lockdown frame is being decreased
                    lockDownHourSpinner.setEnabled(false);
                    lockDownMinSpinner.setEnabled(false);
                    popupMaker.show(frame, localeMessages.getString("areYouSureYouShouldDoThis"),
                        localeMessages.getString("cancel"),
                        localeMessages.getString("confirm"),
                        () -> {
                            // positive runnable
                            ConfigElement el = new ConfigElement();
                            el.setKey(ConfigurationEnum.LOCKDOWN_FROM);
                            el.setValue(String.valueOf(targetTimeForDb));
                            configService.add(el).subscribe();
                            lockDownHourSpinner.setEnabled(true);
                            lockDownMinSpinner.setEnabled(true);
                        }, () -> {
                            // negative runnable
                            lockDownMinSpinner.setValue(TimeConverter.getMinutes(savedTime));
                            lockDownHourSpinner.setValue(TimeConverter.getHours(savedTime));
                            lockDownHourSpinner.setEnabled(true);
                            lockDownMinSpinner.setEnabled(true);
                        }, SENSITIVE_WAITING_SECONDS);
                } else {
                    ConfigElement el = new ConfigElement();
                    el.setKey(ConfigurationEnum.LOCKDOWN_FROM);
                    el.setValue(String.valueOf(targetTimeForDb));
                    configService.add(el).subscribe();
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error getting values from spinners to db", e);
            }
        });
    }

    private void handleLockdownToChange() throws Exception {
        if (!lockDownStarted) {
            return;
        }
        JSpinner fromHourSpinner = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_FROM_HOUR, JSpinner.class).orElseThrow(() -> new Exception("wrong object"));
        JSpinner fromMinSpinner = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_FROM_MIN, JSpinner.class).orElseThrow(() -> new Exception("wrong object"));
        JSpinner toHourSpinner = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_TO_HOUR, JSpinner.class).orElseThrow(() -> new Exception("wrong object"));
        JSpinner toMinSpinner = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_TO_MIN, JSpinner.class).orElseThrow(() -> new Exception("wrong object"));
        configService.configElement(ConfigurationEnum.LOCKDOWN_TO).subscribe(lockdownToResult -> {
            final long savedToTimeFinal = Long.valueOf(lockdownToResult.getValue());
            try {
                Long fromTime = TimeConverter.minutesToMilis((Long) fromMinSpinner.getValue());
                fromTime += TimeConverter.hoursToMilis((Long) fromHourSpinner.getValue());
                Long toTime = TimeConverter.minutesToMilis((Long) toMinSpinner.getValue());
                toTime += TimeConverter.hoursToMilis((Long) toHourSpinner.getValue());
                final long targetTimeForDb = toTime; // to use inside the lambda has to be final

                if (toTime < fromTime) {
                    // for example start time 23:00 and end time at 5:00 then make 5:00 -> 29:00
                    toTime = toTime + TimeConverter.hoursToMilis(24);
                }
                long savedToTime = savedToTimeFinal;
                if (savedToTime < fromTime) {
                    // for example start time 23:00 and end time at 5:00 then make 5:00 -> 29:00
                    savedToTime = savedToTime + TimeConverter.hoursToMilis(24);
                }

                long diffNow = toTime - fromTime;
                long savedDiff = savedToTime - fromTime;

                if (diffNow < savedDiff) { // if the lockdown frame is being decreased
                    toHourSpinner.setEnabled(false);
                    toMinSpinner.setEnabled(false);
                    popupMaker.show(frame, localeMessages.getString("areYouSureYouShouldDoThis"),
                        localeMessages.getString("cancel"),
                        localeMessages.getString("confirm"),
                        () -> {
                            // positive runnable
                            ConfigElement el = new ConfigElement();
                            el.setKey(ConfigurationEnum.LOCKDOWN_TO);
                            el.setValue(String.valueOf(targetTimeForDb));
                            configService.add(el).subscribe();
                            toHourSpinner.setEnabled(true);
                            toMinSpinner.setEnabled(true);
                        }, () -> {
                            // negative runnable
                            toMinSpinner.setValue(TimeConverter.getMinutes(savedToTimeFinal));
                            toHourSpinner.setValue(TimeConverter.getHours(savedToTimeFinal));
                            toHourSpinner.setEnabled(true);
                            toMinSpinner.setEnabled(true);
                        }, SENSITIVE_WAITING_SECONDS);
                } else {
                    ConfigElement el = new ConfigElement();
                    el.setKey(ConfigurationEnum.LOCKDOWN_TO);
                    el.setValue(String.valueOf(targetTimeForDb));
                    configService.add(el).subscribe();
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error getting values from spinners to db", e);
            }
        });
    }

    private void handleLockDownNotificationChange() throws Exception {
        if (!lockDownNotificationStarted) {
            return;
        }
        JToggleButton lockDownNotification = (JToggleButton) getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_NOTIFY, JToggleButton.class).orElseThrow(() -> new Exception("wrong object"));
        boolean checked = lockDownNotification.isSelected();
        ConfigElement el = new ConfigElement();
        el.setKey(ConfigurationEnum.LOCK_NOTIFY);
        el.setValue(String.valueOf(checked));
        configService.add(el).subscribe();
    }

    private void handleLockDownMinutesNotificationChange() throws Exception {
        if (!lockDownNotificationStarted) {
            return;
        }
        JSpinner lockDownMinSpinner = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_NOTIFY_MIN, JSpinner.class).orElseThrow(() -> new Exception("wrong object"));
        try {
            Long time = TimeConverter.minutesToMilis((Long) lockDownMinSpinner.getValue());
            ConfigElement el = new ConfigElement();
            el.setKey(ConfigurationEnum.LOCK_NOTIFY_MINUTES);
            el.setValue(String.valueOf(time));
            configService.add(el).subscribe();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting values from spinners to db", e);
        }
    }

    private void handleMinLeftNotificationChange() throws Exception {
        if (!minLeftNotificationStarted) {
            return;
        }
        JToggleButton minLeftNotification = (JToggleButton) getObjectFromPanel(ConfigurationPanelEnum.NOTIFY_MIN_LEFT, JToggleButton.class).orElseThrow(() -> new Exception("wrong object"));
        boolean checked = minLeftNotification.isSelected();
        ConfigElement el = new ConfigElement();
        el.setKey(ConfigurationEnum.MIN_LEFT_BUTTON);
        el.setValue(String.valueOf(checked));
        configService.add(el).subscribe();
    }

    private void handleMinLeftQtyNotificationChange() throws Exception {
        if (!minLeftNotificationStarted) {
            return;
        }
        JSpinner minLeftQty = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.NOTIFY_MIN_LEFT_QTY, JSpinner.class).orElseThrow(() -> new Exception("wrong object"));
        try {
            Long time = TimeConverter.minutesToMilis((Long) minLeftQty.getValue());
            ConfigElement el = new ConfigElement();
            el.setKey(ConfigurationEnum.MIN_LEFT_QTY);
            el.setValue(String.valueOf(time));
            configService.add(el).subscribe();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting values from spinners to db", e);
        }
    }

    private void handleExportToggle() throws Exception {
        if (!exportStarted) {
            return;
        }
        JToggleButton exportToggle = (JToggleButton) getObjectFromPanel(ConfigurationPanelEnum.EXPORT_TOGGLE, JToggleButton.class).orElseThrow(() -> new Exception("wrong object"));
        boolean checked = exportToggle.isSelected();
        ConfigElement el = new ConfigElement();
        el.setKey(ConfigurationEnum.EXPORT_TOGGLE);
        el.setValue(String.valueOf(checked));
        configService.add(el).subscribe();
    }

    private void handleExportButton() throws Exception {
        if (!exportStarted) {
            return;
        }
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
        configService.add(el).subscribe();
        // change button's name
        int size = 25;
        String path = file.getPath();
        exportButton.setText(path.length() > size ? path.substring(path.length()-size) : path);
    }
    
    private void handleImportButton() throws Exception {
        if (!importStarted) {
            return;
        }
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
        importService.add(file.getPath()).subscribe();
        refreshImportPanel();
    }
    
    private void refreshImportPanel() throws Exception {
        JPanel importPanel = (JPanel) getObjectFromPanel(ConfigurationPanelEnum.IMPORT_PANEL, JPanel.class).orElseThrow(() -> new Exception("wrong object"));
        importPanel.removeAll();
        
        Observer<RemovableLabel<String>> subscriber = new Observer<RemovableLabel<String>>() {
            @Override
            public void onComplete() {
                importPanel.revalidate();
                importPanel.repaint();
                importStarted = true;
            }

            @Override
            public void onError(Throwable thrwbl) {
                // nothing to do here
            }

            @Override
            public void onNext(RemovableLabel<String> label) {
                importPanel.add(label);
            }

            @Override
            public void onSubscribe(Disposable d) {
                // nothing here
            }
        };
        
        importService.findAll().map(path -> {
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
                        importService.deleteById(tipo).subscribe();
                        try {
                            refreshImportPanel();
                        } catch (Exception e) {
                            logger.log(Level.SEVERE, "could not refresh import panel after path deletion", e);
                        }
                    }
                });
                return label;
            }).subscribe(subscriber);
    }
    
    private void handleIdleChange() throws Exception {
        if (!idleStarted) {
            return;
        }
        JSpinner spinner = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.IDLE_SPINNER, JSpinner.class).orElseThrow(() -> new Exception("wrong object"));
        try {
            Long time = TimeConverter.minutesToMilis((Long) spinner.getValue());
            ConfigElement el = new ConfigElement();
            el.setKey(ConfigurationEnum.IDLE);
            el.setValue(String.valueOf(time));
            configService.add(el).subscribe();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting values from spinners to db", e);
        }
    }
    
    private void handleNotifySound() throws Exception {
        if (!notifySoundStarted) {
            return;
        }
        JCheckBox check = (JCheckBox) getObjectFromPanel(ConfigurationPanelEnum.NOTIFY_WITH_SOUND, JCheckBox.class).orElseThrow(() -> new Exception("wrong object"));
        try {
            ConfigElement el = new ConfigElement();
            el.setKey(ConfigurationEnum.SPEAK);
            el.setValue(String.valueOf(check.isSelected()));
            configService.add(el).subscribe();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting values from notify with sound checkbox", e);
        }
    }
    
    private void handleChangeOfDay() throws Exception {
        if (!changeOfDayStarted) {
            return;
        }
        JSpinner spinner = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.CHANGE_OF_DAY, JSpinner.class).orElseThrow(() -> new Exception("wrong object"));
        try {
            ConfigElement el = new ConfigElement();
            el.setKey(ConfigurationEnum.CHANGE_OF_DAY);
            el.setValue(String.valueOf(spinner.getValue()));
            configService.add(el).subscribe();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting values from change of day spinner", e);
        }
    }

    @Override
    protected void doBeforeExit() {
        // blank
    }

    private void handleNotifyChangeOfDay() throws Exception {
        if (!changeOfDayNotificationStarted) {
            return;
        }
        JToggleButton notifyToggle = (JToggleButton) getObjectFromPanel(ConfigurationPanelEnum.NOTIFY_CHANGE_OF_DAY, JToggleButton.class).orElseThrow(() -> new Exception("wrong object"));
        boolean checked = notifyToggle.isSelected();
        ConfigElement el = new ConfigElement();
        el.setKey(ConfigurationEnum.NOTIFY_CHANGE_OF_DAY);
        el.setValue(String.valueOf(checked));
        configService.add(el).subscribe();
    }

    private void handleNotifyChangeOfDayMinutes() throws Exception {
        if (!changeOfDayNotificationMinutesStarted) {
            return;
        }
        JSpinner spinner = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.NOTIFY_CHANGE_OF_DAY_MINUTES, JSpinner.class).orElseThrow(() -> new Exception("wrong object"));
        try {
            ConfigElement el = new ConfigElement();
            el.setKey(ConfigurationEnum.NOTIFY_CHANGE_OF_DAY_MINUTES);
            el.setValue(String.valueOf(spinner.getValue()));
            configService.add(el).subscribe();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting values from change of day spinner", e);
        }
    }

}
