/**
 * 
 */
package deepak.yelp.searchsystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Deepak
 *
 */
public class Map extends Mapper<Object, Text, Text, Text> {
	
	public String ReadJson(String file)  {
		StringBuffer sb = new StringBuffer();
		String line;
		try {
			FileSystem fs = FileSystem.get(new Configuration());
			Path pt = new Path(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(pt)));
			while ((line=br.readLine()) != null) {
				sb.append(line);
			}		
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return sb.toString();
	}

	public void map(Object id, Text word, Context context){
		
		if(word.toString().length() > 0){
			try {
				String documentContents=ReadJson(word.toString().toUpperCase().charAt(0)+".json");
				JSONObject valueGetter=new JSONObject(documentContents);
				String valueList=valueGetter.optString(word.toString());
				context.write(word, new Text(valueList));
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
