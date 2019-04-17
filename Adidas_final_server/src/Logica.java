import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import common.Camiseta;
import common.Equipo;
import common.User;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

public class Logica implements Observer{

	private PApplet app;
	private ArrayList<Client> clients;
	
	private User user;
	private Camiseta cam,cam2;
	private Equipo team;
	

	private int pantalla, x1, x2, y;
	private int cant_clientes;
	int eq; // Que uso para cambiar el PImage de la camiseta;
	private boolean iniciar;

	// --UI Elements
	private PImage fondoUno;
	private PImage fondoDos;
	private PImage fondoTres;
	private PImage logoMadrid;
	private PImage escudoRealM;
	private PImage ticket;

	private PFont bodyFont;
	private PFont titleFont;
	private float variable;

	// --Variables de usuario
	private String user1; // Para pintar el nombre del usuario en el input de
							// arriba
	private String user2; // Para pintar el nombre del usuario en el input de
							// arriba
	
	private String nombreEquipo;

	private String[] talla = new String[4];
	
	
	private int precio1,precio2; // Valor global de la camiseta
	private int number1,number2; // Numero que elije el usuario
	private int pos1,pos2; // Numero que elije el usuario

	//Variables Control de cada Usuario --> Posiciones del Selector del control
	int ys1,ys2;
	
	// OJO CON ESTA VARIABLE VAMOS A RECIBIR LA INFORMACION DESDE ANDROID PARA
	// EJECUTAR TODO AC�
	String mensaje;

	public Logica(PApplet app) {

		this.app = app;

		// Iniciamos las variables
		clients = new ArrayList<Client>();

		// UI Elements
		pantalla = 0;
		x1 = 210;
		x2 = 620;
		y = 150;
		iniciar = false;

		// Cargamos todas las im�genes y fuentes que vamos a usar en las
		// interfaces
		fondoUno = app.loadImage("../data/img/mainbg_blue.png");
		fondoDos = app.loadImage("../data/img/bg_selector.png");
		fondoTres = app.loadImage("../data/img/bg_ticket.png");
		logoMadrid = app.loadImage("../data/img/logoMadrid.png");
		ticket = app.loadImage("../data/img/code.png");
		// bodyFont = app.loadFont("../data/fonts/bariol-regular.vlw");

		

		// los peque�os 48 X 48 PX

		escudoRealM = app.loadImage("../data/img/real.png");

		// Tallas camisetas es variable
		talla[0] = "S";
		talla[1] = "M";
		talla[2] = "L";
		talla[3] = "XL";

		// Cambiar todos estos valores por los reales de cada usuario
		user1 = "cambiar";
		user2 = "cambiar";
		precio1 = 114;
		precio2 = 114;
		number1 = 1;
		number2 = 1;
		pos1 = 1;
		pos2 = 1;

		
		
	}
	
	public void setClientes(int cant_clientes){
		
		this.cant_clientes=cant_clientes;
		System.out.println("------------------------->:" + cant_clientes);
	}

