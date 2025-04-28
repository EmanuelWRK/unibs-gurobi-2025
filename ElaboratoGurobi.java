package it.unibs.ro.elaborato.coppia22;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.gurobi.gurobi.*;

/**
 * Elaborato sull'utilizzo del software Gurobi 12.0.0 per l'a.A. 24/25
 * @author Coppia 22: Ferrari Emanuel, Lupica Benedetto
 */
public class ElaboratoGurobi {
	
    private static final String Q_MASSIMO = "Q = %.4f\n";
	private static final String VARIABILI_DUEFASI = "\nvalore variabili: [";
	private static final String OBIETTIVO_DUEFASI = "\nfunzione obiettivo = %.4f";
	private static final String VARIAZIONE_P_R = "variazione p_";
	private static final String VARIAZIONE_BMAX_E = "variazione bmax_";
	private static final String QUESITO_III = "\nQUESITO III:";
	private static final String COMPONENTI_DUALE = "\nComponenti duale: ";
	private static final String NO = "no";
	private static final String SÌ = "sì";
	private static final String MULTIPLA = "Multipla: ";
	private static final String DEGENERE = "Degenere: ";
	private static final String FUNZIONE_OBIETTIVO = "Funzione obiettivo = %.4f\n";
	private static final String QUESITO_I = "\nQUESITO I:";
	private static final String QUESITO_II = "\nQUESITO II:";
	private static final String GRUPPO_COMPONENTI = "Gruppo 22\nComponenti: Ferrari E., Lupica B.";
	private static final String DIVIDER = "\n================\n";
	private static final String VINCOLO_MASSIMA_PERCENTUALE = "Vincolo percentuale massima elemento ";
	private static final String VINCOLO_MINIMA_PERCENTUALE = "Vincolo percentuale minima elemento ";
	private static final String VINCOLO_PRODUZIONE_TOTALE = "Vincolo di produzione totale";
	private static final String ERROR_CREATING_DIRECTORY = "Error creating directory: ";
	private static final String DIRECTORY_CREATED_SUCCESSFULLY = "Directory created successfully.";
	private static final String DIRECTORY_ALREADY_EXISTS = "Directory already exists.";
	private static final String LOG_NAME = "miscelazioneApplicataMetallurgia.log";
	private static final int NR_ROTTAMI = 20;
	private static final int NR_ELEMENTI = 10;
	private static final int NR_CONSTRAINTS = 21;
	private static final double MAX_PRODUCTION = 23100.00;
	private static final int PEDICE_R = 9;
	private static final int PEDICE_E = 6;
	private static final double Z_MAX = 6740.3605;
	private static final String VAR_ROTTAME = "Rottame ";
	private static final String VAR_DUEFASI = "Variabile y";
	/*
	 * Costante che indica il path del file .log
	 * ATTENZIONE! Cambiare!
	 */
	private static final String LOG_PATH = "/Users/emanuel/Desktop";

    static double[] pr = {
    		0.360,
    		0.215,
    		0.352,
    		1.210,
    		0.783,
    		2.300,
    		24.000,
    		0.359,
    		1.240,
    		0.360,
    		0.405,
    		1.379,
    		0.360,
    		17.300,
    		0.625,
    		20.000,
    		0.430,
    		0.457,
    		0.739,
    		1.796
    		};
    
    /*
     * Array di numeri reali che rappresentano le percentuali di rottame che viene effettivamente fuso per l'utlizzo
     */
    static double[] ur = {
    		94.000,
    		94.000,
    		90.000,
    		100.000,
    		94.000,
    		100.000,
    		60.000,
    		94.000,
    		94.000,
    		94.000,
    		94.000,
    		94.000,
    		94.000,
    		100.000,
    		94.000,
    		90.000,
    		94.000,
    		94.000,
    		94.000,
    		94.000	
    };
    
