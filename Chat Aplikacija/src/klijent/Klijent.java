package klijent;

import java.io.*;
import java.net.*;


public class Klijent extends Thread {

	static Socket soketZaKomunikaciju = null;
	static PrintStream izlazniTokKaServeru = null;
	static BufferedReader ulazniTokOdServera = null;
	static BufferedReader ulazKonzola = null;
	static boolean kraj = false;

	
	public static void main(String[] args){
		
		try {
			
			int port = 9999;
			soketZaKomunikaciju = new Socket("localhost", port);
			ulazKonzola = new BufferedReader(new InputStreamReader(System.in));
			
			izlazniTokKaServeru = new PrintStream
					(soketZaKomunikaciju.getOutputStream());
			
			ulazniTokOdServera = new BufferedReader
					(new InputStreamReader
							(soketZaKomunikaciju.getInputStream()));
			
			new Thread(new Klijent()).start();
			
			
			
			
			
			
			
			// SLANJE UDP			
			//DatagramSocket soketZaKom = new DatagramSocket();			
			//InetAddress IPAdresa = InetAddress.getByName("localhost");			
			//byte[] podaciZaServer = new byte[1024];	
			//byte[] podaciOdServera = new byte[1024];	
			//DatagramPacket paketZaServer = new DatagramPacket(podaciZaServer, podaciZaServer.length, IPAdresa, 10000);			
			//soketZaKom.send(paketZaServer);			
			//ZAVRSETAK UDP
				
			
			
			while(!kraj){
				
				String s = ulazKonzola.readLine();
				izlazniTokKaServeru.println(s);
			}
			
			soketZaKomunikaciju.close();
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	
	
	@Override
	public void run() {
		
		String recenicaOdServera;
		try {
			while((recenicaOdServera = ulazniTokOdServera.readLine()) != null){
				System.out.println(recenicaOdServera);
				
				if(recenicaOdServera.startsWith("||| Prijatno!")){
					kraj = true;
					return;
				}
				
				if(recenicaOdServera.contains("___")){
					udpMetoda();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	public static void udpMetoda(){

		
		try {
			
			
			// SLANJE UDP	
			DatagramSocket soketZaKom = new DatagramSocket();
			InetAddress IPAdresa = InetAddress.getByName("localhost");	
			
			byte[] podaciZaServer = new byte[1024];		
			byte[] podaciOdServera = new byte[1024];
			
			DatagramPacket paketOdServera = new DatagramPacket(podaciZaServer, podaciZaServer.length);
			String random = "AAA";
			podaciZaServer = random.getBytes();
			DatagramPacket paketZaServer = new DatagramPacket(podaciZaServer, podaciZaServer.length, IPAdresa, 10000);	
			soketZaKom.send(paketZaServer);			
			//ZAVRSETAK UDP
			
			
			int i =0;
			while(true){
				soketZaKom.receive(paketOdServera);
				String elementListe = new String(paketOdServera.getData());

				if(elementListe.contains("Ovo je kraj")){
					soketZaKom.close();
					return;
				}

				i++;
				elementListe.toString();
				System.out.println(i+"."+elementListe.trim()); 

			}
			
			
			
			
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			

	}

	
	
	
	
	
}
