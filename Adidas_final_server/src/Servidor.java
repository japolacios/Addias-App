import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;


import common.Camiseta;
import common.User;
import common.Equipo;
import processing.core.PApplet;


//Esta clase se encarga de la conexión por medio de TCP, extiende de hilo e implementa observer
public class Servidor extends Observable implements Observer, Runnable {
	
	
	private ServerSocket socket;
	private ArrayList<Client> clients;

	private XMLusers xUsers;
	private XMLcamiseta xCamisetas;
	private XMLequipo xEquipos;
	
	
	private boolean iniciado;
	
	private Logica logica;
	
	private boolean myNameOne = false;
	private boolean myNameTwo = false;
	private boolean startThisShit = false;
	//ArrayList de los equipos, para tener guardada una copia local de ellos en el XML y no tener que estar leyendolo constantemente
	//private ArrayList<Equipo> equipos = new ArrayList<Equipo>();
	
	//Constructor del servidor
	public Servidor(PApplet app, Logica _logica) {
		
		//Iniciamos las variables
		clients = new ArrayList<Client>();
		xUsers = new XMLusers(app);
		xCamisetas = new XMLcamiseta(app);
		xEquipos = new XMLequipo();
		iniciado = false;
		logica = _logica;
		
		

		try {
			//Iniciamos el socket en el puerto 5000
			socket = new ServerSocket(5687);
			System.out.println("SERVER: " + socket.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void run() {
		
		//Mantenemos el hilo constantemente corriendo
		while (true) {
			
			try {
				

				if(clients.size() <= 2 ){
				
				//Cuando llega un cliente lo añadimos al arrayList de Clientes
				clients.add(new Client(socket.accept(), this, clients.size()));
				clients.get(clients.size()-1).addObserver(this);
				System.out.println("new client number: " + clients.get(clients.size() - 1).toString());
				System.out.println("Total clients: " + clients.size());
				
				
					//registra
					
					//++++++++++++++++++++++++++****!!!!!!!!!!!!!!!!!!!!!!!!!
				}
				
				
				
				
				
				//---------> CAMBIAR A 2
				//------------------------
				
				//se revisa por clientes cada 100 milisegundos
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public boolean unoOk = false;
	public boolean dosOk = false;
	public void update(Observable client, Object a) {
		checkNames();
		//Si el objeto que llega es un usuario se realiza esto
		if (a instanceof User) {
			User b = (User) a;
				//Primer Usuario
			System.out.println("Mensaje de Usuario:" + b.getId());
				if (b.getId()==0) {
					
					if(b.getName().contains("control")){
						System.out.println("Server llego Control");
						System.out.println("El COMANDO: "+ b.getMsg());
						String n1 = b.getMsg();
						int tempInt = Integer.parseInt(n1);
						
						
						if(tempInt == 1){
							System.out.println("Mandando Control a logica");
							logica.setUserOneControl(0);
						}
						if(tempInt == 2){
							System.out.println("Mandando Control a logica");
							logica.setUserOneControl(4);
						}
						
						if(tempInt == 3){
							System.out.println("Mandando Control a logica");
							unoOk = true;
							//+++++++++++++++++++++++++++++++++++++
							if(unoOk == true && dosOk == true){
								for (int i = 0; i < clients.size(); i++) {
									
									System.out.println("Notificacndo a: " + clients.get(i).getSocket());
									
									String siga = "next";
									clients.get(i).send(siga);
									logica.setPantalla(3);
									
								}

							}
						}
						
					}
					
					
			} 
				
				if(b.getId()==1){
					//Segundo Usuario
					if(b.getName().contains("control")){
						System.out.println("Server llego Control");
						System.out.println("El COMANDO: "+ b.getMsg());
						String n1 = b.getMsg();
						int tempInt = Integer.parseInt(n1);
						
						
						if(tempInt == 1){
							System.out.println("Mandando Control a logica");
							logica.setUserDosControl(0);
						}
						if(tempInt == 2){
							System.out.println("Mandando Control a logica");
							logica.setUserDosControl(4);
						}
						
						if(tempInt == 3){
							System.out.println("Mandando Control a logica");
							dosOk = true;
							//+++++++++++++++++++++++++++++++++++++
							if(unoOk == true && dosOk == true){
								for (int i = 0; i < clients.size(); i++) {
									
									System.out.println("Notificacndo a: " + clients.get(i).getSocket());
									
									String siga = "next";
									clients.get(i).send(siga);
									logica.setPantalla(3);
									
								}

							}
						}
						
					}
				}
		}
		
	}

	public void checkNames(){
		System.out.println("CHecking Names");
		try{
			System.out.println("TRYYYYYY");
		if(!clients.get(0).getName().equals("")){
			System.out.println("EL PRIMERO A TRUE");
			logica.setNameUno(clients.get(0).getName());
			myNameOne = true;
		}
		if(!clients.get(1).getName().equals("")){
			myNameTwo = true;
			System.out.println("EL SEGUNDO A TRUE");
			logica.setNameDos(clients.get(1).getName());
		}

		}catch (Exception e){
			
		}
		
		
		
		if( myNameOne == true && myNameTwo == true){
			System.out.println("dos Nombres");
			startThisShit = true;
			if( startThisShit == true){
				//System.out.println("YA HAY DOS!");
				if(iniciado == false){
					for (int i = 0; i < clients.size(); i++) {
						
						System.out.println("Notificacndo a: " + clients.get(i).getSocket());
						
						String siga = "next";
						clients.get(i).send(siga);
						
					}

					logica.setPantalla(2);
					iniciado = true;
				}
			}
		}
	
	}
	

}
