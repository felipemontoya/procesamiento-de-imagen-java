package readvideo;

/*  JMF_Movie_Analyzer.java

Glen Cowan
RHUL Physics Department
4 July, 2002
Based on JMF_MovieReader.java distributed with the ImageJ package

This ImageJ plugin reads in an avi file and steps through frame by frame.
The R, G and B pixel values are individually summed in a 4x4
box in the centre of the image.  These values and the frame number
are written to the output window and also to an output file.

*/

//import ij.*;
//import ij.plugin.frame.*;
//import ij.process.*;
//import ij.io.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import javax.media.*;
import javax.media.control.FrameGrabbingControl;
import javax.media.control.FramePositioningControl;
import javax.media.format.VideoFormat;
import javax.media.protocol.*;
import javax.media.util.BufferToImage;
import javax.swing.JFileChooser;
import javax.swing.JFrame;


public class JMF_Movie_Analyzer extends JFrame implements ControllerListener, ActionListener {
	Player p;
	FramePositioningControl fpc;
	FrameGrabbingControl fgc;
	Object waitSync = new Object();
	boolean stateTransitionOK = true;
	int totalFrames = FramePositioningControl.FRAME_UNKNOWN;
	BufferToImage frameConverter;
	String name;
	boolean grayscale = false;
	//ImageStack stack;

	public JMF_Movie_Analyzer() {
		//System.out.println("");
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog( null );

		String url = encodeURL("file://"+fileChooser.getSelectedFile().getAbsolutePath());
		//System.out.println("path: "+path);
		//System.out.println("url: "+url);
		//url = "http://rsb.info.nih.gov/movie.avi";
		//url = "file:///Macintosh HD/Desktop%20Folder/test.mov";
		MediaLocator ml;
		if ((ml = new MediaLocator(url)) == null) {
			System.out.println("Cannot build media locator from: ");
			return;
		}
		DataSource ds = null;
		// Create a DataSource given the media locator.
		System.out.println("creating JMF data source");
		try {
		ds = Manager.createDataSource(ml);
		} catch (Exception e) {
			System.out.println("Cannot create DataSource from: " + ml);
			return;
		}
 		openMovie(ds);
	}

	public String encodeURL(String url) {
		int index = 0;
		while (index>-1) {
			index = url.indexOf(' ');
			if (index>-1)
				url = url.substring(0,index)+"%20"+
                              url.substring(index+1,url.length());
		}
		return url;
	}

	public boolean openMovie(DataSource ds) {

		System.out.println("opening: "+ds.getContentType());
		try {
			p = Manager.createPlayer(ds);
		} catch (Exception e) {
			error("Failed to create a player from the given DataSource:\n \n" +
                        e.getMessage());
			return false;
		}

		p.addControllerListener(this);
		p.realize();//TODO: aqu'i es donde falla!!!!!!
		if (!waitForState(p.Realized)) {
			error("Failed to realize the JMF player.");
			return false;
		}

		// Try to retrieve a FramePositioningControl from the player.
		fpc = (FramePositioningControl)p.getControl(
                   "javax.media.control.FramePositioningControl");
		if (fpc == null) {
			error("The player does not support FramePositioningControl.");
			return false;
		}

		// Try to retrieve a FrameGrabbingControl from the player.
		fgc = (FrameGrabbingControl)p.getControl(
                   "javax.media.control.FrameGrabbingControl");
		if (fgc == null) {
			error("The player does not support FrameGrabbingControl.");
			return false;
		}

		Time duration = p.getDuration();
                double durationSeconds = duration.getSeconds();
                System.out.println("durationSeconds = " + durationSeconds);
		if (duration != Duration.DURATION_UNKNOWN) {
			//System.out.println("Movie duration: " + duration.getSeconds());
			totalFrames = fpc.mapTimeToFrame(duration)+1;
			if (totalFrames==FramePositioningControl.FRAME_UNKNOWN)
			System.out.println("The FramePositioningControl does not support mapTimeToFrame.");
		} else {
			System.out.println("Movie duration: unknown");
		}

		// Prefetch the player.
		p.prefetch();
		if (!waitForState(p.Prefetched)) {
			error("Failed to prefetch the player.");
			return false;
		}

		// Display the visual & control component if there's one.
		setLayout(new FlowLayout());
		Component vc;
		if ((vc = p.getVisualComponent()) != null) {
			add(vc);
		}

		Buffer frame = fgc.grabFrame();
		VideoFormat format = (VideoFormat)frame.getFormat();
		frameConverter = new BufferToImage(format);
		setVisible(true);

// get output file for analyzed pixel data

		
		return true;
	}

