import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "etu")
public class Etudiant {
    private String nomprenom;
    private String mdp;
    private int age;
    private String email;
    public String getNomprenom() {
        return nomprenom;
    }

    @XmlElement(name = "nomprenom")
    public void setNomprenom(String nomprenom) {
        this.nomprenom = nomprenom;
    }

    public String getMdp() {
        return mdp;
    }

    @XmlElement(name = "mdp")
    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public int getAge() {
        return age;
    }
    @XmlElement(name = "age")
    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }
    @XmlElement(name = "email")
    public void setEmail(String email) {
        this.email = email;
    }


}
