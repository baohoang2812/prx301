/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import prx.utils.HttpUtils;
import prx.utils.TextUtils;

/**
 *
 * @author Eden
 */
public class Course4U {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Crawling data...");
        System.out.println("Crawling finished...");
        List<String> urlList = new ArrayList();
        urlList.add("https://www.coursera.org/browse/computer-science");
        String pageContent = null;
        try {
            for (String page : urlList) {
                pageContent = HttpUtils.getContent(page);
                // test output
                // [BHG] TODO Delete on finish project
                PrintWriter pw = new PrintWriter("rawPageContent.html");
                pw.print(pageContent);
                pw.close();
                pageContent = TextUtils.refineHtml(pageContent);
                pw = new PrintWriter("refinedPageContent.html");
                pw.print(pageContent);
                pw.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

  
}
