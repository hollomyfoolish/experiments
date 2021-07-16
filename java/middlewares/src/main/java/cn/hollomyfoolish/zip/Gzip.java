package cn.hollomyfoolish.zip;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Gzip {
    static String s = "https://webclient-2-gravity.smes.sap.corp/webx/index.html#webclient-ORDR&/Objects/ORDR/List?s=~(status~(version~1~userFilter~'*28DocStatus*20EQ*20*27O*27*29*20and*20*28UserSign*20EQ*20*27!*5bCURRENT_USER*5d*27*29~filterBarLayout~'*7bvisibleItems*3a*5bUserSign*2cDocNum*2cCardCode*2cCardName*2cDocDueDate*2cDocStatus*2cSlpCode*5d*7d~selectedColumns~(~(p~'DocNum)~(p~'CardCode)~(p~'CardName)~(p~'NumAtCard)~(p~'DocDate)~(p~'DocDueDate)~(p~'DocTotal)~(p~'Printed))~groupBy~(~)~sortBy~(~(key~'DocNum~dir~'Descending))))&r=GkXtoEhT";

    public static void compressGZIP(File input, File output) throws IOException {
        try (GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(output))){
            try (FileInputStream in = new FileInputStream(input)){
                byte[] buffer = new byte[1024];
                int len;
                while((len=in.read(buffer)) != -1){
                    out.write(buffer, 0, len);
                }
            }
        }
    }

    public static byte[] compressGZIP(File input) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(512);
        try (GZIPOutputStream out = new GZIPOutputStream(bos)){
            try (FileInputStream in = new FileInputStream(input)){
                byte[] buffer = new byte[1024];
                int len;
                while((len=in.read(buffer)) != -1){
                    out.write(buffer, 0, len);
                }
                out.flush();
            }
        }
        return bos.toByteArray();
    }

    public static byte[] compressGZIP(String s) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(512);
        try (GZIPOutputStream out = new GZIPOutputStream(bos)){
            byte[] src = s.getBytes(StandardCharsets.UTF_8);
            out.write(src, 0, src.length);
            out.flush();
        }
        return bos.toByteArray();
    }

    public static void decompressGzip(File input, File output) throws IOException {
        try (GZIPInputStream in = new GZIPInputStream(new FileInputStream(input))){
            try (FileOutputStream out = new FileOutputStream(output)){
                byte[] buffer = new byte[1024];
                int len;
                while((len = in.read(buffer)) != -1){
                    out.write(buffer, 0, len);
                }
            }
        }
    }

    public static void test(){
        try {
            byte[] srcBytes = s.getBytes(StandardCharsets.UTF_8);
            System.out.println("before: " + srcBytes.length);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(512);
            GZIPOutputStream out = new GZIPOutputStream(bos);
            out.write(srcBytes, 0, srcBytes.length);
            out.finish();
            byte[] destBytes = bos.toByteArray();
            System.out.println("after: " + destBytes.length);

            System.out.println("unzip ...");
            ByteArrayInputStream bin = new ByteArrayInputStream(destBytes);
            GZIPInputStream in = new GZIPInputStream(bin);
            bos.reset();
            byte[] buff = new byte[128];
            for(int read = in.read(buff); read >= 0; read = in.read(buff)){
                bos.write(buff, 0, read);
            }

            System.out.println(new String(bos.toByteArray(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        File in = new File("/Users/i311688/gitrepo/experiments/java/middlewares/src/main/resources/gzip/src.txt");
        File out = new File("/Users/i311688/gitrepo/experiments/java/middlewares/src/main/resources/gzip/dest.gz");

        try {
            compressGZIP(in, out);
            byte[] dest = compressGZIP(in);
            System.out.println("after 1: " + dest.length);
            dest = compressGZIP(s);
            System.out.println("after 2: " + dest.length);
            test();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
