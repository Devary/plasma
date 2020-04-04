package services.reporting;

import java.util.HashMap;

public class Report {


    private HashMap<Integer,String> messages;
    private int i=0;

    public Report(String message) {
        addMessage(message);
    }


    public Report() {

    }

    public void addMessage(String message) {
        messages.put(i,message);
        this.i++;
    }

    public HashMap<Integer, String> getMessages() {
        return messages;
    }

    public String showMessages()
    {
        StringBuilder sb = new StringBuilder();
        if (messages.isEmpty())
            return "";
        else
        {
            for (String mes:this.messages.values())
            {
                sb.append(mes).append("\n");
            }
        }

        return sb.toString();
    }
}
