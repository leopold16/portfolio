// Leopold Wohlgemuth
// Date: 12/19/2022

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.util.Iterator;
import java.util.Objects;


public class CommonWordFinder {

    @SuppressWarnings("unchecked")
    public static void main(String[] args){

//assign variables to help me later in checking the conditions and attempt to  load the files
       int length = args.length;
       File file = new File(args[0]);
       boolean exists = file.exists();


//check if the length meets the required condition (either two or three inputs)
        if (length != 2 && length != 3) {
            System.err.println("Usage: java CommonWordFinder <filename> <bst|avl|hash> [limit]");
            System.exit(-1);
        }
//check if the file exists
        if (!exists){
            System.err.println("Error: Cannot open file '" + args[0] + "' for input.");
            System.exit(-1);
        }

// assign variable name to args[1] so I can check condition more easily
        String type1 = args[1];

//if args[1] is not hash, avl, or bst the terminal prints and error message
        if (!(type1.equals("hash")) && !(type1.equals("avl")) && !(type1.equals("bst"))){
            System.err.println("Error: Cannot open file '" + args[0] + "' for input.");
            System.exit(-1);
        }


//assign limit to be zero at first, then the program tries to parse integer from string input, if this is successful,
//        limit gets updated to be user input. In case there is no args[2] the limit is set to be 10 by default.
        int limit = 0;
        if(length==3) {
            try {
                Integer.parseInt(args[2]);
                limit = Integer.valueOf(args[2]);
            } catch (NumberFormatException e) {
                System.err.println("Error: Invalid limit '" + args[2] + "' received.");
                System.exit(-1);
            }
        }else {
            limit = 10;
        }

        //check if the limit fulfills condition (positive)
        if (limit < 0){
            System.err.println("Error: Invalid limit '" + args[2] + "' received.");
            System.exit(-1);
        }

/*
The way the code is structured in this program is admittedly repetitive. Everything is contained inside the main method.
When I started out writing this code, I did not realize how similar the individual steps would be. I could have turned
some elements into method and made the program a bit shorter but for logic reasons this set up made most sense to me.
The comments on the BST section will be very detailed, but because they are basically the same for avl and hash, I
will only comment when they differ.
 */
        if (args[1].equals("bst")){
            try{
                //In this program, I used the buffered reader class. Reference used:
                // https://docs.oracle.com/javase/8/docs/api/java/io/BufferedReader.html
                FileReader fr = new FileReader(args[0]);
                BufferedReader br = new BufferedReader(fr);
                StringBuilder sb = new StringBuilder();
                MyMap<String, Integer> map = new BSTMap<>();

                //I needed to use two variables here, one to store the char version of the current character and the
                //other one to store the integer version. I use the char version to check for conditions such as space
                // and end of line and the integer version for the while loop condition -1
                //(I encountered a problem where I just used the char version and got stuck in infinite while loop)
                int current_ch;
                while((current_ch = br.read()) != -1) {
                    char current_char = (char) current_ch;
                    while (current_char!= ' ' && current_char !=  '\n' && current_char != '\r') {
                        //if it is lowercase letter, append to stringbuilder
                        if (Character.isLetter(current_char) && Character.isLowerCase(current_char)){
                            sb.append(current_char);
                        }
                        //if it is uppercase letter, convert to lowercase and append to strinbuilder
                        if (Character.isLetter(current_char) && Character.isUpperCase(current_char)) {
                            sb.append(Character.toLowerCase(current_char));
                        }
                        if((current_ch = br.read())==-1){break;}
                        current_char = (char) current_ch;
                    }

                    // I added this if statement to counter the problem where it would count the spaces as a word
                    if(sb.length()!= 0 && sb.charAt(0) != ' '){
                        String word = sb.toString();
                        //I check if word is in the map already, if it is not I add it with a count of one. If it
                        //exists I increase the count
                        if (map.get(word) == null){
                            map.put(word, 1);
                        }else{
                            map.put(word, map.get(word) +1);
                        }
                    }
                    //this resets the stringbuilder
                    sb.setLength(0);
                }
                // this iterates through the BST and store values in array of type entry
                Entry<String, Integer> [] entry_array = new Entry[map.size()];
                Iterator<Entry<String, Integer>> itr = map.iterator();
                for (int i=0; i < map.size();i++){
                    entry_array[i] = itr.next();
                }
                // sort values using insertion sort (reference is code from lecture 15/11/2022)
                for (int i = entry_array.length; i > 0; i--) {
                    for (int k = i; k < entry_array.length && entry_array[k-1].value < entry_array[k].value; k++) {
                        Entry<String,Integer> ent = entry_array[k];
                        entry_array[k] = entry_array[k-1];
                        entry_array[k-1] = ent;
                    }
                }
                //this checks limit and updates it to be map size in case it is too high
                if (limit>map.size()){
                    limit = map.size();
                }

                //get the longest word for spacing
                int length_counter = 0;
                for(int i=0; i<limit;i++){
                    String temp_word = entry_array[i].key;
                    if (temp_word.length() > length_counter){
                        length_counter = temp_word.length();
                    }
                }
                // the length between the dot and the value is the length of the longest word +1
                int space_length = length_counter +1;

                //prints the total number of unique words, using map.size
                System.out.println("Total unique words: " + map.size());

                //print out with format considered using stringbuilder and repeat for adjustable space length
                StringBuilder sb1 = new StringBuilder();
                for (int i=0; i<limit;i++){
                    sb1.append(" ".repeat(Integer.toString(limit).length()-Integer.toString(i+1).length())
                            + (i+1) + ". " + entry_array[i].key +
                            " ".repeat(space_length-entry_array[i].key.length()) + entry_array[i].value+
                            System.lineSeparator());
                }

                System.out.print(sb1);


            } catch (IOException ioException){
                System.err.println("Error: An I/O error occurred reading '" + args[0] + "'.");
                System.exit(-1);
            }
        }

        if (args[1].equals("avl")){
            try{
                FileReader fr = new FileReader(args[0]);
                BufferedReader br = new BufferedReader(fr);
                StringBuilder sb = new StringBuilder();
                //initialize avl map
                MyMap<String, Integer> map = new AVLTreeMap<>();

                int current_ch;
                while((current_ch = br.read()) != -1) {
                    char current_char = (char) current_ch;
                    while (current_char!= ' ' && current_char !=  '\n' && current_char != '\r') {
                        if (Character.isLetter(current_char) && Character.isLowerCase(current_char)){
                            sb.append(current_char);
                        }
                        if (Character.isLetter(current_char) && Character.isUpperCase(current_char)) {
                            sb.append(Character.toLowerCase(current_char));
                        }
                        if((current_ch = br.read())==-1){break;}
                        current_char = (char) current_ch;
                    }

                    if(sb.length()!= 0 && sb.charAt(0) != ' '){
                        String word = sb.toString();
                        if (map.get(word) == null){
                            map.put(word, 1);
                        }else{
                            map.put(word, map.get(word) +1);
                        }
                    }


                    sb.setLength(0);
                }

                Entry<String, Integer> [] entry_array = new Entry[map.size()];
                Iterator<Entry<String, Integer>> itr = map.iterator();
                for (int i=0; i < map.size();i++){
                    entry_array[i] = itr.next();
                }

                for (int i = entry_array.length; i > 0; i--) {

                    for (int k = i; k < entry_array.length && entry_array[k-1].value < entry_array[k].value; k++) {
                        Entry<String,Integer> ent = entry_array[k];
                        entry_array[k] = entry_array[k-1];
                        entry_array[k-1] = ent;
                    }
                }
                if (limit>map.size()){
                    limit = map.size();
                }
                int length_counter = 0;
                for(int i=0; i<limit;i++){
                    String temp_word = entry_array[i].key;
                    if (temp_word.length() > length_counter){
                        length_counter = temp_word.length();
                    }
                }
                int space_length = length_counter +1;

                System.out.println("Total unique words: " + map.size());
                StringBuilder sb1 = new StringBuilder();
                for (int i=0; i<limit;i++){
                    sb1.append(" ".repeat(Integer.toString(limit).length()-Integer.toString(i+1).length())
                            + (i+1) + ". " + entry_array[i].key +
                            " ".repeat(space_length-entry_array[i].key.length()) + entry_array[i].value+
                            System.lineSeparator());
                }

                System.out.print(sb1);


            } catch (IOException ioException){
                System.err.println("Error: An I/O error occurred reading '" + args[0] + "'.");
                System.exit(-1);
            }
        }
        if (args[1].equals("hash")){
            try{
                FileReader fr = new FileReader(args[0]);
                BufferedReader br = new BufferedReader(fr);
                StringBuilder sb = new StringBuilder();
                //initialize hashmap
                MyMap<String, Integer> map = new MyHashMap<>();

                int current_ch;
                while((current_ch = br.read()) != -1) {
                    char current_char = (char) current_ch;
                    while (current_char!= ' ' && current_char !=  '\n' && current_char != '\r') {
                        if (Character.isLetter(current_char) && Character.isLowerCase(current_char)){
                            sb.append(current_char);
                        }
                        if (Character.isLetter(current_char) && Character.isUpperCase(current_char)) {
                            sb.append(Character.toLowerCase(current_char));
                        }
                        if((current_ch = br.read())==-1){break;}
                        current_char = (char) current_ch;
                    }

                    if(sb.length()!= 0 && sb.charAt(0) != ' '){
                        String word = sb.toString();
                        if (map.get(word) == null){
                            map.put(word, 1);
                        }else{
                            map.put(word, map.get(word) +1);
                        }
                    }


                    sb.setLength(0);
                }

                Entry<String, Integer> [] entry_array = new Entry[map.size()];
                Iterator<Entry<String, Integer>> itr = map.iterator();
                for (int i=0; i < map.size();i++){
                    entry_array[i] = itr.next();
                }
                for (int i = entry_array.length; i > 0; i--) {

                    for (int k = i; k < entry_array.length && entry_array[k-1].value < entry_array[k].value; k++) {
                        Entry<String,Integer> ent = entry_array[k];
                        entry_array[k] = entry_array[k-1];
                        entry_array[k-1] = ent;
                    }
                }
                //sort words with same value alphabetically (hashmap only!)
                for (int i=0;i<entry_array.length;i++){
                    for(int j=i+1;j<entry_array.length;j++){
                        if (Objects.equals(entry_array[i].value, entry_array[j].value)){
                            if ((entry_array[i].key.compareTo(entry_array[j].key)>0)){
                                Entry<String,Integer> ent = entry_array[i];
                                entry_array[i] = entry_array[j];
                                entry_array[j] = ent;
                            }
                        }
                    }
                }
                if (limit>map.size()){
                    limit = map.size();
                }
                int length_counter = 0;
                for(int i=0; i<limit;i++){
                    String temp_word = entry_array[i].key;
                    if (temp_word.length() > length_counter){
                        length_counter = temp_word.length();
                    }
                }
                int space_length = length_counter +1;
                System.out.println("Total unique words: " + map.size());
                StringBuilder sb1 = new StringBuilder();
                for (int i=0; i<limit;i++){
                    sb1.append(" ".repeat(Integer.toString(limit).length()-Integer.toString(i+1).length())
                            + (i+1) + ". " + entry_array[i].key +
                            " ".repeat(space_length-entry_array[i].key.length()) + entry_array[i].value+
                            System.lineSeparator());
                }
                System.out.print(sb1);


            } catch (IOException ioException){
                System.err.println("Error: An I/O error occurred reading '" + args[0] + "'.");
                System.exit(-1);
            }
        }




    }
}
