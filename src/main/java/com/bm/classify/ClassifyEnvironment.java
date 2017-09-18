package com.bm.classify;

/**
 * Legt die umgebung fest unter der klassifiziert werden soll.
 * @author wiesed
 *
 */
public enum ClassifyEnvironment {

	/** windows settings. **/
	WINDOWS("svmLight-windows-8.4.jar", "svm_classify.exe", "svm_learn.exe", false, null),

	/** linux settings. **/
	LINUX("svmLight-linux-8.4.jar", "svm_classify", "svm_learn", false, null),

	/** mac settings. **/
	MAC("svmLight-mac-8.4.jar", "svm_classify", "svm_learn", true, "a+x");

	private String identyfier;
	private String classify_name;
	private String learn_name;
	private final boolean executeChmodCommand;
	private final String chmodCommand;

	/**
	 * Constructor
	 * @param identyfier der identifyer der die jar datei lokalisiert
	 * @param classify_name der dateiname der classify datei
	 * @param learn_name der dateiname der learn datei
	 * @param executeChmodCommand true wenn ein chmod command nach dem entpacken ausgefuhrt werden soll
	 * @param chmodCommand das chmod kommando was ausgefuhrt werden soll
	 */
	private ClassifyEnvironment(String identyfier, String classify_name,
			String learn_name, boolean executeChmodCommand, String chmodCommand) {
		this.identyfier = identyfier;
		this.classify_name = classify_name;
		this.learn_name = learn_name;
		this.executeChmodCommand = executeChmodCommand;
		this.chmodCommand = chmodCommand;
	}

	/**
	 * Retruns the identyfier.
	 * 
	 * @return the identyfier
	 */
	public String getIdentyfier() {
		return identyfier;
	}

	/**
	 * Retruns the classify_name.
	 * 
	 * @return the classify_name
	 */
	public String getClassify_name() {
		return classify_name;
	}

	/**
	 * Retruns the learn_name.
	 * 
	 * @return the learn_name
	 */
	public String getLearn_name() {
		return learn_name;
	}

	/**
	 * Retruns the executeChmodCommand.
	 * @return the executeChmodCommand
	 */
	public boolean isExecuteChmodCommand() {
		return executeChmodCommand;
	}

	/**
	 * Retruns the chmodCommand.
	 * @return the chmodCommand
	 */
	public String getChmodCommand() {
		return chmodCommand;
	}
	

}