    /*
     * Array di numeri reali che rappresentano le percentuali minime per ogni elemento chimico che deve essere presente nei rottami
     */
    static double[] beta_min = {
    		0.000,
    		0.000,
    		0.000,
    		0.000,
    		0.000,
    		0.000,
    		0.000,
    		0.000,
    		0.000,
    		0.000
    };
    
    /*
     * Array di numeri reali che rappresentano le percentuali minime per ogni elemento chimico che deve essere presente nei rottami
     */
    static double[] beta_max = {
    		0.286,
    		0.800,
    		0.600,
    		0.037,
    		0.045,
    		0.433,
    		0.424,
    		0.191,
    		0.300,
    		0.030
    };
    
    /*
     * Matrice di numeri reali che rappresenta la percentuale di elemento chimico nella
     * j-esima colonna utilizzato nella i-esima riga
     */
    static double[][] theta = {
    		{0.0038, 0.1189, 0.0545, 0.0040, 0.0024, 0.0446, 0.0349, 0.0086, 0.0485, 0.0009},
    		{0.2700, 0.9000, 0.4000, 0.0220, 0.0180, 0.3300, 0.2500, 0.0600, 0.1800, 0.0010},
    		{99.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0000},
    		{1.8000, 67.0000, 17.0000, 0.2000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0000},
    		{0.3246, 0.3915, 0.7667, 0.0096, 0.0014, 5.6123, 0.1972, 1.1000, 0.1531, 0.4676},
    		{0.0250, 0.0000, 99.5000, 0.0100, 0.0150, 0.0000, 0.0000, 0.0000, 0.0000, 0.0000},
    		{0.0000, 0.1500, 0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0000},
    		{0.2000, 1.2000, 0.2000, 0.0150, 0.0080, 1.1500, 0.1500, 0.0800, 0.2000, 0.0050},
    		{0.0522, 0.9371, 0.2533, 0.0354, 0.0020, 14.2392, 3.8210, 0.2765, 2.7993, 0.0213},
    		{0.5030, 0.8186, 0.4580, 0.0085, 0.0016, 0.1983, 0.1175, 0.0550, 0.1762, 0.0105},
    		{0.0200, 0.2000, 0.2000, 0.0020, 0.0010, 0.0300, 0.0300, 0.0100, 0.0300, 0.0000},
    		{0.1000, 0.6500, 0.2500, 0.0100, 0.0080, 0.2000, 9.0000, 0.1000, 0.1500, 0.0010},
    		{0.1200, 0.2000, 0.2000, 0.0180, 0.0250, 0.1100, 0.1000, 0.0100, 0.2750, 0.0010},
    		{0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 99.8000, 0.0000, 0.0000, 0.0000},
    		{0.2640, 0.5240, 0.2500, 0.0070, 0.0020, 0.8470, 3.0520, 0.4230, 0.1430, 0.0300},
    		{0.0000, 0.0000, 0.8000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 80.5000},
    		{0.0200, 0.2000, 0.2000, 0.0020, 0.0010, 0.0300, 0.0300, 0.0100, 0.0300, 0.0000},
    		{0.1510, 0.6560, 0.0000, 0.0060, 0.0020, 1.4300, 0.5000, 0.9000, 0.2580, 0.0000},
    		{0.1562, 0.5471, 0.2062, 0.0066, 0.0011, 2.2311, 0.1798, 0.9916, 0.1388, 0.0456},
    		{0.1370, 0.7000, 0.0000, 0.0060, 0.0030, 0.0570, 3.0330, 3.2720, 0.1600, 0.0000}
    };
	
	
	public static void main(String[] args) throws GRBException {
		
		/**
		 * INIZIALIZZAZIONE DELL'AMBIENTE E DEL MODELLO
		 */
		
        /*
         * Metodo scritto dal dott. A. Gobbi durante le esercitazioni.
         * Crea la directory in cui salvare il file .log in caso non esista quella indicata
         */
        createDirectoryIfNotExists(LOG_PATH);

        /*
         * Istanziamo l'environment passando la dir path e il nome del file
         */
        GRBEnv env = new GRBEnv(LOG_PATH + LOG_NAME);
        
        /*
         * Infine istanziamo il modello sulla base dell'oggetto GRBEnv
         */
        
        impostaParametri(env);
        GRBModel model = new GRBModel(env);

		
        /**
         * CREAZIONE DELLE VARIABILI Xr CHE INDICANO LE QUANTITÀ IN KG DI ROTTAME r DA PRODURRE
         * E DEI PARAMETRI
         */
        GRBVar[] rottami = setVars(model);
        
        /**
         * DEFINIZIONE DELLA FUNZIONE OBIETTIVO
         */
        setObj(model, rottami);
        
        /**
         * VINCOLI
         */
        
        /**
         * VINCOLO DI PRODUZIONE TOTALE
         */
        maxQuantityConstr(model, rottami, MAX_PRODUCTION);
        
        /**
         * VINCOLI DI PERCENTUALE MINIMA E MASSIMA
         */
        minMaxPercentage(model, rottami, MAX_PRODUCTION);
        
        /**
         * MODELLO ED ENVIRONMENT PER IL QUESITO III
         */
        GRBEnv envIII = new GRBEnv();
        impostaParametri(envIII);
        GRBModel modelIII = new GRBModel(envIII);
        GRBVar[] rottamiIII = setVars(modelIII);
        GRBVar[] y = twoPhasesVariables(modelIII);
        twoPhasesObject(modelIII, y);
        twoPhasesConstraints(modelIII, MAX_PRODUCTION, rottamiIII, y);
        /**
         * OTTIMIZZAZIONE
         */
        model.update();
        model.optimize();
        modelIII.update();
        modelIII.optimize();
        /**
         * STAMPA A VIDEO
         */
        if (model.get(GRB.IntAttr.Status) == GRB.OPTIMAL) {
	        System.out.println(DIVIDER);
	        System.out.println(GRUPPO_COMPONENTI);
	        
	        /**
	         * QUESITO 1
	         */
	        System.out.println(QUESITO_I);
	        System.out.printf(FUNZIONE_OBIETTIVO, model.get(GRB.DoubleAttr.ObjVal));

	        /**
	         * VARIABILI IN BASE
	         */
	        inBase(model);
	        /**
	         * COEFFICIENTI DI COSTO RIDOTTO
	         */
	        ccr(model);
	        /**
	         * SOLUZIONE DEGENERE
	         */
	        System.out.print(DEGENERE);
				if(degenere(model)) {
					System.out.println(SÌ);
				} else System.out.println(NO);
	        /**
	         * OTTIMO MULTIPLO
	         */
	        System.out.print(MULTIPLA);
	        if(multipla(model)) {
	        	System.out.println(SÌ);
	        } else System.out.println(NO);
	        /**
	         * VINCOLI ATTIVI
	         */
	        vincoliAttivi(model);
	        
	        /**
	         * PREZZI D'OMBRA NULLI
	         */
	    	System.out.print(COMPONENTI_DUALE);
	        lambdaAZero(model);
	        
	        /**
	         * QUESITO II
	         */
	        System.out.println(QUESITO_II);
	        
	        /**
	         * VARIAZIONE PR
	         */
	        System.out.print(VARIAZIONE_P_R + PEDICE_R + " = ");
	        rangeObj(model);
	        /**
	         * VARIAZIONE BETA_E_MAX
	         */
	        System.out.print(VARIAZIONE_BMAX_E+ PEDICE_E + " = ");
	        rangeConstr(model, MAX_PRODUCTION);
	        /**
	         * MASSIMO VALORE DI Q
	         */

	        System.out.printf(Q_MASSIMO,maxQforZMAX(model, rottami));
	        
	        /**
	         * QUESITO III
	         */
	        System.out.println(QUESITO_III);
	        /**
	         * FUNZIONE OBIETTIVO SOLUZIONE INIZIALE (METODO DUE FASI)
	         */
	        System.out.printf(OBIETTIVO_DUEFASI, twoPhasesObjectValue(modelIII));
	        
	        /**
	         * VALORI DELLE VARIABILI DELLA SOLUZIONE INIZIALE
	         */
	        System.out.print(VARIABILI_DUEFASI);
	        twoPhasesVarValue(modelIII);
	        System.out.print("]\n");
	        
	        System.out.print(DIVIDER);
	        
	        env.dispose();
	        envIII.dispose();
	        model.dispose();
	        modelIII.dispose();
        }
	}
	
	
	/**
	 * Metodo che crea l'espressione lineare della funzione obiettivo e la inserisce nel modello
	 * @param model
	 * @param rottami
	 * @throws GRBException
	 */
	public static void setObj(GRBModel model, GRBVar[] rottami) throws GRBException{
        /*
         * Espressione lineare che identifica la funzione obiettivo (object function)
         */
        GRBLinExpr of = new GRBLinExpr();
        
        /*
         * Iterazione per aggiungere all'espressione lineare le variabili che identificano
         * i rottami con coefficiente pari al prezzo €/kg
         */
        for(int i = 0; i < NR_ROTTAMI; i++) {
        	of.addTerm(pr[i], rottami[i]);
        }
        
        /*
         * Problema di minimo con of espressione lineare come funzione obiettivo
         */
        model.setObjective(of, GRB.MINIMIZE);
        
	}
    
	
	/**
	 * Metodo che aggiunge le variabili decisionali al modello
	 * @param model
	 * @return Array di GRBVar
	 * @throws GRBException
	 */
	public static GRBVar[] setVars(GRBModel model) throws GRBException{
        /*
         * Array di variabili GRB di dimensione 20 (indicizzate da 0 a 19)
         */
        GRBVar[] rottami = new GRBVar[NR_ROTTAMI];
        
        for(int i = 0; i < NR_ROTTAMI; i++) {
        	rottami[i] = model.addVar(0, GRB.INFINITY, 0, GRB.CONTINUOUS, VAR_ROTTAME + (i + 1));
        }
        
        return rottami;
	}
	
	
	/**
	 * Metodo che crea l'espressione lineare del vincolo d'uguaglianza rappresentante la quantità 
	 * in kg di acciaio da produrre e la inserisce nel modello
	 * @param model
	 * @param rottami
	 * @throws GRBException
	 */
	public static void maxQuantityConstr(GRBModel model, GRBVar[] rottami, double maxProd) throws GRBException{
        GRBLinExpr lhsMaxQty = new GRBLinExpr();
        for(int i = 0; i < NR_ROTTAMI; i++) {
        	lhsMaxQty.addTerm(ur[i] * 0.01, rottami[i]);
        }
        
        /*
         * Aggiunta del vincolo nel modello
         */
        model.addConstr(lhsMaxQty, GRB.EQUAL, maxProd, VINCOLO_PRODUZIONE_TOTALE);
	}
	
