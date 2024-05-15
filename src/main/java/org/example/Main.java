package org.example;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static AtomicInteger threeBeautifulWords = new AtomicInteger();
    public static AtomicInteger fourBeautifulWords = new AtomicInteger();
    public static AtomicInteger fiveBeautifulWords = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        //сгенерированное слово является палиндромом, т. е. читается одинаково как слева направо, так и справа налево, например, abba
        Runnable firstTask = () -> {
            for (String text : texts) {
                if (text.contentEquals(new StringBuilder(text).reverse())) {
                    lengthOfWords(text);
                }
            }
        };

        //сгенерированное слово состоит из одной и той же буквы, например, aaa
        Runnable secondTask = () -> {
            for (String text : texts) {
                if (singleLetterWord(text)) {
                    lengthOfWords(text);
                }
            }
        };

        //буквы в слове идут по возрастанию: сначала все a (при наличии), затем все b (при наличии), затем все c и т. д. Например, aaccc
        Runnable thirdTask = () -> {
            for (String text : texts) {
                if (alphabeticalLetters(text) && !singleLetterWord(text)) {
                    lengthOfWords(text);
                }
            }
        };

        Thread thread1 = new Thread(firstTask);
        thread1.start();

        Thread thread2 = new Thread(secondTask);
        thread2.start();

        Thread thread3 = new Thread(thirdTask);
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();

        System.out.printf("Красивых слов с длинной 3: %s шт\n", threeBeautifulWords);
        System.out.printf("Красивых слов с длинной 4: %s шт\n", fourBeautifulWords);
        System.out.printf("Красивых слов с длинной 5: %s шт\n", fiveBeautifulWords);
    }

    public static boolean alphabeticalLetters(String word) {
        if (word == null || word.isEmpty()) {
            return false;
        }

        for (int i = 1; i < word.length(); i++) {
            if (word.charAt(i) < word.charAt(i - 1)) {
                return false;
            }
        }
        return true;
    }

    public static boolean singleLetterWord(String word) {
        if (word == null || word.isEmpty()) {
            return false;
        }
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(0) != word.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public static void lengthOfWords(String word) {
        if (word.length() == 3) {
            threeBeautifulWords.getAndIncrement();
        } else if (word.length() == 4) {
            fourBeautifulWords.getAndIncrement();
        } else if (word.length() == 5) {
            fiveBeautifulWords.getAndIncrement();
        }
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}