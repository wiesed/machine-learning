package com.bm.classify.exception;

/**
 * Wird geworfen wenn das model nicht gueltig ist.
 * @author Daniel Wiese
 * @since 02.06.2006
 */
public class SVMModelNotValidException extends Exception {
  
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     * @param msg - die nachricht
     */
    public SVMModelNotValidException(String msg) {
        super(msg);
    }
}