	/**
	 * Metodo che crea l'espressione lineare dei vincoli di minima e massima percentuale degli e
	 * elementi chimici che devono essere presenti (in kg) nei Q kg prodotti
	 * @param model
	 * @param rottami
	 * @throws GRBException
	 */
	public static void minMaxPercentage(GRBModel model, GRBVar[] rottami, double maxProd) throws GRBException{
        GRBLinExpr lhs;
		lhs = new GRBLinExpr();
        
        for(int j = 0; j < NR_ELEMENTI; j++) {
        	for(int i = 0; i < NR_ROTTAMI; i++) {
	        		/*
	        		 * Percentuale di elemento chimico j dopo la lavorazione del rottame i in quantità xr
	        		 */
	        		double currCoeff = theta[i][j] * 0.01 * ur[i] * 0.01;
	        		lhs.addTerm(currCoeff, rottami[i]);
        		}
        	
        	/*
        	 * Percentuali minime e massime sulla produzione finale
        	 */
    		double rhsMin = maxProd * beta_min[j] * 0.01;
    		double rhsMax = maxProd * beta_max[j] * 0.01;
    		
    		model.addConstr(lhs, GRB.GREATER_EQUAL, rhsMin, VINCOLO_MINIMA_PERCENTUALE + (j + 1));
    		model.addConstr(lhs, GRB.LESS_EQUAL, rhsMax, VINCOLO_MASSIMA_PERCENTUALE + (j + 1));
    		lhs.clear();
        }
	}
	
    
	/**
	 * Metodo che stabilisce se la soluzione ottima trovata è degenere.
	 * La soluzione è degenere se una variabile è contemporaneamente in base e a valore nullo
	 * @param model
	 * @return
	 * @throws GRBException
	 */
	public static boolean degenere(GRBModel model) throws GRBException {
        /*
         * Controlla tra le variabili non di slack se la variabile in esame è sia in base che con valore nullo
         */
        for(GRBVar v: model.getVars()) {
        	if(v.get(GRB.IntAttr.VBasis) == 0 && v.get(GRB.DoubleAttr.X) == 0) {
        		return true;
        	}
        }
        
        /*
         * Controlla tra le variabili di slack se la variabile in esame è sia in base che con valore nullo
         */
        for(GRBConstr c: model.getConstrs()) {
        	if(c.get(GRB.IntAttr.CBasis) == 0 && c.get(GRB.DoubleAttr.Slack) == 0) {
        		return true;
        	}
        }
        
        return false;
    }
    /**
     * Metodo che controlla se la soluzione ottima trovata è multipla
     * @param model
     * @return booleano che indica se la soluzione ottima corrente è multipla
     * @throws GRBException
     */
    public static boolean multipla(GRBModel model) throws GRBException {
        
        /*
         * Controlla tra le variabili non di slack se la variabile in esame è sia fuori base che con ccr nullo
         */
        for(GRBVar v: model.getVars()) {
        	if(v.get(GRB.IntAttr.VBasis) != 0 && v.get(GRB.DoubleAttr.RC) == 0 ) {
        		return true;
        	}
        }
        
        /*
         * Controlla tra le variabili di slack se la variabile in esame è sia fuori base che con ccr nullo
         */
        for(GRBConstr c: model.getConstrs()) {
        	if(c.get(GRB.IntAttr.CBasis) != 0 && c.get(GRB.DoubleAttr.Pi) == 0) {
        		return true;
        	}
        }
        
        return false;
    }
    /**
     * Metodo che stampa a video i coefficienti di costo ridotto
     * @param model
     * @throws GRBException
     */
    public static void ccr(GRBModel model) throws GRBException {
        
        System.out.print("Coefficienti di costo ridotto: [");
        for(GRBVar v: model.getVars()) {
        	System.out.printf(" %.4f ", v.get(GRB.DoubleAttr.RC));
        }
        for(GRBConstr c: model.getConstrs()) {
        	System.out.printf(" %.4f ", c.get(GRB.DoubleAttr.Pi));
        }
        System.out.println("]");
    }
    /**
     * Metodo che stabilisce quali variabili del modello sono in base o non in base,
     * stampa a video 1 se la variabile è in base, 0 altrimenti.
     * Se l'attributo della variabile VBasis (CBasis in caso di variabile di vincolo/slack è a 0
     * allora si trova in base, è fuori base altrimenti
     * @param model
     * @throws GRBException
     */
    public static void inBase(GRBModel model) throws GRBException {
    	System.out.print("Variabili in base (1 se in base, 0 altrimenti): [");
        for(GRBVar v: model.getVars()) {
        	if(v.get(GRB.IntAttr.VBasis) == 0) {
        	System.out.print(" 1 ");
        	} else System.out.print(" 0 ");
        }
        for(GRBConstr c: model.getConstrs()) {
        	if(c.get(GRB.IntAttr.CBasis) == 0) {
        		System.out.print(" 1 ");
        	} else System.out.print(" 0 ");
        }
        System.out.println("]");
    }
    /**
     * Metodo che stabilisce quali vincoli sono attivi. Un vincolo è attivo se
     * il valore della corrispondente slack è nullo
     * @param model
     * @throws GRBException
     */
    public static void vincoliAttivi(GRBModel model) throws GRBException {
        System.out.print("Vincoli attivi: ");
        for(GRBConstr c: model.getConstrs()) {
        	if(c.get(GRB.DoubleAttr.Slack) == 0) {
        		System.out.printf("%s. ", c.get(GRB.StringAttr.ConstrName));
        	}
        }
    }
    /**
     * Metodo che stampa a video il numero di componenti nulle a 0.
     * Una componente del problema duale è nulla se il vincolo a esso associato è non attivo
     * @param model
     * @throws GRBException
     */
    public static void lambdaAZero(GRBModel model) throws GRBException {
    	
    	int count = 0;
    	for(GRBConstr c: model.getConstrs()) {
    		if(c.get(GRB.DoubleAttr.Slack) != 0) {
    			count++;
    		}
    	}
    	System.out.println(count);
    }
    /**
     * Metodo che svolge un'analisi di sensitività sui coefficienti di costo ridotto della soluzione ottima
     * per trovare l'intervallo di valori tali per cui il parametro pr non fa variare la soluzione ottima.
     * Infine stampa a video il range
     * @param model
     * @throws GRBException
     */
	public static void rangeObj(GRBModel model) throws GRBException {
		
		double lowerBound = model.getVarByName(VAR_ROTTAME + PEDICE_R).get(GRB.DoubleAttr.SAObjLow) <= -GRB.INFINITY? Double.NEGATIVE_INFINITY:
			model.getVarByName(VAR_ROTTAME + PEDICE_R).get(GRB.DoubleAttr.SAObjLow);
		double upperBound = model.getVarByName(VAR_ROTTAME + PEDICE_R).get(GRB.DoubleAttr.SAObjUp) <= GRB.INFINITY? Double.POSITIVE_INFINITY:
			model.getVarByName(VAR_ROTTAME + PEDICE_R).get(GRB.DoubleAttr.SAObjUp);
		
		
		System.out.printf("[");
		if(lowerBound != Double.NEGATIVE_INFINITY) 
			System.out.printf("%.4f, ", lowerBound);
		else
			System.out.printf("-INF, ", lowerBound);
		if(upperBound != Double.POSITIVE_INFINITY) 
			System.out.printf("%.4f]", upperBound);
		else
			System.out.print("+INF]\n");
			
	}
	/**
	 * Metodo che svolge un'analisi di sensitività sui termini noti della soluzione ottima
	 * per trovare l'intervallo di valori tali per cui il parametro beta_e_max non fa variare la soluzione ottima.
	 * Infine stampa a video il range
	 * @param model
	 * @throws GRBException
	 */
	public  static void rangeConstr(GRBModel model, double maxProd) throws GRBException {
		double lowerBound = model.getConstrByName(VINCOLO_MASSIMA_PERCENTUALE + PEDICE_E).get(GRB.DoubleAttr.SARHSLow) <= -GRB.INFINITY? Double.NEGATIVE_INFINITY:
			model.getConstrByName(VINCOLO_MASSIMA_PERCENTUALE + PEDICE_E).get(GRB.DoubleAttr.SARHSLow) / maxProd;
		double upperBound = model.getConstrByName(VINCOLO_MASSIMA_PERCENTUALE + PEDICE_E).get(GRB.DoubleAttr.SARHSUp) >= GRB.INFINITY? Double.POSITIVE_INFINITY:
			model.getConstrByName(VINCOLO_MASSIMA_PERCENTUALE + PEDICE_E).get(GRB.DoubleAttr.SARHSUp) / maxProd;
		
		System.out.printf("[");
		if(lowerBound != Double.NEGATIVE_INFINITY) 
			System.out.printf("%.4f, ", lowerBound);
		else
			System.out.printf("-INF, ", lowerBound);
		if(upperBound != Double.POSITIVE_INFINITY) 
			System.out.printf("%.4f]", upperBound);
		else
			System.out.print("+INF]\n");
	}
	
