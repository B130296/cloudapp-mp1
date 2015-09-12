import java.io.File;
import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.Map.Entry;

public class MP1 {
    Random generator;
    String userName;
    String inputFileName;
    String delimiters = " \t,;.?!-:@[](){}_*/";
    String[] stopWordsArray = {"i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours",
            "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its",
            "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that",
            "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having",
            "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while",
            "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before",
            "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again",
            "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each",
            "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than",
            "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"};

    void initialRandomGenerator(String seed) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA");
        messageDigest.update(seed.toLowerCase().trim().getBytes());
        byte[] seedMD5 = messageDigest.digest();

        long longSeed = 0;
        for (int i = 0; i < seedMD5.length; i++) {
            longSeed += ((long) seedMD5[i] & 0xffL) << (8 * i);
        }

        this.generator = new Random(longSeed);
    }

    Integer[] getIndexes() throws NoSuchAlgorithmException {
        Integer n = 10000;
        Integer number_of_lines = 50000;
        Integer[] ret = new Integer[n];
        this.initialRandomGenerator(this.userName);
        for (int i = 0; i < n; i++) {
            ret[i] = generator.nextInt(number_of_lines);
        }
        return ret;
    }

    public MP1(String userName, String inputFileName) {
        this.userName = userName;
        this.inputFileName = inputFileName;
    }

    public String[] process() throws Exception {
        String[] ret = new String[20];
       
        //+++++  Begins added code +++++++
        Scanner sc = new Scanner( new File( this.inputFileName ) );
        String[] lines = new String[50000];
        int l=0;
        while( sc.hasNextLine() ){
               lines[l] = sc.nextLine().toLowerCase().trim();
               l++;
        }
        
        Integer[] idxs=new Integer[10000];
        idxs=getIndexes();
        HashMap<String, Integer> wc = new HashMap<String, Integer>();
        List sw=Arrays.asList(stopWordsArray);
        for( int i: idxs ){
             StringTokenizer st=new StringTokenizer(lines[i],delimiters,false);
             while( st.hasMoreTokens() ){
                    String tk=st.nextToken();
                    if( ! sw.contains(tk) ){
                        if( wc.containsKey(tk) )
                            wc.put( tk, wc.get(tk)+1 );
                        else
                            wc.put( tk, 1);
                    }
             } 
        }
        
        Set<Entry<String,Integer>> mapEntries = wc.entrySet();
        List<Entry<String,Integer>> aList = new LinkedList<Entry<String,Integer>>(mapEntries);

        Collections.sort(aList, new Comparator<Entry<String,Integer>>() {
            @Override
            public int compare(Entry<String, Integer> ele1,
                               Entry<String, Integer> ele2) {
                   if(  ele2.getValue() == ele1.getValue() ){
                        if( ele2.getKey().compareTo(ele1.getKey())<0 )
                            return 1;
                   }
                   return ele2.getValue().compareTo(ele1.getValue());

            }

        });
        
        // Storing the list into Linked HashMap to preserve the order of insertion. 
        Map<String,Integer> aMap2 = new LinkedHashMap<String, Integer>();
        for(Entry<String,Integer> entry: aList) {
            aMap2.put(entry.getKey(), entry.getValue());
        }

        int x=0;
        for(Entry<String,Integer> entry : aMap2.entrySet()){
            ret[x]=entry.getKey();
            x++;
            if( x == 20 ){
                break;
            }
        }
        //+++++  End added code +++++++
        return ret;
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1){
            System.out.println("MP1 <User ID>");
        }
        else {
            String userName = args[0];
            String inputFileName = "./input.txt";
            MP1 mp = new MP1(userName, inputFileName);
            String[] topItems = mp.process();
            for (String item: topItems){
                System.out.println(item);
            }
        }
    }
}
