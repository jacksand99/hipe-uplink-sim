package vega.hipe.must;

import herschel.share.util.ObjectUtil;
import herschel.ia.dataset.Column;
import herschel.ia.dataset.DateParameter;
import herschel.ia.dataset.StringParameter;
import herschel.ia.dataset.TableDataset;
import herschel.ia.dataset.MetaData;
//import herschel.ia.gui.explorer.table.TablePlotter;
import herschel.ia.gui.kernel.Extension;
import herschel.ia.gui.kernel.parts.ViewPart;
import herschel.ia.gui.kernel.parts.Viewable;
import herschel.ia.numeric.Long1d;
import herschel.share.component.WindowManager;
import herschel.share.fltdyn.time.FineTime;
import herschel.share.util.Configuration;
import herschel.ia.gui.kernel.prefs.UserPreferences;
import herschel.ia.gui.kernel.prefs.PreferenceListener;
import herschel.ia.gui.kernel.prefs.PreferenceChangedEvent;
import herschel.ia.dataset.Product;
import herschel.ia.gui.plot.*;
import herschel.ia.numeric.Ordered1dData;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import vega.uplink.Properties;
//import must.vega.hipe.preferences.MustPreferences;
//import must.vega.hipe.preferences.Preferences;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

/**
 * Shows the log of actions in this session.
 * 
 * <h4>View identier</h4>
 * <dd><code>ID = {@value #ID}</code></dd>
 * 
 * <h4>Public actions</h4>
 * <pre>
 *    START_ACTION = {@value #START_ACTION}
 *    STOP_ACTION  = {@value #STOP_ACTION}
 *    CLEAR_ACTION = {@value #CLEAR_ACTION}</pre>
 * 
 * <h4>Site events</h4>
 * <ul>
 * <li><i>triggers:</i> none<p/></li>
 * <li><i>listens:</i> none</li>
 * </ul>
 *
 */
public class MustView extends JPanel implements Viewable,PreferenceListener {

    private static final long serialVersionUID = 1L;

    // GUI elements
    private JComboBox paramBox;
    private JTextField date1;
    private JTextField date2;
    private JButton fetch;
    private JButton plot;

    // Getting properties and putting URL together
    
    private String host = Properties.getProperty("vega.must.server.ip");
    private int portInt = Integer.parseInt(Properties.getProperty("vega.must.server.port"));
    //private String host = UserPreferences.get("Must", "ip");
    //private int portInt = UserPreferences.getInt("Must", "port");
    private String port = (new Integer(portInt)).toString();
    private String repository = Properties.getProperty("vega.must.server.repository");
    //private String repository = UserPreferences.get("Must", "repository");
    private String user = Properties.getProperty("vega.must.server.user");
    //private String user = UserPreferences.get("Must", "user");
    private String password = Properties.getProperty("vega.must.server.password");
    //private String password = UserPreferences.get("Must", "password");
    private String url = "jdbc:mysql://" + host + ":" + port + "/" + repository + "?user=" + user
    + "&password=" + password;
    
    private static final Logger LOGGER = Logger.getLogger(MustView.class.getName());

    /**
     * The ID for this view
     */
    public static final String ID = "site.view.must";

    /**
     * The default extension for this view
     */
    public static final Extension EXTENSION = new Extension(ID, MustView.class.getName(), "Must",
    "vega/hipe/views/images/icon.gif");

    private ViewPart _part;

    public void dispose() {
    }

//    public Icon getIcon() {
//    	return getPart().getIcon();
//    }

    public String getId() {
	return getPart().getId();
    }

    public String getTitle() {
	return getPart().getTitle();
    }

