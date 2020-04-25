import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.vcs.vfs.VcsFileSystem;
import com.intellij.openapi.vfs.VirtualFileSystem;
import org.jdom.JDOMException;

import java.io.IOException;

public class IntellijService {

    public Project getP() {
        return p;
    }

    private Project p=null;

    public  IntellijService() throws IOException, InvalidDataException {
        try {
            p = ProjectManager.getInstance().loadAndOpenProject("C:/Sandboxes/solife_6_1_2_CLV23_FP");
        } catch (JDOMException e) {
            e.printStackTrace();
        }

    }
}