	public void ejecutar() {

		// System.out.println("hello logica");

		switch (pantalla) {

		case 0: // Loading -- esperando por conexi�n de usuarios (visible desde
				// que el servidor esta corriendo)
			app.image(fondoUno, 0, 0);

			// ------------------------ PROGRESS BAR / WAITING EFFECT

			app.noFill();
			app.stroke(255, 100); // Pintar el trazo blanco con opacidad 100
			app.strokeWeight(10); // Grosor del trado a 10px
			app.strokeCap(app.PROJECT); // Borde del trazo cuadrado
			app.arc(app.width / 2, app.height / 2 , 100, 100, 0, 2 * app.PI);

			// Sacamos la matrix
			app.pushMatrix(); //
			app.translate(app.width / 2, app.height / 2 );

			app.rotate(variable); // Lo hacemos rotar
			app.stroke(255, 197, 20); // Pintamos el Trado blanco con 190 de
										// opacidad
			app.arc(0, 0, 100, 100, 0, (float) (2 * app.PI / 5));

			app.popMatrix(); // cerramos la matrix
			variable += 0.08; // Angulo que rotar� el arco

			app.textSize(13); // Ponemos el ancho de texto en 20px
			app.text("Waiting for partners...", 490, 400);

			// ------------------------ FIN PROGRESS BAR / WAITING EFFECT
			app.image(escudoRealM, app.width/2 - 18, app.height/2 -24);
			

			//System.out.println("Clientes: " + clients.size());
			
			if(cant_clientes==2){
				
				pantalla=1;
				
			}

			break;

		case 1: // Pantalla de bienvenida... Shake para continuar
			app.image(fondoDos, 0, 0);
			
			//Condiciones para que cuando se haga shake cambie de pantalla a la 2 o sea el editor....

			break;

		case 2: // Editor de camisetas

			editor();
			
			cam = new Camiseta(app, precio1, number1, user1, talla[0], null, x1, y, false);			
			cam.pintar();
			
			cam2 = new Camiseta(app, precio2, number2, user2, talla[0], null, x2, y, false);
			cam2.pintar();

			// Aqui hacemos el cambio del numero y ojo que el color del texto
			
			app.fill(10, 31, 64);
			app.textSize(94);
			app.text(number1, x1 + 70, y + 160);
			app.text(number2, x2 + 70, y + 160);
			
			//Pinta selector Del Menu de cada Usuario
			app.strokeWeight(2);
			app.stroke(238, 197, 0);
			app.noFill();
			app.rect(0, ys1, 120, 55);
			app.rect(app.width-110, ys2, 120, 55);
			

			break;

		case 3: // Fin de la experiencia -- env�o del codigo promocional...
			
			
			app.image(fondoTres, 0, 0);
			app.image(ticket, app.width/2 + 275, app.height/2-75);
			
			
			//Instrucciones para recibir el shake
			
			break;

		}// Fin pantallas
	}//fin ejecutar

	
	public boolean arriba = true;
	
	public void setUserOneControl(int _numero){
		if(_numero == 4){
			if ( arriba == true){
				//Metodo Cambiar Numero
				if(number1 >= 100){
					number1 = 1;
				} else{
					number1++;
				}
			}
			if ( arriba == false){
				//Metodo Cambiar Talla
				
			}
		}else{
		System.out.println("UserControl Called");
		
			arriba =  !arriba;
		
		if ( arriba == true){
			System.out.println("Position Change");
			ys1 = 252;
			//pos1 = 2;
		}
		if( arriba == false){
			System.out.println("Position Change");
			ys1 = 352;
			//pos1 = 1;
		}
		}
		
	}
	
	public boolean arriba2 = true;
	public void setUserDosControl(int _numero){
		if(_numero == 4){
			if ( arriba2 == true){
				//Metodo Cambiar Numero
				if(number2 >= 100){
					number2 = 1;
				} else{
					number2++;
				}
			}
			if ( arriba2 == false){
				//Metodo Cambiar Talla
				
			}
		}else{
		System.out.println("UserControl Called");
		
			arriba2 =  !arriba2;
		
		if ( arriba2 == true){
			System.out.println("Position Change");
			ys2 = 252;
			//pos1 = 2;
		}
		if( arriba2 == false){
			System.out.println("Position Change");
			ys2 = 352;
			//pos1 = 1;
		}
		}
		
	}
	


	public void setPantalla(int _n){
		pantalla = _n;
	}

