package org.academiadecodigo.bootcamp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by codecadet on 04/03/17.
 * <p>
 * This method is used to read each line from file.
 */

public class ReadFile {

    public static String retreive(String filePath) {
        BufferedReader bufferedReader = null;
        FileReader fileReader = null;
        String currentLine = "";

        try {
            fileReader = new FileReader(filePath);
            bufferedReader = new BufferedReader(fileReader);

            currentLine = bufferedReader.readLine();

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        } finally {

            try {

                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (fileReader != null) {
                    fileReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return currentLine;
    }
}
