package com.jupiter.application.data;


import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;


import java.io.*;
import java.net.URI;
import java.util.StringTokenizer;

/**
 * Used for loading cab data into database. Loads 100,000 lat long points from data.txt.
 * data.txt contains 100k lat long points taken from geonames.org. These lat long points to
 * locations around los angeles. data is 95% clean.
 */
public class DataLoader {

    public static void main(String[] args) throws Exception {
        File file = new File("src/test/java/com/jupiter/Application/data/data.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        long count = 0;
        while ((line = br.readLine()) != null && count < 100000) {
                StringTokenizer st = new StringTokenizer(line);
                String latitude = null;
                String longitude = null;
                while (st.hasMoreTokens()) {
                    try {
                        String someToken = st.nextToken();
                        Float.parseFloat(someToken);
                         if (latitude == null) {
                            latitude = someToken;
                        } else if (longitude == null) {
                            longitude = someToken;
                        }
                    } catch (NumberFormatException ex) {
                        //ignore exceptions for now. application catches bad request errors.
                    }
                }
                count++;
                put(String.valueOf(count), latitude, longitude);

            }
        br.close();
    }

    public static void put(String id, String latitude, String longitude) throws Exception {
        ClientHttpRequestFactory delegate = new SimpleClientHttpRequestFactory();
        String endpoint = "http://localhost:8080/cabs/" + id;
        URI uri = new URI(endpoint);
        ClientHttpRequest req = delegate.createRequest(uri, HttpMethod.PUT);
        OutputStream os = req.getBody();
        OutputStreamWriter wr = new OutputStreamWriter(os);
        //{"latitude":37.412982275051085,"longitude":-122.00695037841797}
        String data = "{\"latitude\":" + latitude + ",\"longitude\":" + longitude + "}";
        wr.write(data);
        wr.flush();
        // set content type
        req.getHeaders().set("Content-type", "application/json");
        ClientHttpResponse resp = req.execute();
        if (resp.getRawStatusCode() == 200){
           // System.out.println("Successfully put "+id);
        } else {
            System.out.println("Put failed! Id "+id+" Data "+data);
        }

    }
//    Some data munging code.
//    public void readWriteFile() throws Exception{
//        File file = new File("src/test/java/com/jupiter/Application/data/US.txt");
//        File fout = new File("src/test/java/com/jupiter/Application/data/data.txt");
//        FileOutputStream fos = new FileOutputStream(fout);
//        BufferedReader br = new BufferedReader(new FileReader(file));
//        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
//        String line;
//        long count = 1;
//        while ((line = br.readLine()) != null && count < 100000) {
//            if (line.contains("Los_Angeles")) {
//                StringTokenizer st = new StringTokenizer(line);
//                String geoName = null;
//                String latitude = null;
//                String longitude = null;
//                while (st.hasMoreTokens()) {
//                    try {
//                        String someToken = st.nextToken();
//                        Float.parseFloat(someToken);
//                        if (geoName == null) {
//                            geoName = someToken;
//                        } else if (latitude == null) {
//                            latitude = someToken;
//                        } else if (longitude == null) {
//                            longitude = someToken;
//                        }
//                    } catch (NumberFormatException ex) {
//
//                    }
//                }
//                count++;
//                bw.write(latitude + " " + longitude);
//                put(String.valueOf(count), latitude, longitude) ;
//                bw.newLine();
//            }
//
//        }
//        br.close();
//        bw.close();
//    }
}




