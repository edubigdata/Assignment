package examples.deo;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Created by deokishore on 30/07/2016.
 */
public class WordCountMapper extends Mapper<Object, Text, Text, IntWritable> {

    public static String WORD_TO_COUNT = "WORD-TO-COUNT";
    private final IntWritable one = new IntWritable(1);
    private Text word = new Text();
    private String wordToCount = null;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
        wordToCount = context.getConfiguration().get(WORD_TO_COUNT);
    }

    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {

        StringTokenizer itr = new StringTokenizer(value.toString());
        while (itr.hasMoreTokens()) {
            String token = itr.nextToken();
            if(token.equals(wordToCount)) {
                word.set(token);
                context.write(word, one);
            }
        }
    }

}
