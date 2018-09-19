package hangman;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Hangman {
    public static void main(String[] args) throws IOException {
        File dictionary = new File("C:\\Users\\Slavo\\Desktop\\besenica\\Hangman\\categories.txt"); // finds the dictionary we will use
        String playAgain = "y";
        int score = 0;
        int mistakes = 0;
        int randomWordSelector;
        String chosenCategory;
        List<String> categories = getCategories(dictionary); //puts all avaible categories in an array
        Scanner reader = new Scanner(System.in); //initializes a scanner for further user input
        while(playAgain.equals("y")){
            System.out.println("Choose category");
            for(int i=0;i<categories.size();i++){
                System.out.println(categories.get(i));
            }
            chosenCategory = reader.nextLine();
            while(!(categories.contains(chosenCategory))){
                System.out.println("Wrong category, please choose again!");
                chosenCategory = reader.nextLine();
            }
            List<String> words = getWordsByCategory("_" + chosenCategory, dictionary);
            
            Random rand = new Random();
            randomWordSelector = rand.nextInt(words.size());
            String currentWord = words.get(randomWordSelector);                            
            String convertedWord = convertWord(currentWord); //converts the chosen word to a "_ _ _ "etc.. format
            
            while(!fullWord(convertedWord) && mistakes < 10){
                System.out.println("Attempts left: " + (10 - mistakes));
                System.out.println("Current word/phrase:" + convertedWord);
                System.out.println("Please enter a letter: ");
                String currentLetter = reader.nextLine();
                while(currentLetter.equals("")){
                    System.out.println("Please enter a letter");
                    currentLetter = reader.nextLine();
                }
                if(currentWord.contains(currentLetter)){
                    convertedWord = fillWord(currentWord,convertedWord,currentLetter.charAt(0));
                }
                else{
                    System.out.println("The word/phrase doesn't contain this letter, try again!");
                    mistakes += 1;
                }                                                                                     
            }
            if(fullWord(convertedWord)){
                mistakes = 0;
                System.out.println("Current score:" + (score+=1));
                System.out.println("Continue playing ? y/n ?");
                playAgain = reader.nextLine();
            }
            else if(mistakes >=10){
                mistakes = 0;
                System.out.println("Game over! Total score:" + score);
                score = 0;
                System.out.println("Start a new game ? y/n ?");
                playAgain = reader.nextLine();
            }
        }
    }
    
    public static List<String> getWordsByCategory(String category, File dictionary) throws FileNotFoundException, IOException //gets all words from a specific category
    {
        List<String> words  = new ArrayList<String>();
        try(BufferedReader reader = new BufferedReader(new FileReader(dictionary))){
            String line;
            while(!((line = reader.readLine()).equals(category))){
                line = reader.readLine();
            }
            line = reader.readLine();
            while(line != null && !(line.substring(0,1).equals("_"))){
                words.add(line);
                line = reader.readLine();
            }
        }
        return words;
    }
    
    public static List<String> getCategories(File dictionary) throws FileNotFoundException, IOException{ //gets all avaible categories
        List<String> categories = new ArrayList<String>();
        try(BufferedReader reader = new BufferedReader(new FileReader(dictionary))){
            String line = null;
            while((line = reader.readLine()) != null){
                if((line.substring(0,1)).equals("_")){
                    categories.add(line.substring(1));
                }
            }
        }
        return categories;
    }
    public static String convertWord(String word){
        String convertedWord = "";
        for(int i=0;i<word.length();i++){
            if((word.charAt(i)>='a' && word.charAt(i)<='z') || (word.charAt(i)>='A' && word.charAt(i)<='Z')){
                convertedWord += "_ ";
            }
            if(word.charAt(i) == ' '){
                convertedWord += "  ";
            }
        }
        return convertedWord;
    }
    public static String fillWord(String wholeWord, String hiddenWord, char letter){ //fills a word in a "_ _ _"etc... format with a letter, if the letter is found in the original word
        List<Integer> occurrences = new ArrayList<Integer>();
        StringBuilder filledWord = new StringBuilder(hiddenWord);
        for(int i=0;i<wholeWord.length();i++){
            if(wholeWord.charAt(i) == letter){
                occurrences.add(i);
            }
        }
        for(int i=0;i<occurrences.size();i++){
                filledWord.setCharAt(occurrences.get(i)*2,letter);
        }
        return filledWord.toString();
    }
    
    public static Boolean fullWord(String word){ //checks if a word is guessed
        return !(word.contains("_"));
    }
}