    public void init(ViewPart part) {
	_part = part;
	((net.infonode.docking.View)_part).getViewProperties().setIcon(getIcon());
	//_part.getViewProperties().setIcon(getIcon());
	setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

	Dimension boxDim = new Dimension(350, 31);
	Dimension labelDim = new Dimension(300, 31);

	JLabel paramLabel = new JLabel("Parameter name:");
	paramLabel.setMaximumSize(labelDim);
	paramLabel.setMinimumSize(labelDim);
	paramBox = new JComboBox();
	paramBox.setEditable(true);
	paramBox.setMaximumSize(boxDim);
	paramBox.setMinimumSize(boxDim);

	JLabel date1Label = new JLabel("Initial date/time:");
	date1Label.setMaximumSize(labelDim);
	date1Label.setMinimumSize(labelDim);
	date1 = new JTextField();
	date1.setText("2009-05-14 3:22:39");
	date1.setMaximumSize(boxDim);
	date1.setMinimumSize(boxDim);
	JLabel date2Label = new JLabel("Final date/time:");
	date2Label.setMaximumSize(labelDim);
	date2Label.setMinimumSize(labelDim);
	date2 = new JTextField();
	date2.setText("2009-05-15 3:22:39");
	date2.setMaximumSize(boxDim);
	date2.setMinimumSize(boxDim);
	plot = new JButton("Plot selected");
	fetch = new JButton("Get parameters");

	JPanel paramPane = new JPanel();
	paramPane.setLayout(new BoxLayout(paramPane, BoxLayout.LINE_AXIS));
	paramPane.add(paramLabel);
	paramPane.add(paramBox);

	JPanel date1Pane = new JPanel();
	date1Pane.setLayout(new BoxLayout(date1Pane, BoxLayout.LINE_AXIS));
	date1Pane.add(date1Label);
	date1Pane.add(date1);

	JPanel date2Pane = new JPanel();
	date2Pane.setLayout(new BoxLayout(date2Pane, BoxLayout.LINE_AXIS));
	date2Pane.add(date2Label);
	date2Pane.add(date2);

	JPanel buttonPane = new JPanel();
	buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
	buttonPane.add(fetch);
	buttonPane.add(Box.createHorizontalStrut(20));
	buttonPane.add(plot);

	add(paramPane);
	add(Box.createVerticalStrut(10));
	add(date1Pane);
	add(Box.createVerticalStrut(10));
	add(date2Pane);
	add(Box.createVerticalStrut(10));
	add(buttonPane);
	makeActions();
	UserPreferences.addListener("must",(PreferenceListener) this);
    }

    public ViewPart getPart() {
	return _part;
    }
    
    public Icon getIcon(){
	ClassLoader loader = Thread.currentThread().getContextClassLoader();
	java.net.URL url=loader.getResource("vega/vega.gif");
	Icon icon = new ImageIcon(url);
	return icon;
    }

    private Vector<String> getParameters() {

	// JDBC connection
	String driverName = "com.mysql.jdbc.Driver";
	try {
	    ObjectUtil.getClass(driverName);
	} catch (Exception e1) {
	    errorMessage(e1.getMessage());
	    LOGGER.severe(e1.getMessage());
	    e1.printStackTrace();
	}
	Connection con = null;
	Statement stmt = null;
	try {
	    con = DriverManager.getConnection(url);
	    stmt = con.createStatement();
	} catch (SQLException e) {
	    errorMessage(e.getMessage());
	    e.printStackTrace();
	}
	// Query to get all the parameters
	String sql = "select PNAME from parameter";
	ResultSet rs = null;
	try {
	    rs = stmt.executeQuery(sql);
	} catch (SQLException e1) {
	    errorMessage(e1.getMessage());
	    e1.printStackTrace();
	}

	Vector<String> params = new Vector<String>();

	try {
	    while (rs.next()) {
		params.add(rs.getString(1));
	    }
	} catch (SQLException e) {
	    errorMessage(e.getMessage());
	    e.printStackTrace();
	}

	// Close database connection
	try {
	    rs.close();
	    stmt.close();
	    con.close();
	} catch (SQLException e1) {
	    errorMessage(e1.getMessage());
	    e1.printStackTrace();
	}

	return params;


    }

