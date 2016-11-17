import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import cc.mallet.fst.SimpleTagger;

public class SequenceTagging {
	
	public static void main(String[] args) throws Exception{
		
		boolean mode = false;
		
		if(mode){
			String ar1[] = {"--train", "true", "--model-file", "carCRF","train/processedTrain"};
			SimpleTagger.main(ar1);
		}else{
			String ar2[] = {"--model-file", "carCRF",  "test/processed/0"};
		    SimpleTagger.main(ar2);
		}
		

		
//	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//	    PrintStream ps = new PrintStream(baos);
	    
//	    PrintStream old = System.out;
//	    System.setOut(ps);
	    
//	    System.out.flush();
//	    System.setOut(old);
//	    System.out.println("Here: " + baos.toString()); 
	}

}
