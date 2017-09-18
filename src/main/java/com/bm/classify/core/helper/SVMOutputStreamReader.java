package com.bm.classify.core.helper;

import java.io.BufferedReader;
import java.io.IOException;


/**
 * Liest kontinuerlich den output stream der SVM aus.
 * @author Daniel Wiese
 * @since 02.06.2006
 */
public class SVMOutputStreamReader extends Thread implements SVMProcessStatusAccess {

    private static final int READ_LENGTH = 1;

    private static final long SLEEPTIME = 500;

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
            .getLogger(SVMOutputStreamReader.class);
    
    private final BufferedReader toRead;

    private final StringBuffer buffer;

    private boolean isRunning;

    /**
     * Constructor.
     * @param _toRead - der stram der gelesen werden soll
     */
    public SVMOutputStreamReader(BufferedReader _toRead) {
        toRead = _toRead;

        buffer = new StringBuffer();

        isRunning = false;
    }

   
    public String getAktString() {
        return this.buffer.toString();
    }

    /**
     * Lauft als thread.
     * @author Daniel Wiese
     * @since 02.06.2006
     * @see java.lang.Thread#run()
     */
    public void run() {
        isRunning = true;

        buffer.setLength(0);

        try {
            int read = 0;

            char[] buf = new char[READ_LENGTH];

            while (read >= 0) {
                if (read > 0) {
                    buffer.append(buf, 0, read);

                    // System.out.print(buf[0]);
                } else {
                    try {
                        Thread.sleep(SLEEPTIME);
                    } catch (Exception e) {
                    }
                }

                read = this.toRead.read(buf, 0, READ_LENGTH);
            }
        } catch (IOException e) {
            log.debug("run()- Can't read from input stream.");
        }

        isRunning = false;
    }

    /**
     * Blockiert sol lange bis die SVM keine Ausgaben mehr macht.
     * @author Daniel Wiese
     * @since 02.06.2006
     * @return - den ausgelesenen string.
     */
    public String waitForStop() {
        while (this.isRunning) {
            try {
                Thread.sleep(SLEEPTIME);
            } catch (InterruptedException ie) {
                // nichts tu tun
            }
        }

        return this.getAktString();
    }
}