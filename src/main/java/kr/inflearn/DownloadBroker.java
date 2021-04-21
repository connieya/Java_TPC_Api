package kr.inflearn;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadBroker implements Runnable {
    private String address;
    private String fileName;

    public DownloadBroker(String address, String fileName) {
        this.address = address;
        this.fileName = fileName;
    }

    @Override
    public void run() {
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            URL url = new URL(address);
            InputStream is = url.openStream();
            BufferedInputStream inputStream = new BufferedInputStream(is);

            int data;
            while ((data=inputStream.read() )!= -1){
                bos.write(data);
            }
            bos.close();
            inputStream.close();
            System.out.println("download complete ... ");
            System.out.println(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
