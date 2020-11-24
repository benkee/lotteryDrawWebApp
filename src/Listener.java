import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.File;

@WebListener()
public class Listener implements ServletContextListener {
    //runs when the server is started
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        deleteDir();
    }
    //runs when the server is stopped
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        deleteDir();
    }
    // this function finds if there is a directory named 'CWLotteryWebApp', if there is, it deletes all the files inside
    // the directory then deleted the directory
    private void deleteDir() {
        File dir = new File("CWLotteryWebApp");
        if(dir.exists()) {
            String[] files = dir.list();
            for(String s: files){
                File currentFile = new File(dir.getPath(),s);
                currentFile.delete();
            }
            dir.delete();
        }
    }
}
