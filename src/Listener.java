import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.File;

@WebListener()
public class Listener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        File dir = new File("CWLotteryWebApp");
        if(dir.exists()) {
            for (File file : dir.listFiles())
                if (!file.isDirectory())
                    file.delete();
            dir.delete();
        }
    }
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        File dir = new File("CWLotteryWebApp");
        if (dir.exists()) {
            for (File file : dir.listFiles())
                if (!file.isDirectory())
                    file.delete();
            dir.delete();
        }
    }
}
