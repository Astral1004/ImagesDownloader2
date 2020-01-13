import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.map.ObjectMapper;
import vk.Example;
import vk.Item;
import vk.Size;

import java.io.*;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws IOException {

        String filePath2 = ("C:\\data/response-vk2800.json") ;//Путь где лежит база

        ObjectMapper mapper = new ObjectMapper();//инициализация библиотеки Jackson
        Example ex = (Example) mapper.readValue(new FileInputStream(filePath2), Example.class);
        for (Item
        		item:ex.getResponse().getItems()){
                download(item,item.getId().toString());
        }
    }

    public static Item download(Item attachments,String dir)
    {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for(final Size attachment : attachments.getSizes()){
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try{
                        String attUrl = attachment.getUrl();
                        String fileName = attachment.getType()+".jpg";
                        String destLoc = "C:/data/loaded/"+dir+"/";
                        URL url = new URL(attUrl);
                        File fileLocation = new File(destLoc, fileName);
                        FileUtils.copyURLToFile(url, fileLocation);
                        if(fileLocation.exists()) {
                            //attachment.setDownStatus("Completed");
                        }
                        /*System.out.println("Ширина = " + attachment.getWidth());*/
                    }
                    catch(Exception e){
                        //attachment.setDownStatus("Failed");
                    }
                }
            });
        }
        executorService.shutdown();
        return attachments;
    }
}
