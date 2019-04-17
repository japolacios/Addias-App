import java.net.InetAddress;
import java.net.UnknownHostException;

import Interfaz.NotifyClients;
import processing.core.PApplet;

public class Main  extends PApplet implements NotifyClients{
	//Ejecutable
	
	//Llamamos la clase servidor
	Servidor server;
	Logica logica;
	
	public void setup(){
		
		size(1100,633);
		//inicializamos el servidor
		logica = new Logica(this);
		server= new Servidor(this, logica);
		
		
		//Iniciamos el hilo del servidor (no tenemos nada en el draw)
		
		//server.start();
		//Iniciamos el hilo
		Thread t= new Thread(server);
		t.start();
		server.addObserver(logica);
		try {
			System.out.println(InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void draw(){
		background(29,36,58);
		logica.ejecutar();
		//logica.click();
		
		//------ESTO LO TENGO QUE USAR PARA OBTENER LA IP DE EL PC CUANDO VAYA A SUSTENTAR EN SALA
/*		String a;
		try {
			a=InetAddress.getLocalHost().toString();
			String[] b=a.split("/");
			System.out.println(b[1]+" "+a);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	public void mousePressed(){
		
		//logica.click();
	}
	
	public void mouseClicked(){
		
		
	}

	@Override
	public void ShowClientes(int cantidad_clientes) {
		
		logica.setClientes(cantidad_clientes);
		
	}
	

}
