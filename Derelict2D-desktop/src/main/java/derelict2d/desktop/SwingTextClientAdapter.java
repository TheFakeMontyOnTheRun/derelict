/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package derelict2d.desktop;


import br.odb.gameapp.ApplicationClient;
import br.odb.gamerendering.rendering.AssetManager;
import br.odb.utils.FileServerDelegate;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 *
 * @author monty
 */
class SwingTextClientAdapter implements ApplicationClient {

    private final JTextArea output;
    String log = "*DERELICT2D LOG*";
    private boolean continueRunning = true;
    private final AssetManager resManager;

    public void clear() {
        output.setText("");
    }

    public SwingTextClientAdapter(JTextArea txtOutput, AssetManager resManager) {
        this.output = txtOutput;
        this.resManager = resManager;
    }

    @Override
    public void setClientId(String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void printWarning(String msg) {
        printNormal("*" + msg + "*");
    }

    @Override
    public void printError(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }

    @Override
    public void printVerbose(String msg) {
        printNormal(">" + msg + "<");
    }

    @Override
    public String requestFilenameForSave() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String requestFilenameForOpen() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getInput(String msg) {
        return JOptionPane.showInputDialog(null, msg, "", 1);
    }

    @Override
    public int chooseOption(String question, String[] options) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FileServerDelegate getFileServer() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void printNormal(String string) {
        this.output.append("\n" + string);
        log += "\n" + string;
    }

    @Override
    public void alert(String string) {
        printNormal("** " + string + " **");
    }

    @Override
    public void playMedia(String uri, String alt) {
        //sub-optimal, I know...
        //newAudioClip(  getClass().getResource( uri )).play();
        resManager.getMediaPlayer(uri).play();
    }

    @Override
    public void sendQuit() {
        continueRunning = false;
    }

    @Override
    public boolean isConnected() {
        return continueRunning;
    }

    @Override
    public String openHTTP(String url) {
        String response = "";
        BufferedReader in = null;
        String inputLine;
        URLConnection conn = null;
        URL myURL = null;

        try {
            myURL = new URL(url);

            conn = myURL.openConnection();
            in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            while ((inputLine = in.readLine()) != null) {

                response += inputLine;
            }
            in.close();
        } catch ( Exception e ) {
            printError("Something went wrong with your request");
        }

        return response;

    }

    @Override
    public void shortPause() {
    }
}
