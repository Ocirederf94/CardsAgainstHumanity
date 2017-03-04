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
    private BufferedReader bufferedReader;
    private FileReader fileReader;
    private String currentLine;

    public ReadFile(String filePath) {
        try {
            fileReader = new FileReader(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bufferedReader = new BufferedReader(fileReader);
    }

    public String retreive() {

        try {

            currentLine = bufferedReader.readLine();

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        } /*finally {

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

        }*/
        return currentLine;
    }
}
