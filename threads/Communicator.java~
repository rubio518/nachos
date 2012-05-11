package nachos.threads;

import nachos.machine.*;

/**
 * A <i>communicator</i> allows threads to synchronously exchange 32-bit
 * messages. Multiple threads can be waiting to <i>speak</i>,
 * and multiple threads can be waiting to <i>listen</i>. But there should never
 * be a time when both a speaker and a listener are waiting, because the two
 * threads can be paired off at this point.
 */
public class Communicator {
	private Condition2 speaky;
	private Condition2 listeny;
	private Lock locky = new Lock();
	int actSpeakers = 0;
	int actListeners = 0;
	boolean isSpeaking = false;
	int data = 0;
	
    /**
     * Allocate a new communicator.
     */
    public Communicator() {
		speaky = new Condition2(locky);
		listeny = new Condition2(locky);
	}

    /**
     * Wait for a thread to listen through this communicator, and then transfer
     * <i>word</i> to the listener.
     *
     * <p>
     * Does not return until this thread is paired up with a listening thread.
     * Exactly one listener should receive <i>word</i>.
     *
     * @param	word	the integer to transfer.
     */
    public void speak(int word) {
	    locky.acquire();
		    actSpeakers++;
		    while((actListeners==0)||(isSpeaking)){
			speaky.sleep();
		    }
		    isSpeaking = true;
		    data = word;
		    listeny.wake();
		    // isSpeaking = false;
		    actSpeakers--;
	    locky.release();
		
    }

    /**
     * Wait for a thread to speak through this communicator, and then return
     * the <i>word</i> that thread passed to <tt>speak()</tt>.
     *
     * @return	the integer transferred.
     */    
    public int listen() {
	    int ret;
		locky.acquire();
			actListeners++;
			while((!isSpeaking)||(actListeners > 1)){
				if(actSpeakers>0){
					speaky.wake();
				}				
				listeny.sleep();
			}
			
			
			ret = data;
			isSpeaking = false;
			speaky.wake();
			actListeners--;
			
			
		locky.release();
	
	    
	    
	return ret;
    }
}
