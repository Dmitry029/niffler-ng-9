package guru.qa.niffler.utils;

import com.github.javafaker.Faker;

public class RandomDataUtils {

    public static String randomCategoryName() {
        return new Faker().funnyName().name();
    }

    public static String randomUserName() {
        return new Faker().name().username();
    }

    public static String randomName() {
        return new Faker().name().firstName();
    }

    public static String randomSurname() {
        return new Faker().name().lastName();
    }

    public static String randomSentence(int wordsCount) {
        return new Faker().lorem().sentence(wordsCount, 0);
    }
}
