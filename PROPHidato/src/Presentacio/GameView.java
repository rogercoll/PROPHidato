package Presentacio;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.BasicStroke;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.plaf.basic.BasicArrowButton;

import com.sun.org.apache.bcel.internal.generic.NEW;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GameView {

	private JFrame frame;
	private String[][] board;
	private String[] params;
	private Vector<Vector<Polygon>> matrix;
	private Vector<Vector<Point>> centers;
	private int moves;
	private int prenext;
	ArrayList<Point> initialNums;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameView window = new GameView();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public JFrame getFrame() {
		return this.frame;
	}

	/**
	 * Create the application.
	 */
	public GameView(String[] params, String[][] matrix) {
		this.params = params;
		this.board = matrix;
		this.initialNums = new ArrayList<Point>();
		fillInitials();
		initialize();
	}
	
	public GameView() {
		initialize();
	}
	
	private boolean isNumeric(String s) {
		for (char c : s.toCharArray()) {
			if (!Character.isDigit(c)) return false;
		}
		return true;
	}
	
	private void fillInitials() {
		for(int i = 0; i < board.length; i++)
			for (int j = 0; j < board[0].length; j++) {
				if(isNumeric(board[i][j])) initialNums.add(new Point(i, j));
			}
	}
	
	private void drawMatrix(Vector<Vector<Polygon>> matrix, Graphics2D g) {
        g.setColor(Color.LIGHT_GRAY);
		g.setStroke(new BasicStroke(3));
		g.setFont(new Font("TimesRoman", Font.PLAIN, 18));
        for(int i = 0; i < matrix.size(); i++) {
        	for(int j = 0; j < matrix.get(0).size(); j++) {
        		Polygon p = matrix.get(i).get(j);
        		if(board[i][j].equals("#")) {
        			g.setColor(Color.WHITE);
        			g.drawPolygon(p);
        			g.setColor(Color.BLACK);
        		}
        		else if(board[i][j].equals("*")) {
					g.setColor(Color.ORANGE);
					g.fillPolygon(p);
					g.setColor(Color.BLACK);
					g.drawPolygon(p);
        		}
        		if(isNumeric(board[i][j])) {
        			g.setColor(Color.BLACK);
        			int xOffset = centers.get(i).get(j).x - (4*board[i][j].length());
        			int yOffset = centers.get(i).get(j).y + (2*board[i][j].length());
        			g.drawString(board[i][j], xOffset, yOffset);
        			g.drawPolygon(p);
        		}
        	}
        }
        for(int i = 0; i < matrix.size(); i++) {
        	for(int j = 0; j < matrix.get(0).size(); j++) {
        		if(!board[i][j].equals("#") ) {
        			Polygon p = matrix.get(i).get(j);
        			g.setColor(Color.BLACK);
        			g.drawPolygon(p);
        		}
        	}
		}
        g.setColor(Color.BLACK);
        g.drawPolygon(matrix.get(matrix.size()/2).get(matrix.get(0).size()/2) );
	}


	
	
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.DARK_GRAY);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize((int)screenSize.getWidth(),(int)screenSize.getHeight());
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		centers = new Vector<Vector<Point>>();
		if(params[0].equals("Q"))
			matrix = ControlPresentacio.getInstance().genSquareMatrix(Integer.parseInt(params[2]),Integer.parseInt(params[3]), centers);
		else if(params[0].equals("T"))
			matrix = ControlPresentacio.getInstance().genTriangleMatrix(Integer.parseInt(params[2]),Integer.parseInt(params[3]), centers);
		else if(params[0].equals("H"))
			matrix = ControlPresentacio.getInstance().genHexagonMatrix(Integer.parseInt(params[2]),Integer.parseInt(params[3]), centers);
		
        JPanel panel = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
            protected void paintComponent(Graphics g3d) {
                super.paintComponent(g3d);
                Graphics2D g = (Graphics2D) g3d;
                drawMatrix(matrix, g);
            }
        };
        
		JLabel movesLabel = new JLabel("");
		movesLabel.setForeground(Color.WHITE);
		movesLabel.setFont(new Font("Tahoma", Font.PLAIN, 29));
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                super.mouseClicked(me);
                for(Integer i = 0; i < matrix.size(); i++)
                	for(Integer j = 0; j < matrix.get(0).size(); j++) {
                		Polygon p = matrix.get(i).get(j);
                		if ((p.contains(me.getPoint()) && !initialNums.contains(new Point(i, j)))) {
                			Graphics2D g = (Graphics2D)panel.getGraphics();
                    		g.setStroke(new BasicStroke(3));
                    		g.setFont(new Font("TimesRoman", Font.PLAIN, 18));
                			int xOffset = centers.get(i).get(j).x - (3*board[i][j].length());
                			int yOffset = centers.get(i).get(j).y + (4*board[i][j].length());
                			int next = -3;
							try {
								next = ControlPresentacio.getInstance().nextMove(i, j);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if(board[i][j].equals(String.valueOf(prenext))) {
								g.setColor(Color.WHITE);
								g.fillPolygon(p);
								g.setColor(Color.BLACK);
								g.drawPolygon(p);
								prenext--;
							}
							else if(next > 0) {
								g.drawString(String.valueOf(next), xOffset, yOffset);
								prenext = next;
								System.out.println("prenext: " + prenext);
								board[i][j] = String.valueOf(next);
								movesLabel.setText(String.valueOf(++moves));
							}
							else if(next == -2) { //partida acabada
								g.drawString(String.valueOf(prenext+1), xOffset, yOffset);
								JOptionPane.showMessageDialog(frame, "Congratulations you solved the Hidato. Return to the Main Menu", "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
								MainMenu mm = new MainMenu();
								mm.getFrame().setVisible(true);
								frame.dispose();
							}
                		}
                	}
                }
        };
        panel.addMouseListener(ma);//add listener to panel 
        
		panel.setBackground(Color.WHITE);
		
		JLabel lblTime = new JLabel("Time ");
		lblTime.setForeground(Color.WHITE);
		lblTime.setBackground(Color.DARK_GRAY);
		lblTime.setFont(new Font("Tahoma", Font.PLAIN, 29));
		
		JLabel chrono = new JLabel("");
		chrono.setForeground(Color.WHITE);
		chrono.setFont(new Font("Tahoma", Font.PLAIN, 29));
		
		JLabel lblMoves = new JLabel("Moves");
		lblMoves.setForeground(Color.WHITE);
		lblMoves.setFont(new Font("Tahoma", Font.PLAIN, 29));
		
		BasicArrowButton basicArrowButton = new BasicArrowButton(7);
		
		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = JOptionPane.showInputDialog(frame, "Name your game please:", null);
				try {
					int n = ControlPresentacio.getInstance().nextMove(-1, -1);
					ControlPresentacio.getInstance().savePartida(name);
					JOptionPane.showMessageDialog(frame, "You have successfully saved your game", "",  JOptionPane.INFORMATION_MESSAGE);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(frame, "An error has  occurred, please try again", "",  JOptionPane.INFORMATION_MESSAGE);
					e1.printStackTrace();
				}
			}
		});
		saveButton.setFont(new Font("Tahoma", Font.PLAIN, 29));
		
		JButton btnNewButton = new JButton("Hint");
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		


		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(46)
							.addComponent(basicArrowButton, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(30)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(btnNewButton)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(panel, GroupLayout.PREFERRED_SIZE, 1600, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(saveButton, GroupLayout.PREFERRED_SIZE, 230, GroupLayout.PREFERRED_SIZE)
										.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
											.addGroup(groupLayout.createSequentialGroup()
												.addComponent(lblTime)
												.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(chrono, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE))
											.addGroup(groupLayout.createSequentialGroup()
												.addComponent(lblMoves, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.UNRELATED)
												.addComponent(movesLabel, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE))))))))
					.addContainerGap(36, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(14)
					.addComponent(basicArrowButton, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 900, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(31, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(197)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblTime, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
						.addComponent(chrono, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE))
					.addGap(60)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(movesLabel, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblMoves, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 490, Short.MAX_VALUE)
					.addComponent(saveButton, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
					.addGap(143))
		);
		frame.getContentPane().setLayout(groupLayout);
		
		
	}
	
	
	public void setBoard(String[][] s) {
		this.board = s;
	}
	
	public void setParams(String[] s) {
		 this.params = s;
	}
}
