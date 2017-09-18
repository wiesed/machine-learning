package com.bm.common.property;

import java.util.ArrayList;
import java.util.List;

/**
 * Keys um Werte aus demConfigurationsfile zu lesen.
 * 
 * @author Daniel Wiese
 * @since 04.12.2005
 */
public enum ConfigKeys {

	/**
	 * Zum drucken des Build-Zeitpunktes.
	 */
	BUILD_INFORMATION(false),

	/**
	 * Ob echter handel oder simulationen.
	 */
	ECHTER_HANDEL(false),

	/**
	 * Flag um die scheduler auch am wochenende zu testen.
	 */
	SIMULIERE_IST_HANDELSZEIT(false),

	/**
	 * Flag um das system im produktionsmodus schnell zu starten (zum testen).
	 */
	FAST_STARTUP(false),

	/**
	 * Wieviel Tage in den cache hereinpassen sollen.
	 */
	CACHE_SIZE_IN_TAGEN(false),

	/**
	 * Nach vieviel tagen der cache neu geladen werden soll.
	 */
	RELOAD_SIZE_IN_TAGEN(false),

	/***************************************************************************
	 * ob das gesteuerte handeln (mit paketen, nur zugelassen aktien) an sein
	 * soll wenn es an ist, wird die grabber que kontrolliert gesteuert .
	 **************************************************************************/
	ACTIVATE_TADING_CONTROLL(false),

	/**
	 * Wie gross das zeitliche limit fuer ein frame sein soll, so dass es noch
	 * inkplett geladen wird (z.B. 3 minuten also um 16.27 wuerden wir das
	 * 16.00-16.30 frame laden)
	 */
	LIMIT_FOR_INCOMPLETE_FRAME(false),

	/**
	 * Die zulaessigen WKNs der Handelskonfuguration.
	 */
	ZULAESSIGE_WKNS,
	/**
	 * Wie viele frames bei der minimumsuche (minimaler verlust) zuruckgegangen
	 * werden soll.
	 */
	FRAME_LENGTH_FOR_MINIMUM,

	/**
	 * Wieviel Prozent die Aktie minimal gafallen sein soll, um gekauft zu
	 * werden.
	 */
	MIN_PROZ_GEFALLEN_FUER_KAUF,

	/**
	 * Wieviel Prozent die Aktie minimal gafallen sein soll, um HOT zu sein.
	 */
	MIN_PROZ_GEFALLEN_FUER_HOT,

	/**
	 * Wieviel Prozent muss die Aktie gestiegen sein.
	 */
	MIN_PROZ_GESTEIGEN,

	/**
	 * Die Minimale anzahl von transaktionen.
	 */
	MIN_TRANSACTIONS,

	/**
	 * Der minimale Kurs.
	 */
	MIN_KURS,

	/**
	 * Die maximale volatilitat.
	 */
	MAX_VOLATILITAET,

	/**
	 * Die minimale volatilitat.
	 */
	MIN_VOLATILITAET,

	/**
	 * Der gewuenschter Gewinn in prozent.
	 */
	GEWUENSCHTER_GEWINN_IN_PROZ,

	/**
	 * Der maximaler Verlust in proz (Stop loss).
	 */
	MAX_VERLUST_IN_PROZ,

	/**
	 * Die anzehl der frames wie lang eine aktie gehalten wird.
	 */
	HALTEZEIT_IN_FRAMES,

	/**
	 * Die Verjaheung nach Verlust.
	 */
	VERJAEHRUNG_NACH_VERLUST_TAGE,

	/**
	 * Die Wartezeit nach kauf in Tagen.
	 */
	WARTEZEIT_NACH_GEWINN_IN_TAGEN,

	/**
	 * Die Wartezeit nach kauf in Tagen.
	 */
	SELL_RULES(true, true),

	/**
	 * Die News basiserenden rules fuer die kaufsperre.
	 */
	NEWS_RULES_KAUFSPERRE(true, true),

	/**
	 * Die News basiserenden rules fuer den verkauf.
	 */
	NEWS_RULES_SOFORTVERKAUF(true, true),

	/**
	 * Die pops fuer den index.
	 */
	INDEX_FENSTER, INDEX_GEFALLEN, KAUFSPERRE_NACH_INDEX_GEFALLEN,

	// GEFORDERTE_NACHRICHTEN_IM_FENSTER,

	/**
	 * Pruefsumme fuer die Handelskonfiguration.
	 */
	MD5_HASH_TRADING_CONFIG(false),

	/**
	 * Kaufsperre: Maximale Anzahl zulaessiger Kaufgelegenheiten.
	 */
	ANZAHL_MAXIMALE_KAUFGELEGENHEITEN,

	/**
	 * Kaufsperre: Groesse des beobachteten Fensters mit Kaufgelgenheiten.
	 */
	KAUFGELEGENHEITEN_FENSTER_IN_HBT,

	/**
	 * Wie der index gedampft werden soll > Wert zwischen 0..1. Null heisst der
	 * index geht in die kaufentscheidung icht ein. Wenn der index faellt wird
	 * ein hoerer rueckgang der Aktie gefordert als wen der index stigt.
	 */
	FAKTOR_MININUM_INDEX;

	private final boolean istGAGeneriert;

	private final boolean istComposite;

	/**
	 * @param istGAGeneriert
	 */
	private ConfigKeys(boolean istGAGeneriert, boolean istComposite) {
		this.istGAGeneriert = istGAGeneriert;
		this.istComposite = istComposite;
	}

	/**
	 * .
	 */
	private ConfigKeys(boolean istGAGeneriert) {
		this(istGAGeneriert, false);
	}

	/**
	 * Der Standardfall ist GA, nicht Komposite.
	 */
	private ConfigKeys() {
		this(true, false);
	}

	/**
	 * alle Key, die Ga-Attribute sind.
	 * 
	 * @return list alle Key, die Ga-Attribute sind.
	 */
	public static List<ConfigKeys> getAlleGAAttribute() {
		List<ConfigKeys> list = new ArrayList<ConfigKeys>();
		for (int i = 0; i < values().length; i++) {
			if (values()[i].istGAGeneriert) {
				list.add(values()[i]);
			}
		}
		return list;
	}

	/**
	 * alle Key, die Ga-Attribute und nicht Composite sind.
	 * 
	 * @return alle Key, die Ga-Attribute und nicht Composite sind.
	 */
	public static List<ConfigKeys> getGAAttributeNichtComposite() {
		List<ConfigKeys> list = new ArrayList<ConfigKeys>();
		for (int i = 0; i < values().length; i++) {
			if (values()[i].isIstGAGeneriert() && !values()[i].isIstComposite()) {
				list.add(values()[i]);
			}
		}
		return list;
	}

	public boolean isIstGAGeneriert() {
		return istGAGeneriert;
	}

	public boolean isIstComposite() {
		return istComposite;
	}

}
