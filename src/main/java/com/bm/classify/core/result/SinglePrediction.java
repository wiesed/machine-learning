package com.bm.classify.core.result;

import java.io.Serializable;

import com.bm.classify.utils.LabelTransformerHelper;
import com.bm.common.enums.Label;


/**
 * Entaelt eine einzelne vorhersage der SVM.
 * 
 * @author Daniel Wiese
 * @since 02.06.2006
 */
public class SinglePrediction implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Die Vorhersagewahrscheinlichekeit
     */
    private final double predictedValue;

    private final double treshold;

    /**
     * Konstructor.
     * 
     * @param predictedValue -
     *            der wert von der SVM
     * @param treshold -
     *            treshold - falls wecher benutzt weurde
     */
    public SinglePrediction(double predictedValue, double treshold) {
        this.predictedValue = predictedValue;
        this.treshold = treshold;
    }

    /**
     * Konstructor mit treshold 0.
     * 
     * @param predictedValue -
     *            der wert von der SVM
     */
    public SinglePrediction(double predictedValue) {
        this(predictedValue, 0);
    }

    /**
     * Returns the predictedValue from SVM.
     * 
     * @return Returns the predictedValue.
     */
    public double getPredictedValue() {
        return this.predictedValue;
    }

    /**
     * Returns the treshold.
     * 
     * @return Returns the treshold.
     */
    public double getTreshold() {
        return this.treshold;
    }

    /**
     * Liefert den label als enumeration.
     * 
     * @author Daniel Wiese
     * @since 02.06.2006
     * @return - den label als enumeration
     */
    public Label getLabel() {
        final double prediction = this.predictedValue + this.treshold;
        return LabelTransformerHelper.getLabelFromDouble(prediction);
    }
    
    

}
