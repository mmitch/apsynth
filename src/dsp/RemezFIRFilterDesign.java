package dsp;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;

public class RemezFIRFilterDesign extends Applet {

  boolean isStandalone = false;
  static int freqPoints = 250;   // number of points in FR plot
  int order, numBands, numTaps;
  double[] desired, bands, weights;
  double[] a; // filter coefficients
  float trband, atten, ripple, deltaP, deltaS, rippleRatio;
  float[] gain = new float[freqPoints+1];
  GraphPlot frPlot = new GraphPlot();
  RemezFIRFilter rf = new RemezFIRFilter();
  boolean lp, hp, bp, bs;
  //UI components
  Font font = new Font("Dialog", 0, 11);
  Panel pnlDisplay = new Panel();
  Panel pnlCoeffs  = new Panel();
  Panel pnlControls = new Panel();
  BorderLayout borderLayout1 = new BorderLayout();
  GridLayout gridLayout1 = new GridLayout();
  Panel pnlRipple = new Panel();
  FlowLayout flowLayout1 = new FlowLayout();
  Label lblRipple = new Label();
  Panel pnlFrequency = new Panel();
  FlowLayout flowLayout2 = new FlowLayout();
  TextField tfRipple = new TextField();
  Panel pnlOrder = new Panel();
  Panel pnlAtten = new Panel();
  Label lblOrder = new Label();
  TextField tfOrder = new TextField();
  FlowLayout flowLayout3 = new FlowLayout();
  Label lblAtten = new Label();
  TextField tfAtten = new TextField();
  Panel pnlTransitionBandwidth = new Panel();
  FlowLayout flowLayout4 = new FlowLayout();
  Label lblTransitionBandwidth = new Label();
  TextField tfTransitionBandwidth = new TextField();
  FlowLayout flowLayout5 = new FlowLayout();
  Panel pnlButtons = new Panel();
  FlowLayout flowLayout6 = new FlowLayout();
  Button btnDesign = new Button();
  Panel pnlFilterType = new Panel();
  Panel pnlMinPlotGain = new Panel();
  CheckboxGroup cbgFilterType = new CheckboxGroup();
  Checkbox cbBP = new Checkbox();
  Checkbox cbHP = new Checkbox();
  Checkbox cbLP = new Checkbox();
  Checkbox cbBS = new Checkbox();
  Label lblFrequency = new Label();
  TextField tfFreq1 = new TextField();
  Label lblTo = new Label();
  TextField tfFreq2 = new TextField();
  FlowLayout flowLayout7 = new FlowLayout();
  Label lblSpace = new Label();
  Button btnEstimate = new Button();
  Label lblMinPlotGain = new Label();
  FlowLayout flowLayout8 = new FlowLayout();
  Choice chMinPlotGain = new Choice();
  CardLayout cardLayout1 = new CardLayout();
  TextArea txtCoeffs = new TextArea();
  Button btnPlot = new Button();
  Button btnCoeffs = new Button();
  BorderLayout borderLayout2 = new BorderLayout();

  //Get a parameter value
  public String getParameter(String key, String def) {
    return isStandalone ? System.getProperty(key, def) :
      (getParameter(key) != null ? getParameter(key) : def);
  }

  //Construct the applet
  public RemezFIRFilterDesign() {
  }

  //Initialize the applet
  public void init() {
    try { jbInit(); } catch (Exception e) { e.printStackTrace(); }
  }