	public void addNotify() {
		super.addNotify();
		pack();
	}

	/** Block until the player has transitioned to the given state. */
	boolean waitForState(int state) {
		synchronized (waitSync) {
			try {
				while (p.getState() < state && stateTransitionOK)
				waitSync.wait();
 			} catch (Exception e) {}
		}
		return stateTransitionOK;
	}

	public void actionPerformed(ActionEvent ae) {
		int currentFrame = fpc.mapTimeToFrame(p.getMediaTime());
		if (currentFrame != FramePositioningControl.FRAME_UNKNOWN)
		System.out.println("Current frame: " + currentFrame);
	}

	private void error(String msg) {
		System.out.println("JMF Movie Reader"+ msg);
		System.out.println("");
	}

	private boolean saveImage(int count, Buffer frame) {
//		Image img = frameConverter.createImage(frame);
//		ImageProcessor ip = new ColorProcessor(img);
//		if (count==1) {
//			int width = ip.getWidth();
//			int height = ip.getHeight();
//			int size = (width*height*totalFrames*4)/(1024*1024);
//			System.out.println("Allocating "+width+"x"+height+"x"+totalFrames+
//                              "stack ("+size+"MB)");
//			stack = allocateStack(width, height, totalFrames);
//			if (stack==null) {
//				IJ.outOfMemory("JMF Movie Opener");
//				return false;
//			}
//			grayscale = isGrayscale(ip);
//		}
//		if (grayscale)
//			ip = ip.convertToByte(false);
//		stack.setPixels(ip.getPixels(), count);
		return true;
	}

	/**  Controller Listener */
	public void controllerUpdate(ControllerEvent evt) {

		if (evt instanceof ConfigureCompleteEvent ||
		evt instanceof RealizeCompleteEvent ||
		evt instanceof PrefetchCompleteEvent) {
			synchronized (waitSync) {
			stateTransitionOK = true;
			waitSync.notifyAll();
		}
		} else if (evt instanceof ResourceUnavailableEvent) {
			synchronized (waitSync) {
				stateTransitionOK = false;
				waitSync.notifyAll();
			}
		} else if (evt instanceof EndOfMediaEvent) {
			p.setMediaTime(new Time(0));
			//p.start();
			//p.close();
			//System.exit(0);
		} else if (evt instanceof SizeChangeEvent) {
		}
	}

//	ImageStack allocateStack(int width, int height, int size) {
//		ImageStack stack=null;
//		byte[] temp;
//		try {
//			stack = new ImageStack(width, height);
//			for (int i=0; i<size; i++) {
//				if (grayscale)
//					stack.addSlice(null, new byte[width*height]);
//				else
//					stack.addSlice(null, new int[width*height]);
//			}
//			temp = new byte[width*height*4*5+1000000];
//	 	}
//		catch(OutOfMemoryError e) {
//			if (stack!=null) {
//				Object[] arrays = stack.getImageArray();
//				if (arrays!=null)
//					for (int i=0; i<arrays.length; i++)
//				arrays[i] = null;
//			}
//			stack = null;
//		}
//		temp = null;
//		System.gc();
//		System.gc();
//		return stack;
//	}

//	boolean isGrayscale(ImageProcessor ip) {
//		int[] pixels = (int[])ip.getPixels();
//		boolean grayscale = true;
//		int c, r, g, b;
//		for (int i=0; i<pixels.length; i++) {
//			c = pixels[i];
//			r = (c&0xff0000)>>16;
//			g = (c&0xff00)>>8;
//			b = c&0xff;
//			if (r!=g || r!=b || g!=b) {
//				grayscale = false;
//				break;
//			}
//		}
//		return grayscale;
//	}

                public static void main (String[] args){
                JMF_Movie_Analyzer testPlayer = new JMF_Movie_Analyzer ();
                testPlayer.setSize (300, 300);
                testPlayer.setLocation (300, 300);
                testPlayer.setDefaultCloseOperation (EXIT_ON_CLOSE);
                testPlayer.setVisible (true);
        }

}