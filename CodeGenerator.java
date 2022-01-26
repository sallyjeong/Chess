package chessproject;

import java.util.Random;

/** [CodeGenerator.java]
 * Randomly generates a 5 letter code for rooms
 * @author Katherine Liu, Rachel Liu, Sally Jeong
 * @version 1.0 Jan 25, 2022
 */
public class CodeGenerator {
    public static String generateCode () {
        int min = 97; // 'a'
        int max = 123; // 'z + 1'
        Random random = new Random();
        String generatedCode = "";

        do {
            generatedCode = "";
            for (int i = 0; i < 5; i++) {
                int number = random.nextInt (max - min) + min;
                char letter = (char)number;
                generatedCode += letter;
            }
            CreatePrivateRoomFrame.roomCodes.add(generatedCode); // stores in Set of all existing room Codes
        } while(!CreatePrivateRoomFrame.roomCodes.contains(generatedCode)); // checks if already exists

        return generatedCode;
    }
}
