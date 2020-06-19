/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wellformer;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Eden
 */
public class Wellformer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO TEST SyntaxWellformer
        SyntaxWellformer wellformer = new SyntaxWellformer();
        Map<String,String> map  =new LinkedHashMap<>();
        map.put("Attribute không value", "<h1 checked> YEAH</h1>");
        map.put("Value không bọc trong cặp nháy", "<h1 aa= aa> <img a=a />YEAH</h1>");
        map.put("Attribute dính liền nhau", "<h1 a=\"1\"b='2'c=3>YEAH</h1>");
        map.put("Empty element", "<h1><img src=\"\"><br><hr/></h1>");
        map.put("Lỗi đóng mở thẻ", "<li><a>Sach moi</a></h3>");
        
        for(Map.Entry<String, String> entry: map.entrySet()){
            System.out.println(entry.getKey());
            
            System.out.println(entry.getValue());
            System.out.println(wellformer.check(entry.getValue()));
            System.out.println();
        }
    }
    
}
