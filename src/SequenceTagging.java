import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.ArrayList;

import cc.mallet.fst.SimpleTagger;

public class SequenceTagging {

	public static void main(String[] args) throws Exception{

		int  mode = 2;

		if(mode == 0){
			String ar1[] = {"--train", "true", "--model-file", "carCRF","train/processedTrain"};
			SimpleTagger.main(ar1);
		}else if(mode == 1){

			File file = new File("test/processed_new_sites");
			
			ArrayList<String> ar = new ArrayList<String>(); 

			BufferedReader bf = new BufferedReader(new FileReader(file));
			String line;
			while((line = bf.readLine()) != null){
				ar.add(line.split(" ")[0]);
			}
			bf.close();

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(baos);		
			PrintStream old = System.out;
			System.setOut(ps);

			String[] ar2 = {"--model-file", "carCRF",  file.getAbsolutePath()};
			SimpleTagger.main(ar2);

			String[] labels = baos.toString().split("\\r\\n|\\n|\\r");

			System.out.flush();
			System.setOut(old);

			if(labels.length != ar.size()) throw new Exception("Different number of labels main features");

			for(int x=0;x<labels.length;x++){
				System.out.println(labels[x] + ": " + ar.get(x));
				
			}

			System.out.println();

			ar.clear();


		}else if(mode == 2){
			String[] ar2 = {"--test", "perclass" , "--model-file", "carCRF",  "test/processed_old_sites"};
			SimpleTagger.main(ar2);
		}






	}

}
