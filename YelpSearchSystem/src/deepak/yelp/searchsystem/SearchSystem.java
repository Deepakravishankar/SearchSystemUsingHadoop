package deepak.yelp.searchsystem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import deepak.yelp.tokenizerrules.TokenizerRules;




/**
 * @author Deepak
 *
 */
public class SearchSystem {

	private final static String queryFile = "query.txt";

	private String readFile(String file) {
		StringBuffer sb = new StringBuffer();
		FileSystem fs;
		String line;
		try {
			fs = FileSystem.get(new Configuration());
			try {
				Path pt = new Path(file);
				BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(pt)));
				while ((line=br.readLine()) != null) {
					sb.append(line);
				}
			} finally {
				fs.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public void CreateFiles(ArrayList<String> arraylist) throws IOException {
		StringBuffer filePath = new StringBuffer();
		try {
			for (int i = 0; i < arraylist.size(); i++) {
				filePath.append("Queryinput/QueryInputFile");
				filePath.append(i);
				filePath.append(".txt");
				Path pt = new Path(filePath.toString());
				FileSystem fs = FileSystem.get(new Configuration());
				BufferedWriter br = new BufferedWriter(new OutputStreamWriter(fs.create(pt, true)));
				br.write(arraylist.get(i));
				br.close();
				filePath.setLength(0);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<String> applytokenizatiorule(String query) {
		TokenizerRules tr = new TokenizerRules();
		return tr.applyTokenizationRules(query);
	}

	public static void main(String[] args) {
		ArrayList<String> tokenizedQueryArray=new ArrayList<String>();

		//Get the query from the user and tokenize it.
		SearchSystem getQuery=new SearchSystem();
		String query = getQuery.readFile(queryFile);
		
		//if(query.trim().equals(null) && query.trim()!=""){
		tokenizedQueryArray = getQuery.applytokenizatiorule(query);
		//}
		Configuration conf = new Configuration();
		
		//Set number of input query and number of parsed query files.
		conf.set("query", query);
		conf.setInt("querysize",tokenizedQueryArray.size());
		
		try {
			getQuery.CreateFiles(tokenizedQueryArray);

			Job job = new Job(conf, "SearchSystem");
			job.setJarByClass(SearchSystem.class);

			// Set the mapper and reducer classes respectively.
			job.setMapperClass(Map.class);
			job.setReducerClass(Reduce.class);

			// Set the mapper and reducer input and output key value types.
			job.setMapOutputKeyClass(Text.class);
			job.setMapOutputValueClass(Text.class);
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(Text.class);

			FileInputFormat.addInputPath(job, new Path(args[1]));
			FileOutputFormat.setOutputPath(job, new Path(args[2]));
			int result = job.waitForCompletion(true) ? 0 : 1;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