  //Component initialization
  private void jbInit() throws Exception{
    this.setLayout(borderLayout1);
    this.setSize(new Dimension(500, 350));
    this.add(pnlDisplay, BorderLayout.CENTER);
    this.add(pnlControls, BorderLayout.SOUTH);
    txtCoeffs.setText("Filter coefficients\n\n");
    pnlDisplay.setLayout(cardLayout1);
    pnlDisplay.add(frPlot, "FRPlot");
    pnlDisplay.add(pnlCoeffs, "Coeffs");
    pnlCoeffs.setLayout(borderLayout2);
    pnlCoeffs.add(txtCoeffs, BorderLayout.CENTER);
    pnlFilterType.setLayout(flowLayout7);
    pnlFilterType.add(cbLP, null);
    pnlFilterType.add(cbHP, null);
    pnlFilterType.add(cbBP, null);
    pnlFilterType.add(cbBS, null);
    pnlControls.add(pnlFilterType, null);
    pnlControls.add(pnlRipple, null);
    pnlRipple.add(lblRipple, null);
    pnlRipple.add(tfRipple, null);
    pnlControls.add(pnlFrequency, null);
    pnlFrequency.add(lblFrequency, null);
    pnlFrequency.add(tfFreq1, null);
    pnlFrequency.add(lblTo, null);
    pnlFrequency.add(tfFreq2, null);
    pnlControls.add(pnlAtten, null);
    pnlAtten.add(lblAtten, null);
    pnlAtten.add(tfAtten, null);
    pnlControls.add(pnlTransitionBandwidth, null);
    pnlTransitionBandwidth.add(lblTransitionBandwidth, null);
    pnlTransitionBandwidth.add(tfTransitionBandwidth, null);
    pnlControls.add(pnlOrder, null);
    pnlOrder.add(lblOrder, null);
    pnlOrder.add(tfOrder, null);
    pnlOrder.add(lblSpace, null);
    pnlOrder.add(btnEstimate, null);
    pnlControls.add(pnlMinPlotGain, null);
    pnlMinPlotGain.add(lblMinPlotGain, null);
    pnlMinPlotGain.add(chMinPlotGain, null);
    pnlControls.add(pnlButtons, null);
    pnlButtons.add(btnDesign, null);
    pnlButtons.add(btnPlot, null);
    pnlButtons.add(btnCoeffs, null);
    // Gain frequency response plot
    frPlot.setPlotStyle(GraphPlot.SPECTRUM);
    frPlot.setTracePlot(true);
    frPlot.setLogScale(true);
    pnlControls.setLayout(gridLayout1);
    frPlot.setHorzIntervals(10);
    gridLayout1.setRows(4);
    gridLayout1.setColumns(2);
    flowLayout1.setAlignment(0);
    lblRipple.setFont(font);
    lblRipple.setText("Passband ripple (dB):        ");
    flowLayout2.setAlignment(0);
    tfRipple.setFont(font);
    tfRipple.setText("1");
    tfRipple.setColumns(4);
    tfRipple.addTextListener(new RemezFIRFilterDesign_tfRipple_textAdapter(this));
    lblOrder.setFont(font);
    lblOrder.setText("Order:");
    tfOrder.setFont(font);
    tfOrder.setColumns(4);
    tfOrder.addTextListener(new RemezFIRFilterDesign_tfOrder_textAdapter(this));
    tfOrder.addActionListener(new RemezFIRFilterDesign_tfOrder_actionAdapter(this));
    flowLayout3.setAlignment(0);
    lblAtten.setFont(font);
    lblAtten.setText("Stopband attenuation (dB):");
    tfAtten.setFont(font);
    tfAtten.setText("40");
    tfAtten.setColumns(4);
    tfAtten.addTextListener(new RemezFIRFilterDesign_tfAtten_textAdapter(this));
    flowLayout4.setAlignment(0);
    lblTransitionBandwidth.setFont(font);
    lblTransitionBandwidth.setText("Transition bandwidth: ");
    tfTransitionBandwidth.setFont(font);
    tfTransitionBandwidth.setText("0.02");
    tfTransitionBandwidth.setColumns(5);
    tfTransitionBandwidth.addTextListener(new RemezFIRFilterDesign_tfTransitionBandwidth_textAdapter(this));
    flowLayout5.setAlignment(0);
    flowLayout6.setAlignment(0);
    cbLP.setState(true);
    cbLP.setFont(font);
    cbLP.setLabel("LP");
    cbLP.setCheckboxGroup(cbgFilterType);
    cbLP.addItemListener(new RemezFIRFilterDesign_cbLP_itemAdapter(this));
    cbHP.setFont(font);
    cbHP.setLabel("HP");
    cbHP.setCheckboxGroup(cbgFilterType);
    cbHP.addItemListener(new RemezFIRFilterDesign_cbHP_itemAdapter(this));
    cbBP.setFont(font);
    cbBP.setLabel("BP");
    cbBP.setCheckboxGroup(cbgFilterType);
    cbBP.addItemListener(new RemezFIRFilterDesign_cbBP_itemAdapter(this));
    cbBS.setFont(font);
    cbBS.setLabel("BS");
    cbBS.setCheckboxGroup(cbgFilterType);
    cbBS.addItemListener(new RemezFIRFilterDesign_cbBS_itemAdapter(this));
    cbgFilterType.setSelectedCheckbox(cbLP);
    lblFrequency.setFont(font);
    lblFrequency.setText("Passband:");
    tfFreq1.setFont(font);
    tfFreq1.setText("0");
    tfFreq1.setEditable(false);
    tfFreq1.setColumns(5);
    tfFreq1.addTextListener(new RemezFIRFilterDesign_tfFreq1_textAdapter(this));
    lblTo.setFont(font);
    lblTo.setAlignment(1);
    lblTo.setText("to");
    tfFreq2.setFont(font);
    tfFreq2.setText("0.10");
    tfFreq2.setColumns(5);
    tfFreq2.addTextListener(new RemezFIRFilterDesign_tfFreq2_textAdapter(this));
    lblSpace.setText("    ");
    btnEstimate.setFont(font);
    btnEstimate.setLabel("Estimate");
    flowLayout8.setAlignment(0);
    lblMinPlotGain.setFont(font);
    lblMinPlotGain.setText("Minimum plot gain (dB):");
    chMinPlotGain.setFont(font);
    btnPlot.setEnabled(false);
    btnPlot.setFont(new Font("Dialog", 0, 11));
    btnPlot.setLabel("Plot response");
    btnPlot.addActionListener(new RemezFIRFilterDesign_btnPlot_actionAdapter(this));
    btnCoeffs.setEnabled(false);
    btnCoeffs.setFont(new Font("Dialog", 0, 11));
    btnCoeffs.setLabel("Coefficients");
    btnCoeffs.addActionListener(new RemezFIRFilterDesign_btnCoeffs_actionAdapter(this));
    chMinPlotGain.addItem("-10");
    chMinPlotGain.addItem("-50");
    chMinPlotGain.addItem("-100");
    chMinPlotGain.addItem("-200");
    chMinPlotGain.select("-100");
    chMinPlotGain.addItemListener(new RemezFIRFilterDesign_chMinPlotGain_itemAdapter(this));
    pnlMinPlotGain.setLayout(flowLayout8);
    btnEstimate.addActionListener(new RemezFIRFilterDesign_btnEstimate_actionAdapter(this));
    btnDesign.setEnabled(false);
    btnDesign.setFont(font);
    btnDesign.setLabel("Design");
    btnDesign.addActionListener(new RemezFIRFilterDesign_btnDesign_actionAdapter(this));
    pnlButtons.setLayout(flowLayout6);
    pnlOrder.setLayout(flowLayout5);
    pnlTransitionBandwidth.setLayout(flowLayout4);
    pnlAtten.setLayout(flowLayout3);
    pnlFrequency.setLayout(flowLayout2);
    pnlRipple.setLayout(flowLayout1);
  }

