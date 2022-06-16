package hierarchy.persistence.types;

public class VirtualLink {
    private String link_id;
    private String persistent_id;
    private String from;
    private String to;
    private String text;
    private String toText;
    private String name;
    private String inverseName;

    public VirtualLink(String link_id, String persistent_id, String from, String to, String text, String toText, String name, String inverseName) {
        this.link_id = link_id;
        this.persistent_id = persistent_id;
        this.from = from;
        this.to = to;
        this.text = text;
        this.toText = toText;
        this.name = name;
        this.inverseName = inverseName;
    }

    public String getLink_id() {
        return link_id;
    }

    public String getPersistent_id() {
        return persistent_id;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getText() {
        return text;
    }

    public String getToText() {
        return toText;
    }

    public String getName() {
        return name;
    }

    public String getInverseName() {
        return inverseName;
    }
}
