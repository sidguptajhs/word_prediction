import java.io.*;
import java.util.*;

import static java.lang.Math.log10;

public class SolutionID3
{
    HashMap<String,Values<Integer>> terms;
    HashMap<String,Values<Double>> tfIDF;
    Set<String> stopSet;

    public SolutionID3() throws IOException {
        generateStopSet();
        terms = new HashMap();
        tfIDF = new HashMap();
        learn();
    }

    public static void main(String args[]) throws Exception{
        BufferedReader br =new BufferedReader(new InputStreamReader(System.in));
        int i,count = Integer.parseInt(br.readLine());
        String inputs[] = new String[count];
        for(i = 0; i<count;i++){
            inputs[i] = br.readLine();
        }
        SolutionID3 solution = new SolutionID3();
        String[] predictions = solution.predictAll(inputs);
        for(i=0;i<predictions.length;i++)
            System.out.println(predictions[i]);
    }

    public String[] predictAll(String[] inputs) throws IOException
    {
        String[] predictions=new String[inputs.length];
        int i;
        for(i=0;i<inputs.length;i++) {
            int removeObviousResults = checkObvious(inputs[i]);
            if(removeObviousResults>0)
            {
                if(removeObviousResults==1)
                    predictions[i]="computer-company";
                else
                    predictions[i]="fruit";
            }
            else {
                String[] words = tokenize(inputs[i]);
                predictions[i] = classify(words);
            }
        }
        return predictions;
    }

    private void learn() throws IOException {
        int i;
        String[] fruitWords = getWordsFromFile("C:\\Users\\User\\IdeaProjects\\ByteTheApple\\NaiveBayes\\src\\apple-fruit.txt");
        String[] computerWords = getWordsFromFile("C:\\Users\\User\\IdeaProjects\\ByteTheApple\\NaiveBayes\\src\\apple-computers.txt");
        for(i=0;i<fruitWords.length;i++){
            if(fruitWords[i].length()>2) {
                Values values = terms.get(fruitWords[i]);
                if (values == null) {
                    terms.put(fruitWords[i], new Values(1, 0));
                } else {
                    Integer fruitValue = (Integer) values.fruitValue;
                    values.setFruitValue(fruitValue + 1);
                }
            }
        }
        for(i=0;i<computerWords.length;i++){
            if(computerWords[i].length()>2) {
                Values values = terms.get(computerWords[i]);
                if (values == null) {
                    terms.put(computerWords[i], new Values(0, 1));
                } else {
                    Integer computerValue = (Integer) values.computerValue;
                    values.setComputerValue(computerValue + 1);
                }
            }
        }
        Set<Map.Entry<String, Values<Integer>>> entries = terms.entrySet();
        for (Map.Entry<String, Values<Integer>> entry : entries) {
            Values<Integer> value = entry.getValue();

            double tf0 = log10(value.fruitValue+1), tf1 = log10(value.computerValue+1);
            double idf = log10(2.0/((value.fruitValue!=0?1:0)+(value.computerValue!=0?1:0)));
            tfIDF.put(entry.getKey(),new Values<Double>(tf0*idf,tf1*idf));
        }
    }


    private int checkObvious(String input) {
        if(input.contains("apples")||input.contains("the apple")||input.contains("The Apple is")||input.contains("An Apple is")||input.contains("peel")||input.contains("tree")||input.contains("cider")||input.contains("ingredient")||input.contains("orchard")||input.contains("juice")){
            return 2;
        }
        if(input.contains(" Apple ")){
            return 1;
        }
        input=input.toLowerCase();
        if(input.contains("apple's")||input.contains("apple inc")||input.contains("iphone")||input.contains("ipod")||input.contains("macbook")||input.contains("apple computers")||input.contains("million")||input.contains("billion")||input.contains("macintosh")||input.contains("laptops")||input.contains("itunes")){
            return 1;
        }
        else
            return 0;
    }

    private String classify(String words[]){
        double computerScore=0.0,fruitScore = 0.0;
        Values<Double> temp;
        int i;
        for(i=0;i<words.length;i++){
            temp = tfIDF.get(words[i]);
            if(temp!=null){
                computerScore=computerScore+temp.computerValue;
                fruitScore=fruitScore+temp.fruitValue;
            }
        }
        if(computerScore-fruitScore>0.0000001)
            return "computer-company";
        else
            return "fruit";
    }

    private String[] getWordsFromFile(String fileName) throws IOException {
        String words[];
        String text = getDataFromFile(fileName);
        words=tokenize(text);
        return words;
    }

    public static String getDataFromFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
        StringBuilder sb = new StringBuilder();

        String line = br.readLine();
        while (line != null) {
            sb.append(line);
            sb.append("\n");
            line = br.readLine();
        }
        br.close();
        return sb.toString();
    }

    public void generateStopSet(){
        String stopString="a|about|above|after|again|against|all|am|an|and|any|are|aren't|as|at|be|because|been|before|being|below|between|both|but|by|can't|cannot|could|couldn't|did|didn't|do|does|doesn't|doing|don't|down|during|each|few|for|from|further|had|hadn't|has|hasn't|have|haven't|having|he|he'd|he'll|he's|her|here|here's|hers|herself|him|himself|his|how|how's|i|i'd|i'll|i'm|i've|if|in|into|is|isn't|it|it's|its|itself|let's|me|more|most|mustn't|my|myself|no|nor|not|of|off|on|once|only|or|other|ought|our|ours|ourselves|out|over|own|same|shan't|she|she'd|she'll|she's|should|shouldn't|so|some|such|than|that|that's|the|their|theirs|them|themselves|then|there|there's|these|they|they'd|they'll|they're|they've|this|those|through|to|too|under|until|up|very|was|wasn't|we|we'd|we'll|we're|we've|were|weren't|what|what's|when|when's|where|where's|which|while|who|who's|whom|why|why's|with|won't|would|wouldn't|you|you'd|you'll|you're|you've|your|yours|yourself|yourselves";
//        String stopString ="dummyvaluehere|anotherdummyvalue";
        String[] stopList=stopString.split("\\|");
        // System.out.println("Stopwords->"+stopList.length);
        stopSet = new HashSet<String>(Arrays.asList(stopList));
    }
    private String[] tokenize(String string){
        string = string.toLowerCase();
        ArrayList<String> words = new ArrayList();
        String output="";
        int i;
        char c;
        for(i=0;i<string.length();i++){
            c=string.charAt(i);
            if(c>='a'&&c<='z')
                output=output+c;
            else{
                if(!stopSet.contains(output)&&output.length()>0)
                    words.add(output);
                output=new String("");
            }
        }
        if(output.length()>0)
            words.add(output);
        String[] wordArr = new String[words.size()];
        wordArr = words.toArray(wordArr);
        return wordArr;
    }

    private class Values<T> {
        T fruitValue;
        T computerValue;
        Values(T fruit,T comp){
            fruitValue=fruit;
            computerValue=comp;
        }

        public void setFruitValue(T fruitValue) {
            this.fruitValue = fruitValue;
        }

        public void setComputerValue(T computerValue) {
            this.computerValue = computerValue;
        }

    }
}