  //Get Applet information
  public String getAppletInfo() {
    return "(C) 1998 Dr Iain A Robin (i.robin@bell.ac.uk)\n\nDerived from\n\nParks-McClellan algorithm for FIR filter design (C version)\nCopyright (C) 1995  Jake Janovetz (janovetz@coewl.cen.uiuc.edu)";
  }

  //Get parameter info
  public String[][] getParameterInfo() {
    return null;
  }

  float log10(float x) {
    return (float)Math.log(x) / (float)Math.log(10.0);
  }

  float minPlotGain() {
    return Math.abs(Float.valueOf(chMinPlotGain.getSelectedItem()).floatValue());
  }

  public float[] filterGain(int freqPoints) {

    // filter gain at (freqPoints+1) uniformly spaced frequency points
    float[] g = new float[freqPoints+1];
    float theta, s, c, sac, sas;
    float t = (float)Math.PI / freqPoints;
    order = numTaps - 1;
    for (int i=0; i<=freqPoints; i++) {
      theta = i*t;
      sac = 0.0f;
      sas = 0.0f;
      for (int k=0; k<=order; k++) {
        c = (float)Math.cos(k*theta);
        s = (float)Math.sin(k*theta);
        sac += c*a[k];
        sas += s*a[k];
      }
      g[i] = 10.0f*log10(sac*sac + sas*sas);
    }
    return g;
  }

  void setDisplayEnabled(boolean b) {
    btnPlot.setEnabled(b);
    btnCoeffs.setEnabled(b);
    chMinPlotGain.setEnabled(b);
  }

  void plotResponse() {
    gain = filterGain(freqPoints);
    frPlot.setYmax(minPlotGain());
    frPlot.setPlotValues(gain);
    cardLayout1.show(pnlDisplay, "FRPlot");
  }

