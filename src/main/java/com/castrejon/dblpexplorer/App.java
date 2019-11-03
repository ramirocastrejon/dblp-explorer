package com.castrejon.dblpexplorer;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        long uniqueWords =0;
        try(Stream<String> lines =
                Files.lines(Paths.get("src","dblp_papers100.txt"), Charset.defaultCharset())){
            uniqueWords=lines.flatMap(line -> Arrays.stream(line.split(" ")))
                    .distinct()
                    .count();
        }
        catch (IOException e){

        }
        System.out.println(uniqueWords + " is the number of unique words.");
    }
}
