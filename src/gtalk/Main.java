package gtalk;



import java.util.ArrayList;
import java.util.Collection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;


public class Main implements PacketListener{

    private static String username = "Youremail@gmail.com";
    private static String password = "YourPassword email";

    ConnectionConfiguration connConfig;
    XMPPConnection connection;
    private List<Message> messages = new ArrayList();

    public Main() throws XMPPException {
        connConfig =
                new ConnectionConfiguration("talk.google.com", 5222, "gmail.com");
        connection = new XMPPConnection(connConfig);
        connection.connect();
        connection.login(username, password);
        // tell the world (or at least our friends) that we are around
        Presence presence = new Presence(Presence.Type.available);
        presence.setMode(Presence.Mode.chat);
        connection.sendPacket(presence);

        // to listen for incoming messages on this connection
        PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
        connection.addPacketListener((PacketListener)this, filter);

        Roster friendsRoster = connection.getRoster();
        Collection<RosterEntry> rosterIter = friendsRoster.getEntries();
        for (RosterEntry entry : rosterIter) {
            System.out.println(entry.getName() + " (" + entry.getUser() + ")");
        } // rosterIter

    }

    public void sendMessage(String to, String message) {
        Message msg = new Message(to, Message.Type.chat);
        msg.setBody(message);
        connection.sendPacket(msg);

    }

    public void disconnect() {
        connection.disconnect();
    }


    public void processPacket(Packet packet) {
        Message message = (Message)packet;
        System.out.println("Message (from: " + message.getFrom() + "): " +
                           message.getBody());
        messages.add(0, message);

    }


    public static void main(String[] args) throws XMPPException {
        Main messageSender = new Main();
        messageSender.sendMessage("email@gmail.com",
                                  "your message chat");
        messageSender.disconnect();
    }

}
