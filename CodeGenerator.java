package chessproject;

//maybe merge with another class later

import java.util.Random;

public class CodeGenerator {
    public static String generateCode () {
        //System.out.println("method called");
        int min = 97; // 'a'
        int max = 123; // 'z + 1'
        Random random = new Random();
        String generatedCode = "";

        do {
            generatedCode = "";
            for (int i = 0; i < 5; i++) {
                int number = random.nextInt (max - min) + min;//random.ints (startLimit, endLimit);
                //String generatedLetter = ((char)number).toString();
                char letter = (char)number;
                generatedCode += letter;
            }
            CreatePrivateRoomFrame.roomCodes.add(generatedCode);
        } while(!CreatePrivateRoomFrame.roomCodes.contains(generatedCode));

        return generatedCode;
    }
}