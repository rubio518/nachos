package nachos.threads;
import nachos.ag.BoatGrader;

public class Boat
{
    static BoatGrader bg;
	public static int oahuAdults = 0;
	public static int oahuChildren = 0;
	public static int molokaiAdults = 0;
	public static int molokaiChildren = 0;
	
	public static int childPasados = 0;    //cuantos niños se fueron desde que yo estoy en oahu 
	public static boolean pasaronTodos = false;
	public static int genteBote = 0;
	public static int lugarBote = 1;
	public static Lock bote = new Lock();

	public static Condition2  condChild =  new Condition2(bote);  //wake() , sleep() , wakeall()
		public static Condition2  condChildP =  new Condition2(bote);  //wake() , sleep() , wakeall()
		public static Condition2  condChildM =  new Condition2(bote);  //wake() , sleep() , wakeall()
		
	public static Condition2  condAdult =  new Condition2(bote); 

	
	
    public static void selfTest()
    {
	BoatGrader b = new BoatGrader();
	
		System.out.println("\n ***Testing Boats with only 2 children***");
		begin(0, 2, b);

//  	System.out.println("\n ***Testing Boats with 2 children, 1 adult***");
//  	begin(1, 2, b);

//  	System.out.println("\n ***Testing Boats with 3 children, 3 adults***");
//  	begin(3, 3, b);
    }

    public static void begin( int adults, int children, BoatGrader b )
    {
	// Store the externally generated autograder in a class
	// variable to be accessible by children.
	bg = b;
	
	// Instantiate global variables here
	
	// Create threads here. See section 3.4 of the Nachos for Java
	// Walkthrough linked from the projects page.
	/** creo adultos y niños.. mi "contador" seria una variable true */

	Runnable r = new Runnable() {
		public void run() {
			AdultItinerary();
		}
    };
	Runnable q = new Runnable() {
		public void run() {
			ChildItinerary();
		}
    };
	for(int i=0;i < adults;i++){
		oahuAdults++;	
		KThread t = new KThread(r);
        t.setName("Adulto " + i);
		t.fork();
	}
	
	for(int i=0;i < children;i++){
		oahuChildren++;
		KThread t = new KThread(q);
        t.setName("patojo " + i);
		t.fork();
	}
	
	
        
        

    }

    static void AdultItinerary()
    {
		int lugar = 1; //estoy en oahu
		bote.acquire();
			while((genteBote > 0)||(pasaronTodos)||(childPasados==0)||(lugar!=lugarBote)||(lugar==2)){
				condAdult.sleep();
			}
			lugar=2;
			montarseA();
		bote.release();
	
    }

    static void ChildItinerary()
    {
		int lugar = 1;//estoy en oahu 
		while(true){
		bote.acquire();
			while((pasaronTodos)||(genteBote > 1)||(lugar!=lugarBote)){
				condChild.sleep();
				condChildP.sleep();
			}
			if(lugar == 1){
				if(genteBote == 1){
					genteBote=2;
					lugar = 2; 
					condChild.wake();//despierto a el piloto
					montarseC(1); //oahu -> molokai patojo de pasaje
/* This is where you should put your solutions. Make calls
	    to the BoatGrader to show that it is synchronized. For
	    example:	System.out.println("afsd"+i); 
	       bg.AdultRowToMolokai();
	    indicates that an adult has rowed the boat across to Molokai
		*/
				}
				
				if(genteBote == 0){
					if(oahuChildren > 1){
						genteBote=1;
					//	condChildP.wake(); //despierto al pasajero 
						   //cambiarle lugar al patojo 
						condChildP.wake();
						condChild.sleep(); //me duermo hasta que llegue pasajero
						lugar = 2; 
						montarseC(0);//oahu -> molokai patojo de piloto
					}else if(oahuAdults > 0){		
						genteBote = 1;
						lugar = 2;
						condAdult.wake(); //adultos solo pueden ir de pilotos y solo de oahu -> molokai 
						montarseA();
					}
				}
			}if(lugar == 2){
				while((pasaronTodos)||(genteBote > 0)||(lugar!=lugarBote)){
					condChildM.sleep();
				}
				lugar = 1;
				montarseC(2); //2 molokai -> oahu patojo de piloto
					
			}
			
		
			
	
		bote.release();
		}
		
    }
	public static void montarseA(){
			//montarse en el bote
			bg.AdultRowToMolokai();
			oahuAdults--;
			condChildM.wake();
			lugarBote = 2;
		
	}
	public static void montarseC(int direccion){
		//direccion = 0 oahu -> molokai patojo de piloto
		//direccion = 1 oahu -> molokai patojo de pasajero
		//direccion = 2 molokai -> oahu patojo de piloto
		if(direccion == 0){
			bg.ChildRowToMolokai();
			oahuChildren--;
			childPasados++;
			lugarBote = 2;
			
		}else if (direccion == 1){
			bg.ChildRideToMolokai();
			oahuChildren--;
			childPasados++;
			lugarBote = 2;
		}else if (direccion ==2){
			bg.ChildRowToOahu();
			oahuChildren++;
			childPasados--;
			lugarBote = 1;
		}
		
	//montarse en el bote
	}
    static void SampleItinerary()
    {
		// Please note that this isn't a valid solution (you can't fit
		// all of them on the boat). Please also note that you may not
		// have a single thread calculate a solution and then just play
		// it back at the autograder -- you will be caught.
		System.out.println("\n ***Everyone piles on the boat and goes to Molokai***");
		bg.AdultRowToMolokai();
		bg.ChildRideToMolokai();
		bg.AdultRideToMolokai();
		bg.ChildRideToMolokai();
    }
    
}
