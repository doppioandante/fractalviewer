import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class FractalViewer
{
	public static void main(String[] args) throws Exception
	{
		try
		{
			UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName());
		}
		catch(UnsupportedLookAndFeelException|ClassNotFoundException e)
		{
			// just ignore this exception, the default look and feel will be ok
		}

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				FractalViewerFrame frame = new FractalViewerFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				frame.pack();
				frame.setVisible(true);
			}
		});
	}
}