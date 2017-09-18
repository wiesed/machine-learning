package com.bm.classify.utils;

import com.bm.common.enums.Label;

/**
 * Hilft einen SVM label von verschiedenen darstellungsformen. hin und her zu
 * trnasformieren.
 * 
 * @author Daniel Wiese
 * @since 06.08.2006
 */
public final class LabelTransformerHelper {

    private LabelTransformerHelper() {
        // intentionally left blank
    }

    /**
     * Transformiert einen integer label in ein label enum objekt.
     * 
     * @author Daniel Wiese
     * @since 06.08.2006
     * @param intLabel -
     *            der integer label
     * @return - der transformierte label
     */
    public static Label getLabelFromInteger(int intLabel) {
        Label back = Label.UNLABLED;
        if (intLabel == 0) {
            back = Label.UNLABLED;
        } else if (intLabel > 0) {
            back = Label.POSITIVE;
        } else {
            back = Label.NEGATIVE;
        }

        return back;
    }

    /**
     * Hilfsmethode um aus einem double wert einen label zu bekommen.
     * 
     * @author Daniel Wiese
     * @since 02.06.2006
     * @param value -
     *            der value als double wert
     * @return - einen label
     */
    public static Label getLabelFromDouble(double value) {
        Label back = Label.UNLABLED;
        if (value == 0) {
            back = Label.UNLABLED;
        } else if (value > 0) {
            back = Label.POSITIVE;
        } else if (value < 0) {
            back = Label.NEGATIVE;
        }

        return back;
    }

}
