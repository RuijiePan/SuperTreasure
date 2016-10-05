package com.supertreasure.service;

import android.net.Uri;

import com.supertreasure.util.MD5;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by yum on 2016/1/5.
 */
public class MySmallShopService {
/*

    */
/**
     * 从网络获取联系人信息
     * @return
     *//*

    public static List<MyContact> getContacts() throws Exception {//容易网络阻塞
        // android.os.NetworkOnMainThreadException 网络访问出现在 主线程，报错
        System.out.println("getContacts begin");
        String path = "http://172.18.0.157:8080/AndroidWebData/contacts.xml";
        HttpURLConnection connection = (HttpURLConnection) new URL(path).openConnection();
        connection.setConnectTimeout(5000);
        connection.setRequestMethod("GET");
        System.out.println("getContacts end");
        if (connection.getResponseCode()==200){
            return parseXML(connection.getInputStream());
        }
        return null;
    }

    private static List<MyContact> parseXML(InputStream inputStream) throws Exception {
        System.out.println("parseXML begin");
        XmlPullParser parser = Xml.newPullParser();
        MyContact contact = null;
        List<MyContact> contacts = new ArrayList<MyContact>();
        parser.setInput(inputStream, "utf-8");
        int event = parser.getEventType();
        while(event != XmlPullParser.END_DOCUMENT){
            switch (event){
                case XmlPullParser.START_TAG:
                    if (parser.getName().equals("contact")){
                        contact = new MyContact();
                        contact.id = new Integer(parser.getAttributeValue(0));//获取第一个属性
                    }else if(parser.getName().equals("name")){
                        contact.name = parser.nextText();//获取下一个节点的文本
                    }else if(parser.getName().equals("image")){
                        contact.image = parser.getAttributeValue(0);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if (parser.getName().equals("contact")){
                        contacts.add(contact);
                        contact = null;
                    }
                    break;
            }
            event = parser.next();
        }
        for (MyContact c:contacts)
            System.out.println(c.toString());
        System.out.println("parseXML end");
        return contacts;
    }
*/

    /**
     *  获取网络图片，如果图片存在于缓存中，就返回该图片uri，否则从网络中加载并且把图片缓存起来。
     * @param path
     * @return
     */
    public static Uri getImage(String path,File cacheDir) throws IOException {
        //path ->MD5->32字符串.jpg
        String fileName = MD5.GetMD5Code(path)+path.substring(path.lastIndexOf("."));
        File localFile = new File(cacheDir,fileName);//在cacheDir目录下寻找  文件
        if (localFile.exists()){//判断是否存在本地文件
            System.out.println("本地存在文件，从本地获取");
            return Uri.fromFile(localFile);//存在就 直接返回文件的uri
        }else{//不存在文件，就要加载了
            System.out.println("本地不存在文件，从网络获取");
            HttpURLConnection connection = (HttpURLConnection) new URL(path).openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            if (connection.getResponseCode() == 200){
                FileOutputStream outputStream = new FileOutputStream(localFile);//把网络的数据 输出到 这个文件上
                InputStream inputStream = connection.getInputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while((len=inputStream.read(buffer))!=-1){
                    outputStream.write(buffer,0,len);
                }
                inputStream.close();
                outputStream.close();
                System.out.println("本地不存在文件，从网络获取 获取成功");

                return Uri.fromFile(localFile);
            }
            System.out.println("本地不存在文件，从网络获取 获取失败");

        }
        return null;
    }
}