  void listCoeffs() {
    // list coefficients in text area
    txtCoeffs.setText("Parks-McClellan FIR Filter Design\n\n");
    String filterType = new String();
    if (lp) filterType = "Low pass";
    if (hp) filterType = "High pass";
    if (bp) filterType = "Band pass";
    if (bs) filterType = "Band stop";
    txtCoeffs.append("Filter type: " + filterType + "\n");
    if (!bs) txtCoeffs.append("Passband: "); else txtCoeffs.append("Stopband: ");
    txtCoeffs.append(tfFreq1.getText() + " - " + tfFreq2.getText() + "\n");
    txtCoeffs.append("Order: " + String.valueOf(order) + "\n");
    txtCoeffs.append("Passband ripple: " + String.valueOf(ripple) + " dB\n");
    txtCoeffs.append("Transition band: " + String.valueOf(trband) + "\n");
    txtCoeffs.append("Stopband attenuation: " + String.valueOf(atten) + " dB\n\n");
    txtCoeffs.append("Coefficients:\n\n");
    for (int i = 0; i <= order; i++) {
      txtCoeffs.append("a[" + String.valueOf(i) + "] =\t"
                            + String.valueOf(a[i] + "\n"));
    }
    cardLayout1.show(pnlDisplay, "Coeffs");
  }

  void setParameters() {
    ripple = Float.valueOf(tfRipple.getText()).floatValue();
    atten = Float.valueOf(tfAtten.getText()).floatValue();
    deltaP = 0.5f*(1.0f - (float)Math.pow(10.0f, -0.05f*ripple));
    deltaS = (float)Math.pow(10.0f, -0.05f*atten);
    rippleRatio = deltaP / deltaS;
    trband = Float.valueOf(tfTransitionBandwidth.getText()).floatValue();
  }

  void btnEstimate_actionPerformed(ActionEvent e) {
    // estimate required filter order using Kaiser formula
    setParameters();
    int order = Math.round((-10.0f*log10(deltaP*deltaS) - 13)/(14.6f*trband));
    tfOrder.setText(String.valueOf(order));
    btnDesign.setEnabled(true);
    setDisplayEnabled(false);
  }

  void design() {
    numTaps = Integer.parseInt(tfOrder.getText()) + 1;
    a = new double[numTaps];
    float f1 = Float.valueOf(tfFreq1.getText()).floatValue();
    float f2 = Float.valueOf(tfFreq2.getText()).floatValue();
    lp = cbLP.getState();
    hp = cbHP.getState();
    bp = cbBP.getState();
    bs = cbBS.getState();
    if (lp || hp) numBands = 2;
    if (bp || bs) numBands = 3;
    desired = new double[numBands];
    bands   = new double[2*numBands];
    weights = new double[numBands];
    if (lp) {
      desired[0] = 1.0;
      desired[1] = 0.0;
      bands[0] = 0.0;
      bands[1] = f2;
      bands[2] = f2 + trband;
      bands[3] = 0.5;
      weights[0] = 1.0;
      weights[1] = rippleRatio;
    }
    if (hp) {
      desired[0] = 0.0;
      desired[1] = 1.0;
      bands[0] = 0.0;
      bands[1] = f1 - trband;
      bands[2] = f1;
      bands[3] = 0.5;
      weights[0] = rippleRatio;
      weights[1] = 1.0;
    }
    if (bp) {
      desired[0] = 0.0;
      desired[1] = 1.0;
      desired[2] = 0.0;
      bands[0] = 0.0;
      bands[1] = f1 - trband;
      bands[2] = f1;
      bands[3] = f2;
      bands[4] = f2 + trband;
      bands[5] = 0.5;
      weights[0] = rippleRatio;
      weights[1] = 1.0;
      weights[2] = rippleRatio;
    }
    if (bs) {
      desired[0] = 1.0;
      desired[1] = 0.0;
      desired[2] = 1.0;
      bands[0] = 0.0;
      bands[1] = f1 - trband;
      bands[2] = f1;
      bands[3] = f2;
      bands[4] = f2 + trband;
      bands[5] = 0.5;
      weights[0] = 1;
      weights[1] = rippleRatio;
      weights[2] = 1;
    }
    a = rf.remez(numTaps, bands, desired, weights, RemezFIRFilter.BANDPASS);
  }

  void btnDesign_actionPerformed(ActionEvent e) {
    design();
    setDisplayEnabled(true);
  }

  void tfRipple_textValueChanged(TextEvent e) {
    btnDesign.setEnabled(false);
    setDisplayEnabled(false);
  }

