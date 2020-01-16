package nl.dehaagsehogeschool.thechallenge;

import java.text.SimpleDateFormat;
import java.util.*; // Scanner om invoer te lezen
import java.util.concurrent.TimeUnit;

import com.fazecast.jSerialComm.*;
import static com.fazecast.jSerialComm.SerialPort.*;


public class ComPortSendReceive {

    public static SerialPort serialPort;

    public static void main(String[] args) {

        String portName;
        SerialPort portNames[] = SerialPort.getCommPorts();

        if (portNames.length == 0) {
            System.out.println("Er zijn geen seriële poorten. Sluit je Micro:bit aan!");
            return;
        }

        if (portNames.length == 1) {
            portName = portNames[0].getSystemPortName();
            System.out.println(portName + " wordt nu gebruikt.");
        } else {
            System.out.println("Meerdere seriële poorten gedetecteerd: ");
            for (int i = 0; i < portNames.length; i++) {
                System.out.println(portNames[i].getSystemPortName());
            }

            System.out.println("Type poortnaam die je wilt gebruiken en druk Enter...");
            Scanner in = new Scanner(System.in);
            portName = in.next();
        }

        serialPort = SerialPort.getCommPort(portName);

        try {
            // seriële poort openen en instellen
            serialPort.openPort();
            serialPort.setComPortParameters(9600, 8, ONE_STOP_BIT, NO_PARITY);
            serialPort.setFlowControl(FLOW_CONTROL_DISABLED);

            // Schrijven naar seriële poort: schrijf string naar poort
            String uitvoer = " nse rulez "; // de tekst die je naar de Microbit wilt sturen
            byte[] buffer = uitvoer.getBytes();
            serialPort.writeBytes(buffer, uitvoer.length());

            System.out.println("String naar seriële poort geschreven...");

        } catch (Exception ex) {
            System.out.println("Fout bij schrijven naar seriële poort: " + ex);
        }

        try {
            Thread.sleep(1000); // 5 seconden pauzeren
        } catch (Exception e) {
            e.printStackTrace();
        }

        StringBuilder bericht = new StringBuilder();
        InsertIntoSQL database = new InsertIntoSQL();   //Deze regel uitcommenten als SQL nog niet werkt.
        serialPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }

            public void serialEvent(SerialPortEvent event) {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) { return; }
                byte buffer[] = new byte[serialPort.bytesAvailable()];
                int numRead = serialPort.readBytes(buffer, buffer.length);
                SerialPort port = SerialPort.getCommPorts()[0];


                for (byte b : buffer) {
                    if ((b == '\r' || b == '\n') && bericht.length() > 0) { // regeleinde gedetecteerd ('\r' of '\n')

                        // StringBuilder naar String converteren
                        String berichtData = bericht.toString();

                        // String naar float omzetten
//                        Float temperatuur = Float.parseFloat(berichtData);

//                        String naam = berichtData;

                        Scanner data = new Scanner(port.getInputStream());  // Lees de info uit de poort met Scanner

                        float afstand = data.nextInt();

//                        database.insert(naam);

                        String naam = "A4.2";
                        database.update(naam, afstand);

                        // delay 1 seconde
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                        }

                        System.out.print("Uitgevoerd");

                        bericht.setLength(0);
                    } else {
                        bericht.append((char) b);
                    }
                }
            }
        });
    }
}
