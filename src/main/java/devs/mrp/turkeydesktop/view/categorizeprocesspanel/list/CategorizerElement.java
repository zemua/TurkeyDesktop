/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.categorizeprocesspanel.list;

import devs.mrp.turkeydesktop.common.FeedbackListener;
import devs.mrp.turkeydesktop.common.Feedbacker;
import devs.mrp.turkeydesktop.database.type.Type;
import devs.mrp.turkeydesktop.i18n.LocaleMessages;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 *
 * @author miguel
 */
public class CategorizerElement extends JPanel implements Feedbacker<Type.Types, String> {

    private LocaleMessages locale = LocaleMessages.getInstance();
    private static final String UNDEFINED_TEXT = "undefined";
    private static final String POSITIVE_TEXT = "positive";
    private static final String NEGATIVE_TEXT = "negative";
    private static final String NEUTRAL_TEXT = "neutral";
    private static final String DEPENDS_TEXT = "depends";

    private JLabel label = new JLabel();
    
    private List<FeedbackListener<Type.Types, String>> listeners = new ArrayList<>();

    private JRadioButton undefinedRadio = new JRadioButton();
    private JRadioButton positiveRadio = new JRadioButton();
    private JRadioButton negativeRadio = new JRadioButton();
    private JRadioButton neutralRadio = new JRadioButton();
    private JRadioButton dependsRadio = new JRadioButton();
    private ButtonGroup radioGroup = new ButtonGroup();

    public CategorizerElement() {
        initialize();
    }
    
    public CategorizerElement(int width, int height) {
        this.setSize(width, height);
        initialize();
    }
    
    private void initialize() {
        //this.setLayout(new BoxLayout(CategorizerElement.this, BoxLayout.X_AXIS));
        this.setLayout(new GridLayout(1, 6));
        //this.setLayout(new GridBagLayout());
        initializeButtonsName();
        initializeRadioGroup();
        setListeners();
        addElements();
        //addElementsToGridBag();
    }
    
    public void init(String text, Type.Types type) {
        setLabelText(text);
        setSelected(type);
    }
    
    public JLabel getLabel() {
        return this.label;
    }
    
    public JPanel getButtons() {
        JPanel p = new JPanel();
        p.add(undefinedRadio);
        p.add(positiveRadio);
        p.add(negativeRadio);
        p.add(neutralRadio);
        p.add(dependsRadio);
        return p;
    }

    private void setLabelText(String txt) {
        label.setText(txt);
    }

    private void initializeButtonsName() {
        undefinedRadio.setText(locale.getString(UNDEFINED_TEXT));
        positiveRadio.setText(locale.getString(POSITIVE_TEXT));
        negativeRadio.setText(locale.getString(NEGATIVE_TEXT));
        neutralRadio.setText(locale.getString(NEUTRAL_TEXT));
        dependsRadio.setText(locale.getString(DEPENDS_TEXT));
    }

    private void initializeRadioGroup() {
        this.radioGroup.add(undefinedRadio);
        this.radioGroup.add(positiveRadio);
        this.radioGroup.add(negativeRadio);
        this.radioGroup.add(neutralRadio);
        this.radioGroup.add(dependsRadio);
    }
    
    private void setSelected(Type.Types t) {
        undefinedRadio.setSelected(t.equals(Type.Types.UNDEFINED));
        positiveRadio.setSelected(t.equals(Type.Types.POSITIVE));
        negativeRadio.setSelected(t.equals(Type.Types.NEGATIVE));
        neutralRadio.setSelected(t.equals(Type.Types.NEUTRAL));
        dependsRadio.setSelected(t.equals(Type.Types.DEPENDS));
    }

    private void setListeners() {
        undefinedRadio.addActionListener(ev -> giveFeedback(Type.Types.UNDEFINED, label.getText()));
        positiveRadio.addActionListener(ev -> giveFeedback(Type.Types.POSITIVE, label.getText()));
        negativeRadio.addActionListener(ev -> giveFeedback(Type.Types.NEGATIVE, label.getText()));
        neutralRadio.addActionListener(ev -> giveFeedback(Type.Types.NEUTRAL, label.getText()));
        dependsRadio.addActionListener(ev -> giveFeedback(Type.Types.DEPENDS, label.getText()));
    }

    private void addElements() {
        this.add(label);
        this.add(undefinedRadio);
        this.add(positiveRadio);
        this.add(negativeRadio);
        this.add(neutralRadio);
        this.add(dependsRadio);
    }
    
    private void addElementsToGridBag() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridwidth = 6;
        this.add(label, c);
        c.gridx = 1;
        this.add(undefinedRadio, c);
        c.gridx = 2;
        this.add(positiveRadio, c);
        c.gridx = 3;
        this.add(negativeRadio, c);
        c.gridx = 4;
        this.add(neutralRadio, c);
        c.gridx = 5;
        this.add(dependsRadio, c);
    }

    @Override
    public void addFeedbackListener(FeedbackListener<Type.Types, String> listener) {
        listeners.add(listener);
    }

    @Override
    public void giveFeedback(Type.Types tipo, String feedback) {
        listeners.forEach(l -> l.giveFeedback(tipo, feedback));
    }

}
