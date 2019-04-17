import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

import common.User;

//Clase client, implementa runnable para manejar cada cliente como un hilo, además de ser un observable para que el servidor pueda saber cuando llegan datos
public class Client extends Observable implements Runnable{
	
	//Creamos un socket
	private Socket socket;
	//Añadimos el servidor como observer
	private Observer boss;
	//booleano para saber si está disponible
	private boolean available;
	private int id;
	private String myName = "";
	
	//Constructor
	public Client(Socket socket, Observer jefe, int _id) {
		// TODO Auto-generated constructor stub
		
		//Inicializamos variables
		this.socket=socket;
		this.boss=jefe;
		available=true;
		id = _id;
		//Iniciamos el hilo
		Thread t= new Thread(this);
		t.start();
	}

	public void run() {
		//Mientras esté disponible que intente recibir
		while(available){
			
			try {
				receive();
				Thread.sleep(33);
				
			}
			//Si no está disponible deja de escuchar
			catch( IOException e){
				System.out.println("[ TROUBLE WITH CLIENT: " + e + " ]");
				setChanged();
				boss.update(this, "Client is not available");
				available = false;
				clearChanged();
			} catch (ClassNotFoundException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		//cerrar el socket cada vez que nos desconectemos de algún cliente para no colapsarlo
		
		try {
			socket.close();
		} catch (IOException e) {			
			e.printStackTrace();
		} finally {
			socket = null;
		}
	}
	
	//Metodo para recibir
	private void receive() throws IOException, ClassNotFoundException{
		
		//Deserializamos lo que llegue por el socket
		ObjectInputStream ois= new ObjectInputStream(socket.getInputStream());
		Object obj=ois.readObject();
		System.out.println("Recibiendo");
		if(obj instanceof String){
			System.out.println("Got String");
			String tempString = (String) obj;
			if(tempString.equals("up") || tempString.equals("down")){
				System.out.println("Got Up or Down");
				User tempU = new User("control","1",id);
				setChanged();
				notifyObservers(tempU);
				clearChanged();
			}
			if(tempString.equals("left") || tempString.equals("right")){
				System.out.println("Got Up or Down");
				User tempU = new User("control","2",id);
				setChanged();
				notifyObservers(tempU);
				clearChanged();
			}
			if(tempString.equals("ok")){
				System.out.println("OK");
				User tempU = new User("control","3",id);
				setChanged();
				notifyObservers(tempU);
				clearChanged();
			}
		}
		if (obj instanceof User){
			System.out.println("llego un Objeto User");
			User tempUser = (User)obj;
			myName = tempUser.getName();
			System.out.println("Se va a settear el nombre : "+ myName);
			System.out.println("Name Set!");
			/*
			setChanged();
			notifyObservers(tempU);
			clearChanged();
			*/
		}
		
		
		//y avisamos al servidor
		boss.update(this, obj);
		
	}
	
	//Metodo para enviar
	public void send(Object obj){
		try {
			
			//Serializamos el objeto y lo enviamos
			ObjectOutputStream oos= new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(obj);
		} catch (IOException e) {
			System.out.println("OBJECT CAN NOT BE SEND: "+ obj.getClass());

			e.printStackTrace();
		}
		
	}
	
	public String toString(){
		return socket.toString();
	}
	
	public Socket getSocket(){
		return socket;
	}
	
	public String getName(){
		return myName;
	}
	
}
