import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.TextArea;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.awt.Font;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import oracle.spatial.geometry.JGeometry;
import oracle.sql.STRUCT;

public class Hw3 extends JFrame{
	private JPanel contentPane;
	private Connection conn;
	private JLabel label1 = new JLabel();
	private JLabel label2 = new JLabel();
	private JLabel label3 = new JLabel();
	private final JCheckBox bCheckBox = new JCheckBox("Building");
	private final JCheckBox bonfireCheckBox = new JCheckBox("Building On Fire");
	private final JCheckBox hCheckBox = new JCheckBox("Hydrants");
	
	private final JPanel panel1 = new JPanel();
	private final JPanel panel2 = new JPanel();
	private final JRadioButton wholeRadioButton = new JRadioButton(
			"Whole Region");
	private final JRadioButton rangeRadioButton = new JRadioButton(
			"Range query");
	private final JRadioButton nRadioButton = new JRadioButton(
			"Find Neighbor Buildings");
	private final JRadioButton hRadioButton = new JRadioButton(
			"Find Closets Fire Hydrants");
	
	private final JButton submitButton = new JButton("Submit Query");
	private final JLabel topLabel = new JLabel("Active Feature Type");
	private final JLabel topLabel1 = new JLabel("Query");
	private final JTextField mouselabel = new JTextField("");
	
	Font font = new Font("Serif", Font.BOLD, 20 );
	//topLabel.SetFont(font);
	
	
	private int queryCount = 1;
	private int flagforq4 = 1;
	private final TextArea textArea = new TextArea();
	
	private final String mapPath = "InputFiles/map.jpg";
	private int[] pointQuerySelectedCord = new int[2];
	private boolean hasPointSelected = false;
	private ArrayList<Integer> rectCords = new ArrayList<Integer>();
	private boolean rectClosed = false;
	private int[] surroundingQueryPoint = new int[2];
	private boolean surroundingPointSelected = false;
	