    private void makeActions() {
	plot.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {

		// Get data from GUI elements
		String startDate = date1.getText();
		String endDate = date2.getText();
		String parameterName = (String) paramBox.getSelectedItem();

		// Date formatting for the query
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date1 = null;
		Date date2 = null;

		try {
		    date2 = formatter.parse(startDate);
		} catch (ParseException e1) {
		    errorMessage("Format of initial date/time is invalid.");
		    return;
		}

		try {
		    date1 = formatter.parse(endDate);
		} catch (ParseException e1) {
		    errorMessage("Format of final date/time is invalid.");
		    return;
		}

		// JDBC connection
		String driverName = "com.mysql.jdbc.Driver";
		try {
		    Class.forName(driverName);
		} catch (ClassNotFoundException e1) {
		    e1.printStackTrace();
		}
		Connection con = null;
		try {
		    con = DriverManager.getConnection(url);
		} catch (SQLException e1) {
		    errorMessage(e1.getMessage());
		    e1.printStackTrace();
		}
		Statement stmt = null;
		try {
		    stmt = con.createStatement();
		} catch (SQLException e1) {
		    errorMessage(e1.getMessage());
		    e1.printStackTrace();
		}

		// Query to get PID from the parameter name and get the value type of the parameter
		String sql = "select PID,DBTYPE from parameter where (PNAME='" + parameterName + "')";
		ResultSet rs = null;
		try {
		    rs = stmt.executeQuery(sql);
		} catch (SQLException e1) {
		    errorMessage(e1.getMessage());
		    e1.printStackTrace();
		}

		int pid = 0;
		String tableName = null;
		try {
		    while (rs.next()) {
			pid = rs.getInt(1);
			tableName = rs.getString(2) + "paramvalues";
		    }
		} catch (SQLException e1) {
		    errorMessage(e1.getMessage());
		    e1.printStackTrace();
		}

		// Query to get the parameter times and values from MUST
		sql = "select datetime,value from " + tableName + " where (pid= " + pid
		+ " and datetime<'" + date1.getTime() + "' and datetime>'" + date2.getTime()
		+ "s')";
		try {
		    rs = stmt.executeQuery(sql);
		} catch (SQLException e1) {
		    errorMessage(e1.getMessage());
		    e1.printStackTrace();
		}
		// Store the results in a Long1d
		Long1d dates = new Long1d();
		Long1d values = new Long1d();
		try {
		    while (rs.next()) {
			dates.append(rs.getLong(1));
			values.append(rs.getLong(2));
		    }
		} catch (SQLException e1) {
		    errorMessage(e1.getMessage());
		    e1.printStackTrace();
		}

		MetaData meta;
		// Creates a table to store the data (later on will be attached to the product)
		TableDataset ah = new TableDataset();
		// Store the data into the table
		ah.addColumn("Time", new Column(dates, null, "Time"));
		ah.addColumn(parameterName, new Column(values, null, parameterName));
		// creates metadata for the table
		Product rawProduct=new Product();
		meta = rawProduct.getMeta();
		meta.set("creator", new StringParameter("Must"));
		meta.set("description", new StringParameter(parameterName));
		meta.set("startDate", new DateParameter(new FineTime(date2)));
		meta.set("endDate", new DateParameter(new FineTime(date1)));
		rawProduct.getSets().put(parameterName,ah);
		if (values.getSize()==0){
			errorMessage("No data available for these inputs");
		} else {
			PlotXY cp = new PlotXY();
			LayerXY p1=new LayerXY((Ordered1dData) dates,(Ordered1dData) values);
			p1.setName(parameterName);
			cp.setLayer(0,p1,0,0);  

			cp.getYaxis().getTitle().setText(parameterName);
			cp.getXaxis().getTitle().setText("Time");
			cp.getLegend().setVisible(true);
		}
		// Close database connection
		try {
		    rs.close();
		    stmt.close();
		    con.close();
		} catch (SQLException e1) {
		    errorMessage(e1.getMessage());
		    e1.printStackTrace();
		}

	    }

	});

	fetch.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {

			Vector<String> v=getParameters();
			for (String s : v)
	    			paramBox.addItem(s);

	    }
	});

    }

    private void errorMessage(String text) {
	JOptionPane.showMessageDialog(null, text, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void preferenceChanged(PreferenceChangedEvent event){
        host = Properties.getProperty("must.ip");
        portInt = Integer.parseInt(Properties.getProperty("must.port"));
        //private String host = UserPreferences.get("Must", "ip");
        //private int portInt = UserPreferences.getInt("Must", "port");
        port = (new Integer(portInt)).toString();
        repository = Properties.getProperty("must.repository");
        //private String repository = UserPreferences.get("Must", "repository");
        user = Properties.getProperty("must.user");
        //private String user = UserPreferences.get("Must", "user");
        password = Properties.getProperty("must.password");
        //private String password = UserPreferences.get("Must", "password");
        url = "jdbc:mysql://" + host + ":" + port + "/" + repository + "?user=" + user
        + "&password=" + password;

    	/*host = UserPreferences.get("Must", "ip");
    	portInt = UserPreferences.getInt("Must", "port");
    	port = (new Integer(portInt)).toString();
    	repository = UserPreferences.get("Must", "repository");
    	user = UserPreferences.get("Must", "user");
    	password = UserPreferences.get("Must", "password");
    	url = "jdbc:mysql://" + host + ":" + port + "/" + repository + "?user=" + user+ "&password=" + password;*/
    }
}