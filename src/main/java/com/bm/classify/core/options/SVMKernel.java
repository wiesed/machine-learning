package com.bm.classify.core.options;

/**
 * Representiert den SVM kernel -t int - type of kernel function:<br>
 * 0: linear(default) <br>
 * 1: polynomial (s a*b+c)^d <br>
 * 2: radial basis function exp(-gamma||a-b||^2) <br>
 * 3: sigmoid tanh(s a*b + c) 4: user defined kernel from kernel.h<br>.
 * 
 * @author Daniel Wiese
 * @since 02.06.2006
 */
public enum SVMKernel {
    /** linear(default). * */
    LINEAR,
    /** polynomial (s a*b+c)^d. * */
    POLYNOMIAL,
    /** radial basis function exp(-gamma||a-b||^2). * */
    RADIAL,
    /** sigmoid tanh(s a*b + c) 4: user defined kernel from kernel.h. * */
    SIGMOID;

    /**
     * Kinvertiert den wert in eine svm option.
     * @author Daniel Wiese
     * @since 02.06.2006
     * @param kernel - die kernel option 
     * @return - svm option
     */
    public static String getStringValue(SVMKernel kernel) {
        String back = null;
        switch (kernel) {
        case LINEAR:
            back = "-t 0 -b 0";
            break;
        case POLYNOMIAL:
            back = "-t 1 -d 2 -b 0";
            break;
        case RADIAL:
            back = "-t 2 -g 0.01 -b 0";
            break;
        case SIGMOID:
            back = "-t 3 -s 0.02 -r 0.01 -b 0";
            break;
        default:
            back = "-t 0 -b 0";
        }

        return back;
    }

}