  void tfAtten_textValueChanged(TextEvent e) {
    btnDesign.setEnabled(false);
    setDisplayEnabled(false);
  }

  void tfTransitionBandwidth_textValueChanged(TextEvent e) {
    btnDesign.setEnabled(false);
    setDisplayEnabled(false);
  }

  void cbLP_itemStateChanged(ItemEvent e) {
    if (cbLP.getState()) {
      tfFreq1.setEditable(false);
      tfFreq1.setText("0");
      tfFreq2.setEditable(true);
      tfFreq2.selectAll();
      tfFreq2.requestFocus();
      lblFrequency.setText("Passband:");
      setDisplayEnabled(false);
    }
  }

  void cbHP_itemStateChanged(ItemEvent e) {
    if (cbHP.getState()) {
      tfFreq1.setEditable(true);
      tfFreq1.selectAll();
      tfFreq1.requestFocus();
      tfFreq2.setEditable(false);
      tfFreq2.setText("0.5");
      lblFrequency.setText("Passband:");
      setDisplayEnabled(false);
    }
  }

  void cbBP_itemStateChanged(ItemEvent e) {
    if (cbBP.getState()) {
      tfFreq1.setEditable(true);
      tfFreq1.selectAll();
      tfFreq1.requestFocus();
      tfFreq2.setEditable(true);
      tfFreq2.setText("0.5");
      lblFrequency.setText("Passband:");
      setDisplayEnabled(false);
    }
  }

  void cbBS_itemStateChanged(ItemEvent e) {
    if (cbBS.getState()) {
      tfFreq1.setEditable(true);
      tfFreq1.selectAll();
      tfFreq1.requestFocus();
      tfFreq2.setEditable(true);
      tfFreq2.setText("0.5");
      lblFrequency.setText("Stopband:");
      setDisplayEnabled(false);
    }
  }

  void chMinPlotGain_itemStateChanged(ItemEvent e) {
    plotResponse();
  }

  void btnPlot_actionPerformed(ActionEvent e) {
    plotResponse();
  }

  void btnCoeffs_actionPerformed(ActionEvent e) {
    listCoeffs();
  }

  void tfOrder_actionPerformed(ActionEvent e) {
    setParameters();
    btnDesign.setEnabled(true);
    setDisplayEnabled(false);
  }

  void tfOrder_textValueChanged(TextEvent e) {
    setDisplayEnabled(false);
  }

  void tfFreq1_textValueChanged(TextEvent e) {
    setDisplayEnabled(false);
  }

  void tfFreq2_textValueChanged(TextEvent e) {
    setDisplayEnabled(false);
  }

}

class RemezFIRFilterDesign_btnDesign_actionAdapter implements java.awt.event.ActionListener{
  RemezFIRFilterDesign adaptee;

  RemezFIRFilterDesign_btnDesign_actionAdapter(RemezFIRFilterDesign adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.btnDesign_actionPerformed(e);
  }
}


class RemezFIRFilterDesign_tfRipple_textAdapter implements java.awt.event.TextListener{
  RemezFIRFilterDesign adaptee;

  RemezFIRFilterDesign_tfRipple_textAdapter(RemezFIRFilterDesign adaptee) {
    this.adaptee = adaptee;
  }

  public void textValueChanged(TextEvent e) {
    adaptee.tfRipple_textValueChanged(e);
  }
}

class RemezFIRFilterDesign_tfAtten_textAdapter implements java.awt.event.TextListener{
  RemezFIRFilterDesign adaptee;

  RemezFIRFilterDesign_tfAtten_textAdapter(RemezFIRFilterDesign adaptee) {
    this.adaptee = adaptee;
  }

  public void textValueChanged(TextEvent e) {
    adaptee.tfAtten_textValueChanged(e);
  }
}

class RemezFIRFilterDesign_tfTransitionBandwidth_textAdapter implements java.awt.event.TextListener{
  RemezFIRFilterDesign adaptee;

  RemezFIRFilterDesign_tfTransitionBandwidth_textAdapter(RemezFIRFilterDesign adaptee) {
    this.adaptee = adaptee;
  }

  public void textValueChanged(TextEvent e) {
    adaptee.tfTransitionBandwidth_textValueChanged(e);
  }
}

class RemezFIRFilterDesign_cbLP_itemAdapter implements java.awt.event.ItemListener{
  RemezFIRFilterDesign adaptee;

  RemezFIRFilterDesign_cbLP_itemAdapter(RemezFIRFilterDesign adaptee) {
    this.adaptee = adaptee;
  }

