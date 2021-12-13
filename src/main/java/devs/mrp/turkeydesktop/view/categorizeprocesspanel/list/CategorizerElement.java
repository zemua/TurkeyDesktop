/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.categorizeprocesspanel.list;

import devs.mrp.turkeydesktop.database.type.Type;
import devs.mrp.turkeydesktop.i18n.LocaleMessages;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 *
 * @author miguel
 */
public class CategorizerElement extends JPanel {
    
    /**
     * LEFT FOR REFERENCE
     * this was an element of a JList with text label and radio buttons
     * but the content of a jlist is just a render, it has no "physical" presence
     * this means the radio buttons cannot be clicked / would not fire events.
     * 
     * BETTER to layout the JButtons vertically on a JPanel using a GridLayout(0,1) or something like that
     * and then put the JPanel in a JScrollPane mocking a JList of JButtons
     */

    private LocaleMessages locale = LocaleMessages.getInstance();
    private static final String UNDEFINED_TEXT = "undefined";
    private static final String POSITIVE_TEXT = "positive";
    private static final String NEGATIVE_TEXT = "negative";
    private static final String NEUTRAL_TEXT = "neutral";
    private static final String DEPENDS_TEXT = "depends";

    private JLabel label = new JLabel();

    private JRadioButton undefinedRadio = new JRadioButton();
    private JRadioButton positiveRadio = new JRadioButton();
    private JRadioButton negativeRadio = new JRadioButton();
    private JRadioButton neutralRadio = new JRadioButton();
    private JRadioButton dependsRadio = new JRadioButton();
    private ButtonGroup radioGroup = new ButtonGroup();

    public CategorizerElement() {
        this.setLayout(new BoxLayout(CategorizerElement.this, BoxLayout.X_AXIS));
        initializeButtonsName();
        initializeRadioGroup();
        setListeners();
        addElements();
    }
    
    public void init(String text, Type.Types type) {
        setLabelText(text);
        setSelected(type);
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
        undefinedRadio.addActionListener(ev -> CategorizerStaticData.giveFeedback(Type.Types.UNDEFINED, label.getText()));
        positiveRadio.addActionListener(ev -> CategorizerStaticData.giveFeedback(Type.Types.POSITIVE, label.getText()));
        negativeRadio.addActionListener(ev -> CategorizerStaticData.giveFeedback(Type.Types.NEGATIVE, label.getText()));
        neutralRadio.addActionListener(ev -> CategorizerStaticData.giveFeedback(Type.Types.NEUTRAL, label.getText()));
        dependsRadio.addActionListener(ev -> CategorizerStaticData.giveFeedback(Type.Types.DEPENDS, label.getText()));
    }

    private void addElements() {
        this.add(label);
        this.add(undefinedRadio);
        this.add(positiveRadio);
        this.add(negativeRadio);
        this.add(neutralRadio);
        this.add(dependsRadio);
    }

}
