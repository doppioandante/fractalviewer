import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.io.File;
import java.text.NumberFormat;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class FractalViewerFrame extends JFrame implements ActionListener, ChangeListener, KeyListener
{
	public FractalViewerFrame()
	{
		super();

		// TODO: could be a parameter of this constructor?
		this.drawer = new HistogramMTMandelbrotDrawer(
				new Point2D.Double(0.0, 0.0),
				-2.0, 1.0, -1.0, 1.0,
				1.0,
				512,
				Runtime.getRuntime().availableProcessors());

		createUI();
		this.panel = new FractalPanel(
				new Dimension(1024, 1024/3*2),
				this.drawer);

		add(this.panel, BorderLayout.CENTER);
	}

	private void createUI()
	{
		// toolbar = save + reset + threads
		JToolBar toolbar = new JToolBar(JToolBar.HORIZONTAL);
		toolbar.setFloatable(false);

		JButton saveButton = new JButton("Save");
		saveButton.setActionCommand("save");
		JButton resetButton = new JButton("Reset");
        resetButton.setActionCommand("reset");

		saveButton.addActionListener(this);
        resetButton.addActionListener(this);

		JLabel threadLabel = new JLabel("#Threads:");
		int processors = Runtime.getRuntime().availableProcessors();
		JSpinner threadSpinner = new JSpinner(
				new SpinnerNumberModel(processors, 1, processors, 1));
		JSpinner.DefaultEditor ed = (JSpinner.DefaultEditor)threadSpinner.getEditor();
		ed.getTextField().setColumns(5);

		threadSpinner.addChangeListener(this);

		toolbar.add(saveButton);
		toolbar.add(resetButton);
		toolbar.add(Box.createGlue());
		toolbar.add(threadLabel);
		toolbar.add(threadSpinner);

		// info = x, y, zoom
		JToolBar info = new JToolBar(JToolBar.HORIZONTAL);
		info.setFloatable(false);
		//info.setLayout(new FlowLayout(FlowLayout.LEADING));

		JLabel xLabel = new JLabel("x:");
		JLabel yLabel = new JLabel("y:");
		JLabel zoomLabel = new JLabel("zoom:");
		JTextField xField = new JFormattedTextField(NumberFormat.getInstance());
		xField.setColumns(8);
		JTextField yField = new JFormattedTextField(NumberFormat.getInstance());
		yField.setColumns(8);
		JTextField zoomField = new JFormattedTextField(NumberFormat.getInstance());
		zoomField.setColumns(8);
		
		xField.addKeyListener(this);
		yField.addKeyListener(this);
		zoomField.addKeyListener(this);
		
		info.add(xLabel);
		info.add(xField);
		info.addSeparator();
		info.add(yLabel);
		info.add(yField);
		info.add(Box.createGlue());
		info.add(zoomLabel);
		info.add(zoomField);

		// top = toolbar / info
		JPanel top = new JPanel(new BorderLayout());
		top.add(toolbar, BorderLayout.PAGE_START);
		top.add(info, BorderLayout.PAGE_END);

		setLayout(new BorderLayout());
		add(top, BorderLayout.PAGE_START);

		setTitle("Fractal Viewer");
	}
	
	public void actionPerformed(ActionEvent event)
	{
		if (event.getActionCommand().equals("reset"))
		{
			this.drawer.setOrigin(new Point2D.Double(0.0, 0.0));
			this.drawer.setZoom(1.0);
			this.panel.repaint();
		}
		else if (event.getActionCommand().equals("save"))
		{
			JFileChooser fileChooser = new JFileChooser();
			FileFilter filter = new FileNameExtensionFilter("JPEG files", "jpg", "jpeg");
			fileChooser.setAcceptAllFileFilterUsed(false);
			fileChooser.addChoosableFileFilter(filter);
			int returnVal = fileChooser.showOpenDialog(this);
			
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				File file = fileChooser.getSelectedFile();
				if (!filter.accept(file))
				{
					file = new File(file.getAbsolutePath() + ".jpg");
				}
				try
				{
					// TODO: gray out the fractal panel... maybe in other ops as well?
					// TODO: more options for saving?
					FractalSaver.save(file, this.drawer, this.panel.getSize());
					JOptionPane.showMessageDialog(this, "Fractal written to " + file.getName());
				}
				catch(Exception e)
				{
					// TODO: show an error dialog, more granularity on the exception
				}
			}
		}
	}
	
	
	public void stateChanged(ChangeEvent e) 
	{
        SpinnerModel model = ((JSpinner)e.getSource()).getModel();
        assert(model instanceof SpinnerNumberModel);
        
        this.drawer.setThreads(((SpinnerNumberModel)model).getNumber().intValue());
    }
	
	public void keyPressed(KeyEvent event) 
	{
		if(event.getKeyCode() == KeyEvent.VK_ENTER)
		{
			// TODO: get specified coordinates
			Point2D origin = new Point2D.Double(0.0, 0.0);
			this.drawer.setOrigin(origin);
			
			this.panel.repaint();
		}
	}	
	public void keyReleased(KeyEvent event) {}
	
	public void keyTyped(KeyEvent event) {}
	
	private AbstractMTFractalDrawer drawer;
	private FractalPanel panel;
}