	public static void main(String[] args) throws SQLException{
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Hw3 h3 = new Hw3();
					h3.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private void setConnection() throws Exception{
		try {
			conn = DriverManager.getConnection(
					"jdbc:oracle:thin:@dagobah.engr.scu.edu:1521:db11g", "gvemulap",
					"geetha24");
		} catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	public Hw3() throws Exception{
		setTitle("Geetha Madhuri Vemulapalli(00001094040)");
		setConnection();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Cleaning and closing session");
				try {
					conn.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					throw new RuntimeException("Fail to close DB connection!", e1);
				}
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		setBounds(100, 100, 1203, 760);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		initialImage();
		initialCheckBoxs();
		initialRadioButtons();
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					sumbitHandler();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		submitButton.setBounds(864, 450, 278, 33);
		contentPane.add(submitButton);
		textArea.setBounds(5, 630, 1160, 80);
		contentPane.add(textArea);
		mouselabel.setBounds(5,600,250,20);
		contentPane.add(mouselabel);
		
		setVisible(true);
	}
		private void sumbitHandler() throws IOException, SQLException {
			
			if (wholeRadioButton.isSelected()) {
				File file = new File(mapPath);
				BufferedImage image = ImageIO.read(file);
				Graphics2D g2d = image.createGraphics();
				label1.setIcon(new ImageIcon(image));
				queryWholeRegion(g2d);
			} else if (rangeRadioButton.isSelected()) {
				File file = new File(mapPath);
				BufferedImage image = ImageIO.read(file);
				Graphics2D g2d = image.createGraphics();
				label1.setIcon(new ImageIcon(image));
				queryRange(g2d);
			
			}else if (nRadioButton.isSelected()) {
			
				ImageIcon yourImage= (ImageIcon) label1.getIcon();
				Image myimage = yourImage.getImage();
				
				BufferedImage image1 = (BufferedImage) myimage;
				Graphics2D g2d1 = image1.createGraphics();
				findneighborbuilding(g2d1);
			} else if (hRadioButton.isSelected()) {
				ImageIcon yourImage= (ImageIcon) label1.getIcon();
				Image myimage = yourImage.getImage();
				
				BufferedImage image1 = (BufferedImage) myimage;
				Graphics2D g2d1 = image1.createGraphics();
				findfirehydrants(g2d1);
			} else {
				textArea.append("No radioButton is selected!\n");
			}
		}
			private void initialImage() throws SQLException, IOException {
				File file = new File(mapPath);
				BufferedImage image = ImageIO.read(file);
				label1.setIcon(new ImageIcon(image));
				label1.setBounds(5, 5, 820, 580);
				contentPane.add(label1);
				label1.addMouseMotionListener(new MouseHandler());
				label1.addMouseListener(new MouseHandler());
			}
			
			private void initialCheckBoxs() {
				panel1.setBorder(new LineBorder(new Color(0, 0, 0)));
				panel1.setBounds(864, 51, 300, 100);
				contentPane.add(panel1);
				panel1.setLayout(null);
				topLabel.setBounds(16, 3, 227, 29);
				panel1.add(topLabel);
				bCheckBox.setBounds(6, 27, 234, 40);
				panel1.add(bCheckBox);
				bonfireCheckBox.setBounds(6, 60, 150, 33);
				panel1.add(bonfireCheckBox);
				hCheckBox.setBounds(170, 56, 122, 40);
				panel1.add(hCheckBox);
			}
			private void deSelectRadionButtions(JRadioButton except) throws IOException {
				if (except != wholeRadioButton) {
					wholeRadioButton.setSelected(false);
				}
				if (except != rangeRadioButton) {
					rangeRadioButton.setSelected(false);
				}
				if (except != nRadioButton) {
					flagforq4=1;
					nRadioButton.setSelected(false);
				}
				if (except != hRadioButton) {
					hRadioButton.setSelected(false);
				}
				
				rectCords.clear();
				surroundingPointSelected = false;
				File file = new File(mapPath);
				BufferedImage image = ImageIO.read(file);
				label1.setIcon(new ImageIcon(image));
			}
			private void initialRadioButtons() {
				panel2.setBorder(new LineBorder(new Color(0, 0, 0)));
				panel2.setBounds(864, 190, 300, 200);
				contentPane.add(panel2);
				panel2.setLayout(null);
				topLabel1.setBounds(25, 6, 227, 29);
				panel2.add(topLabel1);
				wholeRadioButton.setBounds(20, 36, 178, 23);
				panel2.add(wholeRadioButton);
				wholeRadioButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							deSelectRadionButtions(wholeRadioButton);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				});
				rangeRadioButton.setBounds(20, 74, 178, 23);
				rangeRadioButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						hasPointSelected = false;
						try {
                          
							deSelectRadionButtions(rangeRadioButton);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				});
				panel2.add(rangeRadioButton);
				nRadioButton.setBounds(20, 111, 200, 23);
				nRadioButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							
							/*if(nRadioButton.isSelected())
								
							   
							{
							ImageIcon yourImage= (ImageIcon) label1.getIcon();
							Image myimage = yourImage.getImage();
							
							BufferedImage image1 = (BufferedImage) myimage;
							Graphics2D g2d1 = image1.createGraphics();
							findneighborbuilding(g2d1);}
							else
							{*/
								deSelectRadionButtions(nRadioButton);
								//}
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				});
				panel2.add(nRadioButton);
				hRadioButton.setBounds(20, 151, 220, 23);
				hRadioButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							
							if(hRadioButton.isSelected())
								
							   
							/*{
								ImageIcon yourImage= (ImageIcon) label1.getIcon();
								Image myimage = yourImage.getImage();
								
								BufferedImage image1 = (BufferedImage) myimage;
								Graphics2D g2d1 = image1.createGraphics();
								findfirehydrants(g2d1);}
							else
							{*/deSelectRadionButtions(hRadioButton);//}
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				});
				
				panel2.add(hRadioButton);
				
			}
			
			private class MouseHandler extends MouseAdapter {
		
					public void mouseClicked(MouseEvent e) {
					      if(rangeRadioButton.isSelected()){
							
							if(e.getButton() == MouseEvent.BUTTON1){
								System.out.println("reached button1");
								rectClosed = false;
								File file = new File(mapPath);
								BufferedImage image = null;
								try {
									image = ImageIO.read(file);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								label1.setIcon(new ImageIcon(image));
								rectCords.add(e.getX());
								rectCords.add(e.getY());
								double[] temp = new double[rectCords.size()];
								for(int i = 0; i < temp.length; i++){
									temp[i] = rectCords.get(i);
								}
								drawRectangle(temp, image.createGraphics(), Color.red);
							}else if(e.getButton() == MouseEvent.BUTTON3){
								System.out.println("reached button3");
								rectClosed = true;
								File file = new File(mapPath);
								BufferedImage image = null;
								try {
									image = ImageIO.read(file);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								label1.setIcon(new ImageIcon(image));
								if(rectCords.size() > 0){
									rectCords.add(rectCords.get(0));
									rectCords.add(rectCords.get(1));
								}
								double[] temp = new double[rectCords.size()];
								for(int i = 0; i < temp.length; i++){
									temp[i] = rectCords.get(i);
								}
								drawRectangle(temp, image.createGraphics(), Color.red);
							}
					}else if(hRadioButton.isSelected()){
								
								surroundingPointSelected  = true;
								surroundingQueryPoint[0] = e.getX();
								surroundingQueryPoint[1] = e.getY();
								
								ImageIcon yourImage= (ImageIcon) label1.getIcon();
								Image myimage = yourImage.getImage();
								
								BufferedImage image = (BufferedImage) myimage;
								String sqlNearest = getSelectSQL(new String[]{"building b1"}, new String[]{"b1.shape"}, new String[]{generateSDO_NN("b1.shape", generatePointGeoType(surroundingQueryPoint), 1) + " = 'TRUE'"});
								textArea.append("Query " + queryCount++
										+ ": " + sqlNearest + "\n");
								
								try {
								
									for (double[] cords : getAllRectangleCords(sqlNearest)) {
										
										drawRectangle(cords, image.createGraphics(), Color.red);
									}
								} catch (SQLException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								
							}
							
						}
						
							
							
						

				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
					//drawCircleAndSquare(new double[]{e.getX(), e.getY(), 50}, image.createGraphics(), Color.red, 5);
				}

				public void mouseMoved(MouseEvent e) {
					int x = e.getX();
					int y = e.getY();
					mouselabel.setText("Current mouse location : " + "("+ String.valueOf(x)+"," +String.valueOf(y)+ ")");
					
				}

				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
				}
			}
		    
		
			
		
		
		private void queryWholeRegion(Graphics2D g2d) throws IOException,
		SQLException {
			if (!bonfireCheckBox.isSelected()
			&& !hCheckBox.isSelected()
			&& !bCheckBox.isSelected()) {
				textArea.append("No feature is selected!\n");
				return;
			}
			
			if (hCheckBox.isSelected()) {
				textArea.append("Query " + queryCount++
				+ " : Select shape from firehydrant \n");
				for (double[] cords : getAllPointCords("Select shape from firehydrant")) {
					drawPoint(cords, g2d, Color.green, 15);
				}
			}
			if (bCheckBox.isSelected()) {
				
				textArea.append("Query " + queryCount++
				+ " : Select shape from building\n");
				for (double[] cords : getAllRectangleCords("Select shape from building")) {
					System.out.println(cords[0] + "cords length");
					drawRectangle(cords, g2d, Color.yellow);
				}
			if (bonfireCheckBox.isSelected()) {
					textArea.append("Query " + queryCount++
					+ " : Select shape from firebuilding\n");
					for (double[] cords : getAllRectangleCords("Select b.shape from building b, firebuilding fb where fb.name = b.buildingname")) {
						drawRectangle(cords, g2d, Color.red);
					}
			 }
			}
		}
		
		
		
		
		private ArrayList<double[]> getAllPointCords(String query)
				throws SQLException {
			ArrayList<double[]> res = new ArrayList<double[]>();
			Statement stmt = null;
			ResultSet rs = null;
			try {
				stmt = conn.createStatement();
				rs = stmt.executeQuery(query);
				while (rs.next()) {
					//@SuppressWarnings("deprecation")
					STRUCT st = (oracle.sql.STRUCT) rs.getObject("shape");
					JGeometry j_geom = JGeometry.load(st);
					double[] pointCord = new double[2];
					pointCord[0] = j_geom.getJavaPoint().getX();
					pointCord[1] = j_geom.getJavaPoint().getY();
					res.add(pointCord);
				}
			} finally {
				stmt.close();
			}
			return res;
		}
		
	    
		private ArrayList<double[]> getAllRectangleCords(String query)
				throws SQLException {
			ArrayList<double[]> res = new ArrayList<double[]>();
			Statement stmt = null;
			ResultSet rs = null;
			try {
				stmt = conn.createStatement();
				rs = stmt.executeQuery(query);
				while (rs.next()) {
					//@SuppressWarnings("deprecation")
					STRUCT st = (oracle.sql.STRUCT) rs.getObject("shape");
					JGeometry j_geom = JGeometry.load(st);
					res.add(j_geom.getOrdinatesArray());
				}
			} finally {
				stmt.close();
			}
			System.out.println(res.get(0));
			System.out.println(res.size());
			
			return res;
		}
		private void drawPoint(double[] cordinates, Graphics2D g, Color color,
				int squareR) {
			g.setColor(color);
			g.fillRect((int) cordinates[0] - squareR / 2, (int) cordinates[1]
					- squareR / 2, squareR, squareR);
		}
		
		private void drawRectangle(double[] cordinates, Graphics2D g, Color color) {
			System.out.println(cordinates.length);
			g.setColor(color);
			int currentX = (int) cordinates[0];
			System.out.println(currentX);
			int currentY = (int) cordinates[1];
			for (int i = 1; i < cordinates.length / 2; i++) {
			//	g.setColor(color);
				int nextX = (int) cordinates[i * 2];
				int nextY = (int) cordinates[i * 2 + 1];
				g.drawLine(currentX, currentY, nextX, nextY);
				currentX = nextX;
				currentY = nextY;
			}
		}
		
	
		
		
		private void queryRange(Graphics2D g2d) throws IOException, SQLException {
			System.out.println(rectCords.size() + "rectCords size");
			if(rectCords.size() <= 0){ //|| !rectClosed){
				textArea.append("Please select coordinates and draw a polygon!\n");
				return;
			}
			//drawCircleAndSquare(new double[]{pointQuerySelectedCord[0], pointQuerySelectedCord[1], 50}, g2d, Color.red, 5);
			double[] temp = new double[rectCords.size()];
			for(int i = 0; i < temp.length; i++){
				temp[i] = rectCords.get(i);
			}
			drawRectangle(temp, g2d, Color.red);
			if (!bonfireCheckBox.isSelected()
					&& !hCheckBox.isSelected()
					&& !bCheckBox.isSelected()) {
				textArea.append("No feature is selected!\n");
				return;
			}//getSQLAnyIntersetWithCircule("announcementSystems", pointQuerySelectedCord)
			
			if (hCheckBox.isSelected()) {
				String sqlInteract = getSelectSQL(new String[]{"firehydrant fh"}, new String[]{"fh.shape"}, new String[]{generateSDO_RELATE("fh.shape", generateRectGeoType(temp), "anyinteract") + " = 'TRUE'"});
				textArea.append("Query " + queryCount++
						+ ": " + sqlInteract + "\n");
				for (double[] cords : getAllPointCords(sqlInteract)) {
					drawPoint(cords, g2d, Color.green, 15);
				}
			}
			if (bCheckBox.isSelected()) {
				String sqlInteract = getSelectSQL(new String[]{"building b"}, new String[]{"b.shape"}, new String[]{generateSDO_RELATE("b.shape", generateRectGeoType(temp), "anyinteract") + " = 'TRUE'"});
				textArea.append("Query " + queryCount++
						+ ": " + sqlInteract + "\n");
				for (double[] cords : getAllRectangleCords(sqlInteract)) {
					drawRectangle(cords, g2d, Color.yellow);
				}
			}
			
            if (bonfireCheckBox.isSelected()) {
				
				String[] params = new String[]{"b.shape"};
				String[] tables = new String[]{"firebuilding fb","building b"};
				String[] whereClause1 = new String[]{"fb.name=b.buildingname"};
				String[] whereClause = new String[]{generateSDO_RELATE("b.shape", generateRectGeoType(temp), "anyinteract") + " = 'TRUE'"};			
				String sqlInteract = getSelectSQL1(tables, params, whereClause1, whereClause);
				textArea.append("Query " + queryCount++
						+ ": " + sqlInteract + "\n");
				for (double[] cords : getAllRectangleCords(sqlInteract)) {
					drawRectangle(cords, g2d, Color.red);
				}
			}
		}
		private String getSelectSQL1(String[] tableNames, String[] requiredParas, String[] whereClause1,String[] whereClause){
			StringBuilder sql = new StringBuilder();
			sql.append("Select ");
			for(int i = 0; i < requiredParas.length; i++){
				sql.append(requiredParas[i]);
				if(i != requiredParas.length - 1){
					sql.append(",");
				}
			}
			sql.append(" from ");
			for(int i = 0; i < tableNames.length; i++){
				sql.append(tableNames[i]);
				if(i != tableNames.length - 1){
					sql.append(",");
				}
			}
			sql.append(" where ");
			for(int i = 0; i < whereClause1.length; i++){
				sql.append(whereClause1[i]);
				if(i != whereClause1.length - 1){
					sql.append(",");
				}
			}
			
			sql.append(" and ");
			for(int i = 0; i < whereClause.length; i++){
				sql.append(whereClause[i]);
				if(i != whereClause.length - 1){
					sql.append(",");
				}
			}
			return sql.toString();
		}
		
		private String generateRectGeoType(double[] cords){
			StringBuilder cordsStr = new StringBuilder();
			for(int i = 0; i < cords.length; i++){
				cordsStr.append(cords[i]);
				if(i != cords.length - 1){
					cordsStr.append(",");
				}
			}
			return "SDO_GEOMETRY(2003,NULL,NULL,SDO_ELEM_INFO_ARRAY(1,1003,1),SDO_ORDINATE_ARRAY(" + cordsStr.toString() + "))";
		}
		
		private String generateSDO_RELATE(String geoPara1, String geoPara2, String mask){
			StringBuilder res = new StringBuilder();
			res.append("SDO_RELATE(");
			res.append(geoPara1);
			res.append(",");
			res.append(geoPara2);
			res.append(",");
			res.append("'mask=");
			res.append(mask);
			res.append("')");
			return res.toString();
		}
		
		private String getSelectSQL(String[] tableNames, String[] requiredParas, String[] whereClause){
			StringBuilder sql = new StringBuilder();
			sql.append("Select ");
			for(int i = 0; i < requiredParas.length; i++){
				sql.append(requiredParas[i]);
				if(i != requiredParas.length - 1){
					sql.append(",");
				}
			}
			sql.append(" from ");
			for(int i = 0; i < tableNames.length; i++){
				sql.append(tableNames[i]);
				if(i != tableNames.length - 1){
					sql.append(",");
				}
			}
			sql.append(" where ");
			for(int i = 0; i < whereClause.length; i++){
				sql.append(whereClause[i]);
				if(i != whereClause.length - 1){
					sql.append(",");
				}
			}
			return sql.toString();
		}
		
		
		private void findfirehydrants(Graphics2D g2d) throws IOException, SQLException{
			
			String sqlNearest = getSelectSQL(new String[]{"building b1"}, new String[]{"b1.shape"}, new String[]{generateSDO_NN("b1.shape", generatePointGeoType(surroundingQueryPoint), 1) + " = 'TRUE'"});
			textArea.append("Query " + queryCount++
					+ ": " + sqlNearest + "\n");
			double[] buildcords = null;
			try{
				ArrayList<double[]> arrycords = getAllRectangleCords(sqlNearest);
				buildcords= new double[arrycords.get(0).length];
				for(int i=0;i<arrycords.get(0).length;i++){
					buildcords[i]=arrycords.get(0)[i];
					
				}
				
			}catch(Exception e){
				System.out.println(e);
			}
			
			String[] params = new String[]{"fh.shape"};
			String[] tables = new String[]{"firehydrant fh"};
			String[] whereClause = new String[]{generateSDO_NN("fh.shape", generatePointGeoType(surroundingQueryPoint), 1) + " = 'TRUE'"};//generateSDO_WITHIN_DISTANCE("fh.shape", generateRectGeoType(buildcords), "80") + " = 'TRUE'"};			
			String sqlInteract = getSelectSQL(tables, params, whereClause);
			textArea.append("Query " + queryCount++
					+ ": " + sqlInteract + "\n");
			
			for (double[] cords1 : getAllPointCords(sqlInteract)) {
				
				drawPoint(cords1, g2d, Color.green, 15);
			}
			
		}
		
		
		
		
		private void findneighborbuilding(Graphics2D g2d) throws IOException, SQLException{
			/*for (double[] cords : getAllRectangleCords("Select shape from building ,firebuilding where building.buildingname=firebuilding.name")) {
				//System.out.println(cords[0] + "cords length");
				drawRectangle(cords, g2d, Color.red);
			}*/
		
		double[] buildcords = null;
		double[] buildcords1 = null;
		double[] buildcords2 = null;
			try{
				
				
				ArrayList<double[]> arrycords = getAllRectangleCords("Select shape from building ,firebuilding where building.buildingname=firebuilding.name");
				
				buildcords= new double[arrycords.get(0).length];

				for(int i=0;i<arrycords.get(0).length;i++){
					buildcords[i]=arrycords.get(0)[i];

				}
					
				buildcords1= new double[arrycords.get(1).length];

				for(int i=0;i<arrycords.get(1).length;i++){
					buildcords1[i]=arrycords.get(1)[i];
				}
				
				buildcords2= new double[arrycords.get(2).length];

				for(int i=0;i<arrycords.get(2).length;i++){
					buildcords2[i]=arrycords.get(2)[i];
				}
						
			
				
			}catch(Exception e){
				System.out.println(e);
			}
			
			String[] params = new String[]{"b.shape"};
			String[] tables = new String[]{"building b"};
			String[] whereClause = new String[]{generateSDO_WITHIN_DISTANCE("b.shape", generateRectGeoType(buildcords), "100") + " = 'TRUE'"};			
			String sqlInteract = getSelectSQL(tables, params, whereClause);
			textArea.append("Query " + queryCount++
					+ ": " + sqlInteract + "\n");
			
			for (double[] cords1 : getAllRectangleCords(sqlInteract)) {
				drawRectangle(cords1, g2d, Color.yellow);
				
			}
			
			String[] params1 = new String[]{"b.shape"};
			String[] tables1 = new String[]{"building b"};
			String[] whereClause1 = new String[]{generateSDO_WITHIN_DISTANCE("b.shape", generateRectGeoType(buildcords1), "100") + " = 'TRUE'"};			
			String sqlInteract1 = getSelectSQL(tables1, params1, whereClause1);
			textArea.append("Query " + queryCount++
					+ ": " + sqlInteract + "\n");
			
			for (double[] cords1 : getAllRectangleCords(sqlInteract1)) {
				drawRectangle(cords1, g2d, Color.yellow);
				
			}
			
			String[] params2 = new String[]{"b.shape"};
			String[] tables2 = new String[]{"building b"};
			String[] whereClause2 = new String[]{generateSDO_WITHIN_DISTANCE("b.shape", generateRectGeoType(buildcords2), "100") + " = 'TRUE'"};			
			String sqlInteract2 = getSelectSQL(tables2, params2, whereClause2);
			textArea.append("Query " + queryCount++
					+ ": " + sqlInteract + "\n");
			
			for (double[] cords1 : getAllRectangleCords(sqlInteract2)) {
				drawRectangle(cords1, g2d, Color.yellow);
				
			}
			
			for (double[] cords : getAllRectangleCords("Select shape from building ,firebuilding where building.buildingname=firebuilding.name")) {
				//System.out.println(cords[0] + "cords length");
				drawRectangle(cords, g2d, Color.red);
			}
			
			
		}
		
		
		
		
		
		
		private String generateSDO_WITHIN_DISTANCE(String geoPara1, String geoPara2, String distance){
			StringBuilder res = new StringBuilder();
			res.append("SDO_WITHIN_DISTANCE(");
			res.append(geoPara1);
			res.append(",");
			res.append(geoPara2);
			res.append(",");
			res.append("'distance=");
			res.append(distance);
			res.append("')");
			return res.toString();
		}
		
		private String generateSDO_NN(String geoPara1, String geoPara2, int num){
			StringBuilder res = new StringBuilder();
			res.append("SDO_NN(");
			res.append(geoPara1);
			res.append(",");
			res.append(geoPara2);
			res.append(",");
			res.append("'sdo_num_res=" + num + "')");
			return res.toString();
		}
		
		
		private String generatePointGeoType(int[] center){
			StringBuilder cords = new StringBuilder();
			cords.append(center[0]);
			cords.append(",");
			cords.append(center[1]);
			return "SDO_GEOMETRY(2001,NULL,SDO_POINT_TYPE(" + cords.toString() + ", NULL), NULL, NULL)";
		}
		
		
		
		
	}
	

	
	
	