	public void editor() {

		app.image(fondoUno, 0, 0);
		app.noStroke();
		app.fill(23, 95);
		app.rect(0, 0, 1100, 88);

		app.fill(23, 42, 76);

		

		// Input logos equipos
		app.rect(0, 0, 183, 88);
		app.rect(917, 0, 183, 88);

		// Esto es variable--- debe cambiar a medida que cambian los equipos!!
		// Equipo Usuaio 1
		app.image(escudoRealM, 21, 21);
		app.fill(255);
		app.textSize(16);
		app.text("Real Madrid", 65, 51);
		// Equipo Usuaio 2
		app.image(escudoRealM, 1050, 21);
		app.text("Real Madrid", 940, 51);

		// Inputs nombres de usuario
		app.fill(255);
		app.textSize(15);
		app.text("user 1", 325, 30);
		app.text("user 2", 713, 30);

		app.textSize(20);
		app.text(user1, 285, 60);
		app.text(user2, 710, 60);
		
		

		app.fill(255, 50);
		app.rect(272, 41, 151, 27);
		app.rect(670, 41, 151, 27);

		// ------Pricing

		// Lado usuario 1:
		app.fill(255);
		app.textSize(18);
		app.text("PRICE", 33, 135);
		app.textSize(40);
		app.text("$" + precio1, 33, 182);

		// Lado usuario 2:
		app.fill(255);
		app.textSize(18);
		app.text("PRICE", 1020, 135);
		app.textSize(40);
		app.text("$" + precio2, 1000, 182);

		// ------Number

		// Lado usuario 1:
		app.fill(255, 197, 0);
		app.rect(0, 209, 135, 32);
		app.fill(255);
		app.textSize(18);
		app.text("NUMBER", 33, 231);
		app.textSize(40);
		app.text(number1, 33, 291);

		// Lado usuario 2:
		app.fill(255, 197, 0);
		app.rect(965, 209, 135, 32);
		app.fill(255);
		app.textSize(18);
		app.text("NUMBER", 1000, 231);
		app.textSize(40);
		app.text(number2, 1028, 291);

		// ------Size

		// Lado usuario 1:

		app.fill(23, 42, 76);
		app.rect(0, 311, 135, 32);
		app.fill(255);
		app.textSize(18);
		app.text("SIZE", 33, 333);
		app.textSize(40);
		app.text("S", 35, 393); // Aqui traemos el valor real de la talla desde
								// la clase camiseta

		// Lado usuario 2:

		app.fill(23, 42, 76);
		app.rect(965, 311, 135, 32);
		app.fill(255);
		app.textSize(18);
		app.text("SIZE", 1030, 333);
		app.textSize(40);
		app.text("M", 1034, 393); // Aqui traemos el valor real de la talla
									// desde la clase camiseta

		// ------ Vistas camiseta

		// Lado usuario 1:
		app.textSize(18);
		app.text("view", 33, 440);
		app.stroke(255);
		app.strokeWeight(1);
		app.noFill();
		app.rect(33, 465, 67, 67);
		

		// Lado usuario 2:
		app.textSize(18);
		app.text("view", 1030, 440);
		app.stroke(255);
		app.strokeWeight(1);
		app.noFill();
		app.rect(1000, 465, 67, 67);

		// ------Boton guardar

		// Lado usuario 1:

		app.noStroke();
		app.fill(255, 197, 0);
		app.rect(277, 548, 139, 45);
		app.fill(255);
		app.textSize(22);
		app.text("SAVE", 317, 579);

		// Lado usuario 2:

		app.noStroke();
		app.fill(255, 197, 0);
		app.rect(679, 548, 139, 45);
		app.fill(255);
		app.textSize(22);
		app.text("SAVE", 719, 579);
		
		app.textSize(20);
		app.fill(25,35,62);
		app.text(user1, 300, 180);
		app.text(user2, 760, 180);

	}

	public void setNameUno(String _n){
		user1 = _n;
	}
	
	public void setNameDos(String _n){
		user2 = _n;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		System.out.println("Mensaje para logica");
		if( arg instanceof Integer){
			System.out.println("Mensaje tipo Int");
			int tempUser = (int) arg;
			
			//String tempString = tempUser.getMsg();
			//System.out.println("EL STRING QUE LLEGA: " + tempString);
			if(tempUser == 21){
				System.out.println("Mensaje para cambio de Escena");
				pantalla = 2;
			}
		}
	}

}
