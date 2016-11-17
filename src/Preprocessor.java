import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Preprocessor {

	public static final String NUMERICAL_FEATURE = "numerical";
	public static final String NUMERICAL_AND_OTHER_FEATURE = "numerical_and_other";
	public static final String DOT_FEATURE = "dot";
	public static final String ALL_CAPITALIZED_FEATURE = "all_capitalized";
	public static final String CAPITALIZED_FEATURE = "capitalized";
	public static final String PARENTHESIS_FEATURE = "parenthesis";
	public static final String KM_FEATURE = "km";
	public static final String RS_FEATURE = "r$";
	public static final String LENGTH_BIGGER_THEN_4_FEATURE = "LENGTH_BIGGER_THEN_4";


	public static void main(String[] args) throws IOException{
		Preprocessor pro = new Preprocessor();
		
		pro.generateProcessedTrain("train/raw/", "processedTrain");
		//pro.generateProcessedTest("test/raw/", "test/processed/");
	}
	
	private void addFeature(ArrayList<ArrayList<String>> ar, String pattern, String feature){
		String f1;
		

		for(int x=0;x<ar.size();x++){
			f1 = ar.get(x).get(0);

			Pattern pat = Pattern.compile(pattern);
			Matcher mt = pat.matcher(f1);
			if(mt.find() ){
				System.out.println(f1 + " " +feature + " " +mt.find());
				
				ar.get(x).add(1, feature);
			}
			
		}
	}

	private void generateProcessedTest(String raw, String processed) throws IOException {
		File rawTests[] = new File(raw).listFiles();

		int i=0;
		for(File rawTest : rawTests){
			ArrayList<ArrayList<String>> ar=  new ArrayList<ArrayList<String>>();

			Scanner in = new Scanner(rawTest);
			String line = in.nextLine();

			if(line.length() > 0){

				String words[] = line.split(" ");

				for(int x=0;x < words.length;x++){
					ar.add(new ArrayList<String>());
					ar.get(x).add(words[x]);
				}

				ar = processInstance(ar);

				FileWriter fw = new FileWriter(processed + (i++));


				for(int x=0;x<ar.size();x++){	
					for(int y=0;y<ar.get(x).size();y++){
						if(y != 0) fw.write(" ");
						fw.write(ar.get(x).get(y));
					}
					fw.write("\n");
				}
				fw.flush();
				fw.close();

			}

		}
	}
	
	public void generateProcessedTrain(String rawBaseLocation, String baseFileLocation)
			throws FileNotFoundException, UnsupportedEncodingException{
		File folder = new File(rawBaseLocation);
		File files[] = folder.listFiles();

		ArrayList<ArrayList<ArrayList<String>>> ar = new ArrayList<ArrayList<ArrayList<String>>>();

		for(int x=0;x<files.length;x++){
			ar.add(processInstance(toInstance(files[x])));
		}

		saveToFile(ar,baseFileLocation);

	}

	public void saveToFile(ArrayList<ArrayList<ArrayList<String>>> ar, String baseFileLocation)
			throws FileNotFoundException, UnsupportedEncodingException{

		PrintWriter writer = new PrintWriter(baseFileLocation, "UTF-8");

		for(int x=0;x<ar.size();x++){
			for(int y=0;y<ar.get(x).size();y++){
				for(int z=0;z<ar.get(x).get(y).size();z++){
					if(z!=0)writer.print(" ");
					writer.print(ar.get(x).get(y).get(z).toLowerCase());

				}
				writer.println();	
			}
			writer.println();
		}

		writer.flush();
		writer.close();
	}

	public ArrayList<ArrayList<String>> toInstance(File file) throws FileNotFoundException{
		ArrayList<ArrayList<String>> ar = new ArrayList<ArrayList<String>>();

		Scanner in = new Scanner(new FileReader(file));

		String line;
		String words[];

		while(in.hasNextLine()){
			ar.add(new ArrayList<String>());

			line = in.nextLine();

			words = line.split(" ");

			for(String word : words){
				ar.get(ar.size() -1).add(word);
			}
		}	

		return ar;
	}


	public ArrayList<ArrayList<String>> processInstance(ArrayList<ArrayList<String>> ar){
		
		addFeature(ar,"[0-9]", NUMERICAL_FEATURE);
		addFeature(ar,"([0-9][^0-9]+)|([^0-9]+[0-9])", NUMERICAL_AND_OTHER_FEATURE);
		addFeature(ar,"[A-Z]", CAPITALIZED_FEATURE);
		addFeature(ar,"\\b[A-Z]+\\b",ALL_CAPITALIZED_FEATURE);
		addFeature(ar,"\\(.*\\)", PARENTHESIS_FEATURE);
		addFeature(ar,"\\.", DOT_FEATURE);
		addFeature(ar,".....", LENGTH_BIGGER_THEN_4_FEATURE);
		
		toLowerCase(ar);
		
		addFeature(ar,"km", KM_FEATURE);
		addFeature(ar,"r\\$", RS_FEATURE);
		
		addSequenceOrderFeature(ar);
		
		return ar;
	}

	private void toLowerCase(ArrayList<ArrayList<String>> ar) {
		for(int x=0;x<ar.size();x++){
			for(int y=0;y<ar.get(x).size();y++){
				ar.get(x).set(y, ar.get(x).get(y).toLowerCase());
			}
		}

	}

	private void addSequenceOrderFeature(ArrayList<ArrayList<String>> ar) {	
		for(int x=0;x<ar.size();x++){
			ar.get(x).add(1, Integer.toString(x + 1));
		}

	}

}
