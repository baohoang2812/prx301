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
        List<String> urlList = new ArrayList();
        urlList.add("https://www.coursera.org/learn/python");
        urlList.add("https://www.coursera.org/learn/python-data-analysis");
        urlList.add("https://www.coursera.org/learn/html");
        String pageContent = null;
        try {
            for (int i = 0; i < urlList.size(); i++) {
            pageContent = HttpUtils.getContent(urlList.get(i));
            // test output
            // [BHG] TODO Delete on finish project
            PrintWriter pw = new PrintWriter("src/test/courseDetail_" + i + ".html");
            pageContent = TextUtils.refineHtml(pageContent);
            pw.print(pageContent);
            pw.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Crawling finished...");

    }

}
