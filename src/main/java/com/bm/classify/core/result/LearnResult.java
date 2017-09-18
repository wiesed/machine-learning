package com.bm.classify.core.result;

import java.io.File;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Serializable;
import java.io.StringReader;

import com.bm.classify.core.helper.LinearHyperplane;
import com.bm.classify.exception.SVMModelNotValidException;

/**
 * Enthaelt das ergebniss eines SVM lern laufs.
 * 
 * @author Daniel Wiese
 * @since 02.06.2006
 */
public class LearnResult implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final int CODE_TIMEOUT = 10;

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
            .getLogger(LearnResult.class);

    private final String error;

    private double leaveOneOutError;

    private boolean leaveOneOutPerformaceEstimated;

    private double leaveOneOutPrecision;

    private double leaveOneOutRecall;

    private LinearHyperplane linModel;

    private final File model;

    private final String out;

    private final int returnCode;

    private final boolean sucessfulRun;

    private final long totalTime;

    private double xiAlphaError;

    private boolean xiAlphaPerformaceEstimated;

    private double xiAlphaPrecision;

    private double xiAlphaRecall;

    /**
     * 
     * Constructor.
     * 
     * @param filenameModel -
     *            des model (file)
     * @param stdOut -
     *            die ausgabe der svm wahrend des laufs
     * @param stError -
     *            die ausgabe auf dem error stream
     * @param code -
     *            der result code
     * @param _time -
     *            die geammt lern zeit
     */
    public LearnResult(String filenameModel, String stdOut, String stError, int code, long _time) {
        model = new File(filenameModel);

        sucessfulRun = model.exists() && (code == 0);

        totalTime = _time;

        out = stdOut;

        error = stError;

        returnCode = code;

        this.parseOutput();

        if (sucessfulRun) {
            try {
                linModel = new LinearHyperplane(model);

                linModel.toString();
            } catch (SVMModelNotValidException svme) {
                log.error("Model konnte nicht gelesen werden", svme);

            }
        }
    }

    /**
     * Returns the error.
     * 
     * @return Returns the error.
     */
    public String getError() {
        return this.error;
    }

    /**
     * Returns the leaveOneOutError.
     * 
     * @return Returns the leaveOneOutError.
     */
    public double getLeaveOneOutError() {
        return this.leaveOneOutError;
    }

    /**
     * Returns the leaveOneOutPerformaceEstimated.
     * 
     * @return Returns the leaveOneOutPerformaceEstimated.
     */
    public boolean isLeaveOneOutPerformaceEstimated() {
        return this.leaveOneOutPerformaceEstimated;
    }

    /**
     * Returns the leaveOneOutPrecision.
     * 
     * @return Returns the leaveOneOutPrecision.
     */
    public double getLeaveOneOutPrecision() {
        return this.leaveOneOutPrecision;
    }

    /**
     * Returns the leaveOneOutRecall.
     * 
     * @return Returns the leaveOneOutRecall.
     */
    public double getLeaveOneOutRecall() {
        return this.leaveOneOutRecall;
    }

    /**
     * Returns the linModel.
     * 
     * @return Returns the linModel.
     */
    public LinearHyperplane getLinModel() {
        return this.linModel;
    }

    /**
     * Returns the model.
     * 
     * @return Returns the model.
     */
    public File getModel() {
        return this.model;
    }

    /**
     * Returns the out.
     * 
     * @return Returns the out.
     */
    public String getOut() {
        return this.out;
    }

    /**
     * Returns the returnCode.
     * 
     * @return Returns the returnCode.
     */
    public int getReturnCode() {
        return this.returnCode;
    }

    /**
     * Returns the sucessfulRun.
     * 
     * @return Returns the sucessfulRun.
     */
    public boolean isSucessfulRun() {
        return this.sucessfulRun;
    }

    /**
     * Returns the totalTime.
     * 
     * @return Returns the totalTime.
     */
    public long getTotalTime() {
        return this.totalTime;
    }

    /**
     * Returns the xiAlphaError.
     * 
     * @return Returns the xiAlphaError.
     */
    public double getXiAlphaError() {
        return this.xiAlphaError;
    }

    /**
     * Returns the xiAlphaPerformaceEstimated.
     * 
     * @return Returns the xiAlphaPerformaceEstimated.
     */
    public boolean isXiAlphaPerformaceEstimated() {
        return this.xiAlphaPerformaceEstimated;
    }

    /**
     * Returns the xiAlphaPrecision.
     * 
     * @return Returns the xiAlphaPrecision.
     */
    public double getXiAlphaPrecision() {
        return this.xiAlphaPrecision;
    }

    /**
     * Returns the xiAlphaRecall.
     * 
     * @return Returns the xiAlphaRecall.
     */
    public double getXiAlphaRecall() {
        return this.xiAlphaRecall;
    }

    private double extractValue(String line) {
        // SVM-light output format: "error<=50%", "precision=>40%" etc.
        String pre = line.substring(line.indexOf("="));

        if (pre.startsWith("=>")) {
            pre = pre.substring(1);
        }

        String value = pre.substring(1, pre.indexOf("%"));

        return Double.parseDouble(value) / 100.0;
    }

    private void parseOutput() {
        leaveOneOutError = -1;

        leaveOneOutPrecision = -1;

        leaveOneOutRecall = -1;

        xiAlphaError = -1;

        xiAlphaPrecision = -1;

        xiAlphaRecall = -1;

        try {
            LineNumberReader lnr = new LineNumberReader(new StringReader(out));

            String line = null;

            while ((line = lnr.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("XiAlpha-estimate of the error:")) {
                    xiAlphaError = extractValue(line);
                } else if (line.startsWith("XiAlpha-estimate of the recall:")) {
                    xiAlphaRecall = extractValue(line);
                } else if (line.startsWith("XiAlpha-estimate of the precision:")) {
                    xiAlphaPrecision = extractValue(line);
                } else if (line.startsWith("Leave-one-out estimate of the error:")) {
                    leaveOneOutError = extractValue(line);
                } else if (line.startsWith("Leave-one-out estimate of the recall:")) {
                    leaveOneOutRecall = extractValue(line);
                } else if (line.startsWith("Leave-one-out estimate of the precision:")) {
                    leaveOneOutPrecision = extractValue(line);
                }
            }

            // return pv;
        } catch (IOException ioe) {
            log.error("SVM light output could not be parsed!", ioe);
        } catch (NumberFormatException nfe) {
            log.error("SVM light output could not be parsed!", nfe);
        } catch (IndexOutOfBoundsException ioobe) {
            log.error("SVM light output could not be parsed!", ioobe);
        }

        if ((xiAlphaError != -1) && (xiAlphaPrecision != -1) && (xiAlphaRecall != -1)) {
            this.xiAlphaPerformaceEstimated = true;
        }

        if ((leaveOneOutError != -1) && (leaveOneOutPrecision != -1) && (leaveOneOutRecall != -1)) {
            this.leaveOneOutPerformaceEstimated = true;
        }
    }

    /**
     * Gibt das ergeniss aus.
     * 
     * @author Daniel Wiese
     * @since 02.06.2006
     * @return - des lernergebniss
     */
    public String printResults() {
        StringBuffer buf = new StringBuffer();

        buf.append("Zeit: ").append(totalTime).append(" ms\n");

        if (this.sucessfulRun) {
            if (this.xiAlphaPerformaceEstimated) {
                buf.append("XiAlphaErorr: " + this.xiAlphaError).append("\n");

                buf.append("XiAlphaPrecision: " + this.xiAlphaPrecision).append("\n");

                buf.append("XiAlphaRecall: " + this.xiAlphaRecall).append("\n");
            }

            if (this.leaveOneOutPerformaceEstimated) {
                buf.append("LeaveOneOutErorr: " + this.leaveOneOutError).append("\n");

                buf.append("LeaveOneOutPrecision: " + this.leaveOneOutPrecision).append("\n");

                buf.append("LeaveOneOutRecall: " + this.leaveOneOutRecall).append("\n");
            }
        } else {
            buf.append("ReturnCode: ").append(this.returnCode);

            if (returnCode == CODE_TIMEOUT) {
                buf.append(" (Timeout)");
            }

            buf.append("\n");

            buf.append("ModelFileExist: ").append(this.model.exists()).append("\n");
        }

        return buf.toString();
    }

    /**
     * To string.
     * 
     * @return - die formattierte ausgabe
     * @author Daniel Wiese
     * @since 02.06.2006
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();

        buf.append("[Time=").append(totalTime).append(" ms");

        if (this.sucessfulRun) {
            if (this.xiAlphaPerformaceEstimated) {
                buf.append(", XiAlphaErorr= " + this.xiAlphaError);

                buf.append(", XiAlphaPrecision= " + this.xiAlphaPrecision);

                buf.append(", XiAlphaRecall= " + this.xiAlphaRecall);
            }

            if (this.leaveOneOutPerformaceEstimated) {
                buf.append(", LeaveOneOutErorr= " + this.leaveOneOutError);

                buf.append(", LeaveOneOutPrecision= " + this.leaveOneOutPrecision);

                buf.append(", LeaveOneOutRecall= " + this.leaveOneOutRecall);
            }
        } else {
            buf.append(", ReturnCode=").append(this.returnCode);

            if (returnCode == CODE_TIMEOUT) {
                buf.append(" (Timeout)");
            }

            buf.append(", ModelFileExist()=").append(this.model.exists());
        }

        buf.append("]");

        return buf.toString();
    }

}
