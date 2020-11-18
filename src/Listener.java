import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.File;

@WebListener()
public class Listener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        deleteDir();
    }
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        deleteDir();
    }

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
