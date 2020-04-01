import javax.jws.WebMethod;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/classes")
public class PlasmaClassComposition {
    public static ArrayList<Etudiant> getList() {
        return list;
    }

    private static ArrayList<Etudiant> list = new ArrayList<Etudiant>();

    @GET
    @Path("all")
    @Produces({MediaType.APPLICATION_JSON})
    public Etudiant show()
    {
        return new Etudiant();
    }
    ///TODO: YEEEEEEEEESSSSSSSSSSSSSSSS !!!!!!!!!!!!


}
