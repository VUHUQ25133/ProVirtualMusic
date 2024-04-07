package VirtualPiano;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.SwingConstants;

import org.jfugue.Pattern;
import org.jfugue.Player;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Piano implements ActionListener{
	
	//Khung Giao diện 
	private JFrame frame;
	
	//Khung Nhập Tempo.
	private JTextArea tempo;
	
	// Nút hướng đãn.
	private JButton help;
	// Ô Text số lần lặp lại 			
	private JTextArea repeatNumber;
	
	// Cho phép chọn các nhạc cụ khác nhau. 	
	private JComboBox<String> instrument;
	private String[] instruments = {"Piano", "Guitar", "Vibraphone","Violin"};
	
	// Các note nhạc 
	private String[] notes = {"C","D","E","F","G","A","B"};
	private String[] sharps = {"C#","D#","F#","G#","A#"};
	
	// SỐ Notes 
	public static final int NUM_KEYS = 7;
	public static final int NUM_SHARP = 5;
	// Số lượng quãng 8
	public static final int NUM_OCTAVES = 4;
	private String[] octave = {"3","4","5","6"};
	
	// Nhập các note để tự động phát nhạc
	private JTextArea entryBox;
	
	// Tạo player mới
	private Player player = new Player(); 
	
	// thể hiện nhạc cụ đã lựa chọn
	private String instrumentType = "I[Piano]";
	
	// Tạo viền cho các ô TextArea
	private Border border = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
	
	
	public Piano(){
		
		
		frame = new JFrame("ProVirtualMusic");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// ---- mainPanel ----
		Container mainPanel = frame.getContentPane();
		// BoxLayout sắp xếp các thành phần con theo trục tung
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setForeground(Color.WHITE);
		mainPanel.setBackground(Color.GRAY);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
				
			// ---- Instrument & Tempo Panel ----
				JPanel iTpanel = new JPanel();
				// BoxLayout sắp xếp các thành phần con theo trục hoành
			    iTpanel.setLayout(new BoxLayout(iTpanel,BoxLayout.X_AXIS));
				iTpanel.setForeground(Color.WHITE);
				iTpanel.setBackground(Color.GRAY);
			    iTpanel.add(Box.createRigidArea(new Dimension(100,0)));	
			    
			// ---- Title label ----
			    JLabel titleLabel = new JLabel(" ProVirtualMusic ");
			    titleLabel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
				titleLabel.setFont(new Font("Cooper Black", Font.BOLD | Font.ITALIC, 17));
				titleLabel.setForeground(Color.WHITE);
				titleLabel.setBackground(Color.GRAY);
				iTpanel.add(titleLabel);
				iTpanel.add(Box.createRigidArea(new Dimension(200,0)));
			 
			// ---- Instrument label ----
				JLabel instrumentLabel = new JLabel("Instrument:");
				instrumentLabel.setHorizontalAlignment(SwingConstants.CENTER);
				instrumentLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 17));
				instrumentLabel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
				instrumentLabel.setForeground(Color.WHITE);
				instrumentLabel.setBackground(Color.GRAY);
			    iTpanel.add(instrumentLabel);
			    iTpanel.add(Box.createRigidArea(new Dimension(20,0))); 
			    
			// ---- Instrument combo box ----
			    instrument = new JComboBox(instruments);
			    instrument.setAlignmentY(Component.BOTTOM_ALIGNMENT);
			    instrument.setAlignmentX(Component.LEFT_ALIGNMENT);
			    instrument.setName("instrument");
			    instrument.addActionListener(this);
				instrument.setForeground(Color.WHITE);
				instrument.setBackground(new Color(67, 184, 197));
			    iTpanel.add(instrument);
			    iTpanel.add(Box.createRigidArea(new Dimension(20,0)));
			    
			// ---- Tempo label ----
			    JLabel tempoLabel = new JLabel("Tempo:");
			    tempoLabel.setHorizontalAlignment(SwingConstants.CENTER);
			    tempoLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 17));
			    tempoLabel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
				tempoLabel.setForeground(Color.WHITE);
				tempoLabel.setBackground(Color.GRAY);
			    iTpanel.add(tempoLabel);
			    iTpanel.add(Box.createRigidArea(new Dimension(10,0)));
	
			// ---- Tempo TextArea ----
			    tempo = new JTextArea();
			    tempo.setAlignmentY(Component.BOTTOM_ALIGNMENT);
			    tempo.setAlignmentX(Component.LEFT_ALIGNMENT);
			    tempo.setName("tempo");
			    tempo.setText("270");
				tempo.setFont(new Font("Ariel", Font.BOLD, 14));
			    tempo.setBorder(border);
				tempo.setForeground(Color.BLACK);
				tempo.setBackground(new Color(67, 184, 197));
				
			    iTpanel.add(tempo);
			    iTpanel.add(Box.createRigidArea(new Dimension(100,0)));
			     
			 // ---- nút Save ----
	     		JButton saveButton = new JButton("Save");
	     		saveButton.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 12));
	     		saveButton.setName("Save");
	     		saveButton.setAlignmentY(Component.BOTTOM_ALIGNMENT);
	     		saveButton.setForeground(Color.CYAN);
	     		saveButton.setBackground(new Color(0, 0, 128));
	     		saveButton.addActionListener(this);	
	     		iTpanel.add(saveButton);
        		iTpanel.add(Box.createRigidArea(new Dimension(30,0)));
        		
        	// ---- nút Open ----
	     		JButton openButton = new JButton("Open");
	     		openButton.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 12));
	     		openButton.setName("Open");
	     		openButton.setAlignmentY(Component.BOTTOM_ALIGNMENT);
	     		openButton.setForeground(Color.CYAN);
	     		openButton.setBackground(new Color(0, 0, 128));
	     		openButton.addActionListener(this);	
	     		iTpanel.add(openButton);
        		iTpanel.add(Box.createRigidArea(new Dimension(30,0)));    
			
			// ---- nút Help ----
			    help = new JButton("Help");
			    help.setAlignmentY(Component.BOTTOM_ALIGNMENT);
			    help.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 12));
			    help.setForeground(Color.CYAN);
			    help.setBackground(new Color(0, 0, 128));
			    help.addActionListener(this);
			    help.setName("help");
			    iTpanel.add(help);
			    iTpanel.add(Box.createRigidArea(new Dimension(20,0)));

			// Thêm iTpanel vào mainPanel
		mainPanel.add(iTpanel);
		mainPanel.add(Box.createRigidArea(new Dimension(0,90)));
		

	    
	    // ---- Panel playback (Label + TextArea) ----
	     	JPanel notesPanel = new JPanel();
	     	notesPanel.setLayout(new BoxLayout(notesPanel,BoxLayout.X_AXIS));
	     	notesPanel.setForeground(Color.WHITE);
	     	notesPanel.setBackground(Color.GRAY);
	     	notesPanel.add(Box.createRigidArea(new Dimension(60, 0)));
	     
	     	// 	---- Notes Label ----
	     		JLabel notesLabel = new JLabel("Playback Notes:");
	     		notesLabel.setForeground(Color.WHITE);
	     		notesLabel.setBackground(Color.GRAY);
	     		
	     		notesPanel.add(notesLabel);
	     		notesPanel.add(Box.createRigidArea(new Dimension(10, 0)));
	     
	     	// ---- TextArea ----
	     		entryBox = new JTextArea();
	     		entryBox.setBorder(border);
	     		entryBox.setFont(new Font("Ariel", Font.BOLD, 14));
	     		entryBox.setForeground(Color.BLACK);
	     		entryBox.setBackground(new Color(67, 184, 197));
	     		
	     		notesPanel.add(entryBox);
	     		notesPanel.add(Box.createRigidArea(new Dimension(60, 0)));
	     
	     	// ---- thêm vào mainPanel ----
	    mainPanel.add(notesPanel);
	    mainPanel.add(Box.createRigidArea(new Dimension(0, 80)));
	    
	    
	    // ----Panel Repeat ----
        	JPanel repeatPanel = new JPanel();
        	repeatPanel.setLayout(new BoxLayout(repeatPanel,BoxLayout.X_AXIS));
        	repeatPanel.setForeground(Color.WHITE);
        	repeatPanel.setBackground(Color.GRAY);
        	repeatPanel.add(Box.createRigidArea(new Dimension(500,0)));
        		
        		JLabel repeatLabel = new JLabel("Repeat Num: ");
        		repeatLabel.setForeground(Color.WHITE);
        		repeatLabel.setBackground(Color.GRAY);
        		repeatPanel.add(repeatLabel);
        		repeatPanel.add(Box.createRigidArea(new Dimension(10,0)));
        
        		repeatNumber = new JTextArea();
        		repeatNumber.setAlignmentX(Component.RIGHT_ALIGNMENT);
        		repeatNumber.setText("1");
        		repeatNumber.setFont(new Font("Ariel", Font.BOLD, 14));
        		repeatNumber.setBorder(border);
        		repeatNumber.setForeground(Color.BLACK);
        		repeatNumber.setBackground(Color.CYAN);
        		repeatPanel.add(repeatNumber);
        		repeatPanel.add(Box.createRigidArea(new Dimension(20,0)));
        				
        	// ---- nút Play ----
	     		JButton playButton = new JButton("Play Notes");
	     		playButton.setFont(new Font("Copperplate Gothic Bold", Font.ITALIC, 10));
	     		playButton.setName("play");
	     		playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
	     		playButton.setForeground(Color.CYAN);
	     		playButton.setBackground(new Color(0, 0, 128));
	     		playButton.addActionListener(this);	
	     		repeatPanel.add(playButton);
        		repeatPanel.add(Box.createRigidArea(new Dimension(400,0)));
        		
        	
        
        // Add to main panel
        mainPanel.add(repeatPanel);

	    mainPanel.add(Box.createRigidArea(new Dimension(50, 20)));

		
		// ---- Panel chứa các phím ----
	    JLayeredPane pianoKeyPanel = makeKeys();
	    mainPanel.add(pianoKeyPanel);
	    mainPanel.add(Box.createRigidArea(new Dimension(0,20)));
	    
		// Show window
	    frame.setVisible(true);
	    frame.setResizable(false);
	    frame.setSize(1150,520);
	}
	
	
	// Tạo Panel chứa các phím đàn
	public JLayeredPane makeKeys() {
		int x = 55;
		int y = 0;			
		// layeredPane
			JLayeredPane keyBoard = new JLayeredPane();
			keyBoard.setPreferredSize(new Dimension(900,162));
			keyBoard.add(Box.createRigidArea(new Dimension(x, 0)));
			 
			// Note trắng
		    for(int i=0; i< NUM_OCTAVES; i++){
		     	for(int j=0; j<NUM_KEYS; j++){
		     		String name = notes[j]+octave[i];
		     		JButton jb = new JButton(notes[j]);
		     		jb.setName(name);
		     		jb.setActionCommand(name);
		     		jb.addActionListener(this);
		     		jb.setForeground(Color.BLACK);
		     		jb.setOpaque(true);
		     		jb.setFont(new Font("Consolas", Font.BOLD, 15));
		            jb.setVerticalAlignment(SwingConstants.BOTTOM);
		            jb.setHorizontalAlignment(SwingConstants.CENTER);
		     		jb.setBounds(x,y,35,162);
		     		jb.setBorder(null);
		     		keyBoard.add(jb,new Integer(1));
		     		keyBoard.add(Box.createRigidArea(new Dimension(2, 0)));
		     		x += 37;
		     	}
		    }
		    // Note đen
		    for(int i = 0; i < NUM_OCTAVES; i++) {
		        for(int j = 0; j < NUM_SHARP; j++) {
		        	String noteName = sharps[j] + octave[i];
		            JButton jb = new JButton(sharps[j]);
		            jb.setName(noteName);
		            jb.setActionCommand(noteName);
		            jb.addActionListener(this);
		            jb.setForeground(Color.WHITE);
		            jb.setBackground(Color.BLACK);
		            jb.setOpaque(true);
		            jb.setFont(new Font("Consolas", Font.BOLD, 15));
		            jb.setVerticalAlignment(SwingConstants.BOTTOM);
		            jb.setHorizontalAlignment(SwingConstants.CENTER);
		            int X = 77 + (260 * i) + (j * 38);
		            if (j > 1) X += 35;
		            jb.setBounds(X, y, 25, 95);
		            jb.setBorder(null);
		            keyBoard.add(jb, new Integer(2));
		        }
		    }
		    
		return keyBoard;
	}
	
	// Phát nhạc
	public void playSong(){
		
		// Nhận note từ entryBox
		String notesString = "V0 "+instrumentType+" "+entryBox.getText();
     
		// Nhận số lần repeat (num)
		int num = 1;
		String repeatNum = repeatNumber.getText();
		num = Integer.parseInt(repeatNum);

		// Nhận tempo
		String playTempo = "T["+tempo.getText()+"] ";
     
		
		// chạy nhạc
		Pattern song = new Pattern();
		
		song.add(notesString);
		song.repeat(num);
		
		player.play(playTempo+song);
    
	}
	
	// Mở cửa sổ hướng dẫn
	private void helpWindow(){
	
		JFrame frame2 = new JFrame("Help");

		Container helpPanel = frame2.getContentPane();
			helpPanel.setLayout(new BoxLayout(helpPanel, BoxLayout.Y_AXIS));
			helpPanel.setForeground(Color.WHITE);
			helpPanel.setBackground(Color.BLACK);
		
		JEditorPane instructions= new JEditorPane();
			instructions.setText("Hướng dẫn:\n " +
				"Nháy chuột vào các phím hoặc sử dụng format: \n " +
				"	1. Cơ bản: A4 B5 B#5 A6 \n" +
				"	2. Nốt nghỉ R: A R C \n" +
				"	3. Sử dụng + giữa 2 note trở lên tạo hợp âm\n" +	
				"Thêm các kí tự sau các note để thay đổi độ dài:\n" +
				"	+) w  whole\n" +
				"	+) h  half\n" +
				"	+) q  quarter\n" +
				"	+) i  eighth\n" +
				"	+) s  sixteenth\n"+ 
				"Quy ước 1 note = 1q\n\n");
			instructions.setForeground(Color.WHITE);
			instructions.setBackground(Color.BLACK);
			instructions.setFont(new Font("Ariel", Font.PLAIN, 16));

		helpPanel.add(instructions);
		
		frame2.setSize(500,300);
		frame2.setVisible(true);
	}
	
	// Hàm saveTrack để lưu nội dung từ entryBox vào file .txt
	private void saveTrack() {
	    // Sử dụng JFileChooser để cho phép người dùng chọn vị trí và tên file
	    JFileChooser fileChooser = new JFileChooser();
	    // Thiết lập thư mục mặc định khi mở cửa sổ là "Saved Tracks"
	    fileChooser.setCurrentDirectory(new File("Saved Tracks"));
	    int result = fileChooser.showSaveDialog(frame);
	    if (result == JFileChooser.APPROVE_OPTION) {
	        File fileToSave = fileChooser.getSelectedFile();
	        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
	            writer.write(entryBox.getText());
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}

	// Hàm openTrack để mở file .txt và gán nội dung vào entryBox
	private void openTrack() {
	    // Sử dụng JFileChooser để cho phép người dùng chọn file cần mở
	    JFileChooser fileChooser = new JFileChooser();
	    // Thiết lập thư mục mặc định khi mở cửa sổ là "Saved Tracks"
	    fileChooser.setCurrentDirectory(new File("Saved Tracks"));
	    int result = fileChooser.showOpenDialog(frame);
	    if (result == JFileChooser.APPROVE_OPTION) {
	        File selectedFile = fileChooser.getSelectedFile();
	        try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
	            String line;
	            StringBuilder stringBuilder = new StringBuilder();
	            while ((line = reader.readLine()) != null) {
	                stringBuilder.append(line);
	                stringBuilder.append("\n");
	            }
	            entryBox.setText(stringBuilder.toString());
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	public static void main(String[] args) {
		new Piano();
	}
	
	public void actionPerformed(ActionEvent e) {

		String command = "";
		JButton jb = null;
		String name = "";
		
		Object obj = e.getSource();
		
		//  định dạng nhạc cụ để sử dụng và playback
		if (obj instanceof JComboBox) {
			Object instrumentObj = instrument.getSelectedItem();
			instrumentType = "I["+(String)instrumentObj+"]";
		}
		// 
		else {
			jb = (JButton)obj;
			name = jb.getName();
		}

		
		// nếu bấm Play
		if (name.equals("play")) {
		    Runnable playNotes = new Runnable() {
		        public void run() {
		        	playSong();
		        }
		    };
		    // new thread to start
		    (new Thread(playNotes)).start();
		}
		
		else if (name.equals("help")) {
			helpWindow();
		}
		else if (name.equals("Save")) {
		    saveTrack();
		}

		else if (name.equals("Open")) {
		    openTrack();
		}
		
		else if(obj instanceof JComboBox) {
			// Do nothing
		}

		else {
			command = jb.getActionCommand();
			// Ghi note vào trong EntryBox
			entryBox.append(command+" ");
			// Play note
			player.play(instrumentType+" "+command);
		}
	}	
}


