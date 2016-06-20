import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.DefaultPersistenceDelegate;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;



import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class WhiteboardGUI extends JFrame {
	Canvas canvas;
	Color color;
	private JLabel status;
	private ClientHandler clientHandler;
    private ServerAccepter serverAccepter;
    private JTextArea textArea;
    private java.util.List<ObjectOutputStream> outputs =
            new ArrayList<ObjectOutputStream>();
    WhiteboardGUI clientWB;
	public WhiteboardGUI() {
		showGUI();
	}
	public void showGUI() {
		JFrame myFrame= new JFrame();
		canvas = new Canvas();
		
		JPanel controls=new JPanel(); // left side of the gui
		JPanel controls1=new JPanel(); //first row of controls
		JPanel controls2=new JPanel(); //second row of controls
		JPanel controls3=new JPanel(); //third row of controls
		JPanel controls4=new JPanel(); // fourth row of controls
		JPanel controls5=new JPanel();
		JPanel locations=new JPanel(); // bottom left of the gui
		controls1.setLayout(new BoxLayout(controls1, BoxLayout.LINE_AXIS)); //sets layout of all rows of controls 
		controls2.setLayout(new BoxLayout(controls2, BoxLayout.LINE_AXIS)); // to horizontal
		controls3.setLayout(new BoxLayout(controls3, BoxLayout.LINE_AXIS));
		controls4.setLayout(new BoxLayout(controls4, BoxLayout.LINE_AXIS));
		
		JLabel add= new JLabel("Add");  //creates add label
		JButton addRec=new JButton("Rect"); //creates add rect button
		addRec.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DRectModel rectModel=new DRectModel();
				
				int coordinate=(int)(Math.random()*350 )+ 1;
				int widthAndHeight=30;
				rectModel.setX(coordinate);
				rectModel.setY(coordinate);
				rectModel.setHeight(widthAndHeight);
				rectModel.setWidth(widthAndHeight);
				rectModel.createKnobs();
				canvas.addShape(rectModel);
			}
		});
		JButton addOval=new JButton("Oval") ;// creates add oval button
		addOval.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DOvalModel ovalmodel=new DOvalModel();
				int Xcoordinate=(int)(Math.random()*350 )+ 1;
				int Ycoordinate=(int)(Math.random()*350) + 1;
				int widthAndHeight=30;

				ovalmodel.setX(Xcoordinate);
				ovalmodel.setY(Ycoordinate);
				ovalmodel.setHeight(widthAndHeight);
				ovalmodel.setWidth(widthAndHeight);
				
				ovalmodel.createKnobs();
				canvas.addShape(ovalmodel);
		
			}
		});
		JButton addLine= new JButton("Line"); //creates add line button;
		addLine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DLineModel linemodel=new DLineModel();
				
				int Xcoordinate=(int)(Math.random()*350 )+ 1;
				int Ycoordinate=(int)(Math.random()*350) + 1;
				int length=30;

				linemodel.setX(Xcoordinate);
				linemodel.setY(Ycoordinate);
				linemodel.setWidth(length);
				linemodel.setHeight(10);
				linemodel.createKnobs();
				canvas.addShape(linemodel);
			}
		});
		JButton addText=new JButton("Text"); //creates add text button;
		
		addText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DShapeModel textmodel=new DShapeModel();
				int Xcoordinate=(int)(Math.random()*350 )+ 1;
				int Ycoordinate=(int)(Math.random()*350) + 1;
				textmodel.text=addText.getText();
				textmodel.setX(Xcoordinate);
				textmodel.setY(Ycoordinate);
				textmodel.setWidth(10);
				textmodel.setHeight(10);
				textmodel.createKnobs();
				canvas.addShape(textmodel);
			}
		});
		
		
		controls1.add(add); //adds the controls for the first row
		controls1.add(addRec);
		controls1.add(addOval);
		controls1.add(addLine);
		controls1.add(addText);
		JButton setcolor= new JButton("Set Color"); //creates set color button
		setcolor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(canvas.lastShape!=null) {
				color=JColorChooser.showDialog(null, "Pick your Color", color);
				canvas.lastShape.setColor(color);
				}
				canvas.update();
				
			}
			
		});
		controls2.add(setcolor);
		JTextField textBox=new JTextField(); //creates the textfield
		GraphicsEnvironment graphicsEnvironment=GraphicsEnvironment.getLocalGraphicsEnvironment();
		Font[] fonts=graphicsEnvironment.getAllFonts();	
		String[] fontNames= new String[fonts.length];
		for(int i = 0 ; i < fonts.length;i++) {    //takes the font names from font[]
			String fName=fonts[i].getFontName();
			fontNames[i]=fName;
		}
		JComboBox myFonts=new JComboBox(fontNames); //creates combo box of fonts
	
		controls3.add(textBox); //adds textfield to row3
		controls3.add(myFonts); //adds font chooser to row3 
		JButton moveToFront= new JButton("Move to Front");
		moveToFront.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(canvas.lastShape!=null) {
					DShapeModel tmp = canvas.shapesmodel.get(0);
					for(int i = 0 ; i < canvas.shapesmodel.size();i++) {
						if(canvas.lastShape.compareTo(canvas.shapesmodel.get(i))==0) {
							canvas.shapesmodel.set(0,canvas.lastShape);
							canvas.shapesmodel.set(i, tmp);
						}
					}
				}
			}
		});
		JButton moveToBack= new JButton("Move to Back");
		moveToBack.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent e) {
				 if(canvas.lastShape!=null) {
						DShapeModel tmp = canvas.shapesmodel.get(canvas.shapesmodel.size()-1);
						for(int i = 0 ; i < canvas.shapesmodel.size();i++) {
							if(canvas.lastShape.compareTo(canvas.shapesmodel.get(i))==0) {
								canvas.shapesmodel.set(canvas.shapesmodel.size()-1,canvas.lastShape);
								canvas.shapesmodel.set(i, tmp);
							}
						}
				 }
				 }
						
			 
		});
		JButton removeShape= new JButton("Remove Shape");
		removeShape.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int i = 0 ; i < canvas.shapesmodel.size();i++) {
					if(canvas.lastShape!=null) {
						if(canvas.lastShape.compareTo(canvas.shapesmodel.get(i))==0) {
							canvas.shapesmodel.remove(i);
							canvas.shapes.remove(i);
						}
					}
				}
			
				canvas.update();
				
			}
		});
		controls4.add(moveToFront);
		controls4.add(moveToBack);
		controls4.add(removeShape);
		  JButton button;
		  button = new JButton("Start Server");
	        button.addActionListener( new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	               doServer();
	            }

	        });
	        
	        controls5.add(button);
	    	controls.add(controls5, BorderLayout.SOUTH);
	        button = new JButton("Start Client");
	        button.addActionListener( new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	               doClient();
	            }

	        });
	        controls5.add(button);status = new JLabel();
	        controls5.add(status);
	        textArea = new JTextArea(20, 20);
	        add(new JScrollPane(textArea), BorderLayout.CENTER);
	
		
		class myTableModel extends AbstractTableModel {
			String[] columns={"x", "y", "width", "height"};
			@Override
			public int getColumnCount() {
				// TODO Auto-generated method stub
				return 4;
			}

			@Override
			public int getRowCount() {
				return canvas.shapesmodel.size();
			}

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				if(columnIndex==0) {
					return canvas.shapesmodel.get(rowIndex).getX();
				}else if(columnIndex==1) {
					return canvas.shapesmodel.get(rowIndex).getY();
				}else if(columnIndex==2) {
					return canvas.shapesmodel.get(rowIndex).getWidth();
				}else {
				
					return canvas.shapesmodel.get(rowIndex).getHeight();
				}
			}
			public String getColumnName(int column) {
				
				return columns[column];
			}
			
		}
		myTableModel tableModel=new myTableModel();
		JTable table=new JTable(tableModel);
		
		
		JScrollPane scrollpane=new JScrollPane(table);
		
		table.setPreferredScrollableViewportSize(new Dimension(200,300));
		table.setFillsViewportHeight(true);
		locations.add(scrollpane);
	
		
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.setLayout(new BorderLayout());
		controls.setLayout(new BoxLayout(controls,BoxLayout.PAGE_AXIS)); //set everything vertical in the controls panel
		controls.add(controls1); //adds first row to controls
		
		controls.add(controls2);//adds second row to controls
		controls3.setMaximumSize(new Dimension(500,0));
		controls.add(controls3); //adds third row to controls
		controls.add(controls4);//adds fourth row to controls
		controls.add(locations);
		myFrame.setResizable(true);
		myFrame.add(controls, BorderLayout.WEST);
		myFrame.add(canvas, BorderLayout.CENTER); //adds canvas to the center 
		myFrame.pack();
		myFrame.setVisible(true);
		
		JButton menuItem = new JButton("Open...");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String result = JOptionPane.showInputDialog("File Name", null);
				if (result != null) {
					File f = new File(result);
					open(f);
					
				}
			}
		});
		controls4.add(menuItem);
		
		menuItem = new JButton("Save...");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String result = JOptionPane.showInputDialog("File Name", null);
				if (result != null) {
					File f = new File(result);
					save(f);
				}
			}
		});
		controls4.add(menuItem);
		
        menuItem = new JButton("Save PNG...");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String result = JOptionPane.showInputDialog("File Name", null);
                if (result != null) {
                    File f = new File(result);
                    saveImage(f);
                }
            }
        });
        controls4.add(menuItem);

	}
    public void save(File file) {
        try {
        XMLEncoder xmlOut = new XMLEncoder(
            new BufferedOutputStream(
            new FileOutputStream(file)));
         
            xmlOut.setPersistenceDelegate(DShapeModel.class,
            new DefaultPersistenceDelegate(
            new String[]{ "x", "y", "color" }) );
            
            DShapeModel[] shapeArray = canvas.shapesmodel.toArray(new DShapeModel[0]);
         
        
            
            xmlOut.writeObject(shapeArray);
         
            xmlOut.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void saveImage(File file){
        // Create an image bitmap, same size as ourselves
        BufferedImage image = (BufferedImage) canvas.createImage(canvas.getWidth(), canvas.getHeight());
        // Get Graphics pointing to the bitmap, and call paintAll()
        // This is the RARE case where calling paint() is appropriate
        // (normally the system calls paint()/paintComponent())
        Graphics g = image.getGraphics();
        canvas.paintAll(g);
        //g.dispose(); // Good but not required--
        // dispose() Graphics you create yourself when done with them.
       
        try {
        	File f = new File(file + ".PNG");
			ImageIO.write(image, "PNG", f);
		} catch (IOException e) {
			e.printStackTrace();
		}

    }

	public void open(File file) {
		DShapeModel[] shapeArray = null;
		try {

			XMLDecoder xmlIn = new XMLDecoder(new BufferedInputStream(new FileInputStream(file)));
		
			shapeArray = (DShapeModel[]) xmlIn.readObject();
			xmlIn.close();
		
			clear();
			for (DShapeModel model : shapeArray) {
				doAdd(model);
			}
	
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void doAdd(DShapeModel model) {
		
		canvas.addShape(model);
		canvas.update();
	}

	private class ClientHandler extends Thread {
        private String name;
        private int port;
        ClientHandler(String name, int port) {
            this.name = name;
            this.port = port;
        }
    // Connect to the server, loop getting messages
        public void run() {
            try {
                // make connection to the server name/port
                Socket toServer = new Socket(name, port);
                // get input stream to read from server and wrap in object input stream
                ObjectInputStream in = new ObjectInputStream(toServer.getInputStream());
                System.out.println("client: connected!");
                // we could do this if we wanted to write to server in addition
                // to reading
                // out = new ObjectOutputStream(toServer.getOutputStream());
                clientWB = new WhiteboardGUI();
                while (true) {
                    // Get the xml string, decode to a Message object.
                    // Blocks in readObject(), waiting for server to send something.
                    String xmlString = (String) in.readObject();
                    XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(xmlString.getBytes()));
                    DShapeModel model = (DShapeModel) decoder.readObject();
                    System.out.println("client: read " + model);
                    invokeToGUI(model);
                }
            }
            catch (Exception ex) { // IOException and ClassNotFoundException
               ex.printStackTrace();
            }
            // Could null out client ptr.
            // Note that exception breaks out of the while loop,
            // thus ending the thread.
       }
   } 
    class ServerAccepter extends Thread {
        private int port;
        ServerAccepter(int port) {
            this.port = port;
        }
        public void run() {
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                while (true) {
                    Socket toClient = null;
                    // this blocks, waiting for a Socket to the client
                    toClient = serverSocket.accept();
                    System.out.println("server: got client");
                    // Get an output stream to the client, and add it to
                    // the list of outputs
                    // (our server only uses the output stream of the connection)
                    addOutput(new ObjectOutputStream(toClient.getOutputStream()));
                }
            } catch (IOException ex) {
                ex.printStackTrace(); 
            }
        }
    }
    public synchronized void addOutput(ObjectOutputStream out) {
        outputs.add(out);
    }
    public void invokeToGUI(DShapeModel model) {
        final DShapeModel temp = model;
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                status.setText("Client receive");
                sendRemote(temp);
                clientWB.canvas.shapesmodel.add(model);
                clientWB.canvas.update();
            }
        });
    }
    public void sendLocal(DShapeModel model) {
        textArea.setText(textArea.getText() + model.getClass() + "\n" + model.getBounds() + "\n\n");
    }
    public synchronized void sendRemote(DShapeModel model) {
        status.setText("Server send");
        System.out.println("server: send " + model);

        // Convert the message object into an xml string.
        OutputStream memStream = new ByteArrayOutputStream();
        XMLEncoder encoder = new XMLEncoder(memStream);
        encoder.writeObject(model);
        encoder.close();
        String xmlString = memStream.toString();
        // Now write that xml string to all the clients.
        Iterator<ObjectOutputStream> it = outputs.iterator();
        while (it.hasNext()) {
            ObjectOutputStream out = it.next();
            try {
                out.writeObject(xmlString);
                out.flush();
            }
            catch (Exception ex) {
                ex.printStackTrace();
                it.remove();
                // Cute use of iterator and exceptions --
                // drop that socket from list if have probs with it
            }
        }
    }
	public void clear() {
		canvas.shapesmodel.clear();
		canvas.update();
	}
	  public void doServer() {
	        status.setText("Server Mode");
	        String result = JOptionPane.showInputDialog("Run server on port", "39587");
	        if (result!=null) {
	            System.out.println("server: start");
	            serverAccepter = new ServerAccepter(Integer.parseInt(result.trim()));
	            serverAccepter.start();
	        }
	    }
	    public void doClient() {
	        status.setText("Client Mode");
	        String result = JOptionPane.showInputDialog("Connect to host:port", "127.0.0.1:port");
	        if (result!=null) {
	            String[] parts = result.split(":");
	            System.out.println("client: start");
	            clientHandler = new ClientHandler(parts[0].trim(), Integer.parseInt(parts[1].trim()));
	            clientHandler.start();
	        }
	    }
	
	public static void main(String args[]) {
		WhiteboardGUI a= new WhiteboardGUI();
	}
}