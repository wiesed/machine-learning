package com.bm.classify.core.helper;

/**
 * Interface um die ausgaben der svm zu lesen.
 * @author Daniel Wiese
 * @since 02.06.2006
 */
public interface SVMProcessStatusAccess {
    //~ Methods ----------------------------------------------------------------

    /**
     * Liefert den ausgabe string der SVM.
     * @return - den ausgabe string der SVM
     */
    String getAktString();

}
