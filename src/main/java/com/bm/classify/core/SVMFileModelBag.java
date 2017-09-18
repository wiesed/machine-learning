package com.bm.classify.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse kann mehrere SVM File Modelle enthalten (wird u.a. fuer Bagging
 * verwendet).
 */
public class SVMFileModelBag implements ModelBag<SVMFileModel> {

    private final List<SVMFileModel> fileModells;

    /**
     * Standardkonstructor.
     */
    public SVMFileModelBag() {
        this.fileModells = new ArrayList<SVMFileModel>();
    }

    /**
     * Konstructor mit einem model.
     * @param first - des erste model
     */
    public SVMFileModelBag(SVMFileModel first) {
        this();
        this.fileModells.add(first);
    }

    /**
     * Fuegt ein SVM file model hinzu.
     * 
     * @author Daniel Wiese
     * @since 02.06.2006
     * @param toAdd -
     *            das model welches hinzugefuegt werden soll.
     */
    public void addSVMFileModel(SVMFileModel toAdd) {
        this.fileModells.add(toAdd);
    }

    /**
     * True wenn leer.
     * 
     * @return - true wenn leer.
     * @author Daniel Wiese
     * @since 02.06.2006
     * @see com.bm.classify.core.ModelBag#isEmpty()
     */
    public boolean isEmpty() {
        return this.fileModells.isEmpty();
    }

    /**
     * Die groesse.
     * 
     * @return - die groesse
     * @author Daniel Wiese
     * @since 02.06.2006
     * @see com.bm.classify.core.ModelBag#size()
     */
    public int size() {
        return this.fileModells.size();
    }

    /**
     * Hilfsmethode falls nur ein model existiert.
     * 
     * @return - des erste model
     * @author Daniel Wiese
     * @since 02.06.2006
     * @see com.bm.classify.core.ModelBag#getFirstModel()
     */
    public SVMFileModel getFirstModel() {
        return this.fileModells.get(0);
    }

    /**
     * Liefert das model an der entsprechenden position.
     * 
     * @param i -
     *            die position
     * @return - des model
     * @author Daniel Wiese
     * @since 02.06.2006
     * @see com.bm.classify.core.ModelBag#getModel(int)
     */
    public SVMFileModel getModel(int i) {
        return this.fileModells.get(i);
    }

}