  public void itemStateChanged(ItemEvent e) {
    adaptee.cbLP_itemStateChanged(e);
  }
}

class RemezFIRFilterDesign_cbHP_itemAdapter implements java.awt.event.ItemListener{
  RemezFIRFilterDesign adaptee;

  RemezFIRFilterDesign_cbHP_itemAdapter(RemezFIRFilterDesign adaptee) {
    this.adaptee = adaptee;
  }

  public void itemStateChanged(ItemEvent e) {
    adaptee.cbHP_itemStateChanged(e);
  }
}

class RemezFIRFilterDesign_cbBP_itemAdapter implements java.awt.event.ItemListener{
  RemezFIRFilterDesign adaptee;

  RemezFIRFilterDesign_cbBP_itemAdapter(RemezFIRFilterDesign adaptee) {
    this.adaptee = adaptee;
  }

  public void itemStateChanged(ItemEvent e) {
    adaptee.cbBP_itemStateChanged(e);
  }
}

class RemezFIRFilterDesign_cbBS_itemAdapter implements java.awt.event.ItemListener{
  RemezFIRFilterDesign adaptee;

  RemezFIRFilterDesign_cbBS_itemAdapter(RemezFIRFilterDesign adaptee) {
    this.adaptee = adaptee;
  }

  public void itemStateChanged(ItemEvent e) {
    adaptee.cbBS_itemStateChanged(e);
  }
}

class RemezFIRFilterDesign_btnEstimate_actionAdapter implements java.awt.event.ActionListener{
  RemezFIRFilterDesign adaptee;

  RemezFIRFilterDesign_btnEstimate_actionAdapter(RemezFIRFilterDesign adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.btnEstimate_actionPerformed(e);
  }
}

class RemezFIRFilterDesign_chMinPlotGain_itemAdapter implements java.awt.event.ItemListener{
  RemezFIRFilterDesign adaptee;

  RemezFIRFilterDesign_chMinPlotGain_itemAdapter(RemezFIRFilterDesign adaptee) {
    this.adaptee = adaptee;
  }

  public void itemStateChanged(ItemEvent e) {
    adaptee.chMinPlotGain_itemStateChanged(e);
  }
}

class RemezFIRFilterDesign_btnPlot_actionAdapter implements java.awt.event.ActionListener {
  RemezFIRFilterDesign adaptee;

  RemezFIRFilterDesign_btnPlot_actionAdapter(RemezFIRFilterDesign adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.btnPlot_actionPerformed(e);
  }
}

class RemezFIRFilterDesign_btnCoeffs_actionAdapter implements java.awt.event.ActionListener {
  RemezFIRFilterDesign adaptee;

  RemezFIRFilterDesign_btnCoeffs_actionAdapter(RemezFIRFilterDesign adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.btnCoeffs_actionPerformed(e);
  }
}

class RemezFIRFilterDesign_tfOrder_actionAdapter implements java.awt.event.ActionListener{
  RemezFIRFilterDesign adaptee;

  RemezFIRFilterDesign_tfOrder_actionAdapter(RemezFIRFilterDesign adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.tfOrder_actionPerformed(e);
  }
}

class RemezFIRFilterDesign_tfOrder_textAdapter implements java.awt.event.TextListener {
  RemezFIRFilterDesign adaptee;

  RemezFIRFilterDesign_tfOrder_textAdapter(RemezFIRFilterDesign adaptee) {
    this.adaptee = adaptee;
  }

  public void textValueChanged(TextEvent e) {
    adaptee.tfOrder_textValueChanged(e);
  }
}

class RemezFIRFilterDesign_tfFreq1_textAdapter implements java.awt.event.TextListener {
  RemezFIRFilterDesign adaptee;

  RemezFIRFilterDesign_tfFreq1_textAdapter(RemezFIRFilterDesign adaptee) {
    this.adaptee = adaptee;
  }

  public void textValueChanged(TextEvent e) {
    adaptee.tfFreq1_textValueChanged(e);
  }
}

class RemezFIRFilterDesign_tfFreq2_textAdapter implements java.awt.event.TextListener {
  RemezFIRFilterDesign adaptee;

  RemezFIRFilterDesign_tfFreq2_textAdapter(RemezFIRFilterDesign adaptee) {
    this.adaptee = adaptee;
  }

  public void textValueChanged(TextEvent e) {
    adaptee.tfFreq2_textValueChanged(e);
  }
}

