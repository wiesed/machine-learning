package com.bm.classify.core.exec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.bm.classify.core.SVMFileModel;
import com.bm.classify.core.helper.NativeLibraryLoader;
import com.bm.classify.core.helper.SVMEnviroment;
import com.bm.classify.core.helper.SVMOutputStreamReader;
import com.bm.classify.core.options.SVMKernel;
import com.bm.classify.core.result.LearnResult;
import com.bm.common.util.BmUtil;
import com.bm.common.util.OsTypes;

/**
 * Wrapper fuer die SVMLight-Learn. Startet die SVM und erlernt ein model.
 *
 * 
 * @author Daniel Wiese
 * @since 02.06.2006
 */
public class SVMLightLearn {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
            .getLogger(SVMLightLearn.class);

    /**
     * This attribute value should is used as negative if the value type is
     * CLASSIFICATION.
     */
    public static final String NEGATIVE_CLASS = "-1";

    /**
     * This attribute value should is used as poitive if the value type is
     * CLASSIFICATION.
     */
    public static final String POSITIVE_CLASS = "+1";

    private static final NativeLibraryLoader nativeSVMLoader = new NativeLibraryLoader();

    /**
     * Constructor.
     */
    public SVMLightLearn() {
    }

    /**
     * Creates configuration files and start the learning process on the given
     * examples. Returns a model or null, if it can not be created.
     * 
     * @author Daniel Wiese
     * @since 02.06.2006
     * @param fileModel -
     *            des SVM model zum lernen
     * @param kernel -
     *            der zu verwendende svm kernel
     * @param timeout -
     *            der timeout beim lernen
     * @return - des lernergebniss (summary)
     */
    public LearnResult learn(SVMFileModel fileModel, SVMKernel kernel, long timeout) {
        if (fileModel.getSvmModel() == null) {
            throw new RuntimeException("Der pfad fuer das zu erlernende model ist leer!");
        } else if (fileModel.getSvmTrainTestData() == null) {
            throw new RuntimeException("Keine Trainingsdaten vorhanden!");
        } else if (!fileModel.getSvmTrainTestData().exists()) {
            throw new RuntimeException("Keine Trainingsdaten vorhanden (am angegeben Ort> "
                    + fileModel.getSvmTrainTestData().getAbsolutePath() + "!");
        }

        final String filenameTrain = fileModel.getSvmTrainTestData().getAbsolutePath();
        final String filenameModel = fileModel.getSvmModel().getAbsolutePath();
        log.info("SVMlight starts learning.");

        // Prozess starten
        final String command = this.buildCommand(filenameTrain, filenameModel, SVMKernel
                .getStringValue(kernel));

        int code = 0;

        long startTime = System.currentTimeMillis();

        long endeGeschaetzt = startTime + timeout;

        log.debug("SVMlight called with command '" + command + "', please be patient...");

        try {

            if (BmUtil.getOs() == OsTypes.OSTYPE_LINUX) {
                final String filePath = nativeSVMLoader.prepareSVMEnviroment().getSVMLearn()
                        .getAbsolutePath();
                Process process_perm = Runtime.getRuntime().exec("chmod +x " + filePath);
                int exitValue = process_perm.waitFor();
                if (exitValue != 0) {
                    log.error("Can�t change svm-light learn to executable");
                }
            }

            final Process process = Runtime.getRuntime().exec(command);

            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));

            BufferedReader error = new BufferedReader(new InputStreamReader(process
                    .getErrorStream()));

            SVMOutputStreamReader readStdIn = new SVMOutputStreamReader(in);

            SVMOutputStreamReader readStdErr = new SVMOutputStreamReader(error);

            readStdIn.start();

            readStdErr.start();

            boolean notFinished = true;

            while (notFinished) {
                try {
                    if (endeGeschaetzt <= System.currentTimeMillis()) {
                        // Timeout
                        process.destroy();

                        code = LearnResult.CODE_TIMEOUT;

                        notFinished = false;

                        log.error("Time out trat ein (" + (timeout / 1000) + " sek)!");

                        throw new RuntimeException("Timeout trat ein");
                    } else {
                        code = process.exitValue();

                        notFinished = false;
                    }
                } catch (IllegalThreadStateException the) {
                    // warten
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ie) {
                        // nichts zu tun
                    }
                    // end sleep
                }
                // end try-catch
            }
            // end whlie

            long stopTime = System.currentTimeMillis();

            LearnResult back = new LearnResult(filenameModel, readStdIn.waitForStop(), readStdErr
                    .waitForStop(), code, stopTime - startTime);

            error.close();

            in.close();

            // SVM - Training war erfolgreich -ergebniss zurueck
            return back;
        } catch (IOException e) {
            log.error("SVMlight:  " + "IOError whlie running SVMlight!", e);

            throw new RuntimeException("IOError whlie running SVMlight!");
        } catch (Exception e) {
            log.error("SVMlight has returned with code " + code, e);

            throw new RuntimeException("SVMlight has returned with code " + code);
        }
    }

    private String buildCommand(String filenameTrain, String filenameModel, String options) {
        if ((filenameTrain == null) || (filenameModel == null) || (options == null)) {
            throw new RuntimeException("Einer der uebergebenen Parameter war null");
        }

        SVMEnviroment enviroment = nativeSVMLoader.prepareSVMEnviroment();
        StringBuffer buf = new StringBuffer(enviroment.getSVMLearn().getAbsolutePath());

        buf.append(" ").append(options).append(" ").append(filenameTrain).append(" ").append(
                filenameModel);

        return buf.toString();
    }

}
