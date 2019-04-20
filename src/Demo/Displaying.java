package Demo;

import javax.swing.*;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Displaying{
    private String string;
    private JTextArea textArea;

    public Displaying(String string,JTextArea textArea){
        this.string = string;
        this.textArea = textArea;
    }


    public void show() {
        while (true){
            textArea.append ( string );
        }
    }

}
