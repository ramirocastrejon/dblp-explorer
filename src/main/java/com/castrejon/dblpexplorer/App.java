package com.castrejon.dblpexplorer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import jdk.nashorn.internal.parser.JSONParser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Scanner scanner = new Scanner(System.in);
        HashMap<String,JsonObject> keywordPapers = new HashMap<>();
        HashMap<String,JsonObject> nTier = new HashMap<>();
        ArrayList<String> currentReferences = new ArrayList<>();
        JsonParser jsonParser = new JsonParser();

        List<JsonObject> objects = null;
        System.out.print("Enter a keyword: ");
        String keyword = scanner.nextLine();
        System.out.print("Enter an int value (1-9):");
        int nTierVal = scanner.nextInt();
        try(Stream<String> lines =
                Files.lines(Paths.get("src","dblp_papers100.txt"), Charset.defaultCharset())){
            objects=lines.map(line -> { return (JsonObject) jsonParser.parse(line);}).collect(Collectors.toList());

            objects.parallelStream().filter(obj -> obj.get("title").toString().toLowerCase().contains(keyword))
                    .forEach(refKey ->keywordPapers.put(refKey.get("id").toString(),refKey));
            //objects.forEach(System.out::println);

            /*uniqueWords=lines.flatMap(line -> Arrays.stream(line.split(" ")))
                    .distinct()
                    .count();*/
            JsonArray referencesList = new JsonArray();
            /* Display content using Iterator*/
            Set set = keywordPapers.entrySet();
            Iterator iterator = set.iterator();
            while(iterator.hasNext()) {
                Map.Entry mentry = (Map.Entry)iterator.next();
                System.out.print("ID of the paper is : "+ mentry.getKey() + " & Title is: ");
                JsonObject temp=(JsonObject)mentry.getValue();
                System.out.println(temp.get("title"));
                referencesList.add((JsonArray)temp.get("references"));
                JsonArray jArray =(JsonArray)temp.get("references");
                if (jArray != null) {
                    int len = jArray.size();
                    for (int i = 0; i < len; i++) {
                        if(jArray.get(i)!=null)
                            currentReferences.add(jArray.get(i).toString());
                    }
                }
            }
           System.out.println("The papers reference the following ids: ");
            referencesList.forEach(System.out::println);
            //currentReferences.forEach(System.out::println);
            for( int i =0; i < nTierVal; i++){
                System.out.println("Current tier is " + (i+1));

                List<JsonObject> finalObjects = objects;
                Map<Object, Object> ref = objects.parallelStream()
                        .filter(r -> isReferenced(currentReferences,finalObjects)).collect(Collectors.toMap(r->r,r->r));

                set = ref.entrySet();
                iterator = set.iterator();
                while(iterator.hasNext()){
                    Map.Entry mentry = (Map.Entry)iterator.next();
                    System.out.print("ID of the paper is : "+ mentry.getKey() + " & Title is: ");
                    System.out.println(mentry.getValue());
                }

            }


        }
        catch (IOException e){

        }


    }


    public static boolean isReferenced(ArrayList<String> references, List<JsonObject> objects)
    {
        return references.parallelStream().anyMatch(objects::contains);
    }


}
