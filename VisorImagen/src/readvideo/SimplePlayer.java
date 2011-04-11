package readvideo;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;
import javax.media.*;
import javax.media.control.*;
import java.net.*;
public class SimplePlayer extends JFrame{
        private Player                                        player;
        private Component                                visualMedia;
        private Component                                mediaControl;
        private Container                                container;
        private File                                        mediaFile;
        private URL                                                fileURL;
        private FrameGrabbingControl        frameGrabber;
        public SimplePlayer (){
                super ("Simple player");
                container = getContentPane ();
                // controls panel
                JPanel buttonPanel = new JPanel ();
                container.add (buttonPanel, BorderLayout.NORTH);
                // open file button
                JButton openFile = new JButton ("Open File");
                buttonPanel.add (openFile);
                // register actionListener
                openFile.addActionListener (new ActionListener (){
                        public void actionPerformed (ActionEvent evt){
                                mediaFile = getFile ();
                                if (mediaFile != null){
                                        try{
                                                fileURL = mediaFile.toURL ();
                                        } catch (MalformedURLException e){
                                                e.printStackTrace ();
                                                System.err.println ("Bad URL");
                                        }
                                        makePlayer (fileURL.toString ());
                                }
                        }
                });
                // URL opening button
                JButton openURL = new JButton ("Open Locator");
                buttonPanel.add (openURL);
                // register actionListener
                openURL.addActionListener (new ActionListener (){
                        public void actionPerformed (ActionEvent evt){
                                String addressName = getMediaLocation ();
                                if (addressName != null)
                                        makePlayer (addressName);
                        }
                });
                Manager.setHint (Manager.LIGHTWEIGHT_RENDERER, true);
        }
        public File getFile (){
                JFileChooser fileChooser = new JFileChooser ();
                fileChooser.setFileSelectionMode (JFileChooser.FILES_ONLY);
                int res = fileChooser.showOpenDialog (this);
                if (res == JFileChooser.CANCEL_OPTION){
                        return null;
                }
                else
                        return fileChooser.getSelectedFile ();
        }
        public String getMediaLocation (){
                String input = JOptionPane.showInputDialog (this, "Enter URL");
                // OK - no input
                if (input != null && input.length () == 0){
                        return null;
                }
                return input;
        }
        public void makePlayer (String mediaLocation){
                if (player != null)
                        removePlayerComponents ();
                MediaLocator mediaLocator = new MediaLocator (mediaLocation);
                if (mediaLocator == null){
                        System.err.println ("Error opening file");
                        return;
                }
                // create player
                try{
                        player = Manager.createPlayer (mediaLocator);
                        // register ControllerListener
                        player.addControllerListener (new PlayerEventHandler ());
                        // call realize to enable rendering
                        player.realize ();
                } catch (NoPlayerException e){
                        e.printStackTrace ();
                        System.err.println ("COULDNT FIND PLAYER");
                } catch (IOException e){
                        e.printStackTrace ();
                        System.out.println ("IO error");
                }
        }
        public void removePlayerComponents (){
                // remove previous video component
                if (visualMedia != null){
                        container.remove (visualMedia);
                }
                // remove previous media control if it exists
                if (mediaControl != null)
                        container.remove (mediaControl);
                // stop player
                player.close ();
        }
        public void getMediaComponents (){
                // get visual omponent from player
                visualMedia = player.getVisualComponent ();
                // add to component
                if (visualMedia != null)
                        container.add (visualMedia, BorderLayout.CENTER);
                // get player control GUI
                mediaControl = player.getControlPanelComponent ();
                // add controls
                if (mediaControl != null){
                        container.add (mediaControl, BorderLayout.SOUTH);
                }
        }
        private class PlayerEventHandler extends ControllerAdapter{
                public void realizeComplete (RealizeCompleteEvent evt){
                        // get frame grabber
                        frameGrabber = (FrameGrabbingControl)player.getControl("javax.media.control.FrameGrabbingControl");
                        System.out.println("framegrabber: " + frameGrabber);
                        // start prefetch
                        player.prefetch ();
                }
                public void prefetchComplete (PrefetchCompleteEvent prefectDoneEvent){
                        getMediaComponents ();
                        // validate
                        validate ();
                        // start media
                        player.start ();
                }
                public void endOfMedia (EndOfMediaEvent evt){
                        player.setMediaTime (new Time (0));
                        player.stop ();
                }
        }
        public static void main (String[] args){
                SimplePlayer testPlayer = new SimplePlayer ();
                testPlayer.setSize (300, 300);
                testPlayer.setLocation (300, 300);
                testPlayer.setDefaultCloseOperation (EXIT_ON_CLOSE);
                testPlayer.setVisible (true);
        }
}