	/**
	 * Ritorna il valore di Q massimo tale per cui la funzione obiettivo all'ottimo è minore o uguale
	 * a un valore z_max dato
	 * @throws GRBException
	 */
	public static double maxQforZMAX(GRBModel model, GRBVar[] rottami) throws GRBException {
		
		double i = MAX_PRODUCTION;
		do {
			for(GRBConstr c: model.getConstrs()) {
				model.remove(c);
			}
			i++;
	        maxQuantityConstr(model, rottami, i);
	        minMaxPercentage(model, rottami, i);
			model.optimize();
		} while(model.get(GRB.DoubleAttr.ObjVal) <= Z_MAX);
		return --i;
	}
	/**
	 * Imposta l'obiettivo del modello delle due fasi come la somma di tutte le yi variabili ausiliarie
	 * 
	 * @param model, y
	 * @throws GRBException
	 */
	public static void twoPhasesObject(GRBModel model, GRBVar[] y) throws GRBException {
        /*
         * Espressione lineare che identifica la funzione obiettivo (object function)
         */
        GRBLinExpr ofIII = new GRBLinExpr();
        
        /*
         * Iterazione per aggiungere all'espressione lineare le variabili che identificano
         * i rottami con coefficiente pari al prezzo €/kg
         */
        for(int i = 0; i < y.length; i++) {
        	ofIII.addTerm(1.0, y[i]);
        }
        
        /*
         * Problema di minimo con of espressione lineare come funzione obiettivo
         */
        model.setObjective(ofIII, GRB.MINIMIZE);
	}	
	/**
	 * Aggiunge le variabili ausiliare binarie yi al modello
	 * 
	 * @param model
	 * @throws GRBException
	 * @return GRBVar[]
	 */
	public static GRBVar[] twoPhasesVariables(GRBModel model) throws GRBException {
        GRBVar[] y = new GRBVar[NR_CONSTRAINTS];
        
        for(int i = 0; i < NR_CONSTRAINTS; i++) {
        	y[i] = model.addVar(0, 1, 0, GRB.BINARY, VAR_DUEFASI + (i + 1));
        }
        
        return y;
	}
	/**
	 * Metodo che aggiunge al modello del due fasi tutti i vincoli con l'aggiunta della variabile ausiliaria
	 * 
	 * @param model
	 * @param maxProd
	 * @param rottami
	 * @param y
	 * @throws GRBException
	 */
	public static void twoPhasesConstraints(GRBModel model, double maxProd, GRBVar[] rottami, GRBVar[] y) throws GRBException{
		GRBLinExpr lhs;
		lhs = new GRBLinExpr();
        int ct = 0;
        /*
         * Percentuale minima con y_i
         */
        for(int j = 0; j < NR_ELEMENTI; j++) {
        	for(int i = 0; i < NR_ROTTAMI; i++) {
	        		/*
	        		 * Percentuale di elemento chimico j dopo la lavorazione del rottame i in quantità xr
	        		 */
	        		double currCoeff = theta[i][j] * 0.01 * ur[i] * 0.01;
	        		lhs.addTerm(currCoeff, rottami[i]);
        		}
        	
        	/*
        	 * Percentuali minime e massime sulla produzione finale
        	 */
    		double rhsMin = maxProd * beta_min[j] * 0.01;
    		lhs.addTerm(1, y[ct]);
    		model.addConstr(lhs, GRB.GREATER_EQUAL, rhsMin, VINCOLO_MINIMA_PERCENTUALE + (j + 1));
    		ct++;
    		lhs.clear();
        }
        /*
         * Percentuale massima con yi
         */
        for(int j = 0; j < NR_ELEMENTI; j++) {
        	for(int i = 0; i < NR_ROTTAMI; i++) {
	        		/*
	        		 * Percentuale di elemento chimico j dopo la lavorazione del rottame i in quantità xr
	        		 */
	        		double currCoeff = theta[i][j] * 0.01 * ur[i] * 0.01;
	        		lhs.addTerm(currCoeff, rottami[i]);
        		}
        	
        	/*
        	 * Percentuali minime e massime sulla produzione finale
        	 */
    		double rhsMax = maxProd * beta_max[j] * 0.01;
    		lhs.addTerm(1, y[ct]);
    		model.addConstr(lhs, GRB.LESS_EQUAL, rhsMax, VINCOLO_MASSIMA_PERCENTUALE + (j + 1));
    		ct++;
    		lhs.clear();
        }
        
        GRBLinExpr lhsMaxQty = new GRBLinExpr();
        for(int i = 0; i < NR_ROTTAMI; i++) {
        	lhsMaxQty.addTerm(ur[i] * 0.01, rottami[i]);
        }
        lhsMaxQty.addTerm(1, y[ct]);
        /*
         * Aggiunta del vincolo nel modello
         */
        model.addConstr(lhsMaxQty, GRB.EQUAL, maxProd, VINCOLO_PRODUZIONE_TOTALE);
    }
	/**
	 * Metodo che prende il modello della prima fase del metodo delle due fasi e, se l'obiettivo è nullo,
	 * ritorna il valore della funzione obiettivo del problema iniziale
	 * @param model
	 * @return
	 * @throws GRBException
	 */
	public static double twoPhasesObjectValue(GRBModel model) throws GRBException {
        /*
         * Valido solo se la funzione obiettivo della fase 1 è nulla (tutte le variabili ausiliare sono fuori base)
         */
        if(model.get(GRB.DoubleAttr.ObjVal) == 0) {
        	double objFunc = 0;
        	/*
        	 * Le xi all'ottimo del problema nella prima fase del metodo sono le variabili della soluzione iniziale
        	 * del problema iniziale: per trovare la funzione obiettivo posso semplicemente moltiplicare i valori delle xi
        	 * per i rispettivi costi €/kg
        	 */
        	for(int i = 0; i < NR_ROTTAMI; i++) {
        		objFunc += pr[i] * model.getVarByName(VAR_ROTTAME + (i + 1)).get(GRB.DoubleAttr.X);
        	}
        	
        	return objFunc;
        }
        
        return Double.NaN;
	}
	/**
	 * Metodo che prende il modello della prima fase del metodo delle due fasi e
	 * stampa a video i valori delle xq variabili del problema iniziale (in soluzione iniziale)
	 * @param model
	 * @throws GRBException
	 */
	public static void twoPhasesVarValue(GRBModel model) throws GRBException {
        for(GRBVar v: model.getVars()) {
        	if(v.get(GRB.StringAttr.VarName).startsWith(VAR_ROTTAME))
        		System.out.printf(" %.4f ", v.get(GRB.DoubleAttr.X));
        }
        for(GRBConstr c: model.getConstrs()) {
        	System.out.printf(" %.4f ", c.get(GRB.DoubleAttr.Slack));
        }
	}
	/**
     * Check or create a directory in a specific path, passed as parameter
     * 
     * @param directoryPath Directory Path that needs to be created
     */
	public static void createDirectoryIfNotExists(String directoryPath) {
        // Check if the directory exists
        Path path = Paths.get(directoryPath);
        if (Files.exists(path) && Files.isDirectory(path)) {
            System.out.println(DIRECTORY_ALREADY_EXISTS);
        } else {
            // Create the directory
            try {
                Files.createDirectories(path);
                System.out.println(DIRECTORY_CREATED_SUCCESSFULLY);
            } catch (Exception e) {
                System.err.println(ERROR_CREATING_DIRECTORY + e.getMessage());
            }
        }
    }
	
	private static void impostaParametri(GRBEnv env) throws GRBException 
	{
		env.set(GRB.IntParam.OutputFlag, 0);
		env.set(GRB.IntParam.Method, 0);
		env.set(GRB.IntParam.Presolve, 0);
	}
}