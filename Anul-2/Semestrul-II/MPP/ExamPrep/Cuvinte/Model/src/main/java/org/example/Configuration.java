package org.example;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="configurations")
public class Configuration extends org.example.Entity<Integer> {
    private String letters;
    private String word1;
    private String word2;
    private String word3;
    private String word4;


    public Configuration(){

    }

    public Configuration(String letters, String word1, String word2, String word3, String word4) {
        this.letters = letters;
        this.word1 = word1;
        this.word2 = word2;
        this.word3 = word3;
        this.word4 = word4;
    }

    public String getWord3() {
        return word3;
    }

    public void setWord3(String word3) {
        this.word3 = word3;
    }

    public String getLetters() {
        return letters;
    }

    public void setLetters(String letters) {
        this.letters = letters;
    }

    public String getWord1() {
        return word1;
    }

    public void setWord1(String word1) {
        this.word1 = word1;
    }

    public String getWord2() {
        return word2;
    }

    public void setWord2(String word2) {
        this.word2 = word2;
    }

    public String getWord4() {
        return word4;
    }

    public void setWord4(String word4) {
        this.word4 = word4;
    }
}
